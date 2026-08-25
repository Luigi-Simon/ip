import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu, h:mm a", Locale.ENGLISH);

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an incomplete event with the given description and date-time period.
     *
     * @param description description of the event
     * @param from date and time at which the event starts, in yyyy-MM-dd HHmm format
     * @param to date and time at which the event ends, in yyyy-MM-dd HHmm format
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDateTime.parse(from, INPUT_FORMAT);
        this.to = LocalDateTime.parse(to, INPUT_FORMAT);
    }

    /**
     * Returns the event type icon, task details, and time period.
     *
     * @return display form of this event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + this.from.format(DISPLAY_FORMAT)
                + " to: " + this.to.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns this event in the text format used by the save file.
     *
     * @return save-file representation of this event
     */
    @Override
    public String toFileString() {
        return getFileString("E") + " | " + this.from.format(INPUT_FORMAT)
                + " | " + this.to.format(INPUT_FORMAT);
    }
}
