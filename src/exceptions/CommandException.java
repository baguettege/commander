package exceptions;

/**
 * Thrown once any type of exception occurs whilst handling command input, parsing and execution.
 */

public class CommandException extends IllegalArgumentException {
    public CommandException(String message) {
        super(message);
    }
}
