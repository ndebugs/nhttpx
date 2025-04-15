package com.ndebugs.nhttpx.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SchedulerTask extends Task {

    private SchedulerTaskListener listener;

    public SchedulerTask(SchedulerTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.onComplete(this);
    }
}
