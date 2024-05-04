package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class MainWindowController extends DefaultController {

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.MAIN_WINDOW_MSG;
    }

    @Override
    public void refreshScene() {
    }

    @FXML
    public void openContactList() {
        switchScene(Pages.CONTACT_LIST_WINDOW);
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
        switchScene(Pages.LOGIN_WINDOW);
    }
}
