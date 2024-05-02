package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;

public class ChoosePeopleController extends DefaultController implements CanNavigate, CanReset {
    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.CHOOSE_PEOPLE_WINDOW_MSG;
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
