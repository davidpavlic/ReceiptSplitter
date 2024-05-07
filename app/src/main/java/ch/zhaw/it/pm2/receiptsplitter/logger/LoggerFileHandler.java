package ch.zhaw.it.pm2.receiptsplitter.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;

/**
 * This class represents the logger file handler.
 * It is used to create a new log file every day.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class LoggerFileHandler extends FileHandler {
    /**
     * Constructs a new LoggerFileHandler instance.
     * @throws Exception if an error occurs
     */
    public LoggerFileHandler() throws Exception {
        super("logs/" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "-%u.log");
        // %u is for unique, this is need for when the file limit is reached and therefore rotates.
        // otherwise it would overwrite the existing file.
    }
}