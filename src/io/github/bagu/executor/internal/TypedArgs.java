package io.github.bagu.executor.internal;

import java.util.Map;
import java.util.Objects;

/**
 * Represents typed arguments and options after conversion and validation.
 * <p>
 * Contains the final typed values that will be passed to the command context.
 * This is the result of the binding process performed by {@link ArgBinder}.
 * <p>
 * Arguments and options are stored as maps keyed to their specification names, with
 * values as objects of their specified types.
 *
 * @see ArgBinder
 * @see ParsedArgs
 */
public final class TypedArgs {
    private final Map<String, Object> args;
    private final Map<String, Object> options;

    /**
     * Constructs a new {@link TypedArgs} instance.
     *
     * @param args the map of argument names to typed values
     * @param options the map of option names to typed values (or defaults)
     * @throws NullPointerException if any parameter is null
     */
    TypedArgs(
            Map<String, Object> args,
            Map<String, Object> options
    ) {
        Objects.requireNonNull(args);
        Objects.requireNonNull(options);

        this.args = Map.copyOf(args);
        this.options = Map.copyOf(options);
    }

    /**
     * Returns the map of typed arguments.
     *
     * @return an immutable map of argument names to their typed values
     */
    public Map<String, Object> args() {
        return args;
    }

    /**
     * Returns the map of typed options.
     *
     * @return an immutable map of option names to their typed values or defaults
     */
    public Map<String, Object> options() {
        return options;
    }

    @Override
    public String toString() {
        return "TypedArgs{" +
                "args=" + args +
                ", options=" + options +
                "}";
    }
}
