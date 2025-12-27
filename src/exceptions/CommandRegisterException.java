package exceptions;

/**
 * Thrown once any error occurs during the registration of commands.
 */

public class CommandRegisterException extends CommandException {
    public CommandRegisterException(String message) {
        super(message);
    }
}
