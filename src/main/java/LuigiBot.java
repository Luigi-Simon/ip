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
            if (userInput.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(userInput.substring(7)) - 1;
                tasks[taskIndex].unmark();
                printTaskUnmarked(tasks[taskIndex]);
            } else if (userInput.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(userInput.substring(5)) - 1;
                tasks[taskIndex].mark();
                printTaskMarked(tasks[taskIndex]);
            } else if (userInput.equals("list")) {
                printTaskList(tasks, taskCount);
            } else if (userInput.startsWith("todo ")) {
                Todo todo = new Todo(userInput.substring(5));
                tasks[taskCount] = todo;
                taskCount++;
                printTaskAdded(todo, taskCount);
            } else if (userInput.startsWith("deadline ")) {
                int byIndex = userInput.indexOf(" /by ");
                String description = userInput.substring(9, byIndex);
                String by = userInput.substring(byIndex + 5);
                Deadline deadline = new Deadline(description, by);
                tasks[taskCount] = deadline;
                taskCount++;
                printTaskAdded(deadline, taskCount);
            } else if (userInput.startsWith("event ")) {
                int fromIndex = userInput.indexOf(" /from ");
                int toIndex = userInput.indexOf(" /to ");
                String description = userInput.substring(6, fromIndex);
                String from = userInput.substring(fromIndex + 7, toIndex);
                String to = userInput.substring(toIndex + 5);
                Event event = new Event(description, from, to);
                tasks[taskCount] = event;
                taskCount++;
                printTaskAdded(event, taskCount);
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
