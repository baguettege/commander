package io.github.bagu.executor.api.exception.arg;

import io.github.bagu.executor.api.CompositeCommandEngine;

/**
 * Exception thrown when the number of arguments doesn't match expectations.
 *
 * @see io.github.bagu.executor.api.spec.ArgSpec
 * @see io.github.bagu.executor.api.Environment
 * @see CompositeCommandEngine
 */
public class ArgCountException extends ArgException {
    /**
     * Constructs a new exception with expected and actual argument counts.
     *
     * @param expected the expected number of arguments
     * @param actual the actual number of arguments provided
     */
    public ArgCountException(int expected, int actual) {
        super("Expected " + expected + " args, got " + actual);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ArgCountException(String message) {
        super(message);
    }
}
