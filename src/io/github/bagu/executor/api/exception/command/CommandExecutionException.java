package io.github.bagu.executor.api.exception.command;

import io.github.bagu.executor.api.Context;

/**
 * Exception thrown when command execution fails.
 * <p>
 * This exception is thrown by {@link io.github.bagu.executor.api.Command#execute(Context)} when the
 * command logic encounters an error during execution.
 *
 * @see io.github.bagu.executor.api.Command
 */
public class CommandExecutionException extends CommandException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandExecutionException(String message) {
        super(message);
    }
}
