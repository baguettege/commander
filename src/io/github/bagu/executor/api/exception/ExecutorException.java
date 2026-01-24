package io.github.bagu.executor.api.exception;

/**
 * Base exception class for all executor-related exceptions.
 * <p>
 * This is the root of the exception hierarchy for the {@code Executor} framework.
 * All specific exceptions thrown by the framework extend this class, allowing users
 * to catch all framework exceptions with a single catch block is desired.
 *
 * @see RuntimeException
 * @see io.github.bagu.executor.api.exception.arg.ArgException
 * @see io.github.bagu.executor.api.exception.command.CommandException
 * @see io.github.bagu.executor.api.exception.converter.ConversionException
 * @see io.github.bagu.executor.api.exception.parser.ParseException
 */
public class ExecutorException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ExecutorException(String message) {
        super(message);
    }
}
