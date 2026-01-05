package exceptions;

/**
 * Thrown once an error occurs during the registration of commands within a command registry or
 * command reader.
 */

public class CommandRegisterException extends CommandException {
    public CommandRegisterException(String message) {
        super(message);
    }
}
