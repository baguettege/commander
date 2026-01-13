package io.github.bagu.commander.api.exception.command;

/**
 * Thrown when attempting to execute a command that does not exist.
 *
 * <p>
 *     This exception is thrown when the specified command name cannot be found in the
 *     environment's command group.
 * </p>
 */
public class CommandNotFoundException extends CommandException {
    /**
     * Creates a new exception for the specified command name.
     *
     * @param commandName the name of the command that was not found
     */
    public CommandNotFoundException(String commandName) {
        super("Command not found with name \"" + commandName + "\"");
    }
}
