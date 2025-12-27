package exceptions;

/**
 * Thrown once there is a failure to parse a command into its tokens.
 */

public class CommandParseException extends CommandException {
    public CommandParseException(String reason) {
        super("Failed to parse command: " + reason);
    }
}
