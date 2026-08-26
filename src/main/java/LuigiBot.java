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

        while (true) {
            String userInput = ui.readCommand();

            if (userInput.equals("bye")) {
                break;
            }
            if (userInput.isBlank()) {
                ui.showError("Mamma mia! You didn't-a enter a command.");
            } else {
                String commandWord = parser.getCommandWord(userInput);
                String arguments = parser.getArguments(userInput);
                try {
                    switch (commandWord) {
                    case "delete" -> deleteTask(arguments, tasks, storage, ui, parser);
                    case "unmark" -> executeCommand(
                            new UnmarkCommand(parser.parseTaskNumber(arguments)),
                            tasks, storage, ui);
                    case "mark" -> executeCommand(
                            new MarkCommand(parser.parseTaskNumber(arguments)),
                            tasks, storage, ui);
                    case "list" -> {
                        if (userInput.equals("list")) {
                            executeCommand(new ListCommand(), tasks, storage, ui);
                        } else {
                            ui.showError("Oh no! Luigi doesn't-a recognize that command.");
                        }
                    }
                    case "on" -> executeCommand(
                            new FindCommand(parser.parseDate(arguments)), tasks, storage, ui);
                    case "todo" -> executeCommand(
                            new AddCommand(parser.parseTodo(arguments)), tasks, storage, ui);
                    case "deadline" -> executeCommand(
                            new AddCommand(parser.parseDeadline(arguments)), tasks, storage, ui);
                    case "event" -> executeCommand(
                            new AddCommand(parser.parseEvent(arguments)), tasks, storage, ui);
                    default -> ui.showError("Oh no! Luigi doesn't-a recognize that command.");
                    }
                } catch (IllegalArgumentException exception) {
                    ui.showError(exception.getMessage());
                }
            }
        }

        ui.close();
        ui.showGoodbye();
    }

    /**
     * Executes a command using LuigiBot's application components.
     *
     * @param command command to execute
     * @param tasks stored tasks
     * @param storage storage used to persist task changes
     * @param ui user interface used to display results
     */
    private static void executeCommand(Command command, TaskList tasks,
                                       Storage storage, Ui ui) {
        command.execute(tasks, ui, storage);
    }

    /**
     * Parses a task number and executes a command to delete the selected task.
     *
     * @param taskNumberText user-provided task number
     * @param tasks list containing the stored tasks
     */
    private static void deleteTask(String taskNumberText, TaskList tasks,
                                   Storage storage, Ui ui, Parser parser) {
        if (taskNumberText.isEmpty()) {
            ui.showError("Oh no! Luigi can't-a find that task number.");
            return;
        }

        int taskNumber = parser.parseTaskNumber(taskNumberText);
        executeCommand(new DeleteCommand(taskNumber), tasks, storage, ui);
    }
}
