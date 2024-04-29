package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;

public class EditItemsController extends DefaultController implements CanNavigate, CanReset {

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.EDIT_ITEMS_WINDOW_MSG;
    }

    @Override
    public void confirm() {
        router.gotoScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void back() {
        router.gotoScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void reset() {

    }

}
