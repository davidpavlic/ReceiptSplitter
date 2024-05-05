package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class LoginController extends DefaultController {
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
    public void refreshScene() {
        selectContactDropdown.getItems().clear();
        selectContactDropdown.getItems().addAll(contactRepository.getContacts());
        selectContactDropdown.setPromptText("Please choose a profile");
        if (contactRepository.getProfile() != null) {
            selectContactDropdown.setValue(contactRepository.getProfile());
        }
    }

    private void configureDropdown() {
        selectContactDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue == null);
        });

        // Set the cell factory
        selectContactDropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getDisplayName());
                        }
                    }
                };
            }
        });

        // Set the button cell, which is the cell that is displayed when the ComboBox is not showing its list of items
        selectContactDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
    }
}
