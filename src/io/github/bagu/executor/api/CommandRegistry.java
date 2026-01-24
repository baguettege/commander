package io.github.bagu.executor.api;

import io.github.bagu.executor.api.spec.CommandSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A registry for {@link CommandSpec} instances indexed by their name.
 * <p>
 * Each command name can have at most one specification registered.
 *
 * @param <T> the type of {@link Context} used by commands in this registry
 * @see CommandSpec
 * @see Context
 */
public final class CommandRegistry<T extends Context<T>> {
    private final Map<String, CommandSpec<T>> commandSpecs;

    private CommandRegistry(
            Map<String, CommandSpec<T>> commandSpecs
    ) {
        this.commandSpecs = commandSpecs;
    }

    /**
     * Retrieves the command specification for the given name.
     *
     * @param name the command name
     * @return the command specification, or null if not registered
     * @throws NullPointerException if the name is null
     */
    public CommandSpec<T> get(String name) {
        Objects.requireNonNull(name);
        return commandSpecs.get(name);
    }

    /**
     * Returns all registered command specifications.
     *
     * @return an immutable list of all command specs.
     */
    public List<CommandSpec<T>> getAll() {
        return List.copyOf(commandSpecs.values());
    }

    @Override
    public String toString() {
        return "CommandRegistry{" +
                "commandSpecs=" + commandSpecs +
                "}";
    }

    /**
     * Returns a new builder for creating {@link CommandRegistry} instances.
     *
     * @return new builder
     * @param <T> the type of {@link Context} for this registry
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for creating {@link CommandRegistry} instances.
     *
     * @param <T> the type of {@link Context} for the registry
     */
    public static final class Builder<T extends Context<T>> {
        private Builder() {}

        private final Map<String, CommandSpec<T>> commandSpecs = new HashMap<>();

        /**
         * Builds the {@link CommandRegistry} instance.
         *
         * @return a new {@link CommandRegistry}
         */
        public CommandRegistry<T> build() {
            return new CommandRegistry<>(
                    Map.copyOf(commandSpecs)
            );
        }

        /**
         * Registers a command specification.
         *
         * @param commandSpec the command specification
         * @return this builder
         * @throws IllegalArgumentException if a command with the same name already exists
         * @throws NullPointerException if the command spec is null
         */
        public Builder<T> register(CommandSpec<T> commandSpec) {
            Objects.requireNonNull(commandSpec);

            String name = commandSpec.name();
            if (commandSpecs.containsKey(name)) {
                throw new IllegalArgumentException("Command spec \"" + name + "\" already exists");
            }

            commandSpecs.put(name, commandSpec);
            return this;
        }
    }
}
