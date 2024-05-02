package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class ListItemsController extends DefaultController implements CanNavigate, CanReset {

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.CONTACT_LIST_WINDOW_MSG;
    }

    @FXML
    @Override
    public void confirm() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @FXML
    @Override
    public void back() {
        switchScene(Pages.MAIN_WINDOW);
    }

    @FXML
    public void goToEditItemsController() {
        switchScene(Pages.EDIT_ITEMS_WINDOW);
    }

    @Override
    public void reset() {}

}
