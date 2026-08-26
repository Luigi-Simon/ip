import java.time.LocalDate;
import java.util.List;

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
                    case "unmark" -> updateTaskStatus(
                            arguments, tasks, false, storage, ui, parser);
                    case "mark" -> updateTaskStatus(
                            arguments, tasks, true, storage, ui, parser);
                    case "list" -> {
                        if (userInput.equals("list")) {
                            ui.showTaskList(tasks);
                        } else {
                            ui.showError("Oh no! Luigi doesn't-a recognize that command.");
                        }
                    }
                    case "on" -> printTasksOnDate(arguments, tasks, ui, parser);
                    case "todo" -> addTask(parser.parseTodo(arguments), tasks, storage, ui);
                    case "deadline" -> addTask(
                            parser.parseDeadline(arguments), tasks, storage, ui);
                    case "event" -> addTask(parser.parseEvent(arguments), tasks, storage, ui);
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
     * Adds a task to the task list and prints a confirmation.
     *
     * @param task task to add
     * @param tasks list containing the stored tasks
     */
    private static void addTask(Task task, TaskList tasks, Storage storage, Ui ui) {
        tasks.add(task);
        storage.save(tasks, ui);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Validates a task number and removes the selected task.
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
        if (!tasks.isValidTaskNumber(taskNumber)) {
            ui.showError("Oh no! Luigi can't-a find that task number.");
            return;
        }

        Task removedTask = tasks.delete(taskNumber);
        storage.save(tasks, ui);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Validates a task number and updates the selected task's completion status.
     *
     * @param taskNumberText user-provided task number
     * @param tasks stored tasks
     * @param markAsDone whether the selected task should be marked as done
     */
    private static void updateTaskStatus(String taskNumberText, TaskList tasks,
                                         boolean markAsDone, Storage storage, Ui ui,
                                         Parser parser) {
        int taskNumber = parser.parseTaskNumber(taskNumberText);
        if (!tasks.isValidTaskNumber(taskNumber)) {
            ui.showError("Oh no! Luigi can't-a find that task number.");
            return;
        }

        Task task;
        if (markAsDone) {
            task = tasks.mark(taskNumber);
            storage.save(tasks, ui);
            ui.showTaskMarked(task);
        } else {
            task = tasks.unmark(taskNumber);
            storage.save(tasks, ui);
            ui.showTaskUnmarked(task);
        }
    }

    /**
     * Parses a date and prints all dated tasks that occur on it.
     *
     * @param dateText user-provided date in yyyy-MM-dd format
     * @param tasks stored tasks
     */
    private static void printTasksOnDate(String dateText, TaskList tasks, Ui ui,
                                         Parser parser) {
        LocalDate date = parser.parseDate(dateText);
        List<Integer> matchingIndexes = tasks.findIndexesOnDate(date);
        ui.showTasksOnDate(date, tasks, matchingIndexes);
    }
}
