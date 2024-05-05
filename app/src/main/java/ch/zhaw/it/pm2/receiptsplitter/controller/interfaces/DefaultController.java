package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import javafx.fxml.FXML;

/**
 * This abstract class provides a default implementation for a controller in the application.
 * It includes common properties and methods that all controllers need, such as a logger, router, contactRepository, receiptProcessor, and helpMessage.
 *
 * Each controller that extends this class should call the initialize method to set up these common properties.
 *
 * The class also provides methods to show FAQ and help messages, and to switch scenes.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public abstract class  DefaultController {
    protected final Logger logger = Logger.getLogger(DefaultController.class.getName());
    protected Router router;
    protected ContactRepository contactRepository;
    protected ReceiptProcessor receiptProcessor;
    protected HelpMessages helpMessage;


    /**
     * Initializes the controller with a router, contact repository, and receipt processor.
     *
     * @param router The router to be used for navigation.
     * @param contactRepository The repository to be used for contact management.
     * @param receiptProcessor The processor to be used for receipt processing.
     */
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor){
        this.router = router;
        this.contactRepository = contactRepository;
        this.receiptProcessor = receiptProcessor;
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;
    }


    /**
     * Shows the FAQ message in a modal.
     */
    @FXML
    void showFAQ() {
        try {
            router.openHelpModal(HelpMessages.FAQ_MSG);
        } catch (IllegalStateException | IOException exception) {
            logger.severe("Could not open help modal: " + exception.getMessage());
            logger.fine(Arrays.toString(exception.getStackTrace()));
        }
    }

    /**
     * Shows the help message in a modal.
     */
    @FXML
    void showHelp() {
        if (helpMessage == null) {
            throw new IllegalStateException("No help message set");
        }

        try {
            router.openHelpModal(helpMessage);
        } catch (IllegalStateException | IOException exception) {
            logger.severe("Could not open help modal: " + exception.getMessage());
            logger.fine(Arrays.toString(exception.getStackTrace()));
        }
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
            logger.severe("Could not switch Scenes to " + page.toString() + " Window from Controller " + this.getClass().getSimpleName() + ", " + exception.getMessage());
            logger.fine(Arrays.toString(exception.getStackTrace()));
            throw exception;
        }
    }

    /**
     * Switches the scene to the specified page, and sets the last page.
     *
     * @param page The page to switch to.
     * @param lastPage The last page.
     */
    protected void switchScene(Pages page, Pages lastPage) {
        try {
            router.gotoScene(page, lastPage);
        } catch (IllegalStateException | IllegalArgumentException exception) {
            logger.severe("Could not switch Scenes to " + page.toString() + " Window from Controller " + this.getClass().getSimpleName() + ", " + exception.getMessage());
            logger.fine(Arrays.toString(exception.getStackTrace()));
            throw exception;
        }
    }
}
