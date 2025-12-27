package exceptions;

/**
 * Thrown once the user inputs an unregistered command name.
 */

public class UnknownCommandException extends CommandException {
    public UnknownCommandException(String commandName) {
        super("Unknown command: " + commandName);
    }
}
