package io.github.bagu.executor.api.exception.command;

/**
 * Exception thrown when a command name is not found in the registry.
 * <p>
 * This exception is thrown when the user provides a command name that does not match any
 * registered command in the {@link io.github.bagu.executor.api.CommandRegistry}.
 *
 * @see io.github.bagu.executor.api.CommandRegistry
 * @see io.github.bagu.executor.api.spec.CommandSpec
 */
public class CommandNotFoundException extends CommandException {
    /**
     * Constructs a new exception for the specified command name.
     *
     * @param name the name of the command that was not found
     */
    public CommandNotFoundException(String name) {
        super("Command \"" + name + "\" not found");
    }
}
