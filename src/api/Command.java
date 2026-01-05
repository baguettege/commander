package api;

import exceptions.CommandArgumentException;
import exceptions.CommandExecutionException;

/**
 * Represents a command to the application based off of a specified {@link CommandContext}.
 *
 * <p>
 *     This interface is generic over the type of {@link CommandContext} it uses to allow
 *     type-safety when accessing arguments and specific contexts.
 * </p>
 *
 * <p>
 *     All implementations must be stateless and provide a no-arg constructor to be registered into a
 *     {@link CommandRegistry}.
 * </p>
 *
 * @param <C> type of {@link CommandContext} used by the command
 */

public interface Command<C extends CommandContext<C>> {

    /**
     * Executes the command based off of a given {@link CommandContext}.
     *
     * @param context context to execute the command with
     * @throws CommandArgumentException if arguments provided to the command are invalid
     * @throws CommandExecutionException if an error occurs whilst executing the command
     */
    void execute(CommandContext<C> context) throws CommandArgumentException, CommandExecutionException;

    /**
     * Returns the unique name of this command.
     *
     * <p>
     *     This name is used to look up the command from a {@link CommandRegistry}.
     * </p>
     *
     * @return name of the command
     */
    String name();
}
