package io.github.bagu.executor.api.exception.parser;

import io.github.bagu.executor.api.exception.ExecutorException;

/**
 * Base exception for parsing-related errors.
 * <p>
 * This exception is thrown when issues occur during parsing of command input, either
 * during tokenization or argument parsing.
 *
 * @see io.github.bagu.executor.internal.Tokenizer
 * @see io.github.bagu.executor.internal.ArgParser
 */
public class ParseException extends ExecutorException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ParseException(String message) {
        super(message);
    }
}
