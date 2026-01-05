package exceptions;

/**
 * Thrown if a command reader attempts to read input whilst having no command environments registered.
 */

public class NoCommandEnvironmentsException extends CommandException {
    public NoCommandEnvironmentsException(String message) {
        super(message);
    }
}
