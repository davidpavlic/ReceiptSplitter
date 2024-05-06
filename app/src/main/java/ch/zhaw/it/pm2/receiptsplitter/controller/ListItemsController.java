package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem.roundPrice;

public class ListItemsController extends DefaultController implements CanNavigate, IsObserver {
    private static final ReceiptItem RECEIPT_ITEM_PLACEHOLDER_DATA = new ReceiptItem(0.01F, "[Enter name]", 1);

    private static final String ADD_FAIL_ERROR_MESSAGE = "Could not add Receipt Item";
    private static final String UPDATE_FAIL_ERROR_MESSAGE = "Could not update Receipt Item";
    private static final String DELETE_FAIL_ERROR_MESSAGE = "Could not remove Receipt Item";

    @FXML private HBox errorMessageBox;
    @FXML private Label errorMessageLabel;

    @FXML private TableView<ReceiptItem> tableReceiptItems;
    @FXML private TableColumn<ReceiptItem, Integer> amountColumn;
    @FXML private TableColumn<ReceiptItem, String> nameColumn;
    @FXML private TableColumn<ReceiptItem, String> unitPriceColumn;
    @FXML private TableColumn<ReceiptItem, String> totalPriceColumn;
    @FXML private TableColumn<ReceiptItem, Void> actionColumn;

    private List<ReceiptItem> initialDataReceiptItems;
    private List<ReceiptItem> dataReceiptItems;

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.LIST_ITEMS_WINDOW_MSG;
        receiptProcessor.addObserver(this);
        configureTable();

        errorProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showErrorMessage(newValue);
        });
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onBeforeStage() {
        initialDataReceiptItems = receiptProcessor.getFullCopyReceiptItems();
        dataReceiptItems = new ArrayList<>(receiptProcessor.getFullCopyReceiptItems());
    }

    @Override
    public void update() {
        dataReceiptItems = receiptProcessor.getFullCopyReceiptItems();
        updateTable();
    }

    @FXML
    public void addReceiptItem() {

        boolean placeholderExists = dataReceiptItems.stream().anyMatch(item -> RECEIPT_ITEM_PLACEHOLDER_DATA.getName().equals(item.getName()));

        if (placeholderExists) {
            showErrorMessage("You can only add one placeholder item at a time");
            return;
        }

        try {
            ReceiptItem receiptItem = new ReceiptItem(
                    RECEIPT_ITEM_PLACEHOLDER_DATA.getPrice(),
                    RECEIPT_ITEM_PLACEHOLDER_DATA.getName(),
                    RECEIPT_ITEM_PLACEHOLDER_DATA.getAmount());

            receiptProcessor.addReceiptItem(receiptItem);
        } catch (IllegalArgumentException e) {
            logError(ADD_FAIL_ERROR_MESSAGE, e);
            showErrorMessage(ADD_FAIL_ERROR_MESSAGE);
        }
    }

    /**
     * @inheritDoc Switches to the choose people window.
     */
    @FXML
    @Override
    public void confirm() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    /**
     * @inheritDoc Switches back to the main window.
     */
    @FXML
    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
    }

    /**
     * Resets the receipt items to the initial state.
     */
    @FXML
    public void reset() {
        receiptProcessor.setReceiptItems(Receipt.fullCopyReceiptItems(initialDataReceiptItems));
    }

    /**
     * Closes the error message box.
     */
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
            String newName = event.getNewValue();
            if (event.getOldValue().equals(newName)) return;

            boolean nameAlreadyExists = dataReceiptItems.stream().anyMatch(item -> item.getName().equals(newName));
            if (nameAlreadyExists) {
                showErrorMessage("This name already exists");
                tableReceiptItems.refresh();
                return;
            }

            ReceiptItem item = event.getRowValue();
            String oldName = event.getOldValue();
            item.setName(newName);

            boolean updateSuccess = updateReceiptItem(oldName, item);
            if (!updateSuccess) {
                item.setName(oldName);
                tableReceiptItems.refresh();
            }
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
            updateReceiptItem(item.getName(), item);
        });
    }

    private void configureUnitPriceColumn() {
        unitPriceColumn.setCellValueFactory(cellItem -> {
            ReceiptItem item = cellItem.getValue();
            return new SimpleStringProperty(roundPrice(item.getPrice() / item.getAmount()) + " CHF");
        });

        unitPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitPriceColumn.setOnEditCommit(event -> {
            String unitPriceInput = event.getNewValue();
            if (event.getOldValue().equals(unitPriceInput)) return;

            ReceiptItem item = event.getRowValue();
            Optional<Float> unitPrice = extractPrice(unitPriceInput, item);
            if (unitPrice.isEmpty()) return;

            item.setPrice(unitPrice.get() * item.getAmount());
            updateReceiptItem(item.getName(), item);
        });
    }

    private void configureTotalPriceColumn() {
        totalPriceColumn.setCellValueFactory(cellItem -> {
            ReceiptItem item = cellItem.getValue();
            return new SimpleStringProperty(roundPrice(item.getPrice()) + " CHF");
        });

        totalPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        totalPriceColumn.setOnEditCommit(event -> {
            String totalPriceInput = event.getNewValue();
            if (event.getOldValue().equals(totalPriceInput)) return;

            ReceiptItem item = event.getRowValue();
            Optional<Float> totalPrice = extractPrice(totalPriceInput, item);

            if (totalPrice.isEmpty()) return;

            item.setPrice(totalPrice.get());
            updateReceiptItem(item.getName(), item);
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
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.textFillProperty().setValue(Color.WHITE);
                ReceiptItem receiptItem = getTableView().getItems().get(getIndex());
                deleteButton.setOnAction(e -> handleDeleteAction(receiptItem));
            }
        };
    }

    private void handleDeleteAction(ReceiptItem receiptItem) {
        try {
            boolean removeSuccess = receiptProcessor.deleteReceiptItemByName(receiptItem.getName());

            if (!removeSuccess) {
                logger.warning(DELETE_FAIL_ERROR_MESSAGE + receiptItem.getName());
                errorProperty.setValue(DELETE_FAIL_ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            logError(DELETE_FAIL_ERROR_MESSAGE, e);
            showErrorMessage(DELETE_FAIL_ERROR_MESSAGE);
        }
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

            return Optional.empty();
        }

        return Optional.of(price);
    }

    private boolean updateReceiptItem(String oldName, ReceiptItem item) {
        try {
            receiptProcessor.updateReceiptItemByName(oldName, item);
            return true;
        } catch (IllegalArgumentException e) {
            logError(UPDATE_FAIL_ERROR_MESSAGE, e);
            showErrorMessage(UPDATE_FAIL_ERROR_MESSAGE);
            return false;
        }
    }

    private void updateTable() {
        var sortOrder = new ArrayList<>(tableReceiptItems.getSortOrder());
        tableReceiptItems.setItems(FXCollections.observableArrayList(dataReceiptItems));
        tableReceiptItems.refresh();
        tableReceiptItems.getSortOrder().setAll(sortOrder);
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
