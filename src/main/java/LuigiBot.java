import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static final Path SAVE_PATH = Path.of("data", "luigibot.txt");
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
        ui.showGreeting();

        List<Task> tasks = loadTasks(ui);

        while (true) {
            String userInput = ui.readCommand();

            if (userInput.equals("bye")) {
                break;
            }
            if (userInput.isBlank()) {
                ui.showError("Mamma mia! You didn't-a enter a command.");
            } else if (userInput.equals("delete") || userInput.startsWith("delete ")) {
                deleteTask(userInput.substring(6).trim(), tasks, ui);
            } else if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                updateTaskStatus(userInput.substring(6).trim(), tasks, false, ui);
            } else if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                updateTaskStatus(userInput.substring(4).trim(), tasks, true, ui);
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
                    addTask(todo, tasks, ui);
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
                            addTask(deadline, tasks, ui);
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
                            addTask(event, tasks, ui);
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
    private static void addTask(Task task, List<Task> tasks, Ui ui) {
        tasks.add(task);
        saveTasks(tasks, ui);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Validates a task number and removes the selected task.
     *
     * @param taskNumberText user-provided task number
     * @param tasks list containing the stored tasks
     */
    private static void deleteTask(String taskNumberText, List<Task> tasks, Ui ui) {
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
            saveTasks(tasks, ui);
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
                                         boolean markAsDone, Ui ui) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showError("Oh no! Luigi can't-a find that task number.");
                return;
            }

            Task task = tasks.get(taskNumber - 1);
            if (markAsDone) {
                task.mark();
                saveTasks(tasks, ui);
                ui.showTaskMarked(task);
            } else {
                task.unmark();
                saveTasks(tasks, ui);
                ui.showTaskUnmarked(task);
            }
        } catch (NumberFormatException exception) {
            ui.showError("Mamma mia! Please-a enter a whole task number.");
        }
    }

    /**
     * Writes the current task list to the hard disk.
     *
     * @param tasks tasks to save
     */
    private static void saveTasks(List<Task> tasks, Ui ui) {
        try {
            Files.createDirectories(SAVE_PATH.getParent());
            List<String> taskData = new ArrayList<>();
            for (Task task : tasks) {
                taskData.add(task.toFileString());
            }
            Files.write(SAVE_PATH, taskData);
        } catch (IOException exception) {
            ui.showError("Mamma mia! Luigi couldn't-a save your tasks.");
        }
    }

    /**
     * Loads tasks from the save file, or returns an empty list when no file exists.
     *
     * @return tasks reconstructed from the save file
     */
    private static List<Task> loadTasks(Ui ui) {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(SAVE_PATH)) {
            return tasks;
        }

        try {
            for (String taskData : Files.readAllLines(SAVE_PATH)) {
                try {
                    tasks.add(parseTask(taskData));
                } catch (IllegalArgumentException | DateTimeParseException exception) {
                    ui.showError("Mamma mia! Luigi skipped-a an invalid saved task.");
                }
            }
        } catch (IOException exception) {
            ui.showError("Mamma mia! Luigi couldn't-a read the task file.");
        }
        return tasks;
    }

    /**
     * Reconstructs one task from its save-file representation.
     *
     * @param taskData save-file representation of one task
     * @return reconstructed task
     */
    private static Task parseTask(String taskData) {
        String[] fields = taskData.split(" \\| ", -1);
        if (fields.length < 2 || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task status");
        }

        int expectedFieldCount = switch (fields[0]) {
            case "T" -> 3;
            case "D" -> 4;
            case "E" -> 5;
            default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields.length != expectedFieldCount) {
            throw new IllegalArgumentException("Invalid task field count");
        }
        for (int i = 2; i < fields.length; i++) {
            if (fields[i].isBlank()) {
                throw new IllegalArgumentException("Empty task field");
            }
        }

        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> new Deadline(fields[2], fields[3]);
            case "E" -> new Event(fields[2], fields[3], fields[4]);
            default -> throw new IllegalArgumentException("Unknown task type");
        };

        if (fields[1].equals("1")) {
            task.mark();
        }
        return task;
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
