package io.github.bagu.commander.api.exception.parser;

/**
 * Thrown when tokenization of command input fails.
 *
 * <p>
 *     This exception is thrown when the tokenizer encounters invalid syntax, such as
 *     unterminated quotes, trailing escape sequences, or unknown escape sequences.
 * </p>
 */
public class TokenizationException extends ParseException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message describing the tokenization error
     */
    public TokenizationException(String message) {
        super(message);
    }
}
