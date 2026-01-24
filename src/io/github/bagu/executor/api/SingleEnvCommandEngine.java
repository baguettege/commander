package io.github.bagu.executor.api;

import io.github.bagu.executor.internal.Tokenizer;

import java.util.List;
import java.util.Objects;

/**
 * A command executor for a single environment.
 * <p>
 * Unlike {@link SyncCommandEngine} which dispatches to multiple environments, this executor works
 * with a single environment and passes all tokens directly to it for execution.
 *
 * @param <T> the type of {@link Context} used by the environment
 * @see Environment
 * @see CommandExecutor
 */
public final class SingleEnvCommandEngine<T extends Context<T>> implements CommandExecutor {
    private final Environment<T> environment;

    private SingleEnvCommandEngine(
            Environment<T> environment
    ) {
        this.environment = environment;
    }

    /**
     * Executes a command string by tokenizing it and passing it to the environment.
     * <p>
     * The name of the environment is not required and should not be present.
     *
     * @param string the command string
     * @throws NullPointerException if the string is null
     */
    @Override
    public void execute(String string) {
        Objects.requireNonNull(string);
        List<String> tokens = Tokenizer.tokenize(string);
        environment.execute(tokens);
    }

    @Override
    public String toString() {
        return "SingleEnvCommandEngine{" +
                "environment=" + environment +
                "}";
    }

    /**
     * Returns a new builder for creating {@link SingleEnvCommandEngine} instances.
     *
     * @param <T> the type of {@link Context} used by the environment
     * @return new builder
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for creating {@link SingleEnvCommandEngine} instances.
     *
     * @param <T> the type of {@link Context} used by the environment
     */
    public static final class Builder<T extends Context<T>> {
        private Builder() {}

        private Environment<T> environment;

        /**
         * Builds the {@link SingleEnvCommandEngine} instance.
         *
         * @return a new {@link SingleEnvCommandEngine}
         * @throws NullPointerException if the environment is null
         */
        public SingleEnvCommandEngine<T> build() {
            Objects.requireNonNull(environment);

            return new SingleEnvCommandEngine<>(
                    environment
            );
        }

        /**
         * Sets the environment for this engine.
         *
         * @param environment the environment
         * @return this builder
         * @throws NullPointerException if the environment is null
         */
        public Builder<T> environment(Environment<T> environment) {
            this.environment = Objects.requireNonNull(environment);
            return this;
        }
    }
}
