package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class MainWindowController extends DefaultController {

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


    @FXML
    public void back() {
        router.gotoScene(Pages.LOGIN_WINDOW);
    }
}
