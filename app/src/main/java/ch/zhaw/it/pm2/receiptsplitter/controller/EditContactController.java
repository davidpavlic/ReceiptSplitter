package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HasDynamicLastPage;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the controller for the EditContact view.
 * It handles the editing of a contact's details and the validation of the input fields.
 * It implements the CanNavigate, HasDynamicLastPage, CanReset, and IsObserver interfaces.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0

 */
public class EditContactController extends DefaultController implements CanNavigate, HasDynamicLastPage, CanReset, IsObserver {
    public static final String EMAIL_NOT_EXISTS_ERROR_MESSAGE = "Could not update contact: Email does not exist";
    public static final String CONTACTS_FILE_ACCESS_ERROR_MESSAGE = "An error occurred trying to access the contacts file.";
    public static final String CONTACTS_UPDATE_UNKNOWN_ERROR_MESSAGE = "An unknown error occurred while updating the contact.";

    private Pages lastPage;

    @FXML private Button confirmButton;
    @FXML private TextField emailInput;
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;
    @FXML private Label emailErrorLabel;

    /**
     * {@inheritDoc} Sets a listener to the text fields to update the UI based on the validation.
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.lastPage = Pages.MAIN_WINDOW;
        this.helpMessage = HelpMessages.EDIT_CONTACT_WINDOW_MSG;
        List<TextField> textFields = Arrays.asList(emailInput, firstNameInput, lastNameInput);

        textFields.forEach(textField -> textField.textProperty().addListener((obs, oldVal, newVal) -> updateUIBasedOnValidation(textFields)));

        updateUIBasedOnValidation(textFields);
    }

    /**
     * {@inheritDoc}Executes update method before the stage is loaded.
     */
    @Override
    public void onBeforeStage() {
        super.onBeforeStage();
        update();
    }

    /**
     * {@inheritDoc} Update the contact fields
     */
    @Override
    public void update() {
        Contact contact = contactRepository.getSelectedToEditContact();
        emailInput.setText(contact.getEmail());
        firstNameInput.setText(contact.getFirstName());
        lastNameInput.setText(contact.getLastName());
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
     * Updates the contact with the new values.
     */
    @FXML
    @Override
    public void confirm() {
        try {
            Contact newContact = new Contact(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText());
            contactRepository.updateContact(contactRepository.getSelectedToEditContact().getEmail(), newContact);
            reset();
            back();
            closeErrorMessage();
        } catch (IllegalArgumentException illegalArgumentException) {
            logError(illegalArgumentException.getMessage(), illegalArgumentException);
            errorMessageProperty.set(EMAIL_NOT_EXISTS_ERROR_MESSAGE);
        } catch (IOException ioException) {
            logError(ioException.getMessage(), ioException);
            errorMessageProperty.set(CONTACTS_FILE_ACCESS_ERROR_MESSAGE);
        } catch (Exception exception) {
            logError("Error updating contact: " + exception.getMessage(), exception);
            errorMessageProperty.set(CONTACTS_UPDATE_UNKNOWN_ERROR_MESSAGE);
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
    {@inheritDoc}
    */
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

        confirmButton.setDisable(anyEmpty || !emailValid);
        emailErrorLabel.setVisible(!emailValid && !emailInput.getText().isEmpty());
    }
}
