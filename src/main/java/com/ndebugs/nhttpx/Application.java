package com.ndebugs.nhttpx;

import com.ndebugs.nhttpx.event.ShutdownHook;
import com.ndebugs.nhttpx.config.BeanConfiguration;
import com.ndebugs.nhttpx.manager.ProcessManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class Application {

    private static Application application;

    private final ApplicationContext context;
    private final Logger LOGGER = LogManager.getLogger();

    private Application(ApplicationContext context) {
        this.context = context;

        init();
    }

    private void init() {
        Runtime runtime = Runtime.getRuntime();

        ShutdownHook hook = new ShutdownHook();
        runtime.addShutdownHook(hook);
    }

    public void start() throws Exception {
        ProcessManager manager = context.getBean(ProcessManager.class);
        manager.doProcess();
    }

    public void stop() throws IOException {
        ProcessManager manager = context.getBean(ProcessManager.class);
        manager.stop();
    }

    public static Application getInstance() {
        return application;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("config", "log4j2.xml");
        if (file.exists()) {
            System.setProperty("log4j.configurationFile", file.getPath());
        } else {
            throw new FileNotFoundException("File not found: " + file);
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
        application = new Application(context);

        try {
            application.start();
        } catch (Exception e) {
            application.LOGGER.catching(e);

            System.exit(0);
        }
    }
}
