package luigibot.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import luigibot.command.AddCommand;
import luigibot.command.Command;
import luigibot.command.DeleteCommand;
import luigibot.command.ExitCommand;
import luigibot.command.FindCommand;
import luigibot.command.FindKeywordCommand;
import luigibot.command.ListCommand;
import luigibot.command.MarkCommand;
import luigibot.command.UnmarkCommand;
import luigibot.task.Deadline;
import luigibot.task.Event;
import luigibot.task.Todo;

/**
 * Parses user input and creates executable commands.
 */
public class Parser {
    private static final DateTimeFormatter DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final String EMPTY_DESCRIPTION_ERROR =
            "Mamma mia! The task description can't-a be empty.";
    private static final String DEADLINE_DETAILS_ERROR =
            "Oh no! Luigi needs-a know the deadline! Use /by.";
    private static final String DEADLINE_DATE_ERROR =
            "Mamma mia! Use-a yyyy-MM-dd HHmm for the deadline date and time.";
    private static final String EVENT_DETAILS_ERROR =
            "Mamma mia! Use: event DESCRIPTION /from START /to END.";
    private static final String EVENT_DATE_ERROR =
            "Mamma mia! Use-a yyyy-MM-dd HHmm for both Event times.";
    private static final String EVENT_RANGE_ERROR =
            "Mamma mia! The Event must-a end after it starts.";
    private static final String EMPTY_COMMAND_ERROR =
            "Mamma mia! You didn't-a enter a command.";
    private static final String UNKNOWN_COMMAND_ERROR =
            "Oh no! Luigi doesn't-a recognize that command.";
    private static final String TASK_NOT_FOUND_ERROR =
            "Oh no! Luigi can't-a find that task number.";

    /**
     * Parses full user input and creates the corresponding command.
     *
     * @param userInput full user input
     * @return command represented by the user input
     */
    public Command parse(String userInput) {
        if (userInput.isBlank()) {
            throw new IllegalArgumentException(EMPTY_COMMAND_ERROR);
        }

        String commandWord = getCommandWord(userInput);
        String arguments = getArguments(userInput);
        return switch (commandWord) {
        case "delete" -> parseDeleteCommand(arguments);
        case "unmark" -> new UnmarkCommand(parseTaskNumber(arguments));
        case "mark" -> new MarkCommand(parseTaskNumber(arguments));
        case "list" -> {
            validateExactCommand(userInput, "list");
            yield new ListCommand();
        }
        case "on" -> new FindCommand(parseDate(arguments));
        case "find" -> new FindKeywordCommand(parseKeyword(arguments));
        case "todo" -> new AddCommand(parseTodo(arguments));
        case "deadline" -> new AddCommand(parseDeadline(arguments));
        case "event" -> new AddCommand(parseEvent(arguments));
        case "bye" -> {
            validateExactCommand(userInput, "bye");
            yield new ExitCommand();
        }
        default -> throw new IllegalArgumentException(UNKNOWN_COMMAND_ERROR);
        };
    }

    /**
     * Returns the command word before the first space.
     *
     * @param userInput full user input
     * @return command word
     */
    private String getCommandWord(String userInput) {
        int firstSpaceIndex = userInput.indexOf(' ');
        if (firstSpaceIndex < 0) {
            return userInput;
        }
        return userInput.substring(0, firstSpaceIndex);
    }

    /**
     * Returns the trimmed text following the command word.
     *
     * @param userInput full user input
     * @return command arguments, or an empty string when none were supplied
     */
    private String getArguments(String userInput) {
        int firstSpaceIndex = userInput.indexOf(' ');
        if (firstSpaceIndex < 0) {
            return "";
        }
        return userInput.substring(firstSpaceIndex + 1).trim();
    }

