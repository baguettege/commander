package io.github.bagu.executor.api;

import io.github.bagu.executor.api.exception.command.CommandExecutionException;

/**
 * Represents a command that can be executed within a given context.
 *
 * @param <T> the type of {@link Context} this command operates on
 * @see Context
 */
@FunctionalInterface
public interface Command<T extends Context<T>> {
    /**
     * Executes the command using the provided context.
     * <p>
     * All implementations must not throw any exceptions apart from a {@link CommandExecutionException} to
     * ensure all errors encountered during execution are caught and handled.
     *
     * @param context the execution context
     * @throws CommandExecutionException if execution fails
     */
    void execute(T context) throws CommandExecutionException;
}
