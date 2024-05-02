package ch.zhaw.it.pm2.receiptsplitter.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;

public class LoggerFileHandler extends FileHandler {
    public LoggerFileHandler() throws Exception {
        super("logs/" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "-%u.log");
        // %u is for unique, this is need for when the file limit is reached and therefore rotates.
        // otherwise it would overwrite the existing file.

    }

}