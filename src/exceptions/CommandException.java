package exceptions;

/**
 * Thrown once any error occurs related to commands.
 */

public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}
