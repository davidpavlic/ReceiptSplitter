package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.Main;
import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.HelpController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Router {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private final Stage stage;
    private final Map<Pages, Pair<Scene, DefaultController>> sceneMap = new HashMap<>();

    public Router(Stage stage) throws IOException {
        this.stage = stage;
        // Initialize the scene map with all pages defined in the Pages enum
        for (Pages page : Pages.values()) {
            addSceneMap(page, page.getPath());
        }
    }

    private void addSceneMap(Pages page, String pathToScene) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToScene));
        logger.info(pathToScene);
        Pane node = loader.load();
        DefaultController controller = loader.getController();
        controller.initialize(this);

        Scene scene = new Scene(node);
        sceneMap.put(page, new Pair<>(scene, controller));
    }

    public void gotoScene(Pages page) {
        if (stage != null) {
            stage.setScene(getScene(page));
            stage.show();
        } else {
           logger.severe("Stage is null, can not switch scene");
        }
    }

    public void openHelpModal(HelpMessages helpText) {
        try {
            Scene helpModalScene = getScene(Pages.HELP_MODAL);

            HelpController controller = (HelpController) getController(Pages.HELP_MODAL);
            controller.setHelpText(helpText);
            controller.initialize(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Help");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setScene(helpModalScene);

            dialogStage.showAndWait();
        } catch (Exception e) {
            logger.severe("An error occurred while trying to open the help modal: " + e.getMessage());
        }
    }

    public Scene getScene(Pages page) {
        return sceneMap.get(page).getKey();
    }

    public DefaultController getController(Pages page) {
        return sceneMap.get(page).getValue();
    }

    public void closeWindow() {
            stage.close();
    }
}
