package io.github.bagu.executor.api.spec;

import io.github.bagu.executor.api.Command;
import io.github.bagu.executor.api.CommandRegistry;
import io.github.bagu.executor.api.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Specification for a command including its action, arguments, options, and subcommands.
 * <p>
 * A command spec defines:
 * <ol>
 * <li>The command's name and description</li>
 * <li>The action to execute when the command is invoked</li>
 * <li>Required positional arguments</li>
 * <li>Optional named options</li>
 * <li>A registry of subcommands (for hierarchical commands)</li>
 * </ol>
 * <p>
 * Commands can be nested to create command hierarchies (e.g. "git commit").
 *
 * @param <T> the type of {@link Context} used by this command
 * @see Command
 * @see ArgSpec
 * @see OptionSpec
 * @see CommandRegistry
 */
public final class CommandSpec<T extends Context<T>> extends AbstractSpec {
    private final Command<T> action;
    private final List<ArgSpec<?>> argSpecs;
    private final List<OptionSpec<?>> optionSpecs;
    private final CommandRegistry<T> subRegistry;

    private CommandSpec(
            String name,
            String description,
            Command<T> action,
            List<ArgSpec<?>> argSpecs,
            List<OptionSpec<?>> optionSpecs,
            CommandRegistry<T> subRegistry
    ) {
        super(name, description);
        this.action = action;
        this.argSpecs = argSpecs;
        this.optionSpecs = optionSpecs;
        this.subRegistry = subRegistry;
    }

    /**
     * Returns the action to execute for this command.
     *
     * @return the command action
     */
    public Command<T> action() {
        return action;
    }

    /**
     * Returns the list of argument specifications for this command.
     *
     * @return an immutable list of argument specs
     */
    public List<ArgSpec<?>> argSpecs() {
        return argSpecs;
    }

    /**
     * Returns the list of option specifications for this command.
     *
     * @return an immutable list of option specs
     */
    public List<OptionSpec<?>> optionSpecs() {
        return optionSpecs;
    }

    /**
     * Returns the registry of subcommands for this command.
     *
     * @return the subcommand registry
     */
    public CommandRegistry<T> subRegistry() {
        return subRegistry;
    }

    @Override
    public String toString() {
        return "CommandSpec{" +
                "name=" + super.name() +
                ", description=" + super.description() +
                ", action=" + action +
                ", argSpecs=" + argSpecs +
                ", optionSpecs=" + optionSpecs +
                ", subRegistry=" + subRegistry +
                "}";
    }

    /**
     * Returns a new builder for creating {@link CommandSpec} instances.
     *
     * @param <T> the type of context
     * @return new builder
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for creating {@link CommandSpec} instances.
     *
     * @param <T> the type of context
     */
    public static final class Builder<T extends Context<T>> {
        private Builder() {}

        private String name;
        private String description;
        private Command<T> action;
        private final List<ArgSpec<?>> argSpecs = new ArrayList<>();
        private final List<OptionSpec<?>> optionSpecs = new ArrayList<>();
        private CommandRegistry<T> subRegistry;

        /**
         * Builds the {@link CommandSpec} instance.
         * <p>
         * If no action is provided, a no-operation action is used. If no subcommand registry
         * is provided, an empty registry is used.
         *
         * @return a new {@link CommandSpec}
         * @throws NullPointerException if name or description is null
         */
        public CommandSpec<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(description);

            if (action == null)
                action = ignored -> {};

            if (subRegistry == null)
                subRegistry = CommandRegistry.<T>builder().build();

            return new CommandSpec<>(
                    name,
                    description,
                    action,
                    List.copyOf(argSpecs),
                    List.copyOf(optionSpecs),
                    subRegistry
            );
        }

        /**
         * Sets the name of the command.
         * <p>
         * This name is used to invoke the command from the input string.
         *
         * @param name the command name
         * @return this builder
         * @throws NullPointerException if the name is null
         */
        public Builder<T> name(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Sets the description of the command.
         *
         * @param description the command description
         * @return this builder
         * @throws NullPointerException if the description is null
         */
        public Builder<T> description(String description) {
            this.description = Objects.requireNonNull(description);
            return this;
        }

        /**
         * Sets the action to execute when this command is invoked.
         *
         * @param action the command action
         * @return this builder
         */
        public Builder<T> action(Command<T> action) {
            this.action = action;
            return this;
        }

        /**
         * Adds an argument specification to this command.
         * <p>
         * Arguments are processed in the order they are added.
         *
         * @param argSpec the argument specification
         * @return this builder
         * @throws IllegalArgumentException if an argument with the same name already exists
         * @throws NullPointerException if the argument spec is null
         */
        public Builder<T> arg(ArgSpec<?> argSpec) {
            Objects.requireNonNull(argSpec);

            String name = argSpec.name();
            for (var spec : argSpecs) {
                if (name.equals(spec.name())) {
                    throw new IllegalArgumentException("Arg spec \"" + name + "\" already exists");
                }
            }

            argSpecs.add(argSpec);
            return this;
        }

        /**
         * Adds an option specification to this command.
         *
         * @param optionSpec the option specification
         * @return this builder
         * @throws IllegalArgumentException if an option with the same name already exists
         * @throws NullPointerException if the option spec is null
         */
        public Builder<T> option(OptionSpec<?> optionSpec) {
            Objects.requireNonNull(optionSpec);

            String name = optionSpec.name();
            for (var spec : optionSpecs) {
                if (name.equals(spec.name())) {
                    throw new IllegalArgumentException("Option spec \"" + name + "\" already exists");
                }
            }

            optionSpecs.add(optionSpec);
            return this;
        }

        /**
         * Sets the registry of subcommands for this command.
         * <p>
         * Subcommands allow creating hierarchical command structures.
         *
         * @param subRegistry the subcommand registry
         * @return this builder
         */
        public Builder<T> subRegistry(CommandRegistry<T> subRegistry) {
            this.subRegistry = subRegistry;
            return this;
        }
    }
}
