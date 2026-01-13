package io.github.bagu.commander.api.exception.argument;

import io.github.bagu.commander.api.exception.CommanderException;

/**
 * Base exception for all argument-related errors.
 *
 * <p>
 *     This exception and its subclasses are thrown when there are issues with command arguments,
 *     such as missing arguments, duplicate definitions, or invalid argument counts.
 * </p>
 *
 * @see ArgumentNotFoundException
 * @see ArgumentAlreadyExistsException
 * @see InvalidArgumentCountException
 * @see OptionAlreadyExistsException
 * @see FlagAlreadyExistsException
 */
public class ArgumentException extends CommanderException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public ArgumentException(String message) {
        super(message);
    }
}
