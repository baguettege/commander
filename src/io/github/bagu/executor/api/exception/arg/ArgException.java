package io.github.bagu.executor.api.exception.arg;

import io.github.bagu.executor.api.exception.ExecutorException;

/**
 * Base exception for argument-related errors.
 * <p>
 * This exception is thrown when issues occur relating to command arguments, including
 * validation failures, missing arguments, or incorrect argument counts.
 *
 * @see io.github.bagu.executor.api.spec.ArgSpec
 * @see io.github.bagu.executor.api.Context
 */
public class ArgException extends ExecutorException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ArgException(String message) {
        super(message);
    }
}
