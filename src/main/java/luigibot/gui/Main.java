package luigibot.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Displays the LuigiBot JavaFX window.
 */
public class Main extends Application {

    /**
     * Creates the JavaFX application instance.
     */
    public Main() {
    }

    @Override
    public void start(Stage stage) {
        Label greeting = new Label("It's-a me, LuigiBot!");
        Scene scene = new Scene(greeting, 400, 200);

        stage.setTitle("LuigiBot");
        stage.setScene(scene);
        stage.show();
    }
}
