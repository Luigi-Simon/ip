package luigibot;

import luigibot.command.Command;
import luigibot.parser.Parser;
import luigibot.storage.Storage;
import luigibot.task.TaskList;
import luigibot.ui.Ui;

/**
 * Runs LuigiBot's command-line task manager.
 */
public class LuigiBot {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Creates LuigiBot using the specified task-file path.
     *
     * @param filePath path used to load and save tasks.
     */
    public LuigiBot(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
    }

    /**
     * Runs LuigiBot and processes commands until the user exits.
     */
    public void run() {
        this.ui.showGreeting();

        this.tasks = new TaskList(this.storage.load(this.ui));

        boolean isExit = false;
        while (!isExit) {
            try {
                String userInput = this.ui.readCommand();
                Command command = this.parser.parse(userInput);
                command.execute(this.tasks, this.ui, this.storage);
                isExit = command.isExit();
            } catch (IllegalArgumentException exception) {
                this.ui.showError(exception.getMessage());
            }
        }

        this.ui.close();
    }

    /**
     * Starts LuigiBot with its default task-file path.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new LuigiBot("data/luigibot.txt").run();
    }
}
