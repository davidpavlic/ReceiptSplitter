package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * This class is the controller for the MainWindow view.
 * It handles the navigation to different views such as the ContactList, Transactions, and AddReceipt views.
 * It also updates the welcome message based on the current profile in the contact repository.
 * It implements the IsObserver interface.
 *
 * @Author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0

 */
public class MainWindowController extends DefaultController implements IsObserver {
    @FXML private Label welcomeMessage;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.MAIN_WINDOW_MSG;
        contactRepository.addObserver(this);
    }

    /**
     * {@inheritDoc} Executes update method  before the stage is loaded.
     */
    @Override
    public void onBeforeStage() {
        super.onBeforeStage();
        update();
    }

    /**
     * {@inheritDoc} Update the welcome message
     */
    @Override
    public void update() {
        if (!(contactRepository.getProfile() == null)) {
            welcomeMessage.setText("Welcome " + contactRepository.getProfile().getFirstName());
        }
    }

    /**
     * Opens the contact list window.
     */
    @FXML
    public void openContactList() {
        switchScene(Pages.CONTACT_LIST_WINDOW, Pages.MAIN_WINDOW);
        closeErrorMessage();
    }

    /**
     * Opens the transactions window.
     */
    @FXML
    public void openTransactions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Currently not available");
        alert.setContentText("This feature will be available in the next version.");

        alert.showAndWait();
    }

    /**
     * Opens the add receipt window.
     */
    @FXML
    public void addReceipt() {
        switchScene(Pages.ADD_RECEIPT_WINDOW);
        closeErrorMessage();
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void back() {
        switchScene(Pages.LOGIN_WINDOW);
    }
}
