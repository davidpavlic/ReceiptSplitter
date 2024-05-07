package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class MainWindowController extends DefaultController implements IsObserver {
    @FXML private Label welcomeMessage;

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.MAIN_WINDOW_MSG;
        contactRepository.addObserver(this);
    }

    /**
     * @inheritDoc Executes update method  before the stage is loaded.
     */
    @Override
    public void onBeforeStage() {
        super.onBeforeStage();
        update();
    }

    /**
     * @inheritDoc Update the welcome message
     */
    @Override
    public void update() {
        if (!(contactRepository.getProfile() == null)) {
            welcomeMessage.setText("Welcome " + contactRepository.getProfile().getFirstName());
        }
    }

    /**
     * Opens the contact list window.
     */
    @FXML
    public void openContactList() {
        switchScene(Pages.CONTACT_LIST_WINDOW, Pages.MAIN_WINDOW);
    }

    /**
     * Opens the transactions window.
     */
    @FXML
    public void openTransactions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Currently not available");
        alert.setContentText("This feature will be available in the next version.");

        alert.showAndWait();
    }

    /**
     * Opens the add receipt window.
     */
    @FXML
    public void addReceipt() {
        prepareListItemDebugTestData();

        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    // TODO: Only used for testing, remove in the end
    private void prepareListItemDebugTestData() {
        Receipt receipt = new Receipt(new ArrayList<>() {{
            add(new ReceiptItem(1.0F, "Test Item 1", 1));
            add(new ReceiptItem(2.0F, "Test Item 2", 2));
            add(new ReceiptItem(3.0F, "Test Item 3", 3));
        }});

        receiptProcessor.setReceipt(receipt);
    }

    // TODO: Only used for testing, remove in the end
    private boolean prepareDebugTestData() {
        List<Contact> contacts = contactRepository.getContacts();

        if (contacts.isEmpty()) {
            return false;
        }

        Receipt receipt = new Receipt(new ArrayList<>() {{
            add(new ReceiptItem(10.0f, "Item 1", 1));
            add(new ReceiptItem(10.0f, "Item 1", 1));
            add(new ReceiptItem(20.0f, "Item 2", 1));
            add(new ReceiptItem(20.0f, "Item 2", 1));
            add(new ReceiptItem(20.0f, "Item 2", 1));
            add(new ReceiptItem(30.0f, "Item 3", 1));

            add(new ReceiptItem(30.0f, "Item 3", 1));
            add(new ReceiptItem(30.0f, "Item 3", 1));
            add(new ReceiptItem(30.0f, "Item 3", 1));
            add(new ReceiptItem(40.0f, "Item 4", 1));
            add(new ReceiptItem(40.0f, "Item 4", 1));
        }});
        receiptProcessor.setReceipt(receipt);

        /*Contact firstContact = contacts.getFirst();
        receiptProcessor.createContactReceiptItem(firstContact, receipt.getReceiptItems().getFirst());
        receiptProcessor.createContactReceiptItem(firstContact, receipt.getReceiptItems().get(2));
        receiptProcessor.createContactReceiptItem(firstContact, receipt.getReceiptItems().get(5));
        receiptProcessor.createContactReceiptItem(firstContact, receipt.getReceiptItems().get(9));

        Contact secondContact = contacts.get(1);
        receiptProcessor.createContactReceiptItem(secondContact, receipt.getReceiptItems().get(4));
        receiptProcessor.createContactReceiptItem(secondContact, receipt.getReceiptItems().get(6));
        receiptProcessor.createContactReceiptItem(secondContact, receipt.getReceiptItems().get(7));

        Contact thirdContact = contacts.get(2);
        receiptProcessor.createContactReceiptItem(thirdContact, receipt.getReceiptItems().get(1));
        receiptProcessor.createContactReceiptItem(thirdContact, receipt.getReceiptItems().get(3));
        receiptProcessor.createContactReceiptItem(thirdContact, receipt.getReceiptItems().get(8));
        receiptProcessor.createContactReceiptItem(thirdContact, receipt.getReceiptItems().get(10));*/
        return true;
    }


    @FXML
    public void back() {
        switchScene(Pages.LOGIN_WINDOW);
    }
}
