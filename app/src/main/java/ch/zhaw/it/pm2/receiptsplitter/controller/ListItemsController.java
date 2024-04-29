package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;

public class ListItemsController extends DefaultController implements CanNavigate, CanReset {

    @Override @FXML
    public void confirm() {
        router.gotoScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @Override @FXML
    public void back() {
        router.gotoScene(Pages.ADD_RECEIPT_WINDOW);
    }

    @FXML
    public void goToEditItemsController() {
        router.gotoScene(Pages.EDIT_ITEMS_WINDOW);
    }

    @Override
    public void reset() {

    }

}
