package api;

import java.util.Arrays;

/**
 * Represents an information container used when executing commands.
 */

public abstract class CommandContext {

    /**
     * Arguments passed into the command.
     */
    private final String[] args;

    /**
     * Constructs a {@link CommandContext} instance with specified command arguments.
     *
     * @param args arguments for the command
     */
    protected CommandContext(String[] args) {
        this.args = args;
    }

    /**
     * Getter method for command arguments.
     * <p>
     *     Prevents accidental changes to the specified arguments by generating a copy of the array.
     * </p>
     *
     * @return command arguments
     */
    public String[] args() {
        return Arrays.copyOf(args, args.length);
    }
}
