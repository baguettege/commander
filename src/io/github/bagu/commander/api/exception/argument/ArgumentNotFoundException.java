package io.github.bagu.commander.api.exception.argument;

/**
 * Thrown when attempting to retrieve an argument that does not exist.
 *
 * <p>
 *     This exception is thrown by {@link io.github.bagu.commander.api.argument.Arguments#arg(String, Class)}
 *     when the requested argument name is not present in the resolved arguments.
 * </p>
 */
public class ArgumentNotFoundException extends ArgumentException {
    /**
     * Creates a new exception for the specified argument name.
     *
     * @param argumentName the name of the argument that was not found
     */
    public ArgumentNotFoundException(String argumentName) {
        super("Argument not found with name \"" + argumentName + "\"");
    }
}
