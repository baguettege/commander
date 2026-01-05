package exceptions;

/**
 * Thrown by a command reader once an unregistered command has been inputted.
 */

public class UnknownCommandException extends CommandException {
    public UnknownCommandException(String name) {
        super("Unknown command: " + name);
    }
}
