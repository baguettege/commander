package io.github.bagu.commander.api.exception.converter;

import io.github.bagu.commander.api.exception.CommanderException;

/**
 * Base exception for converter-related errors.
 *
 * <p>
 *     This exception and its subclasses are thrown when there are issues with type conversion,
 *     such as conversion failures, missing converters, or duplicate converter registrations.
 * </p>
 *
 * @see ConversionFailedException
 * @see ConverterNotFoundException
 * @see ConverterAlreadyExistsException
 */
public class ConverterException extends CommanderException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public ConverterException(String message) {
        super(message);
    }
}
