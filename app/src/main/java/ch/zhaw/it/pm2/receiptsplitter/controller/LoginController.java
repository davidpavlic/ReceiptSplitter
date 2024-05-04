package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;

public class LoginController extends DefaultController {
    private ContactRepository contactRepository;
    @FXML private Button confirmButton;
    @FXML private ComboBox<Contact> selectUserDropdown;

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;
        this.contactRepository = new ContactRepository(Objects.requireNonNull(getClass().getResource("/contacts.csv")).getPath());
        try {
            contactRepository.loadContacts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        contactRepository.getContactList().addListener((ListChangeListener.Change<? extends Contact> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    selectUserDropdown.getItems().addAll(c.getAddedSubList());
                }
            }
        });

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

    public ContactRepository getContactRepository() {
        return contactRepository;
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

        selectUserDropdown.setPromptText("Please choose a profile");
        selectUserDropdown.getItems().addAll(contactRepository.getContactList());
    }
}
