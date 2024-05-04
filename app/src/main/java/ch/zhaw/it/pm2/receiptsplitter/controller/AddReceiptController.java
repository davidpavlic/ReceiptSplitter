package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageReceiptExtractor.*;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;


public class AddReceiptController extends DefaultController implements CanNavigate, CanReset  {
    private File selectedFile;
    private ImageReceiptExtractor imageExtractor = new ImageReceiptExtractor();
    ReceiptProcessor receiptProcessor;

    @FXML private Pane dragAndDropPane;
    @FXML private Button uploadReceiptButton;
    @FXML private Button confirmButton;

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.ADD_RECEIPT_ITEMS_WINDOW_MSG;
        confirmButton.setOnAction(event -> confirm() );
        setupDragAndDrop();
        uploadReceiptButton.setOnAction((actionEvent -> openDialog()));
    }

    @Override
    public void confirm() {
        if(processReceipt(selectedFile)){
            switchScene(Pages.LIST_ITEMS_WINDOW);
            //explicitly set to null to prevent bottlenecks
            selectedFile = null;
        }
        System.out.println("Test");
    }

    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
        //explicitly set to null to prevent bottlenecks
        selectedFile = null;
    }

    @Override
    public void reset() {
            //TODO currentReceipt = null;
    }

    public void handleReceiptDropped(DragEvent dragEvent) {
        boolean success = false;
        Dragboard dragboard = dragEvent.getDragboard();

        if (dragboard.hasFiles())
            success = true;
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
    }

    private boolean processReceipt(File file){
        try {
            ReceiptOCR extractedImage =  imageExtractor.extractReceiptOCR(file);

            ArrayList<ReceiptItem> receiptItems = new ArrayList<>();

            for(ReceiptItemOCR receiptItemOCR : extractedImage.receiptItemOCRList()){
                float price = (float) receiptItemOCR.price();
                int amount = receiptItemOCR.amount();
                String name = receiptItemOCR.name();

                receiptItems.add(new ReceiptItem(price, name, amount));
            }

            receiptProcessor = new ReceiptProcessor(new Receipt(receiptItems));

            return true;
        } catch (ImageReceiptExtractorException e) {
            System.err.println("Error extracting receipt: " + e.getMessage());
            // TODO: Show user error message
        }
        return false;
    }
}
