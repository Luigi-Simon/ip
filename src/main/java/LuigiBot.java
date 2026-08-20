import java.util.Scanner;

public class LuigiBot {
    // Constant line for easy printing
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                break;
            }
            if (userInput.isBlank()) {
                printError("Mamma mia! You didn't-a enter a command.");
            } else if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                updateTaskStatus(userInput.substring(6).trim(), tasks, taskCount, false);
            } else if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                updateTaskStatus(userInput.substring(4).trim(), tasks, taskCount, true);
            } else if (userInput.equals("list")) {
                printTaskList(tasks, taskCount);
            } else if (userInput.equals("todo") || userInput.startsWith("todo ")) {
                String description = userInput.substring(4).trim();
                if (description.isEmpty()) {
                    printError("Mamma mia! The task description can't-a be empty.");
                } else {
                    Todo todo = new Todo(description);
                    tasks[taskCount] = todo;
                    taskCount++;
                    printTaskAdded(todo, taskCount);
                }
            } else if (userInput.equals("deadline") || userInput.startsWith("deadline ")) {
                String deadlineDetails = userInput.substring(8).trim();
                int byIndex = deadlineDetails.indexOf("/by");
                boolean hasByMarker = byIndex >= 0
                        && (byIndex == 0 || Character.isWhitespace(deadlineDetails.charAt(byIndex - 1)))
                        && (byIndex + 3 == deadlineDetails.length()
                        || Character.isWhitespace(deadlineDetails.charAt(byIndex + 3)));

                if (!hasByMarker) {
                    printError("Oh no! Luigi needs-a know the deadline! Use /by.");
                } else {
                    String description = deadlineDetails.substring(0, byIndex).trim();
                    String by = deadlineDetails.substring(byIndex + 3).trim();
                    if (description.isEmpty()) {
                        printError("Mamma mia! The task description can't-a be empty.");
                    } else if (by.isEmpty()) {
                        printError("Oh no! Luigi needs-a know the deadline! Use /by.");
                    } else {
                        Deadline deadline = new Deadline(description, by);
                        tasks[taskCount] = deadline;
                        taskCount++;
                        printTaskAdded(deadline, taskCount);
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
                    printError("Mamma mia! Use: event DESCRIPTION /from START /to END.");
                } else {
                    String description = eventDetails.substring(0, fromIndex).trim();
                    String from = eventDetails.substring(fromIndex + 5, toIndex).trim();
                    String to = eventDetails.substring(toIndex + 3).trim();
                    if (description.isEmpty()) {
                        printError("Mamma mia! The task description can't-a be empty.");
                    } else if (from.isEmpty() || to.isEmpty()) {
                        printError("Mamma mia! Use: event DESCRIPTION /from START /to END.");
                    } else {
                        Event event = new Event(description, from, to);
                        tasks[taskCount] = event;
                        taskCount++;
                        printTaskAdded(event, taskCount);
                    }
                }
            } else {
                printError("Oh no! Luigi doesn't-a recognize that command.");
            }
        }

        scanner.close();
        printGoodbye();
    }

    /**
     * Prints confirmation that a typed task has been stored.
     *
     * @param task task that was stored
     * @param taskCount number of stored tasks after adding the task
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Okie-dokie! Luigi added this task:");
        System.out.println("  " + task);
        System.out.println("You've-a got " + taskCount + " tasks now!");
        System.out.println(LINE);
    }

    /**
     * Prints confirmation that a task has been marked as done.
     *
     * @param task task that was marked
     */
    private static void printTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice-a! Luigi marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Prints confirmation that a task has been marked as not done.
     *
     * @param task task that was unmarked
     */
    private static void printTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("No problem! Luigi marked this task as not done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Validates a task number and updates the selected task's completion status.
     *
     * @param taskNumberText user-provided task number
     * @param tasks stored tasks
     * @param taskCount number of stored tasks
     * @param markAsDone whether the selected task should be marked as done
     */
    private static void updateTaskStatus(String taskNumberText, Task[] tasks,
                                         int taskCount, boolean markAsDone) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                printError("Oh no! Luigi can't-a find that task number.");
                return;
            }

            Task task = tasks[taskNumber - 1];
            if (markAsDone) {
                task.mark();
                printTaskMarked(task);
            } else {
                task.unmark();
                printTaskUnmarked(task);
            }
        } catch (NumberFormatException exception) {
            printError("Mamma mia! Please-a enter a whole task number.");
        }
    }

    /**
     * Prints all stored tasks using numbering that starts from 1.
     *
     * @param tasks stored tasks
     * @param taskCount number of stored tasks
     */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println(LINE);
        System.out.println("Let's-a see what Luigi has on the list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println(LINE);
    }

    /**
     * Prints an error message between separator lines.
     *
     * @param message error message to show the user
     */
    private static void printError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Helper for welcome banner
     */
    private static void printGreeting() {
        System.out.println(LINE);
        String banner = ".____          .__       .____________        __   \n"
                + "|    |    __ __|__| ____ |__\\______   \\ _____/  |_\n"
                + "|    |   |  |  \\  |/ ___\\|  ||    |  _//  _ \\   __\\\n"
                + "|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | \n"
                + "|_______ \\____/|__\\___  /|__||______  /\\____/|__|\n"
                + "        \\/       /_____/            \\/             \n";
        System.out.print(banner); // Using print instead of println because the banner already ends with \n
        System.out.println(LINE);
        System.out.println("Its a-me,LuigiBot!");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Prints the exit message.
     */
    private static void printGoodbye() {
        System.out.println("Mama mia! Leaving already? Cya soon!");
        System.out.println(LINE);
    }
}
