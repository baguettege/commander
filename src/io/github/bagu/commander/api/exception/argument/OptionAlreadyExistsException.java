package io.github.bagu.commander.api.exception.argument;

/**
 * Thrown when attempting to add a duplicate option to a command definition.
 *
 * <p>
 *     This exception is thrown during command building when an option with the same key
 *     is added more than once.
 * </p>
 */
public class OptionAlreadyExistsException extends ArgumentException {
    /**
     * Creates a new exception for the specified option key.
     *
     * @param optionKey the key of the duplicate option
     */
    public OptionAlreadyExistsException(String optionKey) {
        super("Option already exists with key \"" + optionKey + "\"");
    }
}
