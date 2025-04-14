package com.ndebugs.nhttpx.event;

import com.ndebugs.nhttpx.Application;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ShutdownHook extends Thread {

    private final Logger LOGGER = LogManager.getLogger();

    @Override
    public void run() {
        Application application = Application.getInstance();
        try {
            application.stop();
        } catch (IOException e) {
            LOGGER.catching(e);
        }
    }
}
