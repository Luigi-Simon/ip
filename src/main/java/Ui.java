import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Handles console input and output for LuigiBot.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return full command entered by the user
     */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        this.scanner.close();
    }

    /**
     * Shows LuigiBot's welcome banner and greeting.
     */
    public void showGreeting() {
        System.out.println(LINE);
        String banner = ".____          .__       .____________        __   \n"
                + "|    |    __ __|__| ____ |__\\______   \\ _____/  |_\n"
                + "|    |   |  |  \\  |/ ___\\|  ||    |  _//  _ \\   __\\\n"
                + "|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | \n"
                + "|_______ \\____/|__\\___  /|__||______  /\\____/|__|\n"
                + "        \\/       /_____/            \\/             \n";
        System.out.print(banner);
        System.out.println(LINE);
        System.out.println("Its a-me,LuigiBot!");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Shows LuigiBot's goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Mama mia! Leaving already? Cya soon!");
        System.out.println(LINE);
    }

    /**
     * Shows an error message between separator lines.
     *
     * @param message error message to show
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task task that was added
     * @param taskCount number of stored tasks after adding the task
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Okie-dokie! Luigi added this task:");
        System.out.println("  " + task);
        System.out.println("You've-a got " + taskCount + " tasks now!");
        System.out.println(LINE);
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice-a! Luigi marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("No problem! Luigi marked this task as not done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Shows confirmation that a task was deleted.
     *
     * @param task task that was deleted
     * @param taskCount number of stored tasks after deleting the task
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Okie-dokie! Luigi removed this task:");
        System.out.println("  " + task);
        System.out.println("You've-a got " + taskCount + " tasks now!");
        System.out.println(LINE);
    }

    /**
     * Shows all stored tasks using their original task numbers.
     *
     * @param tasks stored tasks
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Let's-a see what Luigi has on the list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.getTasks().get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Shows stored tasks that occur on a specified date.
     *
     * @param date date being searched
     * @param tasks stored tasks
     * @param matchingIndexes indexes of tasks occurring on the date
     */
    public void showTasksOnDate(LocalDate date, TaskList tasks,
                                List<Integer> matchingIndexes) {
        System.out.println(LINE);
        if (matchingIndexes.isEmpty()) {
            System.out.println("Mamma mia! Luigi found-a no tasks on "
                    + date.format(DATE_DISPLAY_FORMAT) + ".");
        } else {
            System.out.println("Luigi found-a these tasks on "
                    + date.format(DATE_DISPLAY_FORMAT) + ":");
            for (int index : matchingIndexes) {
                System.out.println((index + 1) + "." + tasks.getTasks().get(index));
            }
        }
        System.out.println(LINE);
    }
}
