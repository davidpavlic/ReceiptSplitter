package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public abstract class  DefaultController {
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
        router.openHelpModal(helpMessage);
    }
}
