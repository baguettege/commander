package io.github.bagu.commander.api.exception.argument;

/**
 * Thrown when the number of provided arguments does not match the command definition.
 *
 * <p>
 *     This exception is thrown during argument resolution when the user provides a different number
 *     of positional arguments than the command requires.
 * </p>
 */
public class InvalidArgumentCountException extends ArgumentException {
    /**
     * Creates a new exception with the command name and argument counts.
     *
     * @param commandName the name of the command
     * @param expected the expected number of arguments
     * @param actual the actual number of arguments provided
     */
    public InvalidArgumentCountException(String commandName, int expected, int actual) {
        super("Command \"" + commandName + "\" expected " + expected + " arguments, got " + actual);
    }
}
