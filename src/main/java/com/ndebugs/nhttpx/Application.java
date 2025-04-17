package com.ndebugs.nhttpx;

import com.ndebugs.nhttpx.event.ShutdownHook;
import com.ndebugs.nhttpx.config.BeanConfiguration;
import com.ndebugs.nhttpx.manager.ProcessManager;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Log4j2
public class Application {

    private static Application application;

    private final ApplicationContext context;

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
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
        application = new Application(context);

        try {
            application.start();
        } catch (Exception e) {
            Application.log.catching(e);

            System.exit(0);
        }
    }
}
