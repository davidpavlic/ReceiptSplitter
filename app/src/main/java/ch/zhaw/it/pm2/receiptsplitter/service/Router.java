package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.Main;
import ch.zhaw.it.pm2.receiptsplitter.Pages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class Router extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private Stage stage;
    private Map<Pages, String> sceneMap;

    public void gotoScene(Pages page) {

    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/MainWindow.fxml"));
            Pane rootNode = loader.load();

            Scene scene = new Scene(rootNode);

            stage.setMinWidth(300);
            stage.setMinHeight(250);
            stage.setScene(scene);
            stage.setTitle("Receipt Splitter");
            stage.show();

        } catch (Exception e) {
            throw e;
        }
    }
}
