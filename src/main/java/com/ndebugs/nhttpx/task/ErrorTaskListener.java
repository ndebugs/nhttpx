package com.ndebugs.nhttpx.task;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public interface ErrorTaskListener {

    void onError(MessageTask task, Exception e);
}
