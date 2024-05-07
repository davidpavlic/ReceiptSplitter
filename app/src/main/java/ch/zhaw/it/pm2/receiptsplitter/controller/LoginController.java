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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class LoginController extends DefaultController implements IsObserver{

    public static final String PROFILE_NOT_SET_ERROR_MESSAGE = "Could not set the selected profile. Please try again.";
    public static final String SELECTED_PROFILE_NO_EMAIL_ADDRESS_ERROR_MESSAGE = "The selected profile has no email address. Please select another profile.";
    public static final String SELECT_PROFILE_ERROR_MESSAGE = "Please select a profile";

    @FXML private Button confirmButton;
    @FXML private ComboBox<Contact> selectContactDropdown;

    @FXML private HBox errorMessageBox;
    @FXML private Label errorMessageLabel;

    /**
     *
     * @param router The router to be used for navigation.
     * @param contactRepository The repository to be used for contact management.
     * @param receiptProcessor The processor to be used for receipt processing.
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;
        contactRepository.addObserver(this);

        configureDropdown();
        confirmButton.setDisable(true);
        confirmButton.setOnAction(event -> confirm());

        errorMessageProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showErrorMessage(newValue);
        });
    }

    /**
     * @inheritDoc Executes update method before the stage is loaded.
     */
    @Override
    public void onBeforeStage() {
        super.onBeforeStage();
        update();
    }

    /**
     * @inheritDoc Update the contact dropdown list
     */
    @Override
    public void update() {
        Contact selectedContact = selectContactDropdown.getValue();
        selectContactDropdown.getItems().clear();
        selectContactDropdown.getItems().addAll(contactRepository.getContacts());
        if (contactRepository.getProfile() != null) {
            selectContactDropdown.setValue(contactRepository.getProfile());
        } else {
            selectContactDropdown.setValue(selectedContact);
        }
    }

    @FXML
    private void closeWindow() {
        router.closeWindow();
    }

    @FXML
    private void openCreateProfile() {
        switchScene(Pages.CREATE_PROFILE_WINDOW, Pages.LOGIN_WINDOW);
    }

    @FXML
    private void confirm() {
        if (selectContactDropdown.getValue() == null) {
            errorMessageProperty.set(SELECT_PROFILE_ERROR_MESSAGE);
            logger.fine("No contact selected");
            return;
        }

        String selectedEmail = selectContactDropdown.getValue().getEmail();
        if (selectedEmail == null ||  selectedEmail.isEmpty()){
            logger.fine("Email is empty");
            errorMessageProperty.set(SELECTED_PROFILE_NO_EMAIL_ADDRESS_ERROR_MESSAGE);
            return;
        }

        boolean success = contactRepository.setProfile(selectedEmail);

        if (!success){
            logger.fine("Could not set profile");
            errorMessageProperty.set(PROFILE_NOT_SET_ERROR_MESSAGE);
            return;
        }

        switchScene(Pages.MAIN_WINDOW);
    }

    @FXML
    private void closeErrorMessage() {
        errorMessageBox.setVisible(false);
        errorMessageBox.setManaged(false);
        errorMessageProperty.set(null);
    }

    private void configureDropdown() {
        selectContactDropdown.setPromptText("Please choose a profile");
        selectContactDropdown.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> confirmButton.setDisable(newValue == null));

        ContactDropdownConfigurer.configureComboBox(selectContactDropdown);
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageBox.setVisible(true);
        errorMessageBox.setManaged(true);
    }
}
