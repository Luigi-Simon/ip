/**
 * Represents a task that must be completed by a specific time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline with the given description and due time.
     *
     * @param description description of the deadline
     * @param by time by which the deadline must be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline type icon, task details, and due time.
     *
     * @return display form of this deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }

    /**
     * Returns this deadline in the text format used by the save file.
     *
     * @return save-file representation of this deadline
     */
    @Override
    public String toFileString() {
        return getFileString("D") + " | " + this.by;
    }
}
