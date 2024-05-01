package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;

import java.util.Arrays;
import java.util.logging.Logger;
import javafx.fxml.FXML;

public abstract class  DefaultController {
    Logger logger = Logger.getLogger(DefaultController.class.getName());
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
        } catch (IllegalStateException e) {
            logger.severe("Could not open help modal, router returned IllegalStateException: " + e);
            logger.fine(Arrays.toString(e.getStackTrace()));
        }
    }

    protected void switchScene(Pages page) {
        try {
            router.gotoScene(page);
        } catch (IllegalStateException e) {
            logger.severe("Could not switch Scenes to " + page.toString() + " Window from Controller " + this.getClass().getName() + ", " + e);
            logger.fine(Arrays.toString(e.getStackTrace()));
        }
    }
}
