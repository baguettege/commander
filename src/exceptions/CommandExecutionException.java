package exceptions;

/**
 * Thrown once an error occurs during the execution of a command.
 */

public class CommandExecutionException extends CommandException {
    public CommandExecutionException(String message) {
        super("Failed to execute command: " + message);
    }
}
