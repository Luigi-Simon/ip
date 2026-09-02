package luigibot.gui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MainWindowTest {

    @Test
    void portraitImages_packagedResources_bothAvailable() {
        assertNotNull(MainWindow.class.getResource("/images/Luigi.png"));
        assertNotNull(MainWindow.class.getResource("/images/Mario.png"));
    }
}
