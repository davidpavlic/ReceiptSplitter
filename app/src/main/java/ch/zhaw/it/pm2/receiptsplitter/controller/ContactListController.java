package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;

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
        ObservableList<Contact> data = FXCollections.observableArrayList(contactRepository.getContactList());
        tableContactList.setItems(data);
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
        ObservableList<Contact> data = FXCollections.observableArrayList(contactRepository.getContactList());
        tableContactList.setItems(data);

        actionColumn.setMaxWidth(130);
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Contact, String> call(TableColumn<Contact, String> param) {
                return new TableCell<>() {
                    final ComboBox<String> comboBox = new ComboBox<>();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            comboBox.setPromptText("Select Action");
                            comboBox.getItems().setAll("Edit", "Delete");
                            comboBox.setOnAction(event -> {
                                String selectedAction = comboBox.getSelectionModel().getSelectedItem();
                                if ("Edit".equals(selectedAction)) {
                                    // Perform edit action
                                } else if ("Delete".equals(selectedAction)) {
                                    Contact contact = getTableView().getItems().get(getIndex());
                                    try {
                                        contactRepository.removeContact(contact.getEmail());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            // Disable the ComboBox if the current row's Contact is the selected profile
                            //Contact contact = getTableView().getItems().get(getIndex());
                            //comboBox.setDisable(contact.equals(contactRepository.getProfile()));
                            setGraphic(comboBox);
                        }
                    }
                };
            }
        });
    }
}