    /**
     * Parses a Todo from its command arguments.
     *
     * @param arguments Todo description
     * @return parsed Todo
     */
    private Todo parseTodo(String arguments) {
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DESCRIPTION_ERROR);
        }
        return new Todo(arguments);
    }

    /**
     * Parses a Deadline from its command arguments.
     *
     * @param arguments Deadline description and /by value
     * @return parsed Deadline
     */
    private Deadline parseDeadline(String arguments) {
        int byIndex = arguments.indexOf("/by");
        if (!hasMarker(arguments, byIndex, 3)) {
            throw new IllegalArgumentException(DEADLINE_DETAILS_ERROR);
        }

        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + 3).trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DESCRIPTION_ERROR);
        }
        if (by.isEmpty()) {
            throw new IllegalArgumentException(DEADLINE_DETAILS_ERROR);
        }

        try {
            return new Deadline(description, by);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(DEADLINE_DATE_ERROR, exception);
        }
    }

    /**
     * Parses an Event from its command arguments.
     *
     * @param arguments Event description, /from value, and /to value
     * @return parsed Event
     */
    private Event parseEvent(String arguments) {
        int fromIndex = arguments.indexOf("/from");
        int toIndex = arguments.indexOf("/to");
        boolean hasFromMarker = hasMarker(arguments, fromIndex, 5);
        boolean hasToMarker = hasMarker(arguments, toIndex, 3);
        if (!hasFromMarker || !hasToMarker || fromIndex >= toIndex) {
            throw new IllegalArgumentException(EVENT_DETAILS_ERROR);
        }

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + 5, toIndex).trim();
        String to = arguments.substring(toIndex + 3).trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DESCRIPTION_ERROR);
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException(EVENT_DETAILS_ERROR);
        }

        try {
            return new Event(description, from, to);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(EVENT_DATE_ERROR, exception);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(EVENT_RANGE_ERROR, exception);
        }
    }

    /**
     * Parses a displayed task number.
     *
     * @param taskNumberText task number entered by the user
     * @return parsed task number
     */
    private int parseTaskNumber(String taskNumberText) {
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Mamma mia! Please-a enter a whole task number.", exception);
        }
    }

    /**
     * Parses a date used to search for dated tasks.
     *
     * @param dateText date entered by the user
     * @return parsed date
     */
    private LocalDate parseDate(String dateText) {
        if (dateText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Mamma mia! Luigi needs-a date. Use: on yyyy-MM-dd.");
        }

        try {
            return LocalDate.parse(dateText, DATE_INPUT_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Mamma mia! Use-a yyyy-MM-dd for the date.", exception);
        }
    }

    /**
     * Parses a keyword used to search task descriptions.
     *
     * @param keywordText keyword entered by the user
     * @return trimmed keyword
     */
    private String parseKeyword(String keywordText) {
        if (keywordText.isEmpty()) {
            throw new IllegalArgumentException("Mamma mia! Luigi needs-a a keyword to find.");
        }
        return keywordText;
    }

    /**
     * Returns whether text at the given index is a separate command marker.
     *
     * @param text text containing the marker
     * @param markerIndex index where the marker starts
     * @param markerLength number of characters in the marker
     * @return true when whitespace or a string boundary surrounds the marker
     */
    private boolean hasMarker(String text, int markerIndex, int markerLength) {
        return markerIndex >= 0
                && (markerIndex == 0 || Character.isWhitespace(text.charAt(markerIndex - 1)))
                && (markerIndex + markerLength == text.length()
                || Character.isWhitespace(text.charAt(markerIndex + markerLength)));
    }

    /**
     * Parses a DeleteCommand while preserving its missing-number error message.
     *
     * @param arguments task number text
     * @return parsed DeleteCommand
     */
    private DeleteCommand parseDeleteCommand(String arguments) {
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException(TASK_NOT_FOUND_ERROR);
        }
        return new DeleteCommand(parseTaskNumber(arguments));
    }

    /**
     * Validates a command that does not accept arguments.
     *
     * @param userInput full user input
     * @param expectedCommand expected exact command
     */
    private void validateExactCommand(String userInput, String expectedCommand) {
        if (!userInput.equals(expectedCommand)) {
            throw new IllegalArgumentException(UNKNOWN_COMMAND_ERROR);
        }
    }
}
