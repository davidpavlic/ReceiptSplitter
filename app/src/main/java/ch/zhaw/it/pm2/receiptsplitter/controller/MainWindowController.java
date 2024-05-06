package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainWindowController extends DefaultController implements IsObserver {
    @FXML private Label welcomeMessage;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.MAIN_WINDOW_MSG;
        contactRepository.addObserver(this);
    }

    @Override
    public void update() {
        if (!(contactRepository.getProfile() == null)) {
            welcomeMessage.setText("Welcome " + contactRepository.getProfile().getFirstName());
        }
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
        switchScene(Pages.SHOW_SPLIT_WINDOW);
    }


    @FXML
    public void back() {
        switchScene(Pages.LOGIN_WINDOW);
    }
}
