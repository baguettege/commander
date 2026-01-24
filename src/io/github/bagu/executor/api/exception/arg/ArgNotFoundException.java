package io.github.bagu.executor.api.exception.arg;

/**
 * Exception thrown when attempting to retrieve a non-existent argument from a {@link io.github.bagu.executor.api.Context context}.
 * <p>
 * This exception occurs when {@link io.github.bagu.executor.api.Context#getArg(String, Class)} is called
 * with an argument name that was not defined in the command specification.
 * <p>
 * This typically indicates a programming error where the argument name in the retrieval does not
 * match the name in the {@link io.github.bagu.executor.api.spec.ArgSpec}.
 *
 * @see io.github.bagu.executor.api.spec.ArgSpec
 * @see io.github.bagu.executor.api.Context#getArg(String, Class)
 */
public class ArgNotFoundException extends ArgException {
    /**
     * Constructs a new exception for the specified argument name.
     *
     * @param argName the name of the argument that was not found
     */
    public ArgNotFoundException(String argName) {
        super("Arg \"" + argName + "\" not found");
    }
}
