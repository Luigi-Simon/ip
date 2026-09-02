package luigibot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LuigiBotTest {
    private static final String LINE = "____________________________________________________________";

    @TempDir
    private Path temporaryDirectory;

    @Test
    void getResponse_commandsInSequence_taskStatePreserved() {
        Path savePath = this.temporaryDirectory.resolve("tasks.txt");
        LuigiBot luigiBot = new LuigiBot(savePath.toString());

        String addResponse = luigiBot.getResponse("todo read book");
        String listResponse = luigiBot.getResponse("list");

        assertEquals(response(
                LINE,
                "Okie-dokie! Luigi added this task:",
                "  [T][ ] read book",
                "You've-a got 1 tasks now!",
                LINE), addResponse);
        assertEquals(response(
                LINE,
                "Let's-a see what Luigi has on the list:",
                "1.[T][ ] read book",
                LINE), listResponse);
    }

    @Test
    void getResponse_invalidCommand_errorResponseReturned() {
        Path savePath = this.temporaryDirectory.resolve("tasks.txt");
        LuigiBot luigiBot = new LuigiBot(savePath.toString());

        String response = luigiBot.getResponse("jump");

        assertEquals(response(
                LINE,
                "Oh no! Luigi doesn't-a recognize that command.",
                LINE), response);
    }

    @Test
    void getResponse_existingSaveFile_savedTasksLoaded() throws IOException {
        Path savePath = this.temporaryDirectory.resolve("tasks.txt");
        Files.writeString(savePath, "T | 1 | saved task");
        LuigiBot luigiBot = new LuigiBot(savePath.toString());

        String response = luigiBot.getResponse("list");

        assertEquals(response(
                LINE,
                "Let's-a see what Luigi has on the list:",
                "1.[T][X] saved task",
                LINE), response);
    }

    private static String response(String... lines) {
        return String.join(System.lineSeparator(), lines);
    }
}
