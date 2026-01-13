package io.github.bagu.commander.api.exception.argument;

/**
 * Thrown when attempting to add a duplicate argument to a command definition.
 *
 * <p>
 *     This exception is thrown during command building when an argument with the same name
 *     is added more than once.
 * </p>
 */
public class ArgumentAlreadyExistsException extends ArgumentException {
    /**
     * Creates a new exception for the specified argument name.
     *
     * @param argumentName the name of the duplicate argument
     */
    public ArgumentAlreadyExistsException(String argumentName) {
        super("Argument already exists with name \"" + argumentName + "\"");
    }
}
