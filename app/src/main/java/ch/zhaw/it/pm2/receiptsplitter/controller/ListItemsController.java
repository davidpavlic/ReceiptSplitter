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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ListItemsController extends DefaultController implements CanNavigate, IsObserver {
    @FXML private HBox errorMessageBox;
    @FXML private Label errorMessageLabel;
    @FXML private Button closeErrorMessageButton;

    @FXML private TableView<ReceiptItem> tableReceiptItems;
    @FXML private TableColumn<ReceiptItem, Integer> amountColumn;
    @FXML private TableColumn<ReceiptItem, String> nameColumn;
    @FXML private TableColumn<ReceiptItem, String> unitPriceColumn;
    @FXML private TableColumn<ReceiptItem, String> totalPriceColumn;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.LIST_ITEMS_WINDOW_MSG;
        configureTable();

        errorProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showErrorMessage(newValue);
        });
    }

    @Override
    public void update() {
        var testDataReceiptItems = new ArrayList<ReceiptItem>();
        testDataReceiptItems.add(new ReceiptItem(1.0F, "Test Item 1", 1));
        testDataReceiptItems.add(new ReceiptItem(2.0F, "Test Item 2", 2));
        testDataReceiptItems.add(new ReceiptItem(3.0F, "Test Item 3", 3));
        testDataReceiptItems.add(new ReceiptItem(4.0F, "Test Item 4", 4));
        testDataReceiptItems.add(new ReceiptItem(5.0F, "Test Item 5", 5));

        tableReceiptItems.setItems(FXCollections.observableArrayList(testDataReceiptItems));
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
    public void goToEditItemsController() {
        switchScene(Pages.EDIT_ITEMS_WINDOW);
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        totalPriceColumn.setCellValueFactory(cellItem -> {
            ReceiptItem item = cellItem.getValue();
            return new SimpleStringProperty(item.getPrice() + " CHF");
        });

        unitPriceColumn.setCellValueFactory(cellItem -> {
            ReceiptItem item = cellItem.getValue();
            return new SimpleStringProperty((item.getPrice() / item.getAmount()) + " CHF");
        });
    }
}
