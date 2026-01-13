package io.github.bagu.commander.api.exception.parser;

import io.github.bagu.commander.api.exception.CommanderException;

/**
 * Base exception for parsing-related errors.
 *
 * <p>
 *     This exception and its subclasses are thrown when there are issues parsing command input,
 *     such as tokenization failures or invalid invocation formats.
 * </p>
 *
 * @see TokenizationException
 * @see InvocationFormatException
 */
public class ParseException extends CommanderException {
    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message
     */
    public ParseException(String message) {
        super(message);
    }
}
