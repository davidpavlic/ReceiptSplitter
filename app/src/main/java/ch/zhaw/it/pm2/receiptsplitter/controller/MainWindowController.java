package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class MainWindowController implements DefaultController, CanNavigate {
    Router router;

    @Override
    public void initialize(Router router) {
        this.router = router;
    }

    @FXML
    void showFAQ() {
        router.openHelpModal(HelpMessages.FAQ_MSG);
    }

    @FXML
    void showHelpModal() {
        router.openHelpModal(HelpMessages.MAIN_WINDOW_MSG);
    }

    @Override
    public void confirm() {

    }

    @FXML
    public void openContactList() {
        router.gotoScene(Pages.CONTACT_LIST_WINDOW);
    }

    @FXML
    public void openTransactions() {
        //TODO Implement Transactions
    }

    @FXML
    public void addReceipt() {
        //TODO Implement addReceipt
    }


    @Override
    @FXML
    public void back() {
        router.gotoScene(Pages.LOGIN_WINDOW);
    }
}
