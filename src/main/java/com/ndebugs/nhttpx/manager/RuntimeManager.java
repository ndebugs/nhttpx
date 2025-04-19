package com.ndebugs.nhttpx.manager;

import com.ndebugs.nhttpx.config.ApplicationProperties;
import com.ndebugs.nhttpx.message.Message;
import com.ndebugs.nhttpx.message.MessageSettings;
import com.ndebugs.nhttpx.task.SchedulerTask;
import com.ndebugs.nhttpx.task.SchedulerTaskListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Component
@Log4j2
public class RuntimeManager implements SchedulerTaskListener {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MessageSettings messageSettings;

    @Autowired
    private ExecutorManager executorManager;

    @Autowired
    private FileWriterManager fileWriterManager;

    private ScheduledExecutorService schedulerService;
    private ScheduledFuture currentFuture;

    public ScheduledExecutorService getSchedulerService() {
        if (schedulerService == null) {
            schedulerService = Executors.newSingleThreadScheduledExecutor();

            log.info("Scheduler service started.");
        }
        return schedulerService;
    }

    public synchronized void rescheduleTimeout() {
        if (currentFuture != null) {
            currentFuture.cancel(false);
        }

        SchedulerTask task = new SchedulerTask(this);
        ScheduledExecutorService service = getSchedulerService();
        currentFuture = service.schedule(task,
                applicationProperties.getProcessTimeout(), TimeUnit.MILLISECONDS);
    }

    public void stopSchedulerService() {
        if (schedulerService != null) {
            schedulerService.shutdownNow();
            schedulerService = null;

            log.info("Scheduler service stopped.");
        }
    }

    @Override
    public void onComplete(SchedulerTask task) {
        log.info("Scheduler service running.");

        boolean isRunning = false;
        try {
            List<Message> messages = messageSettings.getMessages();
            for (Message message : messages) {
                ExecutorStatus status = executorManager.getFileWriterStatus(message);
                if (status == ExecutorStatus.IDLE) {
                    fileWriterManager.close(message);
                } else if (status == ExecutorStatus.ACTIVE) {
                    isRunning = true;
                }
            }
        } catch (IOException e) {
            log.catching(e);
        }

        ExecutorStatus status = executorManager.getConnectionStatus();
        if (status == ExecutorStatus.IDLE) {
            executorManager.stopConnectionService();
        } else if (status == ExecutorStatus.ACTIVE) {
            isRunning = true;
        }

        if (!isRunning) {
            stopSchedulerService();
        } else {
            rescheduleTimeout();
        }
    }
}
