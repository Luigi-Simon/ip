/**
 * Represents an instruction that LuigiBot can execute.
 */
public abstract class Command {
    /**
     * Executes the command using the application's task list, user interface, and storage.
     *
     * @param tasks stored tasks
     * @param ui user interface used to display results
     * @param storage storage used to persist task changes
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /**
     * Returns whether executing this command should end LuigiBot.
     *
     * @return true when LuigiBot should exit
     */
    public boolean isExit() {
        return false;
    }
}
