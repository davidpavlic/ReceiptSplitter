package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
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
    @FXML private ComboBox<Contact> selectUserDropdown;

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
        contactRepository.setProfile(selectUserDropdown.getValue().getEmail());
        switchScene(Pages.MAIN_WINDOW);
    }

    @Override
    public void refreshScene() {
        selectUserDropdown.getItems().clear();
        selectUserDropdown.setPromptText("Please choose a profile");
        selectUserDropdown.getItems().addAll(contactRepository.getContactList());
    }

    private void configureDropdown() {
        // Add a listener to the selection modelclear
        selectUserDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue == null);
        });

        // Set the cell factory
        selectUserDropdown.setCellFactory(new Callback<>() {
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
        selectUserDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getDisplayName()); // Display the email
                }
            }
        });
    }
}
