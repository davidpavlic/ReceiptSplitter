package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.Main;
import ch.zhaw.it.pm2.receiptsplitter.controller.HelpController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HasDynamicLastPage;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The Router class is responsible for managing the navigation between different scenes in the Receipt Splitter application.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class Router {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    final Stage stage;
    private final Map<Pages, Pair<Scene, DefaultController>> sceneMap = new HashMap<>();

    /**
     * Constructs a new Router with the given stage.
     * Initializes the scene map with all pages defined in the Pages enum.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an error occurs during scene initialization
     */
    public Router(Stage stage, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) throws IOException {
        Objects.requireNonNull(stage, "Stage cannot be null");
        Objects.requireNonNull(contactRepository, "ContactRepository cannot be null");
        Objects.requireNonNull(receiptProcessor, "ReceiptProcessor cannot be null");
        contactRepository.loadContacts();
        this.stage = stage;
        for (Pages page : Pages.values()) {
            addSceneMap(page, page.getPath(), contactRepository, receiptProcessor);
        }
    }

    /**
     * Switches to the specified scene and updates the state if it is InstanceOf IsObserver Interface.
     *
     * @param page the page to switch to
     * @throws IllegalStateException if the stage is null
     */
    public void gotoScene(Pages page) {
        Objects.requireNonNull(page, "Page cannot be null");

        if (stage != null) {
            stage.setScene(getScene(page));
            if (getController(page) instanceof IsObserver observerController) {
                observerController.update();
            }
            stage.show();
        } else {
            throw new IllegalStateException("Stage is null, can not switch scene");
        }
    }

    /**
     * Switches to the specified scene and sets the last page for controllers that implement HasDynamicLastPage.
     *
     * @param page     the page to switch to
     * @param lastPage the last page to set
     * @throws IllegalStateException    if the stage is null
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
    public void openHelpModal(HelpMessages helpText) throws IllegalStateException, IOException {
        //TODO Implement this somewhere else?
        Objects.requireNonNull(helpText, "Help message cannot be null");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/HelpModal.fxml"));
            Pane node = loader.load();

            HelpController controller = loader.getController();
            controller.setHelpText(helpText);

            Stage dialogStage = new Stage();
            Scene scene = new Scene(node);

            dialogStage.setWidth(500);
            dialogStage.setHeight(400);
            dialogStage.setMinWidth(460);
            dialogStage.setMinHeight(360);
            dialogStage.setTitle("Help");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
        } catch (IllegalStateException | IOException exception) {
            logger.severe("Could not open help modal: " + exception.getMessage());
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
     * Gets the scene map.
     *
     * @return the scene map
     */
    protected Map<Pages, Pair<Scene, DefaultController>> getSceneMap() {
        return sceneMap;
    }

    /**
     * Adds a scene to the scene map.
     *
     * @param page        the page to add
     * @param pathToScene the path to the scene
     * @throws IOException if an error occurs during scene loading
     */
    private void addSceneMap(Pages page, String pathToScene, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToScene));
        Pane node = loader.load();
        DefaultController controller = loader.getController();
        controller.initialize(this, contactRepository, receiptProcessor);

        Scene scene = new Scene(node);
        sceneMap.put(page, new Pair<>(scene, controller));
    }
}