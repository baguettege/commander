package io.github.bagu.executor.api;

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
     * @param params the parameters for the context
     * @return a new context instance
     */
    T create(ContextParameters<T> params);
}
