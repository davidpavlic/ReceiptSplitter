package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;

public class Router {
    private static Router instance;
    private final Stage stage;
    private final Map<Pages, String> sceneMap;

    private Router(Stage stage) {
        this.stage = stage;
        this.sceneMap = new HashMap<>();
    }

    public static Router getInstance(Stage stage) {
        if (instance == null) {
            instance = new Router(stage);
        }
        return instance;
    }

    public void gotoScene(Pages page) {

    }
}
