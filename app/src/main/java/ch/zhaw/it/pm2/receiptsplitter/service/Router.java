package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.Main;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.HelpController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HasDynamicLastPage;
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

/**
 * The Router class is responsible for managing the navigation between different scenes in the Receipt Splitter application.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class Router {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private final Stage stage;
    private final Map<Pages, Pair<Scene, DefaultController>> sceneMap = new HashMap<>();

    /**
     * Constructs a new Router with the given stage.
     * Initializes the scene map with all pages defined in the Pages enum.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an error occurs during scene initialization
     */
    public Router(Stage stage) throws IOException {
        this.stage = stage;
        for (Pages page : Pages.values()) {
            addSceneMap(page, page.getPath());
        }
    }

    /**
     * Switches to the specified scene.
     *
     * @param page the page to switch to
     * @throws IllegalStateException if the stage is null
     */
    public void gotoScene(Pages page) throws IllegalStateException {
        if (stage != null) {
            stage.setScene(getScene(page));
            stage.show();
        } else {
            throw new IllegalStateException("Stage is null, can not switch scene");
        }
    }

    /**
     * Switches to the specified scene and sets the last page for controllers that implement HasDynamicLastPage.
     *
     * @param page the page to switch to
     * @param lastPage the last page to set
     * @throws IllegalStateException if the stage is null
     * @throws IllegalArgumentException if the controller does not implement HasDynamicLastPage
     */
    public void gotoScene(Pages page, Pages lastPage) throws IllegalStateException, IllegalArgumentException {
        DefaultController controller = getController(page);
        if (controller instanceof HasDynamicLastPage dynamicLastPageController) {
            dynamicLastPageController.setLastPage(lastPage);
        } else {
            throw new IllegalArgumentException("Controller does not implement HasDynamicLastPage");
        }
        gotoScene(page);
    }

    /**
     * Opens a help modal with the specified help message.
     *
     * @param helpText the help message to display
     * @throws IllegalStateException if an error occurs during modal opening
     */
    public void openHelpModal(HelpMessages helpText) throws IllegalStateException{
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
        } catch (IllegalStateException exception) {
            logger.severe("Could not open help modal: " + exception);
            throw  exception;
        }
    }

    /**
     * Returns the scene for the specified page.
     *
     * @param page the page to get the scene for
     * @return the scene for the specified page
     */
    public Scene getScene(Pages page) {
        return sceneMap.get(page).getKey();
    }

    /**
     * Returns the controller for the specified page.
     *
     * @param page the page to get the controller for
     * @return the controller for the specified page
     */
    public DefaultController getController(Pages page) {
        return sceneMap.get(page).getValue();
    }

    /**
     * Closes the window.
     */
    public void closeWindow() {
        stage.close();
    }

    /**
     * Adds a scene to the scene map.
     *
     * @param page the page to add
     * @param pathToScene the path to the scene
     * @throws IOException if an error occurs during scene loading
     */
    private void addSceneMap(Pages page, String pathToScene) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToScene));
        Pane node = loader.load();
        DefaultController controller = loader.getController();
        controller.initialize(this);

        Scene scene = new Scene(node);
        sceneMap.put(page, new Pair<>(scene, controller));
    }
}