package io.github.bagu.commander.api.argument;

import io.github.bagu.commander.api.context.Context;
import io.github.bagu.commander.api.exception.argument.ArgumentNotFoundException;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

import java.util.*;

/**
 * Contains the resolved, typed arguments for a command execution.
 *
 * <p>
 *     This class holds three types of command arguments after type conversion:<br>
 *     - Positional arguments: required positional arguments with specific types<br>
 *     - Options: optional key-value arguments that may have defaults<br>
 *     - Flags: boolean switches that are either present or absent
 * </p>
 *
 * <p>
 *     All collections are immutable after construction. This class is usually created by
 *     {@link ArgumentResolver} and passed to command handlers via a
 *     {@link Context}.
 * </p>
 *
 * <p>
 *     Example usage in a command handler:
 *     <pre>
 *         {@code CommandHandler<MyContext> handler = ctx -> {
 *     String name = ctx.args().arg("name", String.class);
 *     Integer port = ctx.args().option("port", Integer.class).orElse(8080);
 *     boolean verbose = ctx.args().flag("verbose");
 *     // other logic
 * };}
 *     </pre>
 * </p>
 *
 * @see Context
 * @see ArgumentResolver
 */
public final class Arguments {
    private final Map<String, Object> args;
    private final Map<String, Object> options;
    private final Set<String> flags;

    /**
     * Creates a new arguments instance with the specified values.
     *
     * <p>
     *     All collections are defensively copied to ensure immutability.
     * </p>
     *
     * @param args the map of argument names to their typed values
     * @param options the map of option keys to their typed values (may contain null)
     * @param flags the set of flag names that were specified
     */
    public Arguments(
            Map<String, Object> args,
            Map<String, Object> options,
            Set<String> flags
    ) {
        this.args = Map.copyOf(args);
        this.options = Map.copyOf(options);
        this.flags = Set.copyOf(flags);
    }

    /**
     * Retrieves a required positional argument by name.
     *
     * <p>
     *     This method should be used for arguments defined in the command definition.
     *     The value is cast to the specified type at runtime.
     * </p>
     *
     * @param name the name of the argument as defined in the command
     * @param type the class representing the expected type
     * @return the argument value cast to the specified type
     * @param <T> the expected type of the argument
     * @throws ArgumentNotFoundException if no argument exists with the given name
     * @throws ClassCastException if the argument cannot be cast to the specified type
     */
    public <T> T arg(String name, Class<T> type) throws ArgumentNotFoundException {
        Object object = args.get(name);
        if (object == null)
            throw new ArgumentNotFoundException(name);
        return type.cast(object);
    }

    /**
     * Retrieves an optional option by key.
     *
     * <p>
     *     Options may be absent if the user did not provide them, or may have been set to their default value.
     *     An empty optional in returned if the option was not provided and has no default value.
     * </p>
     *
     * @param key the key of the option defined in the command
     * @param type the class representing the expected type
     * @return an {@link Optional} containing the option value, or empty if not present
     * @param <T> the expected type of the option
     * @throws ClassCastException if the argument cannot be cast to the specified type
     */
    public <T> Optional<T> option(String key, Class<T> type) {
        Object option = options.get(key);
        return Optional.ofNullable(type.cast(option));
    }

    /**
     * Checks if a flag was specified by the user.
     *
     * <p>
     *     Flags are boolean switches that are either present of absent. This method returns:<br>
     *     - {@code true} if the flag was specified by the user
     *     - {@code false} if the flag was not specified
     * </p>
     *
     * @param flag the name of the flag defined in the command
     * @return {@code true} if the flag was specified, {@code false} otherwise
     */
    public boolean flag(String flag) {
        return flags.contains(flag);
    }
}
