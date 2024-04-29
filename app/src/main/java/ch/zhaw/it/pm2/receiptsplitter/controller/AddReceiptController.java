package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.service.ImageExtractor;
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

    public ImageExtractor imageExtractor;
    private Receipt currentReceipt;

    @FXML private Pane dragAndDropPane;
    @FXML private Button uploadReceiptButton;
    @FXML private Button confirmButton;
    @FXML private Button backButton;


    private void setupDragAndDrop() {
        dragAndDropPane.setOnDragOver(event -> {
            if (event.getGestureSource() != dragAndDropPane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragAndDropPane.setOnDragDropped(this::handleReceiptDropped);
    }

    @Override
    public void initialize(Router router) {
        confirmButton.setOnAction(event -> { confirm(); });
        setupDragAndDrop();
        uploadReceiptButton.setOnAction((actionEvent -> openDialog()));
    }
    @Override
    public void confirm() {
        router.gotoScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void back() {
        router.gotoScene(Pages.MAIN_WINDOW);
    }

    @Override
    public void reset() {
            currentReceipt = null;
    }

    public void handleReceiptDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            uploadFile(db.getFiles().get(0));
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    private void openDialog(){
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(uploadReceiptButton.getScene().getWindow());
        if (selectedFile != null) {
            uploadFile(selectedFile);
        }
    }

    private void uploadFile(File file){
        File destination = new File("app/src/main/resources/receipts" + file.getName());
        try {
            Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            currentReceipt = new Receipt();
            System.out.println("Receipt uploaded successfully!");
        } catch (IOException ioException) {
            System.err.println("Error uploading receipt: " + ioException.getMessage());
        }

        imageExtractor = new ImageExtractor();
        String extractedImage =  imageExtractor.extractOCR(file);
    }


}
