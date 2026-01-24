package io.github.bagu.executor.api.exception.parser;

/**
 * Exception thrown when tokenization of a command string fails.
 * <p>
 * This exception occurs during the tokenization phase when the input string
 * contains syntax errors.
 *
 * @see io.github.bagu.executor.internal.Tokenizer
 */
public class TokenizationException extends ParseException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public TokenizationException(String message) {
        super(message);
    }
}
