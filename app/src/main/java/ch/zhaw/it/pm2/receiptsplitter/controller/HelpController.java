package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpController {
    @FXML private TextArea helpTextArea;

    public void setHelpText(HelpMessages helpText){
        helpTextArea.setText(helpText.getMessage());
    }

    @FXML
    public void confirm() {
        Stage stage = (Stage) helpTextArea.getScene().getWindow();
        stage.close();
    }
}