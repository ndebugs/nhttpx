package com.ndebugs.nhttpx.task;

import com.ndebugs.nhttpx.message.Message;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public abstract class MessageTask extends Task {

    public static final int POSITION_FIRST_SECTION = 1;
    public static final int POSITION_LAST_SECTION = 2;

    private Message message;
    private int position;

    public MessageTask(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addPosition(int position) {
        this.position |= position;
    }

    public void removePosition(int position) {
        this.position &= ~position;
    }

    public boolean hasPosition(int position) {
        return (this.position & position) == position;
    }
}
