package io.github.bagu.executor.api;

import io.github.bagu.executor.api.exception.arg.ArgCountException;
import io.github.bagu.executor.api.exception.command.CommandNotFoundException;
import io.github.bagu.executor.internal.ArgBinder;
import io.github.bagu.executor.internal.ArgParser;
import io.github.bagu.executor.internal.ParsedArgs;
import io.github.bagu.executor.internal.TypedArgs;
import io.github.bagu.executor.api.spec.CommandSpec;

import java.util.*;

/**
 * Represents an execution environment for commands.
 * <p>
 * An environment contains a registry of commands, converters for type conversion, and
 * a factory for creating execution contexts. It handles parsing, binding, and executing
 * commands including support for nested subcommands.
 *
 * @param <T> the type of {@link Context} used by commands in this environment
 * @see Context
 * @see CommandRegistry
 * @see ConverterRegistry
 */
public final class Environment<T extends Context<T>> {
    private final String name;
    private final CommandRegistry<T> commandRegistry;
    private final ConverterRegistry converterRegistry;
    private final ContextFactory<T> contextFactory;

    private Environment(
            String name,
            CommandRegistry<T> commandRegistry,
            ConverterRegistry converterRegistry,
            ContextFactory<T> contextFactory
    ) {
        this.name = name;
        this.commandRegistry = commandRegistry;
        this.converterRegistry = converterRegistry;
        this.contextFactory = contextFactory;
    }

    /**
     * Returns the name of this environment.
     *
     * @return the environment name
     */
    public String name() {
        return name;
    }

    /**
     * Executes a command given a list of tokens.
     * <p>
     * This method resolves the command hierarchy, parses arguments and options, binds
     * them to typed values, creates a context and executes the command.
     *
     * @param tokens the tokenized command input
     * @throws ArgCountException if no command is provided
     * @throws CommandNotFoundException if a command is not found
     */
    public void execute(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new ArgCountException("No command provided");
        }

        Iterator<String> iterator = tokens.iterator();
        CommandSpec<T> spec = null;
        CommandRegistry<T> registry = this.commandRegistry;
        List<String> args = new ArrayList<>();

        while (iterator.hasNext()) {
            String token = iterator.next();
            CommandSpec<T> tempSpec = registry.get(token);

            if (tempSpec == null) {
                if (spec == null) {
                    throw new CommandNotFoundException(token);
                }

                args.add(token);
                iterator.forEachRemaining(args::add);
                break;
            } else {
                spec = tempSpec;
                registry = tempSpec.subRegistry();
            }
        }

        ParsedArgs parsedArgs = ArgParser.parse(args);
        TypedArgs typedArgs = ArgBinder.bind(
                parsedArgs,
                converterRegistry,
                Objects.requireNonNull(spec) // will never be null
        );

        ContextParameters<T> params = new ContextParameters<>(typedArgs, commandRegistry);
        T context = contextFactory.create(params);
        spec.action().execute(context);
    }

    @Override
    public String toString() {
        return "Environment{" +
                "name='" + name + '\'' +
                ", commandRegistry=" + commandRegistry +
                ", converterRegistry=" + converterRegistry +
                ", contextFactory=" + contextFactory +
                '}';
    }

    /**
     * Returns a new builder for creating {@link Environment} instances.
     *
     * @return new builder
     * @param <T> the type of {@link Context} for the environment
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for creating {@link Environment} instances.
     *
     * @param <T> the type of {@link Context} for the environment
     */
    public static final class Builder<T extends Context<T>> {
        private Builder() {}

        private String name;
        private CommandRegistry<T> commandRegistry;
        private ConverterRegistry converterRegistry;
        private ContextFactory<T> contextFactory;

        /**
         * Builds the {@link Environment} instance.
         *
         * @return a new {@link Environment}
         * @throws NullPointerException if any required field is null
         */
        public Environment<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(commandRegistry);
            Objects.requireNonNull(converterRegistry);
            Objects.requireNonNull(contextFactory);

            return new Environment<>(
                    name,
                    commandRegistry,
                    converterRegistry,
                    contextFactory
            );
        }

        /**
         * Sets the name of the environment.
         *
         * @param name the environment name
         * @return this builder
         * @throws NullPointerException if the name is null
         */
        public Builder<T> name(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Sets the command registry for this environment.
         *
         * @param commandRegistry the command registry
         * @return this builder
         * @throws NullPointerException if the registry is null
         */
        public Builder<T> commands(CommandRegistry<T> commandRegistry) {
            this.commandRegistry = Objects.requireNonNull(commandRegistry);
            return this;
        }

        /**
         * Sets the converter registry for type conversions in this environment.
         *
         * @param converterRegistry the converter registry
         * @return this builder
         * @throws NullPointerException if the registry is null
         */
        public Builder<T> converters(ConverterRegistry converterRegistry) {
            this.converterRegistry = Objects.requireNonNull(converterRegistry);
            return this;
        }

        /**
         * Sets the context factory for creating execution contexts.
         *
         * @param contextFactory the context factory
         * @return this builder
         * @throws NullPointerException if the factory is null
         */
        public Builder<T> contextFactory(ContextFactory<T> contextFactory) {
            this.contextFactory = Objects.requireNonNull(contextFactory);
            return this;
        }
    }
}
