import java.util.Scanner;

public class LuigiBot {
    //Constant line for easy print
    private static final String LINE = "____________________________________________________________";
    public static void main(String[] args) {
        //Start of program
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        String userInput;

        //Loop to echo user input
        while (true) {
            userInput = scanner.nextLine();

            System.out.println(LINE);
            System.out.println(userInput);
            System.out.println(LINE);
        }
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
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}

