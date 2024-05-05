package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.*;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EditContactController extends DefaultController implements CanNavigate, HasDynamicLastPage, CanReset, IsObserver {
    private Pages lastPage;

    @FXML private Button confirmButton;
    @FXML private TextField emailInput;
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;
    @FXML private Label emailErrorLabel;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.lastPage = Pages.MAIN_WINDOW;
        this.helpMessage = HelpMessages.EDIT_CONTACT_WINDOW_MSG;

        List<TextField> textFields = Arrays.asList(emailInput, firstNameInput, lastNameInput);

        // Add a listener that updates button state and checks email validity
        textFields.forEach(textField -> textField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateUIBasedOnValidation(textFields);
        }));

        updateUIBasedOnValidation(textFields);
        confirmButton.setOnAction(event -> confirm());
    }

    @Override
    public void update() {
        Contact contact = contactRepository.getSelectedToEditContact();
        emailInput.setText(contact.getEmail());
        firstNameInput.setText(contact.getFirstName());
        lastNameInput.setText(contact.getLastName());
    }

    @Override
    public void setLastPage(Pages lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public void confirm() {
        try {
            Contact newContact = new Contact(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText());
            contactRepository.updateContact(contactRepository.getSelectedToEditContact().getEmail(), newContact);
            reset();
            back();
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.severe(illegalArgumentException.getMessage());
            emailErrorLabel.setText("Could not update contact: Email does not exist");
        }  catch (IOException ioException) {
            logger.severe(ioException.getMessage());
            logger.fine(Arrays.toString(ioException.getStackTrace()));
            emailErrorLabel.setText("An error occurred trying to access the contacts file.");
        } catch (Exception exception) {
            logger.severe("Error updating contact: " + exception.getMessage());
            logger.fine(Arrays.toString(exception.getStackTrace()));
            emailErrorLabel.setText("An unknown error occurred while updating the contact.");
        }
    }

    @FXML
    @Override
    public void back() {
        try {
            switchScene(lastPage);
        } catch (IllegalStateException exception) {
            emailErrorLabel.setText("Could not switch to " + lastPage.toString() + " Window");
        }
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

        confirmButton.setDisable(anyEmpty || !emailValid);
        emailErrorLabel.setVisible(!emailValid && !emailInput.getText().isEmpty());
    }
}
