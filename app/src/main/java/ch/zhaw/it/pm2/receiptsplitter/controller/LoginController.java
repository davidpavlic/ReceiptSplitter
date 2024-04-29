package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController implements DefaultController {
    Router router;

    @FXML
    private Button confirmButton;

    @FXML
    private ComboBox<Contact> selectUserDropdown;

    public void initialize(Router router) {
        this.router = router;
        confirmButton.setOnAction(event -> { confirm(); });
    }

    @FXML
    void closeWindow() {
        router.closeWindow();
    }

    @FXML
    void showFAQ() {
        router.openHelpModal(HelpMessages.FAQ_MSG);
    }

    @FXML
    void showHelpModal() {
        router.openHelpModal(HelpMessages.LOGIN_WINDOW_MSG);
    }

    @FXML
    void openCreateProfile() {
        NewContactController controller = (NewContactController) router.getController(Pages.CREATE_PROFILE_WINDOW);
        controller.setLastPage(Pages.LOGIN_WINDOW);
        router.gotoScene(Pages.CREATE_PROFILE_WINDOW);
    }

    @FXML
    void confirm() {
        router.gotoScene(Pages.MAIN_WINDOW);
    }
}
