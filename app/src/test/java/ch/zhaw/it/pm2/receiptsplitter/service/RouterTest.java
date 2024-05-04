
package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class RouterTest {
    private Router router;

    @Mock
    private Stage stage;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ReceiptProcessor receiptProcessor;

    @BeforeAll
    static void setUpAll() {
        // Initialize JavaFX Runtime for testing
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() throws Exception {
        router = new Router(stage, contactRepository, receiptProcessor);
    }

    @Test
    void constructor_ValidArguments_SceneMapHasAllPages() throws IOException {
        // Arrange
        Router router = new Router(stage, contactRepository, receiptProcessor);
        Map<Pages, ?> sceneMap = router.getSceneMap();

        // Assert
        assertEquals(sceneMap.keySet(), getValidPages());
    }

    @Test
    void constructor_NullStage_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Router(null, contactRepository, receiptProcessor));
    }

    @Test
    void constructor_NullContactRepository_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Router(null, contactRepository, receiptProcessor));
    }

    @Test
    void constructor_NullReceiptProcessor_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Router(null, contactRepository, receiptProcessor));
    }

    @ParameterizedTest
    @MethodSource("getValidPages")
    void gotoScene_ValidPage_SceneSwitched(Pages validPage) {
        // Arrange
        doNothing().when(stage).show();
        Scene expectedScene = router.getScene(validPage);

        // Act
        router.gotoScene(validPage);

        // Assert
        Mockito.verify(stage).setScene(expectedScene);
        Mockito.verify(stage).show();
    }

    @Test
    void gotoScene_NullPage_ThrowsException() {
        // Arrange
        Pages invalidPage = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> router.gotoScene(invalidPage));
    }

    @Test
    void openHelpModal_NullHelpMessage_ThrowsException() {
        // Arrange
        HelpMessages invalidHelpMessage = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> router.openHelpModal(invalidHelpMessage));
    }

    static Set<Pages> getValidPages() {
        return new HashSet<>(Arrays.asList(Pages.values()));
    }
}