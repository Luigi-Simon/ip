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
            } else {
                tasks[taskCount] = new Task(userInput);
                taskCount++;
                printTaskAdded(userInput);
            }
        }

        scanner.close();
        printGoodbye();
    }

    /**
     * Prints confirmation that a task has been stored.
     *
     * @param task task that was stored
     */
    private static void printTaskAdded(String task) {
        System.out.println(LINE);
        System.out.println("added: " + task);
        System.out.println(LINE);
    }

    /**
     * Prints confirmation that a typed task has been stored.
     *
     * @param task task that was stored
     * @param taskCount number of stored tasks after adding the task
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints confirmation that a task has been marked as done.
     *
     * @param task task that was marked
     */
    private static void printTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("marked: " + task);
        System.out.println(LINE);
    }

    /**
     * Prints confirmation that a task has been marked as not done.
     *
     * @param task task that was unmarked
     */
    private static void printTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("unmarked: " + task);
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
        System.out.println("Here are the tasks in your list:");
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
