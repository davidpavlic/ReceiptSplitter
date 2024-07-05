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

/**
 * This class is the controller for the NewContact view.
 * It handles the creation of new contacts and the validation of the input fields.
 * It implements the CanNavigate, CanReset, and HasDynamicLastPage interfaces.
 *
 * @Author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class NewContactController extends DefaultController implements CanNavigate, CanReset, HasDynamicLastPage {
    public static final String CONTACT_EMAIL_ALREADY_EXISTS_ERROR_MESSAGE = "Could not add contact: Email does already exist";
    public static final String CONTACTS_FILE_ACCESS_ERROR_MESSAGE = "An error occurred trying to access the contacts file.";
    public static final String CONTACTS_UPDATE_UNKNOWN_ERROR_MESSAGE = "An unknown error occurred while adding the contact.";
    private Pages lastPage;

    @FXML private Button confirmButton;
    @FXML private TextField emailInput;
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastPage(Pages lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * {@inheritDoc}
     *
     * This method adds a new contact to the contact repository.
     */
    @Override
    public void confirm() {
        try {
            Contact contact = new Contact(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText());
            contactRepository.addContact(contact);
            reset();
            back();
            closeErrorMessage();
        } catch (IllegalArgumentException illegalArgumentException) {
            logError(illegalArgumentException.getMessage(), illegalArgumentException);
            //errorMessageProperty.set(CONTACT_EMAIL_ALREADY_EXISTS_ERROR_MESSAGE);
        } catch (IOException ioException) {
            logError(ioException.getMessage(), ioException);
            //errorMessageProperty.set(CONTACTS_FILE_ACCESS_ERROR_MESSAGE);
        } catch (Exception exception) {
            logError("Error adding contact", exception);
           //errorMessageProperty.set(CONTACTS_UPDATE_UNKNOWN_ERROR_MESSAGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    @Override
    public void back() {
        switchScene(lastPage);
    }

    /**
     * {@inheritDoc}
     */
    @FXML  @Override
    public void reset() {
        emailInput.clear();
        firstNameInput.clear();
        lastNameInput.clear();
    }

    private void updateUIBasedOnValidation(List<TextField> textFields) {
        boolean anyEmpty = textFields.stream()
                .anyMatch(field -> field.getText().trim().isEmpty());

        boolean emailValid = EmailService.isValidMail(emailInput.getText());

        /*
        if (!emailValid && !emailInput.getText().isEmpty()) {
            errorMessageProperty.set("Please enter a valid email address.");
        } else {
            closeErrorMessage();
        }*/

        confirmButton.setDisable(anyEmpty || !emailValid);
    }
}
