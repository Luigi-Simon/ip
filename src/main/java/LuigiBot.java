import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs LuigiBot's command-line task manager.
 */
public class LuigiBot {
    private static final DateTimeFormatter DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Starts LuigiBot and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/luigibot.txt");
        ui.showGreeting();

        List<Task> tasks = storage.load(ui);

        while (true) {
            String userInput = ui.readCommand();

            if (userInput.equals("bye")) {
                break;
            }
            if (userInput.isBlank()) {
                ui.showError("Mamma mia! You didn't-a enter a command.");
            } else if (userInput.equals("delete") || userInput.startsWith("delete ")) {
                deleteTask(userInput.substring(6).trim(), tasks, storage, ui);
            } else if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                updateTaskStatus(userInput.substring(6).trim(), tasks, false, storage, ui);
            } else if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                updateTaskStatus(userInput.substring(4).trim(), tasks, true, storage, ui);
            } else if (userInput.equals("list")) {
                ui.showTaskList(tasks);
            } else if (userInput.equals("on") || userInput.startsWith("on ")) {
                printTasksOnDate(userInput.substring(2).trim(), tasks, ui);
            } else if (userInput.equals("todo") || userInput.startsWith("todo ")) {
                String description = userInput.substring(4).trim();
                if (description.isEmpty()) {
                    ui.showError("Mamma mia! The task description can't-a be empty.");
                } else {
                    Todo todo = new Todo(description);
                    addTask(todo, tasks, storage, ui);
                }
            } else if (userInput.equals("deadline") || userInput.startsWith("deadline ")) {
                String deadlineDetails = userInput.substring(8).trim();
                int byIndex = deadlineDetails.indexOf("/by");
                boolean hasByMarker = byIndex >= 0
                        && (byIndex == 0 || Character.isWhitespace(deadlineDetails.charAt(byIndex - 1)))
                        && (byIndex + 3 == deadlineDetails.length()
                        || Character.isWhitespace(deadlineDetails.charAt(byIndex + 3)));

                if (!hasByMarker) {
                    ui.showError("Oh no! Luigi needs-a know the deadline! Use /by.");
                } else {
                    String description = deadlineDetails.substring(0, byIndex).trim();
                    String by = deadlineDetails.substring(byIndex + 3).trim();
                    if (description.isEmpty()) {
                        ui.showError("Mamma mia! The task description can't-a be empty.");
                    } else if (by.isEmpty()) {
                        ui.showError("Oh no! Luigi needs-a know the deadline! Use /by.");
                    } else {
                        try {
                            Deadline deadline = new Deadline(description, by);
                            addTask(deadline, tasks, storage, ui);
                        } catch (DateTimeParseException exception) {
                            ui.showError("Mamma mia! Use-a yyyy-MM-dd HHmm "
                                    + "for the deadline date and time.");
                        }
                    }
                }
            } else if (userInput.equals("event") || userInput.startsWith("event ")) {
                String eventDetails = userInput.substring(5).trim();
                int fromIndex = eventDetails.indexOf("/from");
                int toIndex = eventDetails.indexOf("/to");
                boolean hasFromMarker = fromIndex >= 0
                        && (fromIndex == 0 || Character.isWhitespace(eventDetails.charAt(fromIndex - 1)))
                        && (fromIndex + 5 == eventDetails.length()
                        || Character.isWhitespace(eventDetails.charAt(fromIndex + 5)));
                boolean hasToMarker = toIndex >= 0
                        && (toIndex == 0 || Character.isWhitespace(eventDetails.charAt(toIndex - 1)))
                        && (toIndex + 3 == eventDetails.length()
                        || Character.isWhitespace(eventDetails.charAt(toIndex + 3)));

                if (!hasFromMarker || !hasToMarker || fromIndex >= toIndex) {
                    ui.showError("Mamma mia! Use: event DESCRIPTION /from START /to END.");
                } else {
                    String description = eventDetails.substring(0, fromIndex).trim();
                    String from = eventDetails.substring(fromIndex + 5, toIndex).trim();
                    String to = eventDetails.substring(toIndex + 3).trim();
                    if (description.isEmpty()) {
                        ui.showError("Mamma mia! The task description can't-a be empty.");
                    } else if (from.isEmpty() || to.isEmpty()) {
                        ui.showError("Mamma mia! Use: event DESCRIPTION /from START /to END.");
                    } else {
                        try {
                            Event event = new Event(description, from, to);
                            addTask(event, tasks, storage, ui);
                        } catch (DateTimeParseException exception) {
                            ui.showError("Mamma mia! Use-a yyyy-MM-dd HHmm for both Event times.");
                        } catch (IllegalArgumentException exception) {
                            ui.showError("Mamma mia! The Event must-a end after it starts.");
                        }
                    }
                }
            } else {
                ui.showError("Oh no! Luigi doesn't-a recognize that command.");
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
    private static void addTask(Task task, List<Task> tasks, Storage storage, Ui ui) {
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
    private static void deleteTask(String taskNumberText, List<Task> tasks,
                                   Storage storage, Ui ui) {
        if (taskNumberText.isEmpty()) {
            ui.showError("Oh no! Luigi can't-a find that task number.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showError("Oh no! Luigi can't-a find that task number.");
                return;
            }

            Task removedTask = tasks.remove(taskNumber - 1);
            storage.save(tasks, ui);
            ui.showTaskDeleted(removedTask, tasks.size());
        } catch (NumberFormatException exception) {
            ui.showError("Mamma mia! Please-a enter a whole task number.");
        }
    }

    /**
     * Validates a task number and updates the selected task's completion status.
     *
     * @param taskNumberText user-provided task number
     * @param tasks stored tasks
     * @param markAsDone whether the selected task should be marked as done
     */
    private static void updateTaskStatus(String taskNumberText, List<Task> tasks,
                                         boolean markAsDone, Storage storage, Ui ui) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showError("Oh no! Luigi can't-a find that task number.");
                return;
            }

            Task task = tasks.get(taskNumber - 1);
            if (markAsDone) {
                task.mark();
                storage.save(tasks, ui);
                ui.showTaskMarked(task);
            } else {
                task.unmark();
                storage.save(tasks, ui);
                ui.showTaskUnmarked(task);
            }
        } catch (NumberFormatException exception) {
            ui.showError("Mamma mia! Please-a enter a whole task number.");
        }
    }

    /**
     * Parses a date and prints all dated tasks that occur on it.
     *
     * @param dateText user-provided date in yyyy-MM-dd format
     * @param tasks stored tasks
     */
    private static void printTasksOnDate(String dateText, List<Task> tasks, Ui ui) {
        if (dateText.isEmpty()) {
            ui.showError("Mamma mia! Luigi needs-a date. Use: on yyyy-MM-dd.");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateText, DATE_INPUT_FORMAT);
            List<Integer> matchingIndexes = new ArrayList<>();
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).occursOn(date)) {
                    matchingIndexes.add(i);
                }
            }

            ui.showTasksOnDate(date, tasks, matchingIndexes);
        } catch (DateTimeParseException exception) {
            ui.showError("Mamma mia! Use-a yyyy-MM-dd for the date.");
        }
    }
}
