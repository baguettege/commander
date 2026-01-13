package io.github.bagu.commander.api.exception.command;

/**
 * Thrown when attempting to add a duplicate command to a command group.
 *
 * <p>
 *     This exception is thrown during command group building when a command with the same name
 *     is added more than once.
 * </p>
 */
public class CommandAlreadyExistsException extends CommandException {
    /**
     * Creates a new exception for the specified command name.
     *
     * @param commandName the name of the duplicate command
     */
    public CommandAlreadyExistsException(String commandName) {
        super("Command already exists with name \"" + commandName + "\"");
    }
}
