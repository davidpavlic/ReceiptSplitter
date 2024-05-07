package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.utils.ContactDropdownConfigurer;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
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
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AllocateItemsController extends DefaultController implements IsObserver, CanNavigate, CanReset {
    @FXML private TableView<Combination> contactItemTable;
    @FXML private TableColumn<Combination, String> itemColumn;
    @FXML private TableColumn<Combination, String> priceColumn;
    @FXML private TableColumn<Combination, String> dropdown;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ALLOCATE_ITEMS_WINDOW_MSG;
        contactRepository.addObserver(this);
        receiptProcessor.addObserver(this);
    }

    @Override
    public void update() {
        ComboBox<Contact> contactComboBox = new ComboBox<>();

        List<Combination> receiptItems = new ArrayList<>();
        for (ReceiptItem receiptItem : receiptProcessor.getReceiptItems()) {
            for (int index = 0; index < receiptItem.getAmount(); index++) {
                receiptItems.add(new Combination(receiptItem.getName(), Math.round(receiptItem.getPrice()/receiptItem.getAmount() * 100) / 100F, "", contactComboBox));
            }
        }

        contactItemTable.setItems(FXCollections.observableArrayList(receiptItems));
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().receiptItemProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        dropdown.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    comboBox.setItems(FXCollections.observableArrayList(
                            contactRepository.getContacts().stream()
                                    .map(Contact::getDisplayName)
                                    .collect(Collectors.toList())
                    ));
                    comboBox.setMaxWidth(160);
                    comboBox.setPromptText("Choose a Contact");
                    setGraphic(comboBox);
                }
            }
        });

        contactItemTable.setEditable(true);
    }

    /**
     * Switches to the next scene.
     */
    @Override
    public void confirm() {
    }

    /**
     * Switches to the previous scene.
     */
    @Override
    public void back() {
        router.gotoScene(Pages.MAIN_WINDOW);
    }

    @Override
    public void reset() {
        update();
    }

    // Data model class for a table row
    public static class Combination {
        private final SimpleStringProperty receiptItem;
        private final SimpleStringProperty price;
        private final SimpleStringProperty users;
        private final ComboBox<Contact> contactComboBox;

        public Combination(String receiptItem, float price, String users, ComboBox<Contact> contactComboBox) {
            this.receiptItem = new SimpleStringProperty(receiptItem);
            this.price = new SimpleStringProperty(Float.toString(price));
            this.users = new SimpleStringProperty(users);
            this.contactComboBox = contactComboBox;
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

        public ComboBox<Contact> getContactComboBox() {
            return contactComboBox;
        }

        public void updateContactComboBox(List<Contact> contacts) {
            contactComboBox.getItems().clear();
            contactComboBox.getItems().addAll(contacts);
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