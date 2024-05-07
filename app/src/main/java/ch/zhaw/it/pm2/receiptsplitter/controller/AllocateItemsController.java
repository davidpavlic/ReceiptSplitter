package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.utils.ContactDropdownConfigurer;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;



public class AllocateItemsController extends DefaultController implements IsObserver {
    @FXML private TableView<Combination> contactItemTable;
    @FXML private TableColumn<Combination, String> itemColumn;
    @FXML private TableColumn<Combination, String> priceColumn;
    @FXML private TableColumn<Combination, String> dropdown;

    ObservableList<Combination> contactList = FXCollections.observableArrayList();

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ALLOCATE_ITEMS_WINDOW_MSG;
    }

    @Override
    public void update() {
        // Populate a list of all available contacts
        ObservableList<String> contacts = FXCollections.observableArrayList();

        for(Contact contact : contactRepository.getSelectedContacts()){
            contacts.add(contact.getDisplayName());
        }

        List<Combination> receiptItems = new ArrayList<>();
        for (ReceiptItem receiptItem : receiptProcessor.getReceiptItems()) {
            for (int index = 0; index < receiptItem.getAmount(); index++) {
                receiptItems.add(new Combination(receiptItem.getName(), Math.round(receiptItem.getPrice()/receiptItem.getAmount() * 100) / 100F, "", contacts));
            }
        }

        contactItemTable.setItems(FXCollections.observableArrayList(receiptItems));
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().receiptItemProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        dropdown.setCellValueFactory(cellData -> cellData.getValue().usersProperty());

        // Use a custom cell factory for the ComboBox column
        dropdown.setCellFactory(new ComboBoxTableCellFactory(contacts));
    }

    // Custom TableCell factory for ComboBox
    private static class ComboBoxTableCellFactory implements Callback<TableColumn<Combination, String>, TableCell<Combination, String>> {
        private final ObservableList<String> contacts;

        public ComboBoxTableCellFactory(ObservableList<String> contacts) {
            this.contacts = contacts;
        }

        @Override
        public TableCell<Combination, String> call(TableColumn<Combination, String> param) {
            return new ComboBoxTableCell();
        }

        private class ComboBoxTableCell extends TableCell<Combination, String> {
            private final ComboBox<String> comboBox;

            public ComboBoxTableCell() {
                comboBox = new ComboBox<>(contacts);
                comboBox.setPrefWidth(140.0);
                comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        getTableRow().getItem().setUsers(newVal);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                }
            }
        }
    }

    // Data model class for a table row
    public static class Combination {
        private final SimpleStringProperty receiptItem;
        private final SimpleStringProperty price;
        private final SimpleStringProperty users;
        private final ObservableList<String> availableContacts;

        public Combination(String receiptItem, float price, String users, ObservableList<String> availableContacts) {
            this.receiptItem = new SimpleStringProperty(receiptItem);
            this.price = new SimpleStringProperty(Float.toString(price));
            this.users = new SimpleStringProperty(users);
            this.availableContacts = availableContacts;
        }

        public void setReceiptItem(String receiptItem) {
            this.receiptItem.set(receiptItem);
        }

        public SimpleStringProperty receiptItemProperty() {
            return receiptItem;
        }

        public void setPrice(String price) {
            this.price.set(price);
        }

        public SimpleStringProperty priceProperty() {
            return price;
        }

        public void setUsers(String users) {
            this.users.set(users);
        }

        public SimpleStringProperty usersProperty() {
            return users;
        }
    }
































       /*
    @Override
    public void confirm() {
        switchScene(Pages.SHOW_SPLIT_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @Override
    public void reset() {}
    //TODO Implement initialize method after Contact Repository is implemented

    @Override
    public void initialize(Router router) {

        receipt = new Receipt();
        ObservableList<ReceiptItem> receiptItems = receipt.getReceiptItems();

        contactRepository = new ContactRepository();
        ObservableList<Contact> contacts = FXCollections.observableArrayList(contactRepository.getContactList());

        contactItemTable.setItems(receiptItems);
        itemColumn = new TableColumn<>("Items");

        itemColumn.setCellFactory(ComboBoxTableCell.forTableColumn(contacts);


            for (ReceiptItem item : receiptItems){
                ComboBox<Contact> dropdown = new ComboBox<>();
                dropdown.setPromptText("Choose a contact");

            }

        }
        */
}