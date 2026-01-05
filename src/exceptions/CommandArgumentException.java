package exceptions;

/**
 * Thrown if arguments passed into a command are invalid.
 */

public class CommandArgumentException extends CommandException {
    public CommandArgumentException(String message) {
        super("Failed to parse command arguments: " + message);
    }
}
