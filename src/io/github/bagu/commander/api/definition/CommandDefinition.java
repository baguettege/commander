package io.github.bagu.commander.api.definition;

import io.github.bagu.commander.api.core.CommandHandler;
import io.github.bagu.commander.api.context.Context;
import io.github.bagu.commander.api.exception.argument.ArgumentAlreadyExistsException;
import io.github.bagu.commander.api.exception.argument.FlagAlreadyExistsException;
import io.github.bagu.commander.api.exception.argument.OptionAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines a complete command with its parameters and execution logic.
 *
 * <p>
 *     A command definition encapsulates everything needed to execute a command:<br>
 *     - A unique name for invocation<br>
 *     - A handler containing the execution logic<br>
 *     - Required positional arguments<br>
 *     - Optional key-value options<br>
 *     - Optional boolean flags
 * </p>
 *
 * <p>
 *     Command definitions are registered with a {@link io.github.bagu.commander.api.core.CommandGroup},
 *     which is used by a {@link io.github.bagu.commander.api.core.Environment} for command lookup and
 *     execution.
 * </p>
 *
 * <p>
 *     An example:
 *     <pre>
 *         {@code ArgumentDefinition<String> userArg = ArgumentDefinition.<String>builder()
 *         .name("username")
 *         .type(String.class)
 *         .description("The username to kick")
 *         .build();
 *
 * OptionDefinition<String> reasonOption = OptionDefinition.<String>builder()
 *         .key("reason")
 *         .type(String.class)
 *         .defaultValue("No reason provided")
 *         .description("The reason for kicking the user")
 *         .build();
 *
 * FlagDefinition forceFlag = FlagDefinition.builder()
 *         .name("force")
 *         .description("Skip confirmation prompt")
 *         .build();
 *
 * CommandDefinition<ServerContext> kickCommand = CommandDefinition.<ServerContext>builder()
 *         .name("kick")
 *         .handler(context -> {
 *             String username = context.args().arg("username", String.class);
 *             String reason = context.args().option("reason", String.class).orElse("No reason provided");;
 *             boolean force = context.args().flag("force");
 *
 *             if (force)
 *                 System.out.println("Forcing kick...");
 *
 *             context.server().kickUser(username, reason);
 *             System.out.println("Kicked user: " + username);
 *         })
 *         .arg(userArg)
 *         .option(reasonOption)
 *         .flag(forceFlag)
 *         .build();}
 *     </pre>
 *
 *     Command invocations:<br>
 *     - {@code server kick alice} - default reason, no force<br>
 *     - {@code server kick bob --reason=Spamming --force} - custom reason, force enabled<br>
 *     - {@code server kick --reason=Unknown --force john} - options and flags can be in any position after environment
 *     and command
 * </p>
 *
 * <p>
 *     This class is immutable after construction and is built using the builder pattern.
 * </p>
 *
 * @param <T> the type of context that this command's handler accepts
 *
 * @see CommandHandler
 * @see ArgumentDefinition
 * @see OptionDefinition
 * @see FlagDefinition
 * @see io.github.bagu.commander.api.core.CommandGroup
 */
public final class CommandDefinition<T extends Context<T>> {
    private final String name;
    private final CommandHandler<T> handler;
    private final List<ArgumentDefinition<?>> args;
    private final List<OptionDefinition<?>> options;
    private final List<FlagDefinition> flags;

    /**
     * Creates a new command definition with the specified properties.
     *
     * @param name the command name used in invocations
     * @param handler the handler to execute when this command is invoked
     * @param args the list of required positional arguments
     * @param options the list of optional key-value parameters
     * @param flags the list of optional boolean switches
     */
    private CommandDefinition(
            String name,
            CommandHandler<T> handler,
            List<ArgumentDefinition<?>> args,
            List<OptionDefinition<?>> options,
            List<FlagDefinition> flags
    ) {
        this.name = name;
        this.handler = handler;
        this.args = List.copyOf(args);
        this.options = List.copyOf(options);
        this.flags = List.copyOf(flags);
    }

    /**
     * Returns the command name.
     *
     * <p>
     *     This name is used in command invocations to identify which command to execute. It must be unique
     *     within a {@link io.github.bagu.commander.api.core.CommandGroup}.
     * </p>
     *
     * @return the command name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the command handler.
     *
     * <p>
     *     The handler contains the executable logic for this command and is invoked by the
     *     {@link io.github.bagu.commander.api.core.Environment} after argument resolution.
     * </p>
     *
     * @return the command handler
     */
    public CommandHandler<T> handler() {
        return handler;
    }

