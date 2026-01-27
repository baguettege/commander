package io.github.bagu.executor.api.exception.environment;

/**
 * Exception thrown when an environment name is not found in the composite command engine.
 * <p>
 * This exception is thrown when a user provides an environment name that does not match any
 * registered environment in the {@link io.github.bagu.executor.api.CompositeCommandEngine}.
 *
 * @see io.github.bagu.executor.api.CompositeCommandEngine
 * @see io.github.bagu.executor.api.Environment
 */
public class EnvironmentNotFoundException extends EnvironmentException {
    /**
     * Constructs a new exception for the specified environment name.
     *
     * @param name the name of the environment that was not found
     */
    public EnvironmentNotFoundException(String name) {
        super("Environment \"" + name + "\" not found");
    }
}
