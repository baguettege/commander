package exceptions;

/**
 * Thrown once any error occurs during the execution of a command.
 */

public class CommandExecutionException extends CommandException {
    public CommandExecutionException(String reason) {
        super("Command execution failed: " + reason);
    }
}
