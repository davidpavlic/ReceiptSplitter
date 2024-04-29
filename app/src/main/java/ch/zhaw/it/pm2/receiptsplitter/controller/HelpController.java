package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpController implements DefaultController {
    Router router;
    @FXML
    private Button confirmButton;

    @FXML
    private TextArea helpTextArea;

    public void setHelpText(HelpMessages helpText){
        helpTextArea.setText(helpText.getMessage());
    }

    @Override
    public void initialize(Router router) {
        this.router = router;
        confirmButton.setOnAction(event -> {
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        });
    }
}