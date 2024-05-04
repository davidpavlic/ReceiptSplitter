package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.*;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class EditContactController extends DefaultController implements CanNavigate, HasDynamicLastPage, CanReset {
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
    public void refreshScene() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        reset();
        switchScene(lastPage);
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

        confirmButton.setDisable(anyEmpty || !emailValid);
        emailErrorLabel.setVisible(!emailValid && !emailInput.getText().isEmpty());
    }
}
