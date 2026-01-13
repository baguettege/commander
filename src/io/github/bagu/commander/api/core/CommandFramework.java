package io.github.bagu.commander.api.core;

import io.github.bagu.commander.api.exception.CommanderException;
import io.github.bagu.commander.api.exception.environment.EnvironmentAlreadyExistsException;
import io.github.bagu.commander.api.exception.environment.EnvironmentNotFoundException;
import io.github.bagu.commander.internal.invocation.Invocation;
import io.github.bagu.commander.internal.parser.InvocationParser;
import io.github.bagu.commander.internal.parser.Tokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main entry point for executing commands in the {@code Commander} framework.
 *
 * <p>
 *     A command framework manages multiple {@link Environment}s, each containing their own commands,
 *     converters, and context types. The framework orchestrates the complete command execution pipeline:<br>
 *     - Tokenization: splits input text into tokens<br>
 *     - Parsing: extracts environment, command, arguments, options, and flags<br>
 *     - Routing: routes command invocations to the appropriate environment for execution
 * </p>
 *
 * <p>
 *     Command invocations follow the format:<br>
 *     {@code <environment> <command> [args] [options] [flags]}<br>
 *     - Options are optionally given as key-value pairs, in the format {@code --key=value}<br>
 *     - Flags are optionally given as a boolean switch, in the format {@code --flag}<br>
 *     - Options and flags can be placed anywhere within the command input<br>
 *     - Environment and command are required per input
 * </p>
 *
 * <p>
 *     An example:
 *     <pre>
 *         {@code CommandFramework framework = CommandFramework.builder()
 *         .environment(serverEnv)
 *         .environment(fileEnv)
 *         .environments(dbEnv)
 *         .build();
 *
 * framework.execute("server start 443 --max_clients=100");
 * framework.execute("server kick-all --force");
 *         }
 *     </pre>
 * </p>
 *
 * <p>
 *     This framework is immutable after construction using a builder and thread-safe for command execution. It is
 *     recommended to pair this with a {@link java.util.Scanner} in order to execute commands from the CLI.
 * </p>
 *
 * @see Environment
 * @see Tokenizer
 * @see InvocationParser
 */
public final class CommandFramework {
    private final Map<String, Environment<?>> environments;

    /**
     * Creates a new framework with specified environments.
     *
     * @param environments the environments for the framework to contain
     */
    private CommandFramework(Map<String, Environment<?>> environments) {
        this.environments = environments;
    }

    /**
     * Executes a command from raw text input.
     *
     * <p>
     *     The text is tokenized, parsed into an invocation, and routed to the appropriate environment
     *     for execution.
     * </p>
     *
     * @param text the command text to execute
     * @throws EnvironmentNotFoundException if the specified environment does not exist
     * @throws CommanderException if an error occurs parsing, execution or any other stage of the pipeline
     */
    public void execute(String text) throws CommanderException {
        List<String> tokens = Tokenizer.tokenize(text);
        Invocation invocation = InvocationParser.parse(tokens);

        Environment<?> environment = environments.get(invocation.environment());
        if (environment == null)
            throw new EnvironmentNotFoundException(invocation.environment());

        environment.execute(invocation);
    }

    /**
     * Creates a new builder for constructing a command framework.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for constructing {@link CommandFramework} instance.
     *
     * <p>
     *     This builder allows registration of multiple environments. A framework can be built with zero
     *     environments, though it won't be able to execute any commands until an environment is added.
     * </p>
     */
    public static final class Builder {
        private final Map<String, Environment<?>> environments = new HashMap<>();

        private Builder() {}

        /**
         * Builds the framework.
         *
         * @return a new immutable framework instance
         */
        public CommandFramework build() {
            return new CommandFramework(environments);
        }

        /**
         * Registers an environment with this framework.
         *
         * <p>
         *     Each environment must have a unique name within the framework.
         * </p>
         *
         * @param environment the environment to register
         * @return this builder
         * @throws EnvironmentAlreadyExistsException if an environment with this name is already registered
         */
        public Builder environment(Environment<?> environment) throws EnvironmentAlreadyExistsException {
            String name = environment.name();
            if (environments.containsKey(name))
                throw new EnvironmentAlreadyExistsException(name);
            environments.put(name, environment);
            return this;
        }
    }
}
