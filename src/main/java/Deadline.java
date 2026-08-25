import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specific time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu, h:mm a", Locale.ENGLISH);

    private final LocalDateTime by;

    /**
     * Creates an incomplete deadline with the given description and due date-time.
     *
     * @param description description of the deadline
     * @param by date and time by which the deadline must be completed, in yyyy-MM-dd HHmm format
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);
    }

    /**
     * Returns the deadline type icon, task details, and due time.
     *
     * @return display form of this deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + this.by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns this deadline in the text format used by the save file.
     *
     * @return save-file representation of this deadline
     */
    @Override
    public String toFileString() {
        return getFileString("D") + " | " + this.by.format(INPUT_FORMAT);
    }
}
