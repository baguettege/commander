package io.github.bagu.executor.api.exception.environment;

import io.github.bagu.executor.api.exception.ExecutorException;

/**
 * Base exception for environment-related errors.
 * <p>
 * This exception is thrown when issues occur related to environment lookup or management.
 *
 * @see io.github.bagu.executor.api.Environment
 */
public class EnvironmentException extends ExecutorException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public EnvironmentException(String message) {
        super(message);
    }
}
