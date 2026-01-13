package io.github.bagu.commander.api.core;

import io.github.bagu.commander.api.context.Context;
import io.github.bagu.commander.api.definition.CommandDefinition;
import io.github.bagu.commander.api.exception.command.CommandExecutionException;

/**
 * Executes command logic with a typed context.
 *
 * <p>
 *     A command handler is the executable part of a command definition. It receives a {@link Context} containing the
 *     resolved, typed arguments and performs the command's logic.
 * </p>
 *
 * <p>
 *     Handlers should focus on command implementation and can access any arguments and commands (in the command group)
 *     through the context.
 * </p>
 *
 * <p>
 *     An example:
 *     <pre>
 *         {@code CommandHandler<MyContext> handler = ctx -> {
 *     String name = ctx.args().arg("name", String.class);
 *     Integer count = ctx.args().option("count", Integer.class).orElse(1);
 *     boolean verbose = ctx.args().flag("verbose");
 *
 *     // command implementation
 *     for (int i = 0; i < count; i++ {
 *          System.out.println("Hello, " + name + "!");
 *          if (verbose)
 *              System.out.println("Iteration: " + (i + 1));
 *     }
 * };}
 *     </pre>
 * </p>
 *
 * @param <T> type of context that this handler accepts
 *
 * @see Context
 * @see CommandDefinition
 */
@FunctionalInterface
public interface CommandHandler<T extends Context<T>> {
    /**
     * Executes the command with the given context.
     *
     * <p>
     *     this method is called after all arguments have been resolved and the context has been
     *     created. Implementations should perform the commands logic and should throw a
     *     {@link CommandExecutionException} to indicate any failures.
     * </p>
     *
     * @param context the context to execute the command with
     * @throws CommandExecutionException if a failure occurs during execution
     */
    void execute(T context) throws CommandExecutionException;
}
