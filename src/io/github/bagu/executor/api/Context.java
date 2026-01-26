package io.github.bagu.executor.api;

import io.github.bagu.executor.api.exception.arg.ArgNotFoundException;
import io.github.bagu.executor.api.spec.ArgSpec;
import io.github.bagu.executor.internal.TypedArgs;

import java.util.Map;
import java.util.Objects;

/**
 * Base class for command execution context. Holds typed arguments, options, and a reference
 * to the command registry.
 *
 * @param <T> the type of {@link Context}
 * @see ContextFactory
 */
public abstract class Context<T extends Context<T>> {
    private final Map<String, Object> args;
    private final Map<String, Object> options;
    private final CommandRegistry<T> registry;

    /**
     * Constructs a new {@link Context} instance with the given properties.
     * <p>
     * Concrete implementations should not define the parameters themselves, and instead
     * allow the framework to do so.
     *
     * @param typedArgs the typed arguments
     * @param registry the command registry
     * @throws NullPointerException if any parameter is null
     */
    protected Context(
            TypedArgs typedArgs,
            CommandRegistry<T> registry
    ) {
        Objects.requireNonNull(typedArgs);
        Objects.requireNonNull(registry);

        this.args = typedArgs.args();
        this.options = typedArgs.options();
        this.registry = registry;
    }

    /**
     * Returns the value of a named argument.
     * <p>
     * The name of the argument refers to the name defined in {@link ArgSpec.Builder#name(String)}
     *
     * @param name the argument name
     * @param type the expected type
     * @return the argument value
     * @param <U> the type of the argument value
     * @throws ArgNotFoundException if the argument with the given name does not exist
     * @throws NullPointerException if any parameter is null
     */
    public <U> U getArg(String name, Class<U> type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        Object obj = args.get(name);
        if (obj == null)
            throw new ArgNotFoundException(name);
        return type.cast(obj);
    }

    /**
     * Returns the value of a named option, or null if not present.
     *
     * @param name the option name
     * @param type the expected type
     * @return the option value or null
     * @param <U> the type of the option value
     * @throws NullPointerException if any parameter is null
     */
    public <U> U getOption(String name, Class<U> type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        Object obj = options.get(name);
        if (obj == null) return null;
        return type.cast(obj);
    }

    /**
     * Returns the command registry associated with this context.
     *
     * @return the command registry
     */
    public CommandRegistry<T> registry() {
        return registry;
    }

    @Override
    public String toString() {
        return "Context{" +
                "args=" + args +
                ", options=" + options +
                ", registry=" + registry +
                "}";
    }
}
