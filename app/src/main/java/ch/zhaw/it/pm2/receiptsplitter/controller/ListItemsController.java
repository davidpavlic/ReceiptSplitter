package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListItemsController extends DefaultController implements CanNavigate, IsObserver {
    private static final ReceiptItem RECEIPT_ITEM_PLACEHOLDER_DATA = new ReceiptItem(0.01F, "[Enter name]", 1);

    @FXML private HBox errorMessageBox;
    @FXML private Label errorMessageLabel;

    @FXML private TableView<ReceiptItem> tableReceiptItems;
    @FXML private TableColumn<ReceiptItem, Integer> amountColumn;
    @FXML private TableColumn<ReceiptItem, String> nameColumn;
    @FXML private TableColumn<ReceiptItem, String> unitPriceColumn;
    @FXML private TableColumn<ReceiptItem, String> totalPriceColumn;
    @FXML private TableColumn<ReceiptItem, Void> actionColumn;

    List<ReceiptItem> originalDataReceiptItems;
    ObservableList<ReceiptItem> testDataReceiptItems;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.LIST_ITEMS_WINDOW_MSG;
        configureTable();

        errorProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showErrorMessage(newValue);
        });

        originalDataReceiptItems = receiptProcessor.getReceiptItems();

        List<ReceiptItem> receiptItems = new ArrayList<>();
        receiptItems.add(new ReceiptItem(1.0F, "Test Item 1", 1));
        receiptItems.add(new ReceiptItem(2.0F, "Test Item 2", 2));
        receiptItems.add(new ReceiptItem(3.0F, "Test Item 3", 3));
        receiptItems.add(new ReceiptItem(4.0F, "Test Item 4", 4));
        receiptItems.add(new ReceiptItem(5.0F, "Test Item 5", 5));

        testDataReceiptItems = FXCollections.observableArrayList(receiptItems);
    }

    @Override
    public void update() {
        tableReceiptItems.setItems(FXCollections.observableArrayList(testDataReceiptItems));
        tableReceiptItems.refresh();
    }

    @FXML
    public void addReceiptItem() {
        // TODO: Use ReceiptProcessor to add a new receipt item
        // TODO: Modify with logic from Observable ReceiptProcessor
        List<ReceiptItem> copyReceiptItems = new ArrayList<>(testDataReceiptItems);
        copyReceiptItems.add(RECEIPT_ITEM_PLACEHOLDER_DATA);

        testDataReceiptItems = FXCollections.observableArrayList(copyReceiptItems);
        tableReceiptItems.setItems(testDataReceiptItems);
        tableReceiptItems.refresh();
    }

    @FXML
    @Override
    public void confirm() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @FXML
    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
    }

    @FXML
    public void reset() {
        testDataReceiptItems = (ObservableList<ReceiptItem>) originalDataReceiptItems;
        update();
    }

    @FXML
    public void closeErrorMessage() {
        errorMessageBox.setVisible(false);
        errorMessageBox.setManaged(false);
        errorProperty.set(null);
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageBox.setVisible(true);
        errorMessageBox.setManaged(true);
    }

    private void configureTable() {
        configureNameColumn();
        configureAmountColumn();
        configureUnitPriceColumn();
        configureTotalPriceColumn();

        configureActionColumn();
    }

    private void configureNameColumn() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            if (event.getOldValue().equals(event.getNewValue())) return;

            ReceiptItem item = event.getRowValue();
            item.setName(event.getNewValue());
        });
    }

    private void configureAmountColumn() {
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new TableViewIntegerStringConverter()));
        amountColumn.setOnEditCommit(event -> {
            Integer amount = event.getNewValue();
            if (event.getOldValue().equals(amount)) return;

            ReceiptItem item = event.getRowValue();

            if (amount == Integer.MIN_VALUE) {
                tableReceiptItems.refresh();
                return;
            }

            if (amount < 0) {
                showErrorMessage("You can only enter positive numbers in this cell");
                tableReceiptItems.refresh();
                return;
            }

            item.setAmount(amount);
            tableReceiptItems.refresh();
        });
    }

    private void configureUnitPriceColumn() {
        unitPriceColumn.setCellValueFactory(cellItem -> {
            ReceiptItem item = cellItem.getValue();
            return new SimpleStringProperty((item.getPrice() / item.getAmount()) + " CHF");
        });

        unitPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitPriceColumn.setOnEditCommit(event -> {
            String unitPriceInput = event.getNewValue();
            if (event.getOldValue().equals(unitPriceInput)) return;

            ReceiptItem item = event.getRowValue();
            Optional<Float> unitPrice = extractPrice(unitPriceInput, item);
            if (unitPrice.isEmpty()) return;

            item.setPrice(unitPrice.get() * item.getAmount());
            tableReceiptItems.refresh();
        });
    }

    private void configureTotalPriceColumn() {
        totalPriceColumn.setCellValueFactory(cellItem -> {
            ReceiptItem item = cellItem.getValue();
            return new SimpleStringProperty(item.getPrice() + " CHF");
        });

        totalPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        totalPriceColumn.setOnEditCommit(event -> {
            String totalPriceInput = event.getNewValue();
            if (event.getOldValue().equals(totalPriceInput)) return;

            ReceiptItem item = event.getRowValue();
            Optional<Float> totalPrice = extractPrice(totalPriceInput, item);

            if (totalPrice.isEmpty()) return;

            item.setPrice(totalPrice.get());
            tableReceiptItems.refresh();
        });
    }

    private void configureActionColumn() {
        actionColumn.setCellFactory(param -> createActionCell());
    }

    private TableCell<ReceiptItem, Void> createActionCell() {
        return new TableCell<>() {
            final Button deleteButton = new Button("Del");
            final HBox hbox = new HBox(deleteButton);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    configureButtons();
                    hbox.alignmentProperty().set(Pos.CENTER);
                    setGraphic(hbox);
                }
            }

            private void configureButtons() {
                // set background to light red
                deleteButton.setStyle("-fx-background-color: #ff8080; -fx-border-color: #ff0000; -fx-border-radius: 5px;");
                deleteButton.textFillProperty().setValue(Color.WHITE);
                ReceiptItem receiptItem = getTableView().getItems().get(getIndex());
                deleteButton.setOnAction(e -> handleDeleteAction(receiptItem));
            }
        };
    }

    private void handleDeleteAction(ReceiptItem receiptItem) {
        // TODO: Modify with logic from Observable ReceiptProcessor
        List<ReceiptItem> copyReceiptItems = new ArrayList<>(testDataReceiptItems);
        if (!copyReceiptItems.remove(receiptItem)) {
            errorProperty.setValue("Could not remove contact");
        }

        testDataReceiptItems = FXCollections.observableArrayList(copyReceiptItems);
        tableReceiptItems.setItems(testDataReceiptItems);
        tableReceiptItems.refresh();
    }

    private Optional<Float> extractPrice(String priceInput, ReceiptItem item) {
        float price = new TableViewFloatStringConverter().fromString(priceInput);

        if (item.getPrice() == price) return Optional.empty();
        if (price == Float.MIN_VALUE) {
            tableReceiptItems.refresh();
            return Optional.empty();
        }

        if (price < 0) {
            showErrorMessage("You can only enter positive numbers in this cell");
            tableReceiptItems.refresh();
            return Optional.empty();
        }

        return Optional.of(price);
    }

    private class TableViewIntegerStringConverter extends IntegerStringConverter {
        private final IntegerStringConverter converter = new IntegerStringConverter();
        private final String errorMessage = "You can only enter digits in this cell";

        @Override
        public String toString(Integer object) {
            try {
                return converter.toString(object);
            } catch (NumberFormatException e) {
                showErrorMessage(errorMessage);
            }
            return null;
        }

        @Override
        public Integer fromString(String string) {
            try {
                return converter.fromString(string);
            } catch (NumberFormatException e) {
                showErrorMessage(errorMessage);
            }
            return Integer.MIN_VALUE;
        }
    }

    private class TableViewFloatStringConverter extends FloatStringConverter {
        private final FloatStringConverter converter = new FloatStringConverter();
        private final String errorMessage = "You can only enter numbers in this cell";

        @Override
        public String toString(Float object) {
            try {
                return converter.toString(object);
            } catch (NumberFormatException e) {
                showErrorMessage(errorMessage);
            }
            return null;
        }

        @Override
        public Float fromString(String string) {
            try {
                return converter.fromString(string);
            } catch (NumberFormatException e) {
                showErrorMessage(errorMessage);
            }

            return Float.MIN_VALUE;
        }
    }

}
