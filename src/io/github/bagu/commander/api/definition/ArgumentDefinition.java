package io.github.bagu.commander.api.definition;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

import java.util.Objects;

/**
 * Defines a required positional argument for a command.
 *
 * <p>
 *     An argument definition specifies a required parameter that must be provided in a specific position (ignores flags and options)
 *     when invoking a command. Each argument has a name for identification, a type for conversion, and
 *     a description for documentation purposes.
 * </p>
 *
 * <p>
 *     Arguments are positional and order-dependent. The first argument definition in a command corresponds to
 *     the first positional value provided to the user, the second to the second, and so on.
 * </p>
 *
 * <p>
 *     An example:
 *     <pre>
 *         {@code ArgumentDefinition<String> nameArg = ArgumentDefinition.<String>builder()
 *         .name("name")
 *         .type(String.class)
 *         .description("The server name")
 *         .build();
 *
 * ArgumentDefinition<Integer> port = ArgumentDefinition.<Integer>builder()
 *         .name("port")
 *         .type(Integer.class)
 *         .description("The port to bind the server to")
 *         .build();}
 *     </pre>
 * </p>
 *
 * <p>
 *     During command execution, arguments can be retrieved using {@link Arguments#arg(String, Class)}
 * </p>
 *
 * <p>
 *     This class is immutable after construction and is built using the builder pattern.
 * </p>
 *
 * @param <T> the type of the argument value
 *
 * @see CommandDefinition
 * @see Arguments#arg(String, Class)
 * @see ArgumentResolver
 */
public final class ArgumentDefinition<T> {
    private final String name;
    private final Class<T> type;
    private final String description;

    /**
     * Creates a new argument definition with the specified properties.
     *
     * @param name the argument name used for retrieval
     * @param type the class representing the argument's type
     * @param description human-readable description of the argument's purpose
     */
    private ArgumentDefinition(
            String name,
            Class<T> type,
            String description
    ) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    /**
     * Returns the argument name.
     *
     * <p>
     *     This name is used to retrieve the argument value from {@link Arguments}
     *     during command execution.
     * </p>
     *
     * @return the argument name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the argument type.
     *
     * <p>
     *     This type determines which {@link Converter} is used to convert
     *     the string input into the typed value.
     * </p>
     *
     * @return the class representing the argument's type
     */
    public Class<T> type() {
        return type;
    }

    /**
     * Returns the argument description.
     *
     * <p>
     *     This description can be used for generating help text or documentation for commands.
     * </p>
     *
     * @return the argument description
     */
    public String description() {
        return description;
    }

    /**
     * Creates a new builder for constructing an argument definition.
     *
     * @return a new builder instance
     * @param <T> the type of the argument being defined
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for constructing {@link ArgumentDefinition} instances.
     *
     * <p>
     *     All fields (name, type, description) are required and must be set before building.
     * </p>
     *
     * @param <T> the type of the argument being built
     */
    public static final class Builder<T> {
        private String name;
        private Class<T> type;
        private String description;

        private Builder() {}

        /**
         * Builds the argument definition
         *
         * @return a new immutable argument definition instance
         * @throws NullPointerException if name, type or description is not set
         */
        public ArgumentDefinition<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(type);
            Objects.requireNonNull(description);

            return new ArgumentDefinition<>(
                    name,
                    type,
                    description
            );
        }

        /**
         * Sets the argument name.
         *
         * @param name the argument name used for retrieval
         * @return this builder
         */
        public Builder<T> name(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        /**
         * Sets the argument type.
         *
         * @param type the class representing the argument type
         * @return this builder
         */
        public Builder<T> type(Class<T> type) {
            Objects.requireNonNull(type);
            this.type = type;
            return this;
        }

        /**
         * Sets the argument description.
         *
         * @param description a human-readable description of the argument
         * @return this builder
         */
        public Builder<T> description(String description) {
            Objects.requireNonNull(description);
            this.description = description;
            return this;
        }
    }
}
