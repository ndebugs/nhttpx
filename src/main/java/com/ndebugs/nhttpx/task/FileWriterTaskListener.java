package com.ndebugs.nhttpx.task;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public interface FileWriterTaskListener extends ErrorTaskListener {

    void onComplete(FileWriterTask task);
}
