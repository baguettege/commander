package io.github.bagu.executor.api;

import io.github.bagu.executor.api.internal.TypedArgs;

/**
 * Factory interface for creating {@link Context} instances for commands.
 *
 * @param <T> the type of {@link Context}
 * @see Context
 */
@FunctionalInterface
public interface ContextFactory<T extends Context<T>> {
    /**
     * Creates a new context instance.
     *
     * @param typedArgs the typed arguments and options
     * @param registry the command registry
     * @return a new context instance
     * @throws NullPointerException if any parameter is null
     */
    T create(
            TypedArgs typedArgs,
            CommandRegistry<T> registry
    );
}
