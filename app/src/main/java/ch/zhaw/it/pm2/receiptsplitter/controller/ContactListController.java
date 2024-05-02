package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ContactListController extends DefaultController implements CanNavigate, CanReset {

    @FXML private TableColumn<?, ?> actionColumn;
    @FXML private Button addContactButton;
    @FXML private Button backButton;
    @FXML private Button confirmButton;
    @FXML private TableColumn<?, ?> emailColumn;
    @FXML private TableColumn<?, ?> nameColumn;
    @FXML private TableView<?> tableContactList;

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.CONTACT_LIST_WINDOW_MSG;
    }

    @FXML
    public void openCreateProfile() {
        router.gotoScene(Pages.CREATE_PROFILE_WINDOW, Pages.CONTACT_LIST_WINDOW);
    }

    @FXML
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
    }
}
