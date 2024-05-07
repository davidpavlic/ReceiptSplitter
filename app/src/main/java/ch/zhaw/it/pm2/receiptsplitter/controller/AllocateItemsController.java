package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;


public class AllocateItemsController extends DefaultController implements IsObserver, CanNavigate, CanReset {
    @FXML private TableView<Combination> contactItemTable;
    @FXML private TableColumn<Combination, String> itemColumn;
    @FXML private TableColumn<Combination, String> priceColumn;
    @FXML private TableColumn<Combination, String> dropdown;
    @FXML private Button confirmButton;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ALLOCATE_ITEMS_WINDOW_MSG;
        contactRepository.addObserver(this);
        receiptProcessor.addObserver(this);
        confirmButton.setOnAction(event -> confirm());
    }

    @Override
    public void update() {
        List<ComboBox<Contact>> comboBoxes = new ArrayList<>();
        List<Combination> receiptItems = createComboBoxes(comboBoxes);
        contactItemTable.setItems(FXCollections.observableArrayList(receiptItems));
        configureTableColumns();
        checkAllComboBoxesSelected(comboBoxes);
    }

    private List<Combination> createComboBoxes(List<ComboBox<Contact>> comboBoxes) {
        List<Combination> receiptItems = new ArrayList<>();
        for (ReceiptItem receiptItem : receiptProcessor.getReceiptItems()) {
            for (int index = 0; index < receiptItem.getAmount(); index++) {
                ComboBox<Contact> comboBox = new ComboBox<>();
                comboBox.setItems(FXCollections.observableArrayList(contactRepository.getContacts()));
                comboBox.setPromptText("Choose Contact");
                comboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(Contact contact) {
                        return contact != null ? contact.getDisplayName() : "";
                    }

                    @Override
                    public Contact fromString(String string) {
                        return contactRepository.getContacts().stream()
                                .filter(contact -> contact.getDisplayName().equals(string))
                                .findFirst()
                                .orElse(null);
                    }
                });
                comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    checkAllComboBoxesSelected(comboBoxes);
                });
                comboBoxes.add(comboBox);
                receiptItems.add(new Combination(receiptItem, comboBox));
            }
        }
        return receiptItems;
    }

    private void configureTableColumns() {
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().receiptItemProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        dropdown.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ComboBox<Contact> comboBox = getTableView().getItems().get(getIndex()).getContactComboBox();
                    Pane pane = new Pane(comboBox);
                    setGraphic(pane);
                }
            }
        });
    }

    private void checkAllComboBoxesSelected(List<ComboBox<Contact>> comboBoxes) {
        boolean allSelected = comboBoxes.stream()
                .allMatch(comboBox -> comboBox.getSelectionModel().getSelectedItem() != null);

        confirmButton.setDisable(!allSelected);
    }

    /**
     * Switches to the next scene.
     */
    @Override
    public void confirm() {
        receiptProcessor.deleteAllContactReceiptItems();
        for (Combination combination : contactItemTable.getItems()) {
            Contact contact = combination.getContactComboBox().getSelectionModel().getSelectedItem();
            if (contact != null) {
                receiptProcessor.createContactReceiptItem(contact, combination.getReceiptItem());
            }
        }
        router.gotoScene(Pages.SHOW_SPLIT_WINDOW);
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
        private final ReceiptItem receiptItem;
        private final SimpleStringProperty itemName;
        private final SimpleStringProperty price;
        private final ComboBox<Contact> contactComboBox;

        public Combination(ReceiptItem receiptItem, ComboBox<Contact> contactComboBox) {
            this.itemName = new SimpleStringProperty(receiptItem.getName());
            this.price = new SimpleStringProperty(Math.round(receiptItem.getPrice() / receiptItem.getAmount() * 100) / 100F + " CHF");
            this.receiptItem = receiptItem;
            this.contactComboBox = contactComboBox;
        }

        public SimpleStringProperty receiptItemProperty() {
            return itemName;
        }

        public void setPrice(String price) {
            this.price.set(price);
        }

        public SimpleStringProperty priceProperty() {
            return price;
        }

        public ComboBox<Contact> getContactComboBox() {
            return contactComboBox;
        }

        public ReceiptItem getReceiptItem() {
            return receiptItem;
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