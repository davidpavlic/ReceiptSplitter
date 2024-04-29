package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController extends DefaultController {

    @FXML
    private Button confirmButton;

    @FXML
    private ComboBox<Contact> selectUserDropdown;

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.LOGIN_WINDOW_MSG;
        confirmButton.setOnAction(event -> { confirm(); });
    }

    @FXML
    void closeWindow() {
        router.closeWindow();
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
