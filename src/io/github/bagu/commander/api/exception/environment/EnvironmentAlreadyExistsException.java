package io.github.bagu.commander.api.exception.environment;

/**
 * Thrown when attempting to register a duplicate environment in the framework.
 *
 * <p>
 *     This exception is thrown during framework building when an environment with the same name
 *     is registered more than once.
 * </p>
 */
public class EnvironmentAlreadyExistsException extends EnvironmentException {
    /**
     * Creates a new exception for the specified environment name.
     *
     * @param environmentName the name of the duplicate environment
     */
    public EnvironmentAlreadyExistsException(String environmentName) {
        super("Environment already exists with name \"" + environmentName + "\"");
    }
}
