package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HasDynamicLastPage;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NewContactController extends DefaultController implements CanNavigate, CanReset, HasDynamicLastPage {
    public static final String CONTACT_EMAIL_ALREADY_EXISTS_ERROR_MESSAGE = "Could not add contact: Email does already exist";
    public static final String CONTACTS_FILE_ACCESS_ERROR_MESSAGE = "An error occurred trying to access the contacts file.";
    public static final String CONTACTS_UPDATE_UNKNOWN_ERROR_MESSAGE = "An unknown error occurred while adding the contact.";
    private Pages lastPage;

    @FXML private Button confirmButton;
    @FXML private TextField emailInput;
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.lastPage = Pages.MAIN_WINDOW;
        this.helpMessage = HelpMessages.NEW_CONTACT_WINDOW_MSG;

        List<TextField> textFields = Arrays.asList(emailInput, firstNameInput, lastNameInput);

        // Add a listener that updates button state and checks email validity
        textFields.forEach(textField -> textField.textProperty().addListener((obs, oldVal, newVal) -> updateUIBasedOnValidation(textFields)));

        updateUIBasedOnValidation(textFields);
        confirmButton.setOnAction(event -> confirm());
    }

    @Override
    public void setLastPage(Pages lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public void confirm() {
        try {
            Contact contact = new Contact(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText());
            contactRepository.addContact(contact);
            reset();
            back();
            closeErrorMessage();
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.severe(illegalArgumentException.getMessage());
            errorMessageProperty.set(CONTACT_EMAIL_ALREADY_EXISTS_ERROR_MESSAGE);
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
            logger.fine(Arrays.toString(ioException.getStackTrace()));
            errorMessageProperty.set(CONTACTS_FILE_ACCESS_ERROR_MESSAGE);
        } catch (Exception exception) {
            logger.severe("Error adding contact: " + exception.getMessage());
            logger.fine(Arrays.toString(exception.getStackTrace()));
            errorMessageProperty.set(CONTACTS_UPDATE_UNKNOWN_ERROR_MESSAGE);
        }
    }

    @FXML
    @Override
    public void back() {
        switchScene(lastPage);
    }

    @FXML
    @Override
    public void reset() {
        emailInput.clear();
        firstNameInput.clear();
        lastNameInput.clear();
    }

    private void updateUIBasedOnValidation(List<TextField> textFields) {
        boolean anyEmpty = textFields.stream()
                .anyMatch(field -> field.getText().trim().isEmpty());

        boolean emailValid = EmailService.isValidMail(emailInput.getText());

        if (!emailValid && !emailInput.getText().isEmpty()) {
            errorMessageProperty.set("Please enter a valid email address.");
        } else {
            closeErrorMessage();
        }

        confirmButton.setDisable(anyEmpty || !emailValid);
    }
}
