package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class ListItemsController extends DefaultController implements CanNavigate, CanReset {

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.LIST_ITEMS_WINDOW_MSG;
    }

    @FXML
    @Override
    public void confirm() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @FXML
    @Override
    public void back() {
        switchScene(Pages.ADD_RECEIPT_WINDOW);
    }

    @FXML
    public void goToEditItemsController() {
        switchScene(Pages.EDIT_ITEMS_WINDOW);
    }

    @Override
    public void reset() {}

}
