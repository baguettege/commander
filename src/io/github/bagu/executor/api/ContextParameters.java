package io.github.bagu.executor.api;

import io.github.bagu.executor.internal.TypedArgs;

import java.util.Objects;

/**
 * Container holding the parameters required to construct a {@link Context}.
 * <p>
 * This class acts as a bridge between the internal mechanisms of the framework and the public API,
 * encapsulating fields required for context creation. It is passed to {@link ContextFactory} implementations
 * to construct custom context instances.
 *
 * @param <T> the type of context to be created
 * @see ContextFactory
 * @see Context
 */
public final class ContextParameters<T extends Context<T>> {
    private final TypedArgs typedArgs;
    private final CommandRegistry<T> registry;

    /**
     * Constructs a new instance with the given parameters.
     * <p>
     * This constructor is package-private and intended for framework use only.
     *
     * @param typedArgs the parsed and type-converted arguments and options
     * @param registry the command registry
     * @throws NullPointerException if any parameter is null
     */
    ContextParameters(
            TypedArgs typedArgs,
            CommandRegistry<T> registry
    ) {
        this.typedArgs = Objects.requireNonNull(typedArgs);
        this.registry = Objects.requireNonNull(registry);
    }

    /**
     * Returns the typed arguments and options.
     *
     * @return the typed arguments and options
     */
    public TypedArgs typedArgs() {
        return typedArgs;
    }

    /**
     * Returns the command registry.
     *
     * @return the command registry.
     */
    public CommandRegistry<T> registry() {
        return registry;
    }

    @Override
    public String toString() {
        return "ContextParameters{" +
                "typedArgs=" + typedArgs +
                ", registry=" + registry +
                '}';
    }
}
