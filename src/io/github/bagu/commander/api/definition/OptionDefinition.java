package io.github.bagu.commander.api.definition;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.internal.parser.InvocationParser;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

import java.util.Objects;
import java.util.Optional;

/**
 * Defines an optional key-value parameter for a command.
 *
 * <p>
 *     An option definition specifies an optional parameter that can be provided as a key-value pair in
 *     command invocations. Options are specified using the syntax {@code --key=value}, and can have
 *     default values that are used when the option is not provided, however this is not required.
 * </p>
 *
 * <p>
 *     Options can appear anywhere in the command invocation, after the environment and command. Unlike arguments,
 *     options are non-positional, optional and identified by their key.
 * </p>
 *
 * <p>
 *     An example
 *     <pre>
 *         {@code OptionDefinition<String> hostOption = OptionDefinition.<String>builder()
 *         .key("host")
 *         .type(String.class)
 *         .defaultValue("localhost")
 *         .description("The host to connect to")
 *         .build();
 *
 * OptionDefinition<Integer> portOption = OptionDefinition.<Integer>builder()
 *         .key("port")
 *         .type(Integer.class)
 *         .defaultValue(443)
 *         .description("The port to connect to")
 *         .build();}
 *     </pre>
 * </p>
 *
 * <p>
 *     This class is immutable after construction and is built using the builder pattern.
 * </p>
 *
 * @param <T> the type of the option value
 *
 * @see CommandDefinition
 * @see Arguments#option(String, Class)
 * @see InvocationParser
 * @see ArgumentResolver
 */
public final class OptionDefinition<T> {
    private final String key;
    private final Class<T> type;
    private final T defaultValue;
    private final String description;

    /**
     * Creates a new option definition with the specified properties.
     *
     * @param key the option key used in command invocations
     * @param type the class representing the option's type
     * @param defaultValue the default value to use when the option is not provided
     * @param description human-readable description of the option's purpose
     */
    private OptionDefinition(
            String key,
            Class<T> type,
            T defaultValue,
            String description
    ) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    /**
     * Returns the option key.
     *
     * <p>
     *     This key is used in command invocations with the syntax {@code --key=value} and to retrieve
     *     the value via {@link Arguments#option(String, Class)}
     * </p>
     *
     * @return the option key
     */
    public String key() {
        return key;
    }

    /**
     * Returns the option type.
     *
     * <p>
     *     This type determines which {@link Converter} is used to convert
     *     the string input into the typed value.
     * </p>
     *
     * @return the class representing the option type
     */
    public Class<T> type() {
        return type;
    }

    /**
     * Returns the default value for this option.
     *
     * <p>
     *     If the option is not provided by the user during command invocation, this default value
     *     will be used instead. An empty optional indicates that no default value is set, and the
     *     option will be absent if not provided.
     * </p>
     *
     * @return an {@link Optional} containing the default value, or empty if no default is set
     */
    public Optional<T> defaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    /**
     * Returns the option description.
     *
     * <p>
     *     This description can be used for generating help text or documentation for commands.
     * </p>
     *
     * @return the option description
     */
    public String description() {
        return description;
    }

    /**
     * Creates a new builder for constructing an option definition.
     *
     * @return a new builder instance
     * @param <T> the type of the option being defined
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for constructing {@link OptionDefinition} instances.
     *
     * <p>
     *     The key, type, and description fields are required and must be set before building. The default
     *     value is optional and can be left out if no default is desired.
     * </p>
     *
     * @param <T> the type of the option being built
     */
    public static final class Builder<T> {
        private String key;
        private Class<T> type;
        private T defaultValue = null;
        private String description;

        private Builder() {}

        /**
         * Builds the option definition.
         *
         * @return a new immutable option definition instance
         * @throws NullPointerException if key, type, or description is not set
         */
        public OptionDefinition<T> build() {
            Objects.requireNonNull(key);
            Objects.requireNonNull(type);
            Objects.requireNonNull(description);

            return new OptionDefinition<>(
                    key,
                    type,
                    defaultValue,
                    description
            );
        }

        /**
         * Sets the option key.
         *
         * @param key the option key used in command invocations
         * @return this builder
         */
        public Builder<T> key(String key) {
            Objects.requireNonNull(key);
            this.key = key;
            return this;
        }

        /**
         * Sets the option type.
         *
         * @param type the class representing the option type
         * @return this builder
         */
        public Builder<T> type(Class<T> type) {
            Objects.requireNonNull(type);
            this.type = type;
            return this;
        }

        /**
         * Sets the default value for this option.
         *
         * <p>
         *     This method is optional. If not called, the option will have no default value and
         *     will return an empty Optional when not provided by the user.
         * </p>
         *
         * @param defaultValue the default value to use (may be null to explicitly set no default)
         * @return this builder
         */
        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * Sets the option description.
         *
         * @param description a human-readable description of this option
         * @return this builder
         */
        public Builder<T> description(String description) {
            Objects.requireNonNull(description);
            this.description = description;
            return this;
        }
    }
}
