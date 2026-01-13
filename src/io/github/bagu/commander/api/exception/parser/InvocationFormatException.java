package io.github.bagu.commander.api.exception.parser;

/**
 * Thrown when command invocation format is invalid.
 *
 * <p>
 *     This exception is thrown when the parsed command does not match the expected format,
 *     such as when fewer than two tokens (environment and command name) are provided.
 * </p>
 */
public class InvocationFormatException extends ParseException {
    /**
     * Creates a new exception with the expected format.
     *
     * @param expectedFormat the expected format description
     */
    public InvocationFormatException(String expectedFormat) {
        super("Invalid invocation format, expected " + expectedFormat);
    }
}
