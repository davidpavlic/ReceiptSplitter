package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpController implements DefaultController {
    Router router;

    @FXML
    private TextArea helpTextArea;

    public void setHelpText(HelpMessages helpText){
        helpTextArea.setText(helpText.getMessage());
    }

    @Override
    public void initialize(Router router) {
        this.router = router;
    }

    @FXML
    public void confirm() {
        Stage stage = (Stage) helpTextArea.getScene().getWindow();
        stage.close();
    }
}