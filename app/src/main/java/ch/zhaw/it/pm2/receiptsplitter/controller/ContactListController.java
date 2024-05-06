package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HasDynamicLastPage;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Arrays;

public class ContactListController extends DefaultController implements CanNavigate, HasDynamicLastPage, CanReset, IsObserver {

    @FXML private TableColumn<Contact, String> actionColumn;
    @FXML private TableColumn<Contact, String> emailColumn;
    @FXML private TableColumn<Contact, String> nameColumn;
    @FXML private TableView<Contact> tableContactList;
    private Pages lastPage;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.CONTACT_LIST_WINDOW_MSG;
        contactRepository.addObserver(this);
        this.lastPage = Pages.MAIN_WINDOW;
        configureTable();
    }

    @Override
    public void update() {
        tableContactList.setItems(FXCollections.observableArrayList(contactRepository.getContacts()));
        tableContactList.refresh();
    }

    @FXML
    public void openCreateProfile() {
        router.gotoScene(Pages.CREATE_PROFILE_WINDOW, Pages.CONTACT_LIST_WINDOW);
    }

    @FXML
    @Override
    public void confirm() {
        switchScene(Pages.MAIN_WINDOW);
    }

    @FXML
    @Override
    public void back() {
        switchScene(lastPage);
    }

    @Override
    public void reset() {
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
            }
        } catch (IOException e) {
            logError("Error removing contact", e);
        }
    }

    private void logError(String message, Exception e) {
        logger.severe(message);
        if (e != null) {
            logger.fine(Arrays.toString(e.getStackTrace()));
        }
        // TODO: Show error message to user
    }

    @Override
    public void setLastPage(Pages page) {
        this.lastPage = page;
    }
}
