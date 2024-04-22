package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;

public class Router {
    private final Stage stage;
    private final Map<Pages, String> sceneMap;

    public Router(Stage stage) {
        this.stage = stage;
        this.sceneMap = new HashMap<>();
    }

    public void gotoScene(Pages page) {

    }
}
