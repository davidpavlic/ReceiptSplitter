package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;

import java.util.Arrays;
import java.util.logging.Logger;
import javafx.fxml.FXML;

public abstract class  DefaultController {
    private final Logger logger = Logger.getLogger(DefaultController.class.getName());
    protected Router router;
    protected HelpMessages helpMessage;

    public void initialize(Router router){
        this.router = router;
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;
    }


    @FXML
    void showFAQ() {
        router.openHelpModal(HelpMessages.FAQ_MSG);
    }

    @FXML
    void showHelp() {
        if (helpMessage == null) {
            throw new IllegalStateException("No help message set");
        }
        try {
            router.openHelpModal(helpMessage);
        } catch (IllegalStateException exception) {
            logger.severe("Could not open help modal, router returned IllegalStateException: " + exception);
            logger.fine(Arrays.toString(exception.getStackTrace()));
        }
    }

    protected void switchScene(Pages page) {
        try {
            router.gotoScene(page);
        } catch (IllegalStateException exception) {
            logger.severe("Could not switch Scenes to " + page.toString() + " Window from Controller " + this.getClass().getName() + ", " + exception);
            logger.fine(Arrays.toString(exception.getStackTrace()));
        }
    }

    protected void switchScene(Pages page, Pages lastPage) {
        try {
            router.gotoScene(page, lastPage);
        } catch (IllegalStateException | IllegalArgumentException exception) {
            logger.severe("Could not switch Scenes to " + page.toString() + " Window from Controller " + this.getClass().getName() + ", " + exception);
            logger.fine(Arrays.toString(exception.getStackTrace()));
        }
    }
}
