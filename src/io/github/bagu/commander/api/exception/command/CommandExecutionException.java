package io.github.bagu.commander.api.exception.command;

/**
 * Thrown when a command fails during execution.
 *
 * <p>
 *     This exception can be thrown by command handlers to indicate that the command execution failed.
 * </p>
 */
public class CommandExecutionException extends CommandException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public CommandExecutionException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the specified message and cause.
     *
     * @param message the detail message
     * @param throwable the underlying cause of the failure
     */
    public CommandExecutionException(String message, Throwable throwable) {
        super(message + ": " + throwable.getMessage());
    }
}
