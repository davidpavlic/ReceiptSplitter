package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ContactListController implements CanNavigate, CanReset, DefaultController {
    private Router router;

    @FXML
    private TableColumn<?, ?> actionColumn;
    @FXML
    private Button addContactButton;
    @FXML
    private Button backButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TableColumn<?, ?> emailColumn;
    @FXML
    private TableColumn<?, ?> nameColumn;
    @FXML
    private TableView<?> tableContactList;

    @Override
    public void initialize(Router router) {
        this.router = router;
    }

    @FXML
    void showFAQ() {

    }

    @FXML
    void showHelpModal() {
        router.openHelpModal(HelpMessages.CONTACT_LIST_WINDOW_MSG);
    }

    @Override
    public void confirm() {

    }

    @Override
    public void back() {

    }

    @Override
    public void reset() {

    }
}
