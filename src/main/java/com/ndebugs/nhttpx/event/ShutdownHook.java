package com.ndebugs.nhttpx.event;

import com.ndebugs.nhttpx.Application;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Log4j2
public class ShutdownHook extends Thread {

    @Override
    public void run() {
        Application application = Application.getInstance();
        try {
            application.stop();
        } catch (IOException e) {
            log.catching(e);
        }
    }
}
