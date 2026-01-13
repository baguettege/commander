package io.github.bagu.commander.api.exception;

/**
 * Base exception for all {@code Commander} framework errors.
 *
 * <p>
 *     This is the root of the exception hierarchy for the Commander framework. All framework-specific
 *     exceptions extend this class, allowing callers to catch all Commander-related errors
 *     with a single catch block if desired.
 * </p>
 *
 * @see io.github.bagu.commander.api.exception.argument.ArgumentException
 * @see io.github.bagu.commander.api.exception.command.CommandException
 * @see io.github.bagu.commander.api.exception.converter.ConverterException
 * @see io.github.bagu.commander.api.exception.environment.EnvironmentException
 * @see io.github.bagu.commander.api.exception.parser.ParseException
 */
public class CommanderException extends RuntimeException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public CommanderException(String message) {
        super(message);
    }
}
