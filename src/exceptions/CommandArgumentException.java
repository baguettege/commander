package exceptions;

public class CommandArgumentException extends CommandException {
    public CommandArgumentException(String message) {
        super("Illegal args: " + message);
    }
}
