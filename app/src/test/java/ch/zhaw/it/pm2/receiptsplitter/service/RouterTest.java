/*
package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

public class RouterTest {

    private Router router;
    private Stage stage;

    @BeforeEach
    void setUp() throws Exception {
        stage = Mockito.mock(Stage.class);
        doNothing().when(stage).show();
        router = new Router(stage);
    }

    @Test
    void gotoScene_ValidPage_SceneSwitched() {
        // Arrange
        Pages validPage = Pages.LOGIN_WINDOW;
        Scene expectedScene = router.getScene(validPage);

        // Act
        router.gotoScene(validPage);

        // Assert
        Mockito.verify(stage).setScene(expectedScene);
        Mockito.verify(stage).show();
    }

    @Test
    void gotoScene_InvalidPage_ThrowsException() {
        // Arrange
        Pages invalidPage = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> router.gotoScene(invalidPage));
    }

    @Test
    void openHelpModal_ValidHelpMessage_HelpModalOpened() {
        // Arrange
        HelpMessages validHelpMessage = HelpMessages.LOGIN_WINDOW_MSG;
        doNothing().when(stage).showAndWait();

        // Act & Assert
        router.openHelpModal(validHelpMessage);
    }

    @Test
    void openHelpModal_InvalidHelpMessage_ThrowsException() {
        // Arrange
        HelpMessages invalidHelpMessage = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> router.openHelpModal(invalidHelpMessage));
    }
}*/
