/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with the given description and time period.
     *
     * @param description description of the event
     * @param from time at which the event starts
     * @param to time at which the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event type icon, task details, and time period.
     *
     * @return display form of this event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + this.from + " to: " + this.to + ")";
    }

    /**
     * Returns this event in the text format used by the save file.
     *
     * @return save-file representation of this event
     */
    @Override
    public String toFileString() {
        return getFileString("E") + " | " + this.from + " | " + this.to;
    }
}
