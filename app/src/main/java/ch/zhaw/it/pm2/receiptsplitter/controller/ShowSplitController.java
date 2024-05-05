package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ShowSplitController extends DefaultController implements CanNavigate, IsObserver {
    @FXML private Button buttonPreviousPerson;
    @FXML private Button buttonNextPerson;
    @FXML private Text contactName;
    @FXML private Text totalPrice;

    @FXML private TableView<ContactReceiptItem> itemsTable;
    @FXML private TableColumn<ContactReceiptItem, String> itemNameColumn;
    @FXML private TableColumn<ContactReceiptItem, Double> itemPriceColumn;

    private Contact currentContact;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.SHOW_SPLIT_WINDOW_MSG;
        contactRepository.addObserver(this);
        //TODO: Remove this
        try {
            setupTestData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buttonNextPerson.setOnAction(event -> nextPerson());
        buttonPreviousPerson.setOnAction(event -> previousPerson());
        configureTable();

    }

    private void setupTestData() throws IOException {
        ReceiptItem item1 = new ReceiptItem(10, "Coffee", 1);
        ReceiptItem item2 = new ReceiptItem(15, "Pommes", 1);
        ReceiptItem item3 = new ReceiptItem(5, "Water", 2);
        ReceiptItem item4 = new ReceiptItem(20, "Cake", 2);
        ReceiptItem item5 = new ReceiptItem(20, "Schweine Fleisch", 3);

        // Create two ContactReceiptItem objects
        receiptProcessor.createContactReceiptItem(contactRepository.getContacts().get(0), item1);
        receiptProcessor.createContactReceiptItem(contactRepository.getContacts().get(0), item2);
        receiptProcessor.createContactReceiptItem(contactRepository.getContacts().get(0), item3);
        receiptProcessor.createContactReceiptItem(contactRepository.getContacts().get(1), item4);
        receiptProcessor.createContactReceiptItem(contactRepository.getContacts().get(1), item5);
    }

    public void update() {
        currentContact = receiptProcessor.getDistinctContacts().get(0);
        populateTableWithContactItems(currentContact);
    }

    @Override
    public void confirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to send out the emails?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.showAndWait().ifPresent(response -> {
            alert.hide();
            if (response == ButtonType.YES) {
                buildAndSendEmail();
            }
        });
    }

    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
    }

    private void configureTable() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void populateTableWithContactItems(Contact contact) {
        ObservableList<ContactReceiptItem> items = FXCollections.observableArrayList(
                receiptProcessor.getContactReceiptItems().stream()
                        .filter(item -> item.getContact().equals(contact))
                        .collect(Collectors.toList())
        );
        itemsTable.setItems(items);
        contactName.setText(contact.getDisplayName());

        double totalAmount = receiptProcessor.calculateDebtByPerson(contact);
        totalPrice.setText("Total: " + String.format("%.2f", totalAmount));

        List<Contact> uniqueContacts = receiptProcessor.getDistinctContacts();
        int currentIndex = uniqueContacts.indexOf(contact);

        buttonNextPerson.setDisable(currentIndex >= uniqueContacts.size() - 1);
        buttonPreviousPerson.setDisable(currentIndex <= 0);
    }

    @FXML
    private void nextPerson() {
        List<Contact> uniqueContacts = receiptProcessor.getDistinctContacts();

        int currentIndex = uniqueContacts.indexOf(currentContact);
        if (currentIndex < uniqueContacts.size() - 1) {
            currentContact = uniqueContacts.get(currentIndex + 1);
            populateTableWithContactItems(currentContact);
        }
    }

    @FXML
    private void previousPerson() {
        List<Contact> uniqueContacts = receiptProcessor.getDistinctContacts();

        int currentIndex = uniqueContacts.indexOf(currentContact);
        if (currentIndex > 0) {
            currentContact = uniqueContacts.get(currentIndex - 1);
            populateTableWithContactItems(currentContact);
        }
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    private void buildAndSendEmail() {
        String body = buildEmail();
        if (sendEmails(body)) {
            Alert alert = createAlert(Alert.AlertType.INFORMATION, "Emails Sent", "Emails have been sent out successfully", "The Request has been sent out successfully. Please make sure to check your Spam Folder");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.hide();
                    switchScene(Pages.MAIN_WINDOW);
                }
            });
        } else {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Issue Sending Email", null, "We encountered an Issue while trying to send out the Request. Please try again later.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.hide();
                }
            });
        }
    }

    private String buildEmail() {
        return "This is a test Body";
    }

    private boolean sendEmails(String body) {
        return true;
   /*     EmailService emailService = new EmailService();
        try {
            return emailService.sendEmail(contactRepository.getProfile().getEmail(), "Receipt Splitter - You have a new Request", body);
        } catch (Exception e) {
            return false;
        }*/
    }

}
