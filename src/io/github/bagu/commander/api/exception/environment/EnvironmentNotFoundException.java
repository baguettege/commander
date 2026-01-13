package io.github.bagu.commander.api.exception.environment;

/**
 * Thrown when attempting to execute a command in an environment that does not exist.
 *
 * <p>
 *     This exception is thrown when the specified environment name cannot be found in the
 *     command framework.
 * </p>
 */
public class EnvironmentNotFoundException extends EnvironmentException {
    /**
     * Creates a new exception for the specified environment name.
     *
     * @param environmentName the name of the environment that was not found
     */
    public EnvironmentNotFoundException(String environmentName) {
        super("Environment not found with name \"" + environmentName + "\"");
    }
}
