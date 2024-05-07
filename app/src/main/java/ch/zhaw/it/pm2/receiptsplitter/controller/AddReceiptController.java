package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor.ImageReceiptExtractorException;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor.ReceiptOCR;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class AddReceiptController extends DefaultController implements CanNavigate, CanReset  {
    private File selectedFile;
    private ImageReceiptExtractor imageExtractor;
    private Receipt currentReceipt;

    @FXML private Pane dragAndDropPane;
    @FXML private Button uploadReceiptButton;
    @FXML private Button confirmButton;
    @FXML private Button backButton;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ADD_RECEIPT_WINDOW_MSG;
        confirmButton.setOnAction(event -> confirm() );
        setupDragAndDrop();
        uploadReceiptButton.setOnAction((actionEvent -> openDialog()));
    }

    @Override
    public void confirm() {
        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
    }

    @Override
    public void reset() {
            currentReceipt = null;
    }

    public void handleReceiptDropped(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            success = true;
            uploadFile(dragboard.getFiles().get(0));
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

    private void openDialog(){
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(uploadReceiptButton.getScene().getWindow());
        if (selectedFile != null) {
            uploadFile(selectedFile);
        }
    }

    private void uploadFile(File file){
        //TODO Check how we want to store Receipts.
        File destination = new File(getClass().getResource("receipts") + file.getName());
        try {
            Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //currentReceipt = new Receipt();
            //TODO Upload Receipt correctly
            System.out.println("Receipt uploaded successfully!");
            ReceiptOCR extractedImage =  imageExtractor.extractReceiptOCR(file);

            // TODO: Map the extracted data response to the currentReceipt object and save it in ReceiptProcessor
        } catch (ImageReceiptExtractorException e) {
            System.err.println("Error extracting receipt: " + e.getMessage());
            // TODO: Show user error message
        } catch (IOException ioException) {
            System.err.println("Error uploading receipt: " + ioException.getMessage());
            // TODO: Show user error message
        }
    }
}
