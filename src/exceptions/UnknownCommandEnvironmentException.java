package exceptions;

/**
 * Thrown by a command reader once an unregistered command environment has been inputted.
 */

public class UnknownCommandEnvironmentException extends CommandException {
    public UnknownCommandEnvironmentException(String name) {
        super("Unknown command environment: " + name);
    }
}
