package com.ndebugs.nhttpx.manager;

import com.ndebugs.nhttpx.message.Message;
import com.ndebugs.nhttpx.task.FileWriterTask;
import com.ndebugs.nhttpx.task.MessageTask;
import com.ndebugs.nhttpx.task.RequestTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Component
@Log4j2
public class ExecutorManager {

    @Autowired
    private RuntimeManager runtimeManager;

    private final Map<String, ExecutorService> fileServiceMap = new HashMap<>();
    private final Map<ExecutorService, Future> futureMap = new HashMap<>();

    private ExecutorService connectionService;

    public ExecutorService getConnectionService() {
        if (connectionService == null) {
            connectionService = Executors.newSingleThreadExecutor();

            log.info("Connection service started.");
        }
        return connectionService;
    }

    public ExecutorService getFileWriterService(Message message) {
        String key = message.getName();

        ExecutorService service = fileServiceMap.get(key);
        if (service == null) {
            service = Executors.newSingleThreadExecutor();
            fileServiceMap.put(key, service);

            log.info("File Writer service for '{}' started.", key);
        }
        return service;
    }

    private void execute(ExecutorService service, MessageTask task) {
        synchronized (futureMap) {
            futureMap.put(service, service.submit(task));
        }

        runtimeManager.rescheduleTimeout();
    }

    public void execute(RequestTask task) {
        ExecutorService service = getConnectionService();
        execute(service, task);
    }

    public void execute(FileWriterTask task) {
        ExecutorService service = getFileWriterService(task.getMessage());
        execute(service, task);
    }

    private ExecutorStatus getStatus(ExecutorService service) {
        ExecutorStatus status = null;
        if (service != null) {
            synchronized (futureMap) {
                Future future = futureMap.get(service);
                if (future != null) {
                    status = !future.isDone() ? ExecutorStatus.ACTIVE : ExecutorStatus.IDLE;
                }
            }
        }
        return status != null ? status : ExecutorStatus.INACTIVE;
    }

    public ExecutorStatus getConnectionStatus() {
        return getStatus(connectionService);
    }

    public ExecutorStatus getFileWriterStatus(Message message) {
        String key = message.getName();

        ExecutorService service = fileServiceMap.get(key);
        return getStatus(service);
    }

    public void stopConnectionService() {
        if (connectionService != null) {
            connectionService.shutdownNow();
            connectionService = null;

            log.info("Connection service stopped.");
        }
    }

    public void stopFileWriterService(Message message) {
        String key = message.getName();

        ExecutorService service = fileServiceMap.remove(key);
        if (service != null) {
            service.shutdownNow();

            log.info("File Writer service for '{}' stopped.", key);
        }
    }

    public void stopAllFileWriterService() {
        for (ExecutorService service : fileServiceMap.values()) {
            service.shutdownNow();
        }
        fileServiceMap.clear();

        log.info("All File Writer service stopped.");
    }
}
