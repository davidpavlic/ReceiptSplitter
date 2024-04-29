package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class  DefaultController {
    protected Router router;

    public void initialize(Router router){
        this.router = router;
    }

    @FXML
    void showFAQ() {
        router.openHelpModal(HelpMessages.FAQ_MSG);
    }

    @FXML
    void showHelp() {
        router.openHelpModal(HelpMessages.LOGIN_WINDOW_MSG);
    }


}
