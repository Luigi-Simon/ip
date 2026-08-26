/**
 * Runs LuigiBot's command-line task manager.
 */
public class LuigiBot {
    /**
     * Starts LuigiBot and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/luigibot.txt");
        Parser parser = new Parser();
        ui.showGreeting();

        TaskList tasks = new TaskList(storage.load(ui));

        boolean isExit = false;
        while (!isExit) {
            try {
                String userInput = ui.readCommand();
                Command command = parser.parse(userInput);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (IllegalArgumentException exception) {
                ui.showError(exception.getMessage());
            }
        }

        ui.close();
    }
}
