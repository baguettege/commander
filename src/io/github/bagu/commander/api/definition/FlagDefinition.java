package io.github.bagu.commander.api.definition;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.internal.parser.InvocationParser;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

import java.util.Objects;

/**
 * Defines a boolean flag for a command.
 *
 * <p>
 *     A flag definition specifies an optional boolean switch that can be included in command invocations.
 *     Flags are either present ({@code true}) or absent ({@code false}), they do not accept values.
 *     In a command invocation, they are specified using the syntax {@code --flag}.
 * </p>
 *
 * <p>
 *     Flags can appear anywhere in the command invocation, after the environment and command. Unlike arguments,
 *     flags are non-positional and optional.
 * </p>
 *
 * <p>
 *     An example
 *     <pre>
 *         {@code FlagDefinition verboseFlag = FlagDefinition.builder()
 *         .name("verbose")
 *         .description("Enables verbose output")
 *         .build();
 *
 * FlagDefinition forceFlag = FlagDefinition.builder()
 *         .name("force")
 *         .description("Force the operation without confirmation")
 *         .build();}
 *     </pre>
 * </p>
 *
 * <p>
 *     During command execution, flags can be retrieved using {@link Arguments#flag(String)}
 * </p>
 *
 * <p>
 *     This class is immutable after construction and is built using the builder pattern.
 * </p>
 *
 * @see CommandDefinition
 * @see Arguments#flag(String)
 * @see InvocationParser
 * @see ArgumentResolver
 */
public final class FlagDefinition {
    private final String name;
    private final String description;

    /**
     * Creates a new flag definition with the specified properties.
     *
     * @param name the flag name used in command invocations
     * @param description human-readable description of the flag's purpose
     */
    private FlagDefinition(
            String name,
            String description
    ) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the flag name.
     *
     * <p>
     *     This name is used in command invocations using the syntax {@code --name} and to retrieve
     *     a flag via {@link Arguments#flag(String)}
     * </p>
     *
     * @return the flag name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the flag description.
     *
     * <p>
     *     This description can be used for generating help text or documentation for commands.
     * </p>
     *
     * @return the flag description
     */
    public String description() {
        return description;
    }

    /**
     * Creates a new builder for constructing a flag definition.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for constructing {@link FlagDefinition} instances.
     *
     * <p>
     *     All fields (name, description) are required and must be set before building.
     * </p>
     */
    public static final class Builder {
        private String name;
        private String description;

        private Builder() {}

        /**
         * Builds the flag definition.
         *
         * @return a new immutable flag definition instance
         * @throws NullPointerException if name or description is not set
         */
        public FlagDefinition build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(description);

            return new FlagDefinition(
                    name,
                    description
            );
        }

        /**
         * Sets the flag name.
         *
         * @param name the flag name used in command invocations
         * @return this builder
         */
        public Builder name(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        /**
         * Sets the flag description.
         *
         * @param description a human-readable description of the flag
         * @return this builder
         */
        public Builder description(String description) {
            Objects.requireNonNull(description);
            this.description = description;
            return this;
        }
    }
}
