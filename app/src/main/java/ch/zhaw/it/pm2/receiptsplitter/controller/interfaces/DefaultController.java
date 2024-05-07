package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This abstract class provides a default implementation for a controller in the application.
 * It includes common properties and methods that all controllers need, such as a logger, router, contactRepository, receiptProcessor, and helpMessage.
 * <p>
 * Each controller that extends this class should call the initialize method to set up these common properties.
 * <p>
 * The class also provides methods to show FAQ and help messages, and to switch scenes.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public abstract class DefaultController {
    protected final Logger logger = Logger.getLogger(DefaultController.class.getName());
    private Router router;
    protected ContactRepository contactRepository;
    protected ReceiptProcessor receiptProcessor;
    protected HelpMessages helpMessage;
    protected StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML protected HBox errorMessageBox;
    @FXML protected Label errorMessageLabel;

    /**
     * Initializes the controller with a router, contact repository, and receipt processor.
     *
     * @param router            The router to be used for navigation.
     * @param contactRepository The repository to be used for contact management.
     * @param receiptProcessor  The processor to be used for receipt processing.
     */
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        this.router = router;
        this.contactRepository = contactRepository;
        this.receiptProcessor = receiptProcessor;
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;

        errorMessageProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showErrorMessage(newValue);
        });
    }

    /**
     * Executes code before the stage  is loaded.
     */
    public void onBeforeStage() {
        logger.info("Loading stage for: " + this.getClass().getSimpleName());
    }

    /**
     * Shows the FAQ message in a modal.
     */
    @FXML
    void showFAQ() {
        try {
            router.openHelpModal(HelpMessages.FAQ_MSG);
        } catch (IllegalStateException | IOException exception) {
            logError("Could not open FAQ modal", exception);
            errorMessageProperty.setValue("Could not open FAQ modal");
        }
    }

    /**
     * Shows the help message in a modal.
     */
    @FXML
    void showHelp() {
        if (helpMessage == null) {
            logger.fine("No help message set in " + this.getClass().getSimpleName() + " Controller");
            throw new IllegalStateException("No help message set");
        }

        try {
            router.openHelpModal(helpMessage);
        } catch (IllegalStateException | IOException exception) {
            logError("Could not open help modal", exception);
            errorMessageProperty.setValue("Could not open help modal");
        }
    }

    @FXML
    void closeWindow() {
        router.closeWindow();
    }

    /**
     * Switches the scene to the specified page.
     *
     * @param page The page to switch to.
     */
    protected void switchScene(Pages page) {
        try {
            router.gotoScene(page);
        } catch (IllegalStateException exception) {
            logError(getSwitchSceneErrorMessage(page) + ", " + exception.getMessage(), exception);
            errorMessageProperty.setValue("Could not switch to " + page + " Window");
        }
    }

    /**
     * Switches the scene to the specified page, and sets the last page.
     *
     * @param page     The page to switch to.
     * @param lastPage The last page.
     */
    protected void switchScene(Pages page, Pages lastPage) {
        try {
            router.gotoScene(page, lastPage);
        } catch (IllegalStateException | IllegalArgumentException exception) {
            logError(getSwitchSceneErrorMessage(page) + ", " + exception.getMessage(), exception);
            errorMessageProperty.setValue("Could not switch to " + page + " Window");
        }
    }

    /**
     * Logs an error message and exception stack trace.
     *
     * @param message   The error message.
     * @param exception The exception.
     */
    protected void logError(String message, Exception exception) {
        logger.severe(message);
        if (exception != null) {
            logger.fine(Arrays.toString(exception.getStackTrace()));
        }
    }

    /**
     * Closes the error message box.
     */
    @FXML
    protected void closeErrorMessage() {
        errorMessageBox.setVisible(false);
        errorMessageBox.setManaged(false);
        errorMessageProperty.set(null);
    }

    /**
     * Shows an error message in the error message box.
     * @param message The error message to show.
     */
    protected void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageBox.setVisible(true);
        errorMessageBox.setManaged(true);
    }

    private String getSwitchSceneErrorMessage(Pages page) {
        return "Could not switch Scenes to " + page.toString() + " Window from Controller " + this.getClass().getSimpleName();
    }
}
