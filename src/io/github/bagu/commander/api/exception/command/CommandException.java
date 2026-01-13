package io.github.bagu.commander.api.exception.command;

import io.github.bagu.commander.api.exception.CommanderException;

/**
 * Base exception for command-related errors.
 *
 * <p>
 *     This exception and its subclasses are thrown when there are issues with commands,
 *     such as missing commands, duplicate command definitions, or execution failures.
 * </p>
 *
 * @see CommandNotFoundException
 * @see CommandAlreadyExistsException
 * @see CommandExecutionException
 */
public class CommandException extends CommanderException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public CommandException(String message) {
        super(message);
    }
}
