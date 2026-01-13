package io.github.bagu.commander.internal.invocation;

import io.github.bagu.commander.internal.parser.InvocationParser;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a parsed command invocation with unresolved ({@link String}) arguments.
 *
 * <p>
 *     This is an intermediate representation created by {@link InvocationParser}
 *     after tokenization. It contains the environment name, command name, and all passed arguments as strings, before
 *     type conversion.
 * </p>
 *
 * <p>
 *     All collections are immutable after construction.
 * </p>
 *
 * @see InvocationParser
 * @see ArgumentResolver
 */
public final class Invocation {
    private final String environment;
    private final String command;
    private final List<String> args;
    private final Map<String, String> options;
    private final Set<String> flags;

    /**
     * Creates a new invocation with the specified components.
     *
     * <p>
     *     All collections are defensively copied to ensure immutability.
     * </p>
     *
     * @param environment the name of the environment to execute in
     * @param command the name of the command to execute
     * @param args the positional arguments as strings
     * @param options the key-value options as strings
     * @param flags the specified flag names
     */
    public Invocation(
            String environment,
            String command,
            List<String> args,
            Map<String, String> options,
            Set<String> flags
    ) {
        this.environment = Objects.requireNonNull(environment);
        this.command = Objects.requireNonNull(command);
        this.args = List.copyOf(args);
        this.options = Map.copyOf(options);
        this.flags = Set.copyOf(flags);
    }

    /**
     * Returns the environment name.
     *
     * @return the environment name
     */
    public String environment() {
        return environment;
    }

    /**
     * Returns the command name.
     *
     * @return the command name
     */
    public String command() {
        return command;
    }

    /**
     * Returns an immutable list of positional argument strings.
     *
     * @return the positional arguments
     */
    public List<String> args() {
        return args;
    }

    /**
     * Returns an immutable map of option keys to their string values.
     *
     * <p>
     *     Keys do not include the {@code --} prefix used when passing options into the command.
     * </p>
     *
     * @return the options map
     */
    public Map<String, String> options() {
        return options;
    }

    /**
     * Returns an immutable set of flag names.
     *
     * <p>
     *     Keys do not include the {@code --} prefix used when passing flags into the command.
     * </p>
     *
     * @return the flags set
     */
    public Set<String> flags() {
        return flags;
    }
}
