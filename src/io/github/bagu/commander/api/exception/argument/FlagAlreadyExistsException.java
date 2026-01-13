package io.github.bagu.commander.api.exception.argument;

/**
 * Thrown when attempting to add a duplicate flag to a command definition.
 *
 * <p>
 *     This exception is thrown during command building when a flag with the same name
 *     is added more than once.
 * </p>
 */
public class FlagAlreadyExistsException extends ArgumentException {
    /**
     * Creates a new exception for the specified flag name.
     *
     * @param flagName the name of the duplicate flag
     */
    public FlagAlreadyExistsException(String flagName) {
        super("Flag already exists with name \"" + flagName + "\"");
    }
}
