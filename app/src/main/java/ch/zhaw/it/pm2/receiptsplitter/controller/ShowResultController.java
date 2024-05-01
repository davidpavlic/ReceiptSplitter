package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;

public class ShowResultController extends DefaultController {
    public void confirm() {
        switchScene(Pages.SHOW_SPLIT_WINDOW);
        }
    }

