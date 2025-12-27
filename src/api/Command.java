package api;

import exceptions.CommandArgumentException;
import exceptions.CommandExecutionException;

/**
 * Represents a command to the application.
 * <p>
 *     Implementations must be stateless.
 * </p>
 *
 * @param <T> concrete implementation of {@link CommandContext}
 */

public interface Command<T extends CommandContext> {

    /**
     * Executes the command based off of the specified context.
     *
     * @param context context to execute the command off of
     * @throws CommandArgumentException if the arguments passed into the context were illegal
     * for the specific command
     * @throws CommandExecutionException if any error occurs whilst executing the command
     */
    void execute(T context) throws CommandArgumentException, CommandExecutionException;

    /**
     * Returns the name of the command.
     *
     * @return command name
     */
    String getName();
}
