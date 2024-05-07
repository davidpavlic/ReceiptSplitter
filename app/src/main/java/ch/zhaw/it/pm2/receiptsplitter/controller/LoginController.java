package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.utils.ContactDropdownConfigurer;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;


public class LoginController extends DefaultController implements IsObserver{
    @FXML private Button confirmButton;
    @FXML private ComboBox<Contact> selectContactDropdown;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;
        contactRepository.addObserver(this);

        configureDropdown();
        confirmButton.setDisable(true);
        confirmButton.setOnAction(event -> confirm());
    }

    @FXML
    void closeWindow() {
        router.closeWindow();
    }

    @FXML
    void openCreateProfile() {
        switchScene(Pages.CREATE_PROFILE_WINDOW, Pages.LOGIN_WINDOW);
    }

    @FXML
    void confirm() {
        if (selectContactDropdown.getValue() == null) {
            logger.fine("No contact selected");
            // TODO: Show error message
            return;
        }

        String selectedEmail = selectContactDropdown.getValue().getEmail();
        if (selectedEmail == null ||  selectedEmail.isEmpty()){
            logger.fine("Email is empty");
            //  TODO: Show error message
            return;
        }

        boolean success = contactRepository.setProfile(selectedEmail);

        if (!success){
            logger.fine("Could not set profile");
            // TODO: Show error message
            return;
        }

        switchScene(Pages.MAIN_WINDOW);
    }

    @Override
    public void update() {
        Contact exists = selectContactDropdown.getValue();
        selectContactDropdown.getItems().clear();
        selectContactDropdown.getItems().addAll(contactRepository.getContacts());
        if (contactRepository.getProfile() != null) {
            selectContactDropdown.setValue(contactRepository.getProfile());
        } else {
            selectContactDropdown.setValue(exists);
        }
    }

    private void configureDropdown() {
        selectContactDropdown.setPromptText("Please choose a profile");
        selectContactDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue == null);
        });

        ContactDropdownConfigurer.configureComboBox(selectContactDropdown);
    }
}
