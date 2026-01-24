package io.github.bagu.executor.api;

/**
 * Handles exceptions that occur during command execution.
 * <p>
 * Implementations define how to respond to errors, such as logging, displaying error messages, or
 * recovering from failures.
 */
@FunctionalInterface
public interface ExceptionHandler {
    /**
     * Handles an exception.
     *
     * @param exception the exception to handle
     */
    void handle(Exception exception);
}
