package io.github.bagu.executor.api.spec;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Specification for a named command option.
 * <p>
 * An option is an optional named parameter provided in the format {@code --name=value}.
 * Options can have default values which are used when the option is not provided.
 * <p>
 * Unlike arguments, options are not positional and can be provided in any order.
 *
 * @param <T> the type of the option value
 * @see CommandSpec
 * @see ParameterSpec
 */
public final class OptionSpec<T> extends ParameterSpec<T> {
    private final T defaultValue;

    private OptionSpec(
            String name,
            String description,
            Class<T> type,
            Predicate<T> validator,
            T defaultValue
    ) {
        super(name, description, type, validator);
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value for this option.
     * <p>
     * The default value is used when the option is not provided in the command input.
     *
     * @return an optional containing the default value, or empty is no default is set
     */
    public Optional<T> defaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public String toString() {
        return "OptionSpec{" +
                "name=" + super.name() +
                ", description=" + super.description() +
                ", type=" + super.type() +
                ", validator=" + super.validator() +
                ", defaultValue=" + defaultValue +
                "}";
    }

    /**
     * Returns a new builder for creating {@link OptionSpec} instances.
     *
     * @param <T> the type of the option value
     * @return new builder
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for creating {@link OptionSpec} instances.
     *
     * @param <T> the type of the option value
     */
    public static final class Builder<T> {
        private Builder() {}

        private String name;
        private String description;
        private Class<T> type;
        private Predicate<T> validator;
        private T defaultValue;

        /**
         * Builds the {@link OptionSpec} instance.
         * <p>
         * If no validator is provided, a default validator that accepts all values is used.
         *
         * @return a new {@link OptionSpec}
         * @throws NullPointerException if name, description, or type is null
         */
        public OptionSpec<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(description);
            Objects.requireNonNull(type);

            if (validator == null)
                validator = ignored -> true;

            return new OptionSpec<>(
                    name,
                    description,
                    type,
                    validator,
                    defaultValue
            );
        }

        /**
         * Sets the name of the option.
         * <p>
         * This name is used both in the input format {@code --name=value} and to retrieve the option value
         * from the {@link io.github.bagu.executor.api.Context context}.
         *
         * @param name the option name
         * @return this builder
         * @throws NullPointerException if the name is null
         */
        public Builder<T> name(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Sets the description of the option.
         *
         * @param description the option description
         * @return this builder
         * @throws NullPointerException if the description is null
         */
        public Builder<T> description(String description) {
            this.description = Objects.requireNonNull(description);
            return this;
        }

        /**
         * Sets the type of the option.
         * <p>
         * This type determines which {@link io.github.bagu.executor.api.Converter} will be used to
         * transform the string input into a typed value.
         *
         * @param type the option type
         * @return this builder
         * @throws NullPointerException if the type is null
         */
        public Builder<T> type(Class<T> type) {
            this.type = Objects.requireNonNull(type);
            return this;
        }

        /**
         * Sets the validator for the option.
         * <p>
         * The validator is applied after type conversion to ensure the value meets any additional
         * constraints. If validation fails, an {@link io.github.bagu.executor.api.exception.arg.ArgValidationException}
         * is thrown.
         *
         * @param validator the validator predicate
         * @return this builder
         */
        public Builder<T> validator(Predicate<T> validator) {
            this.validator = validator;
            return this;
        }

        /**
         * Sets the default value for this option.
         * <p>
         * This value is used when the option is not provided in the command input.
         *
         * @param defaultValue the default value, or null for no default
         * @return this builder
         */
        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }
}
