package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class is the controller for the Show Split Window.
 * It is responsible for displaying the split of the receipt to the user.
 * The user can navigate through the different contacts and see the items they have to pay for.
 * The user can also send out the request to the recipients via email.
 *
 * @Author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class ShowSplitController extends DefaultController implements CanNavigate, IsObserver {
    @FXML private Button buttonPreviousPerson;
    @FXML private Button buttonNextPerson;
    @FXML private Text contactName;
    @FXML private Text totalPrice;

    @FXML private Button backButton;
    @FXML private Button confirmButton;

    @FXML private TableView<ContactReceiptItem> itemsTable;
    @FXML private TableColumn<ContactReceiptItem, String> itemNameColumn;
    @FXML private TableColumn<ContactReceiptItem, String> itemPriceColumn;
    @FXML private ProgressIndicator spinner;

    private List<Contact> uniqueContacts;
    private Contact currentContact;


    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.SHOW_SPLIT_WINDOW_MSG;
        contactRepository.addObserver(this);
        buttonNextPerson.setOnAction(event -> nextPerson());
        buttonPreviousPerson.setOnAction(event -> previousPerson());
        buttonNextPerson.setDisable(true);
        buttonPreviousPerson.setDisable(true);

        configureTable();

        String formattedInitialPrice = receiptProcessor.formatPriceWithCurrency(0);
        totalPrice.setText(formattedInitialPrice);
        uniqueContacts = new ArrayList<>();
    }

    /**
     * {@inheritDoc} Executes update method before the stage is loaded.
     */
    @Override
    public void onBeforeStage() {
        super.onBeforeStage();
        update();
    }

    /**
     * {@inheritDoc} Updates the TableView with the ContactItems of the first Contact in the list.
     */
    @Override
    public void update() {
        if (receiptProcessor.getDistinctContacts().isEmpty()) {
            return;
        }
        currentContact = receiptProcessor.getDistinctContacts().getFirst();
        populateTableWithContactItems(currentContact);
    }


    /**
     * {@inheritDoc}
     *
     *  Handles the Confirmation of the Receipt and sends out the Request to the Recipients via Email.
     */
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
        closeErrorMessage();
    }

    /**
     * {@inheritDoc} Switches to the Main Window.
     */
    @Override
    public void back() {
        switchScene(Pages.ALLOCATE_ITEMS_WINDOW);
    }

    private void configureTable() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemPriceColumn.setCellValueFactory(cellItem -> {
            ContactReceiptItem item = cellItem.getValue();

            float unitPrice = item.getPrice();
            String formattedUnitPrice = receiptProcessor.formatPriceWithCurrency(unitPrice);

            return new SimpleStringProperty(formattedUnitPrice);
        });
    }

    private void populateTableWithContactItems(Contact contact) {
        ObservableList<ContactReceiptItem> items = FXCollections.observableArrayList(
                receiptProcessor.getContactReceiptItems().stream()
                        .filter(item -> item.getContact().equals(contact))
                        .collect(Collectors.toList())
        );

        itemsTable.setItems(items);
        contactName.setText(contact.getDisplayName());

        float totalPriceOfContact = receiptProcessor.calculateDebtByPerson(contact);

        String formattedTotalPrice = receiptProcessor.formatPriceWithCurrency(totalPriceOfContact);
        this.totalPrice.setText(formattedTotalPrice);

        this.uniqueContacts = receiptProcessor.getDistinctContacts();
        int currentIndex = uniqueContacts.indexOf(contact);

        buttonNextPerson.setDisable(currentIndex >= uniqueContacts.size() - 1);
        buttonPreviousPerson.setDisable(currentIndex <= 0);
    }

    @FXML
    private void nextPerson() {
        if (uniqueContacts.isEmpty()) {
            logger.fine("No contacts available to switch to");
            return;
        }

        int currentIndex = uniqueContacts.indexOf(currentContact);
        if (currentIndex < uniqueContacts.size() - 1) {
            currentContact = uniqueContacts.get(currentIndex + 1);
            populateTableWithContactItems(currentContact);
        }
    }

    @FXML
    private void previousPerson() {
        if (uniqueContacts.isEmpty()) {
            logger.fine("No contacts available to switch to");
            return;
        }

        int currentIndex = uniqueContacts.indexOf(currentContact);
        if (currentIndex > 0) {
            currentContact = uniqueContacts.get(currentIndex - 1);
            populateTableWithContactItems(currentContact);
        }
    }

    private void setSpinnerActive(boolean active) {
        spinner.setVisible(active);
        backButton.setDisable(active);
        confirmButton.setDisable(active);
        itemsTable.setDisable(active);
        buttonNextPerson.setDisable(active);
        buttonPreviousPerson.setDisable(active);
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
                    setSpinnerActive(false);

                    if (success) {
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
                logError("Failed to send email", e);
                return false;
            }
        }
        return true;
    }

    private String buildEmail(Contact recipient, Contact requester) {
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("<html><body>");
        emailBody.append("<h1>Dear ").append(recipient.getFirstName()).append(" - You have a new Request</h1>");
        emailBody.append("<p>").append(requester.getDisplayName()).append(" has sent you a Request, please see the detailed List below</p>");

        emailBody.append("<ul>");

        List<ContactReceiptItem> receiptItemsOfRecipients = receiptProcessor.getContactReceiptItems().stream()
                .filter(i -> i.getContact().equals(recipient))
                .toList();

        for (ContactReceiptItem item : receiptItemsOfRecipients) {
            String formattedPrice = receiptProcessor.formatPriceWithCurrency(item.getPrice());
            emailBody.append("<li>")
                    .append(item.getName())
                    .append(" - ")
                    .append(formattedPrice)
                    .append("</li>");
        }

        emailBody.append("</ul>");

        float totalPriceOfRecipient = receiptProcessor.calculateDebtByPerson(recipient);
        String formattedTotalPrice = receiptProcessor.formatPriceWithCurrency(totalPriceOfRecipient);

        emailBody.append("<p>Total: ")
                .append(formattedTotalPrice)
                .append("</p>");
        emailBody.append("</body></html>");

        return emailBody.toString();
    }
}
