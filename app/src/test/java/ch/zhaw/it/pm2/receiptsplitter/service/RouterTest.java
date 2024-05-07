package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HasDynamicLastPage;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        //  Is used so that the FXMLLoader does not throw an Exception when loading the FXML files.
        //  No specific action needs to be executed, so the body is empty.
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

        if (validPage == Pages.EDIT_PROFILE_WINDOW) {
            when(contactRepository.getSelectedToEditContact()).thenReturn(mock(Contact.class));
        }

        Scene expectedScene = router.getScene(validPage);

        // Act
        router.gotoScene(validPage);

        // Assert
        Mockito.verify(stage).setScene(expectedScene);
        Mockito.verify(stage).show();
    }

    @Test
    void gotoScene_PageWithoutLastPage_ThrowsException() {
        // Arrange
        Pages lastPage = Pages.MAIN_WINDOW;
        Set<Pages> pagesWithoutLastPageSet = Arrays.stream(Pages.values())
                .filter(page -> !(router.getController(page) instanceof HasDynamicLastPage))
                        .collect(Collectors.toSet());

        for (Pages pageWithoutLastPage : pagesWithoutLastPageSet) {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> router.gotoScene(pageWithoutLastPage, lastPage));
        }
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