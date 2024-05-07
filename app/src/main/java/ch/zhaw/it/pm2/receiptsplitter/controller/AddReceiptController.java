package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor.ImageReceiptExtractorException;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor.ReceiptItemOCR;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor.ReceiptOCR;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//TODO: JavaDoc
public class AddReceiptController extends DefaultController implements CanNavigate, CanReset {
    private File selectedFile;
    private ImageReceiptExtractor imageExtractor;

    @FXML private Pane dragAndDropPane;
    @FXML private ImageView receiptImageView;
    @FXML private Rectangle backgroundShadow;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label errorMessage;

    @FXML private Button uploadReceiptButton;
    @FXML private Button confirmButton;
    @FXML private Button resetButton;
    @FXML private Button backButton;

    /**
     * Initializes the Controller with the necessary dependencies and initial data.
     * Configures the drag and drop pane and the upload receipt button.
     *
     * @param router            The router to be used for navigation.
     * @param contactRepository The repository to be used for contact management.
     * @param receiptProcessor  The processor to be used for receipt processing.
     */
    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ADD_RECEIPT_WINDOW_MSG;
        this.imageExtractor = new ImageReceiptExtractor();

        setLoadingAnimationEnabled(false);
        setProcessButtonsDisabled(true);

        // TODO: Replace with error message box after merge with feat/26
        errorMessage.setVisible(false);

        setupDragAndDrop();
        uploadReceiptButton.setOnAction((actionEvent -> openDialog()));
    }

    /**
     * @inheritDoc Enables the loading animation and disables all buttons.
     * Loads the receipt image and processes the receipt in a separate thread.
     * After processing the receipt, the loading animation is disabled and the user is navigated to the list items window, additionally doing some cleanup.
     */
    @Override
    public void confirm() {
        setLoadingAnimationEnabled(true);
        setAllButtonsDisabled(true);
        errorMessage.setVisible(false);

        // TODO: Check if this Threading is alright (just to make sure)
        //Threading is implemented to prevent blocking the JavaFX Application thread, allowing smooth UI operation.
        new Thread(() -> {
            boolean success = processReceipt(selectedFile);

            //The UI updates occur in the JavaFX Application thread using `Platform.runLater` to ensure thread safety.
            Platform.runLater(() -> {
                setLoadingAnimationEnabled(false);
                if (success) {
                    switchScene(Pages.LIST_ITEMS_WINDOW);
                    clearReceiptData();
                } else {
                    errorMessage.setVisible(true);
                    logger.fine("Showing parsing receipt error, receipt processing has failed.");
                }
                setUtilButtonsDisabled(false);
            });
        }).start();
    }

    private boolean processReceipt(File file) {
        try {
            ReceiptOCR extractedImage = imageExtractor.extractReceiptOCR(file);
            List<ReceiptItem> receiptItems = mapReceiptItems(extractedImage);

            receiptProcessor.setReceipt(new Receipt(receiptItems));
            return true;
        } catch (ImageReceiptExtractorException e) {
            logger.severe("Error while processing receipt: " + e.getMessage());
            return false;
        }
    }

    private List<ReceiptItem> mapReceiptItems(ReceiptOCR extractedImage) {
        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (ReceiptItemOCR receiptItemOCR : extractedImage.receiptItemOCRList()) {
            float price = (float) receiptItemOCR.price();
            int amount = receiptItemOCR.amount();
            String name = receiptItemOCR.name();

            receiptItems.add(new ReceiptItem(price, name, amount));
        }
        return receiptItems;
    }

    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
        clearReceiptData();
    }

    @Override
    public void reset() {
        clearReceiptData();
    }

    public void handleReceiptDropped(DragEvent dragEvent) {
        boolean success = false;
        Dragboard dragboard = dragEvent.getDragboard();

        if (dragboard.hasFiles()) {
            success = true;
            loadReceipt(dragboard.getFiles().getFirst());
        }

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    private void setupDragAndDrop() {
        dragAndDropPane.setOnDragOver(event -> {
            if (event.getGestureSource() != dragAndDropPane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        dragAndDropPane.setOnDragDropped(this::handleReceiptDropped);
    }

    private void openDialog() {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(uploadReceiptButton.getScene().getWindow());
        if (selectedFile != null) {
            loadReceipt(selectedFile);
        }
    }

    private void loadReceipt(File file) {
        selectedFile = file;
        setProcessButtonsDisabled(false);
        receiptImageView.setImage(new Image(file.toURI().toString()));
    }

    private void clearReceiptData() {
        selectedFile = null;
        receiptImageView.setImage(null);
        setProcessButtonsDisabled(true);
    }

    private void setAllButtonsDisabled(boolean enabled) {
        setUtilButtonsDisabled(enabled);
        setProcessButtonsDisabled(enabled);
    }

    private void setUtilButtonsDisabled(boolean enabled) {
        backButton.setDisable(enabled);
        uploadReceiptButton.setDisable(enabled);
    }

    private void setProcessButtonsDisabled(boolean enabled) {
        resetButton.setDisable(enabled);
        confirmButton.setDisable(enabled);
    }

    private void setLoadingAnimationEnabled(boolean enabled) {
        backgroundShadow.setVisible(enabled);
        progressIndicator.setVisible(enabled);
    }
}
