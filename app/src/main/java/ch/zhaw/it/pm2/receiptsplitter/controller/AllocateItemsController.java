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

/**
 * This class is the controller for the AllocateItems view.
 * It handles the allocation of items to contacts.
 * It implements the IsObserver, CanNavigate, and CanReset interfaces.
 *
 * @version 1.0
 * @Author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 */
public class AllocateItemsController extends DefaultController implements IsObserver, CanNavigate, CanReset {
    @FXML private TableView<TableRow> contactItemTable;
    @FXML private TableColumn<TableRow, String> itemColumn;
    @FXML private TableColumn<TableRow, String> priceColumn;
    @FXML private TableColumn<TableRow, String> dropdown;
    @FXML private Button confirmButton;
    private boolean shouldUpdate;

    /**
     * {@inheritDoc} Initializes the controller and sets the help message.
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ALLOCATE_ITEMS_WINDOW_MSG;
        contactRepository.addObserver(this);
        this.shouldUpdate = true;
    }

    /**
     * {@inheritDoc}  Saves the initial state of the receipt items and sets the data receipt items to a copy of the receipt items.
     */
    @Override
    public void onBeforeStage() {
        super.onBeforeStage();
        if (shouldUpdate) {
            update();
        }
    }

    /**
     * {@inheritDoc}  Updates the contact item allocation table.
     */
    @Override
    public void update() {
        List<ComboBox<Contact>> comboBoxes = new ArrayList<>();
        List<TableRow> receiptItems = createComboBoxes(comboBoxes);
        contactItemTable.setItems(FXCollections.observableArrayList(receiptItems));
        configureTableColumns();
        checkAllComboBoxesSelected(comboBoxes);
    }

    /**
     * {@inheritDoc} Switches back to the ChooseContact window.
     */
    @Override
    public void back() {
        shouldUpdate = true;
        switchScene(Pages.CHOOSE_CONTACT_WINDOW);
    }

    /**
     * {@inheritDoc} Executed the update
     */
    @Override
    public void reset() {
        update();
    }

    /**
     * {@inheritDoc} Confirms the allocation of items to contacts.
     * Creates a contact receipt item for each contact and item.
     */
    @Override
    public void confirm() {
        receiptProcessor.deleteAllContactReceiptItems();
        for (TableRow tableRow : contactItemTable.getItems()) {
            Contact contact = tableRow.getContactComboBox().getSelectionModel().getSelectedItem();
            if (contact != null) {
                receiptProcessor.createContactReceiptItem(contact, tableRow.getReceiptItem());
            } else {
                logger.warning("Can not Continue, Contact not selected for item: " + tableRow.getReceiptItem().getName());
                return;
            }
        }
        shouldUpdate = false;
        switchScene(Pages.SHOW_SPLIT_WINDOW);
        closeErrorMessage();
    }

    private List<TableRow> createComboBoxes(List<ComboBox<Contact>> comboBoxes) {
        List<TableRow> receiptItems = new ArrayList<>();
        for (ReceiptItem receiptItem : receiptProcessor.splitReceiptItems()) {
            for (int index = 0; index < receiptItem.getAmount(); index++) {
                ComboBox<Contact> comboBox = new ComboBox<>();
                comboBox.setItems(FXCollections.observableArrayList(contactRepository.getSelectedContacts()));
                comboBox.setPromptText("Choose Contact");
                comboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(Contact contact) {
                        return contact != null ? contact.getDisplayName() : "";
                    }

                    @Override
                    public Contact fromString(String string) {
                        return contactRepository.getSelectedContacts().stream().filter(contact -> contact.getDisplayName().equals(string)).findFirst().orElse(null);
                    }
                });

                comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkAllComboBoxesSelected(comboBoxes));

                comboBoxes.add(comboBox);
                receiptItems.add(new TableRow(receiptItem, comboBox, receiptProcessor));
            }
        }
        return receiptItems;
    }

    private void configureTableColumns() {
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().receiptItemProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().itemUnitPriceProperty());
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
        boolean allSelected = comboBoxes.stream().allMatch(comboBox -> comboBox.getSelectionModel().getSelectedItem() != null);

        confirmButton.setDisable(!allSelected);
    }

    /**
     * This class represents a row in the contact item allocation table.
     * It contains the receipt item, the item name, the item unit price, and a contact combo box.
     */
    public static class TableRow {
        private final ReceiptItem receiptItem;
        private final SimpleStringProperty itemName;
        private final SimpleStringProperty itemUnitPrice;
        private final ComboBox<Contact> contactComboBox;

        public TableRow(ReceiptItem receiptItem, ComboBox<Contact> contactComboBox, ReceiptProcessor receiptProcessor) {
            this.itemName = new SimpleStringProperty(receiptItem.getName());

            String formattedUnitPrice = receiptProcessor.formatPriceWithCurrency(receiptItem.getPrice());

            this.itemUnitPrice = new SimpleStringProperty(formattedUnitPrice);
            this.receiptItem = receiptItem;
            this.contactComboBox = contactComboBox;
        }

        public SimpleStringProperty receiptItemProperty() {
            return itemName;
        }

        public SimpleStringProperty itemUnitPriceProperty() {
            return itemUnitPrice;
        }

        public ComboBox<Contact> getContactComboBox() {
            return contactComboBox;
        }

        public ReceiptItem getReceiptItem() {
            return receiptItem;
        }
    }
}