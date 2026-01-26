package io.github.bagu.executor.internal;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the result of parsing command arguments.
 * <p>
 * Contains separated positional arguments and named options (key-value pairs). This is an
 * intermediate representation before type conversion and validation.
 * <p>
 * Positional arguments are stored as strings in order, while options are stored as a map of
 * option names to their string values.
 *
 * @see ArgParser
 * @see TypedArgs
 */
public final class ParsedArgs {
    private final List<String> args;
    private final Map<String, String> options;

    /**
     * Constructs a new {@link ParsedArgs} instance.
     *
     * @param args the list of positional arguments
     * @param options the map of option names to values
     * @throws NullPointerException if any parameter is null
     */
    ParsedArgs(
            List<String> args,
            Map<String, String> options
    ) {
        Objects.requireNonNull(args);
        Objects.requireNonNull(options);

        this.args = List.copyOf(args);
        this.options = Map.copyOf(options);
    }

    /**
     * Returns the list of positional arguments.
     *
     * @return an immutable list of argument strings
     */
    public List<String> args() {
        return args;
    }

    /**
     * Returns the map of options.
     *
     * @return an immutable map of option names to values
     */
    public Map<String, String> options() {
        return options;
    }

    @Override
    public String toString() {
        return "ParsedArgs{" +
                "args=" + args +
                ", options=" + options +
                "}";
    }
}
