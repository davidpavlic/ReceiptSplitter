package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Arrays;

public class ContactListController extends DefaultController implements CanNavigate, CanReset {

    @FXML private TableColumn<Contact, String> actionColumn;
    @FXML private TableColumn<Contact, String> emailColumn;
    @FXML private TableColumn<Contact, String> nameColumn;
    @FXML private TableView<Contact> tableContactList;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.CONTACT_LIST_WINDOW_MSG;
        contactRepository.addObserver(this);
        configureTable();
    }

    @Override
    public void refreshScene() {
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
        switchScene(Pages.MAIN_WINDOW);
    }

    @Override
    public void reset() {
    }

    private void configureTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        actionColumn.setMaxWidth(140);
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Contact, String> call(TableColumn<Contact, String> param) {
                return new TableCell<>() {
                    final Button editButton = new Button("Edit");
                    final Button deleteButton = new Button("Delete");
                    final HBox hbox = new HBox(editButton, deleteButton);

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            return;
                        }

                        hbox.setSpacing(10);
                        editButton.setPrefWidth(60);
                        deleteButton.setPrefWidth(60);

                        var items = getTableView().getItems();
                        var index = getIndex();
                        Contact contact = items.get(index);

                        editButton.setOnAction(event -> {
                            contactRepository.setSelectedToEditContact(contact);
                            try {
                                switchScene(Pages.EDIT_PROFILE_WINDOW, Pages.CONTACT_LIST_WINDOW);
                            } catch (IllegalStateException | IllegalArgumentException e) {
                                logger.severe("Error Editing profile: " + e.getMessage());
                                logger.fine(Arrays.toString(e.getStackTrace()));
                                // TODO: Show error message to user
                            }
                        });

                        deleteButton.setOnAction(event -> {
                            try {
                                boolean success = contactRepository.removeContact(contact.getEmail());

                                if (!success) {
                                    logger.severe("Could not remove contact");
                                    // TODO: Show error message to user
                                }

                            } catch (IOException e) {
                                logger.severe("Error removing contact: " + e.getMessage());
                                logger.fine(Arrays.toString(e.getStackTrace()));
                                // TODO: Show error message to user
                            }
                        });

                        deleteButton.setDisable(contact.equals(contactRepository.getProfile()));
                        setGraphic(hbox);
                    }
                };
            }
        });
    }
}
