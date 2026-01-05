package exceptions;

/**
 * Thrown once an error occurs during the parsing of a command.
 */

public class CommandParseException extends CommandException {
    public CommandParseException(String message) {
        super("Failed to parse command: " + message);
    }
}
