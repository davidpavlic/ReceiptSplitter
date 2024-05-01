/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ch.zhaw.it.pm2.receiptsplitter;

import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        configureLogging();
        logger.info("Starting the application");
        if (!checkIfEnvConfigsAreSet()) {
            logger.severe("Env Vars are not set correctly, please ensure to follow the documentation in the README.md file");
        } else {
            launch(args);
        }
    }

    public void start(Stage stage) throws Exception {
        stage.setWidth(400);
        stage.setHeight(300);
        stage.setMinWidth(300);
        stage.setMinHeight(250);
        stage.setTitle("Receipt Splitter");

        Router router = new Router(stage);
        try {
            router.gotoScene(Pages.LOGIN_WINDOW);
        } catch (IllegalStateException exception) {
            logger.severe("Could not load the login window: " + exception);
        }
    }

    private static boolean checkIfEnvConfigsAreSet() {
        logger.info("Checking environment configurations");
        String smtpUsername = System.getProperty("SMTP_USERNAME");
        String smtpApiKey = System.getProperty("SMTP_API_KEY");
        return (!smtpUsername.equals("null") && !smtpApiKey.equals("null"));
    }

    private static void configureLogging() {
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdir(); // Create the directory if it does not exist
        }
        LogManager.getLogManager().reset();
        try {
            InputStream configStream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            if (configStream == null) {
                throw new IOException("Could not find logging.properties");
            }
            LogManager.getLogManager().readConfiguration(configStream);
        } catch (IOException exception) {
            System.err.println("Could not setup logger configuration: " + exception);
        }
    }
}
