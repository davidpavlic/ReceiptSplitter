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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ShowSplitController extends DefaultController implements CanNavigate, IsObserver {
    @FXML
    private Button buttonPreviousPerson;
    @FXML
    private Button buttonNextPerson;
    @FXML
    private Text contactName;
    @FXML
    private Text totalPrice;

    @FXML
    private TableView<ContactReceiptItem> itemsTable;
    @FXML
    private TableColumn<ContactReceiptItem, String> itemNameColumn;
    @FXML
    private TableColumn<ContactReceiptItem, Double> itemPriceColumn;
    @FXML
    private ProgressIndicator spinner;


    List<Contact> uniqueContacts;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to send out the Request to the Recipients via Email?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Please Confirm");
        alert.showAndWait().ifPresent(response -> {
            alert.hide();
            if (response == ButtonType.YES) {
                handleConfirmationAndEmails();
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
        totalPrice.setText("Total: " + String.format("%.2f", totalAmount) + " CHF");

        this.uniqueContacts = receiptProcessor.getDistinctContacts();
        int currentIndex = uniqueContacts.indexOf(contact);

        buttonNextPerson.setDisable(currentIndex >= uniqueContacts.size() - 1);
        buttonPreviousPerson.setDisable(currentIndex <= 0);
    }

    @FXML
    private void nextPerson() {
        int currentIndex = uniqueContacts.indexOf(currentContact);
        if (currentIndex < uniqueContacts.size() - 1) {
            currentContact = uniqueContacts.get(currentIndex + 1);
            populateTableWithContactItems(currentContact);
        }
    }

    @FXML
    private void previousPerson() {
        int currentIndex = uniqueContacts.indexOf(currentContact);
        if (currentIndex > 0) {
            currentContact = uniqueContacts.get(currentIndex - 1);
            populateTableWithContactItems(currentContact);
        }
    }

    private void setSpinnerActive(boolean active) {
        spinner.setVisible(active);
        spinner.getScene().getRoot().setDisable(active);
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    private void handleConfirmationAndEmails() {
        setSpinnerActive(true);
        CompletableFuture.supplyAsync(this::buildAndSendEmails)
                .thenAccept(success -> Platform.runLater(() -> {
                    if (success) {
                        setSpinnerActive(false);
                        Alert alert = createAlert(Alert.AlertType.INFORMATION, "Emails Sent", "Emails have been sent out successfully", "The Request has been sent out successfully. Please make sure to check your Spam Folder");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                alert.hide();
                                switchScene(Pages.MAIN_WINDOW);
                            }
                        });
                    } else {
                        setSpinnerActive(false);
                        Alert alert = createAlert(Alert.AlertType.ERROR, "Issue Sending Email", null, "We encountered an Issue while trying to send out the Request. Please try again later.");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                alert.hide();
                            }
                        });
                    }
                }));
    }

    private boolean buildAndSendEmails() {
        EmailService emailService = new EmailService();
        Contact currentProfile = contactRepository.getProfile();

        for (Contact contact : uniqueContacts) {
            if (contact.equals(currentProfile)) {
                continue;
            }
            String body = buildEmail(contact, currentProfile);
            try {
                boolean success = emailService.sendEmail(contact.getEmail(), "Receipt Splitter - You have a new Request", body);
                if (!success) {
                    return false;
                }
            } catch (Exception e) {
                logger.severe("Failed to send email: " + e.getMessage());
                logger.fine(Arrays.toString(e.getStackTrace()));
                return false;
            }
        }
        return true;
    }

    private String buildEmail(Contact Recipient, Contact Requester) {
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("<html><body>");
        emailBody.append("<h1>Dear ").append(Recipient.getFirstName()).append(" - You have a new Request</h1>");
        emailBody.append("<p>").append(Requester.getDisplayName()).append(" has sent you a Request, please see the detailed List below</p>");

        emailBody.append("<ul>");
        for (ContactReceiptItem item : receiptProcessor.getContactReceiptItems().stream()
                .filter(i -> i.getContact().equals(Recipient))
                .toList()) {
            emailBody.append("<li>")
                    .append(item.getName())
                    .append(" - CHF")
                    .append(String.format("%.2f", item.getPrice()))
                    .append("</li>");
        }
        emailBody.append("</ul>");

        emailBody.append("<p>Total: CHF")
                .append(String.format("%.2f", receiptProcessor.calculateDebtByPerson(Recipient)))
                .append("</p>");
        emailBody.append("</body></html>");

        return emailBody.toString();
    }
}
