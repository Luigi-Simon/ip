/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the icon representing this task's completion state.
     *
     * @return {@code [X]} when completed, or {@code [ ]} otherwise
     */
    public String getStatusIcon() {
        return this.isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns this task in the text format used by the save file.
     *
     * @return save-file representation of this task
     */
    public String toFileString() {
        return getFileString("T");
    }

    /**
     * Builds the common save-file fields for a specific task type.
     *
     * @param taskType letter identifying the task type
     * @return common save-file fields for this task
     */
    protected String getFileString(String taskType) {
        String status = this.isDone ? "1" : "0";
        return taskType + " | " + status + " | " + this.description;
    }

    /**
     * Returns the task's status icon followed by its description.
     *
     * @return display form of this task
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + this.description;
    }
}
