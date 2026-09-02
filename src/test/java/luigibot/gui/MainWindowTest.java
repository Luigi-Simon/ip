package luigibot.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

class MainWindowTest {

    @BeforeAll
    static void startJavaFxToolkit() throws InterruptedException {
        CountDownLatch toolkitStarted = new CountDownLatch(1);
        try {
            Platform.startup(toolkitStarted::countDown);
        } catch (IllegalStateException exception) {
            toolkitStarted.countDown();
        }
        assertTrue(toolkitStarted.await(5, TimeUnit.SECONDS));
    }

    @Test
    void portraitImages_packagedResources_bothAvailable() {
        assertNotNull(MainWindow.class.getResource("/images/Luigi.png"));
        assertNotNull(MainWindow.class.getResource("/images/Mario.png"));
    }

    @Test
    void initialize_mainWindowLoaded_welcomeDialogDisplayed() throws Exception {
        FutureTask<String> welcomeDialogText = new FutureTask<>(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/MainWindow.fxml"));
            fxmlLoader.load();
            VBox dialogContainer = (VBox) fxmlLoader.getNamespace().get("dialogContainer");

            assertEquals(1, dialogContainer.getChildren().size());
            DialogBox welcomeDialog = (DialogBox) dialogContainer.getChildren().get(0);
            Label dialogLabel = (Label) welcomeDialog.lookup(".label");
            return dialogLabel.getText();
        });
        Platform.runLater(welcomeDialogText);

        assertEquals("Its a-me,LuigiBot!\nWhat can I do for you?",
                welcomeDialogText.get(5, TimeUnit.SECONDS));
    }

    @Test
    void formatResponse_consoleDividerLines_cleanResponseReturned() {
        String responseWithDividers = "____________________________________________________________\n"
                + "Okie-dokie! Luigi added this task:\n"
                + "  [T][ ] borrow book\n"
                + "You've-a got 1 tasks now!\n"
                + "____________________________________________________________\n";

        assertEquals("Okie-dokie! Luigi added this task:\n"
                        + "  [T][ ] borrow book\n"
                        + "You've-a got 1 tasks now!",
                MainWindow.formatResponse(responseWithDividers));
    }

    @Test
    void getUserDialog_userMessage_userDialogStyleApplied() {
        DialogBox userDialog = DialogBox.getUserDialog("hello", new WritableImage(1, 1));

        assertTrue(userDialog.getStyleClass().contains("user-dialog"));
    }

    @Test
    void getLuigiDialog_luigiMessage_luigiDialogStyleApplied() {
        DialogBox luigiDialog = DialogBox.getLuigiDialog("hello", new WritableImage(1, 1));

        assertTrue(luigiDialog.getStyleClass().contains("luigi-dialog"));
    }
}
