package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;

public class EditItemsController extends DefaultController implements CanNavigate, CanReset {

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.EDIT_ITEMS_WINDOW_MSG;
    }

    @Override
    public void refreshScene() {
    }

    @Override
    public void confirm() {
        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void reset() {}

}
