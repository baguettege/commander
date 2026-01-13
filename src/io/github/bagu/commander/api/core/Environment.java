package io.github.bagu.commander.api.core;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.context.Context;
import io.github.bagu.commander.api.context.ContextFactory;
import io.github.bagu.commander.api.definition.CommandDefinition;
import io.github.bagu.commander.api.exception.CommanderException;
import io.github.bagu.commander.api.exception.command.CommandNotFoundException;
import io.github.bagu.commander.api.exception.converter.ConverterAlreadyExistsException;
import io.github.bagu.commander.internal.invocation.Invocation;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * An isolated namespace for commands with shared converters and a common context type.
 *
 * <p>
 *     An environment represents a grouping of related commands that share:<br>
 *     - A common {@link Context} type with shared dependencies/states
 *     - A set of type {@link Converter}s for argument resolution
 *     - A registry of {@link CommandDefinition}s that can be executed
 * </p>
 *
 * <p>
 *     Each environment is identified by a unique name used in command invocations. The environment
 *     name is the first token in a command string:<br>
 *     {@code <environment> <command> [args] [options] [flags]}
 * </p>
 *
 * <p>
 *     Environments are immutable after construction and must be built using the builder pattern. All commands in
 *     an environment must accept the same context type to ensure type safety.
 * </p>
 *
 * <p>
 *     An example:
 *     <pre>
 *         {@code Environment<ServerContext> environment = Environment.<ServerContext>builder()
 *         .name("server")
 *         .contextFactory(ServerContext.factory())
 *         .converter(new IntegerConverter())
 *         .converter(new StringConverter())
 *         .command(kickUserCommand)
 *         .command(inviteUserCommand)
 *         .build();}
 *     </pre>
 * </p>
 *
 * @param <T> the context type for all commands within this environment
 *
 * @see Context
 * @see CommandDefinition
 * @see Converter
 * @see CommandFramework
 */
public final class Environment<T extends Context<T>> {
    private final String name;
    private final ContextFactory<T> contextFactory;
    private final Map<Class<?>, Converter<?>> converters;
    private final CommandGroup<T> group;

    /**
     * Creates a new environment with a specified properties.
     *
     * @param name unique name of the environment
     * @param contextFactory factory to create contexts for command execution
     * @param converters map of converters to use for argument resolution
     * @param group command group used for command lookup
     */
    private Environment(
            String name,
            ContextFactory<T> contextFactory,
            Map<Class<?>, Converter<?>> converters,
            CommandGroup<T> group
    ) {
        this.name = name;
        this.contextFactory = contextFactory;
        this.converters = Map.copyOf(converters);
        this.group = group;
    }

    /**
     * Returns the environment name.
     *
     * <p>
     *     This name is used as the first token in command invocations to identify which
     *     environment should handle the command.
     * </p>
     *
     * @return the environment name
     */
    public String name() {
        return name;
    }

    /**
     * Executes a command invocation within this environment.
     *
     * <p>
     *     Execution pipeline:<br>
     *     - Looks up the command by name<br>
     *     - Resolves string arguments to typed values using registered converters<br>
     *     - Creates a context instance from the context factory<br>
     *     - Executes the command handler with the created context
     * </p>
     *
     * @param invocation the parsed command invocation
     * @throws CommandNotFoundException if no command exists with the specified name
     * @throws CommanderException if an error occurs during argument resolution or execution
     */
    public void execute(Invocation invocation) throws CommanderException {
        Optional<CommandDefinition<T>> optionalCommand = group.get(invocation.command());
        if (optionalCommand.isEmpty())
            throw new CommandNotFoundException(invocation.command());
        CommandDefinition<T> command = optionalCommand.get();

        Arguments args = ArgumentResolver.resolve(
                        invocation,
                        command,
                        converters
        );

        T context = contextFactory.create(args, group);
        command.handler().execute(context);
    }

    /**
     * Creates a new builder for constructing an environment.
     *
     * @return a new builder instance
     * @param <T> the context type for the environment
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for constructing {@link Environment} instances.
     *
     * <p>
     *     The builder ensures that required fields (name and context factory) are set before building.
     *     Converters and commands are optional, but at least one converter and command should be registered for the
     *     environment to be useful.
     * </p>
     *
     * @param <T> type of context for the environment being built
     */
    public static final class Builder<T extends Context<T>> {
        private String name;
        private ContextFactory<T> contextFactory;
        private final Map<Class<?>, Converter<?>> converters = new HashMap<>();
        private CommandGroup<T> group;

        private Builder() {}

        /**
         * Builds the environment.
         *
         * @return a new immutable environment instance
         * @throws NullPointerException if name or context factory is not set
         */
        public Environment<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(contextFactory);

            return new Environment<>(
                    name,
                    contextFactory,
                    converters,
                    group
            );
        }

        /**
         * Sets the environment name.
         *
         * @param name the environment name used in command invocations
         * @return this builder
         */
        public Builder<T> name(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        /**
         * Sets the context factory used for the creation of context instances.
         *
         * @param contextFactory the factory for creating contexts
         * @return this builder
         */
        public Builder<T> contextFactory(ContextFactory<T> contextFactory) {
            Objects.requireNonNull(contextFactory);
            this.contextFactory = contextFactory;
            return this;
        }

        /**
         * Registers a type converter with this environment.
         *
         * <p>
         *     Converters are used to resolve string arguments to their typed objects during command
         *     execution. Each type can only have one converter.
         * </p>
         *
         * @param converter the converter instance
         * @param type the class representing the type
         * @return this builder
         * @param <C> the type that the converter produces
         * @throws ConverterAlreadyExistsException if a converter for this type is already registered
         */
        public <C> Builder<T> converter(Converter<C> converter, Class<C> type) throws ConverterAlreadyExistsException {
            Objects.requireNonNull(converter);
            if (converters.containsKey(type))
                throw new ConverterAlreadyExistsException(type);
            converters.put(type, converter);
            return this;
        }

        /**
         * Sets the command group used by this environment for command lookup.
         *
         * @param group command group
         * @return this builder
         */
        public Builder<T> group(CommandGroup<T> group) {
            Objects.requireNonNull(group);
            this.group = group;
            return this;
        }
    }
}