    /**
     * Returns an immutable list of argument definitions.
     *
     * <p>
     *     These definitions specify the required positional arguments that must be provided when
     *     invoking this command. The order of arguments in this list determines their positional order.
     * </p>
     *
     * @return the list of argument definitions
     */
    public List<ArgumentDefinition<?>> args() {
        return args;
    }

    /**
     * Returns an immutable list of option definitions.
     *
     * <p>
     *     These definitions specify the optional key-value parameters that can be provided when
     *     invoking this command.
     * </p>
     *
     * @return the list of option definitions
     */
    public List<OptionDefinition<?>> options() {
        return options;
    }

    /**
     * Returns an immutable list of flag definitions.
     *
     * <p>
     *     These definitions specify the optional boolean switches that can be included when
     *     invoking this command.
     * </p>
     *
     * @return the list of flag definitions
     */
    public List<FlagDefinition> flags() {
        return flags;
    }

    /**
     * Creates a new builder for constructing a command definition
     *
     * @return a new builder instance
     * @param <T> the type of context for the command being defined
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for constructing {@link CommandDefinition} instances.
     *
     * <p>
     *     The name and handler fields are required and must be set before building. Arguments, options, and
     *     flags are optional but can be added via their respective methods.
     * </p>
     *
     * <p>
     *     Duplicate names/keys are not allowed - attempting to add a duplicate will throw an exception.
     * </p>
     *
     * @param <T> the type of context for the command being built
     */
    public static final class Builder<T extends Context<T>> {
        private String name;
        private CommandHandler<T> handler;
        private final List<ArgumentDefinition<?>> args = new ArrayList<>();
        private final List<OptionDefinition<?>> options = new ArrayList<>();
        private final List<FlagDefinition> flags = new ArrayList<>();

        private Builder() {}

        /**
         * Builds the command definition.
         *
         * @return a new immutable command definition instance
         * @throws NullPointerException if name or handler is not set
         */
        public CommandDefinition<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(handler);

            return new CommandDefinition<>(
                    name,
                    handler,
                    args,
                    options,
                    flags
            );
        }

        /**
         * Sets the command name.
         *
         * @param name the command name used in invocations
         * @return this builder
         */
        public Builder<T> name(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        /**
         * Sets the command handler.
         *
         * @param handler the handler to execute when this command is invoked
         * @return this builder
         */
        public Builder<T> handler(CommandHandler<T> handler) {
            Objects.requireNonNull(handler);
            this.handler = handler;
            return this;
        }

        /**
         * Adds an argument definition to this command.
         *
         * <p>
         *     Arguments are added in the order they are called, which determines their
         *     positional order during command invocation.
         * </p>
         *
         * @param arg the argument definition to add
         * @return this builder
         * @throws ArgumentAlreadyExistsException if an argument with the same name already exists
         */
        public Builder<T> arg(ArgumentDefinition<?> arg) throws ArgumentAlreadyExistsException {
            String name = arg.name();
            for (ArgumentDefinition<?> argDef : args)
                if (name.equals(argDef.name()))
                    throw new ArgumentAlreadyExistsException(name);

            this.args.add(arg);
            return this;
        }

        /**
         * Adds an option definition to this command.
         *
         * @param option the option definition to add
         * @return this builder
         * @throws OptionAlreadyExistsException if an option with the same key already exists
         */
        public Builder<T> option(OptionDefinition<?> option) throws OptionAlreadyExistsException {
            String key = option.key();
            for (OptionDefinition<?> optDef : options)
                if (key.equals(optDef.key()))
                    throw new OptionAlreadyExistsException(key);

            this.options.add(option);
            return this;
        }

        /**
         * Adds a flag definition to this command.
         *
         * @param flag the flag definition to add
         * @return this builder
         * @throws FlagAlreadyExistsException if a flag with the same name already exists
         */
        public Builder<T> flag(FlagDefinition flag) throws FlagAlreadyExistsException {
            String name = flag.name();
            for (FlagDefinition flagDef : flags)
                if (name.equals(flagDef.name()))
                    throw new FlagAlreadyExistsException(name);

            this.flags.add(flag);
            return this;
        }
    }
}
