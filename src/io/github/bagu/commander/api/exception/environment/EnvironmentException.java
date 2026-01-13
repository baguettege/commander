package io.github.bagu.commander.api.exception.environment;

import io.github.bagu.commander.api.exception.CommanderException;

/**
 * Base exception for environment-related errors.
 *
 * <p>
 *     This exception and its subclasses are thrown when there are issues with environments,
 *     such as missing environments or duplicate environment registrations.
 * </p>
 *
 * @see EnvironmentNotFoundException
 * @see EnvironmentAlreadyExistsException
 */
public class EnvironmentException extends CommanderException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public EnvironmentException(String message) {
        super(message);
    }
}
