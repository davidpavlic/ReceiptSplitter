package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ModalDialogController {
    @FXML private TextArea textMessage;

    public void setTextMessage(String text){
        textMessage.setText(text);
    }

    @FXML
    public void confirm() {
        Stage stage = (Stage) textMessage.getScene().getWindow();
        stage.close();
    }
}