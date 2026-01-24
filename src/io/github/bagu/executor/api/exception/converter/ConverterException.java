package io.github.bagu.executor.api.exception.converter;

import io.github.bagu.executor.api.exception.ExecutorException;

/**
 * Base exception for {@link io.github.bagu.executor.api.Converter converter}-related errors.
 * <p>
 * This exception is thrown when issues occur during type conversion of string arguments to their
 * target types.
 *
 * @see io.github.bagu.executor.api.Converter
 */
public class ConverterException extends ExecutorException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ConverterException(String message) {
        super(message);
    }
}
