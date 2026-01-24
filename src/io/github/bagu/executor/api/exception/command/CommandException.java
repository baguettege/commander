package io.github.bagu.executor.api.exception.command;

import io.github.bagu.executor.api.exception.ExecutorException;

/**
 * Base exception for all command-related errors.
 * <p>
 * This exception is thrown when issues occur related to command lookup or execution.
 *
 * @see io.github.bagu.executor.api.Command
 * @see io.github.bagu.executor.api.CommandRegistry
 */
public class CommandException extends ExecutorException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandException(String message) {
        super(message);
    }
}
