import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses user commands and converts their arguments into domain objects.
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

    /**
     * Returns the command word before the first space.
     *
     * @param userInput full user input
     * @return command word
     */
    public String getCommandWord(String userInput) {
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
    public String getArguments(String userInput) {
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
    public Todo parseTodo(String arguments) {
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
    public Deadline parseDeadline(String arguments) {
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
    public Event parseEvent(String arguments) {
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
    public int parseTaskNumber(String taskNumberText) {
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
    public LocalDate parseDate(String dateText) {
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
}
