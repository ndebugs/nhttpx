package com.ndebugs.nhttpx.task;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class SchedulerTask extends Task {

    private SchedulerTaskListener listener;

    public SchedulerTask(SchedulerTaskListener listener) {
        this.listener = listener;
    }

    public SchedulerTaskListener getListener() {
        return listener;
    }

    public void setListener(SchedulerTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.onComplete(this);
    }
}
