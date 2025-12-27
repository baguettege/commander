package exceptions;

import java.util.Arrays;

public class CommandArgumentException extends CommandException {
    public CommandArgumentException(String[] args) {
        super("Illegal arguments: " + Arrays.toString(args));
    }
}
