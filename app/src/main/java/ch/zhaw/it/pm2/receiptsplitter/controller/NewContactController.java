package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewContactController implements DefaultController, CanNavigate {
    private Router router;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField lastNameInput;

    @Override
    public void initialize(Router router) {
        this.router = router;
        confirmButton.setOnAction(event -> { confirm(); });
    }

    @FXML
    void showFAQ() {
        router.openHelpModal(HelpMessages.FAQ_MSG);
    }
    @FXML
    void showHelpModal() {
        router.openHelpModal(HelpMessages.NEW_CONTACT_WINDOW_MSG);
    }

    @Override
    public void confirm() {

    }

    @Override @FXML
    public void back() {
        router.gotoScene(Pages.LOGIN_WINDOW);
    }
}
