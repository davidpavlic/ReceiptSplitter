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
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ContactListController extends DefaultController implements CanNavigate, HasDynamicLastPage, CanReset, IsObserver {

    @FXML private TableColumn<Contact, String> actionColumn;
    @FXML private TableColumn<Contact, String> emailColumn;
    @FXML private TableColumn<Contact, String> nameColumn;
    @FXML private TableView<Contact> tableContactList;

    @FXML private HBox errorMessageBox;
    @FXML private Label errorMessageLabel;

    private Pages lastPage;

    /**
     * Initializes the Controller with the necessary dependencies and initial data.
     * Configures the contact list table too.
     *
     * @param router            The router to be used for navigation.
     * @param contactRepository The repository to be used for contact management.
     * @param receiptProcessor  The processor to be used for receipt processing.
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.CONTACT_LIST_WINDOW_MSG;
        contactRepository.addObserver(this);
        this.lastPage = Pages.MAIN_WINDOW; // Default last page
        configureTable();

        errorMessageProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showErrorMessage(newValue);
        });
    }

    /**
     * @inheritDoc Updates the contact list table.
     */
    @Override
    public void update() {
        tableContactList.setItems(FXCollections.observableArrayList(contactRepository.getContacts()));
        tableContactList.refresh();
    }

    /**
     * @inheritDoc Switches to the create profile window.
     */
    @FXML
    public void openCreateProfile() {
        router.gotoScene(Pages.CREATE_PROFILE_WINDOW, Pages.CONTACT_LIST_WINDOW);
    }

    /**
     * @inheritDoc Switches to the main window.
     */
    @FXML
    @Override
    public void confirm() {
        switchScene(Pages.MAIN_WINDOW);
    }

    /**
     * @inheritDoc Switches to the last page.
     */
    @FXML
    @Override
    public void back() {
        switchScene(lastPage);
    }

    @Override
    public void reset() {} // TODO: Implement? Or remove?

    /**
     * @param page The last page.
     * @inheritDoc Sets the last page.
     */
    @Override
    public void setLastPage(Pages page) {
        this.lastPage = page;
    }

    @FXML
    private void closeErrorMessage() {
        errorMessageBox.setVisible(false);
        errorMessageBox.setManaged(false);
        errorMessageProperty.set(null);
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageBox.setVisible(true);
        errorMessageBox.setManaged(true);
    }

    private void configureTable() {
        configureColumns();
        configureActionColumn();
    }

    private void configureColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void configureActionColumn() {
        actionColumn.setMaxWidth(140);
        actionColumn.setCellFactory(param -> createActionCell());
    }

    private TableCell<Contact, String> createActionCell() {
        return new TableCell<>() {
            final Button editButton = new Button("Edit");
            final Button deleteButton = new Button("Delete");
            final HBox hbox = new HBox(10, editButton, deleteButton);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    configureButtons();
                    setGraphic(hbox);
                }
            }

            private void configureButtons() {
                Contact contact = getTableView().getItems().get(getIndex());
                editButton.setOnAction(e -> handleEditAction(contact));
                deleteButton.setOnAction(e -> handleDeleteAction(contact));
                deleteButton.setDisable(contact.equals(contactRepository.getProfile()));
            }
        };
    }

    private void handleEditAction(Contact contact) {
        contactRepository.setSelectedToEditContact(contact);
        switchScene(Pages.EDIT_PROFILE_WINDOW, Pages.CONTACT_LIST_WINDOW);
    }

    private void handleDeleteAction(Contact contact) {
        try {
            if (!contactRepository.removeContact(contact.getEmail())) {
                logError("Could not remove contact", null);
                errorMessageProperty.set("Could not remove contact. Please try again.");
            }
        } catch (IOException e) {
            logError("Error removing contact", e);
            errorMessageProperty.set("An error occurred while removing the contact. Please try again.");
        }
    }
}
