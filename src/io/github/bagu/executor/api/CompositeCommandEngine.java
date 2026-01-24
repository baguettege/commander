package io.github.bagu.executor.api;

import io.github.bagu.executor.api.exception.arg.ArgCountException;
import io.github.bagu.executor.internal.Tokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A command executor that dispatches commands to multiple named {@link Environment environments}.
 * <p>
 * Each environment is responsible for executing its own commands. The first token of the input string is
 * interpreted as the environment name, and the remaining tokens are passed to that environment.
 *
 * @see CommandExecutor
 * @see Environment
 */
public final class CompositeCommandEngine implements CommandExecutor {
    private final Map<String, Environment<?>> environments;

    private CompositeCommandEngine(Map<String, Environment<?>> environments) {
        this.environments = environments;
    }

    /**
     * Executes a command string by identifying the environment from the first token.
     *
     * @param string the full command string
     * @throws ArgCountException if no environment name is provided
     * @throws NullPointerException if the string is null
     */
    @Override
    public void execute(String string) {
        Objects.requireNonNull(string);

        List<String> tokens = Tokenizer.tokenize(string);
        if (tokens.isEmpty()) {
            throw new ArgCountException("No environment provided");
        }

        Environment<?> environment = environments.get(tokens.get(0));
        environment.execute(
                tokens.subList(1, tokens.size())
        );
    }

    @Override
    public String toString() {
        return "CompositeCommandEngine{" +
                "environments=" + environments +
                "}";
    }

    /**
     * Returns a new builder for creating {@link CompositeCommandEngine} instances.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating {@link CompositeCommandEngine} instances.
     */
    public static final class Builder {
        private Builder() {}

        private final Map<String, Environment<?>> environments = new HashMap<>();

        /**
         * Builds the {@link CompositeCommandEngine} instance.
         *
         * @return a new {@link CompositeCommandEngine}
         */
        public CompositeCommandEngine build() {
            return new CompositeCommandEngine(
                    Map.copyOf(environments)
            );
        }

        /**
         * Adds an environment to the engine.
         *
         * @param environment the environment to add
         * @return this builder
         * @throws IllegalArgumentException if an environment with the same name already exists
         * @throws NullPointerException if the environment is null
         */
        public Builder environment(Environment<?> environment) {
            Objects.requireNonNull(environment);

            String name = environment.name();
            if (environments.containsKey(name)) {
                throw new IllegalArgumentException("Environment \"" + name + "\" already exists");
            }

            environments.put(name, environment);
            return this;
        }
    }
}
