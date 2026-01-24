package io.github.bagu.executor.api.spec;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Specification for a positional command argument.
 * <p>
 * An argument is a required positional parameter that must be provided when executing a command. Each
 * argument has a name, description, type, and optional validator.
 * <p>
 * Arguments are processed in order and must match the number defined in the command spec.
 *
 * @param <T> the type of the argument value
 * @see CommandSpec
 * @see ParameterSpec
 */
public final class ArgSpec<T> extends ParameterSpec<T> {
    private ArgSpec(
            String name,
            String description,
            Class<T> type,
            Predicate<T> validator
    ) {
        super(name, description, type, validator);
    }

    @Override
    public String toString() {
        return "ArgSpec{" +
                "name=" + super.name() +
                ", description=" + super.description() +
                ", type=" + super.type() +
                ", validator=" + super.validator() +
                "}";
    }

    /**
     * Returns a new builder for creating {@link ArgSpec} instances.
     *
     * @param <T> the type of the argument value
     * @return new builder
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for creating {@link ArgSpec} instances.
     *
     * @param <T> the type of the argument value
     */
    public static final class Builder<T> {
        private Builder() {}

        private String name;
        private String description;
        private Class<T> type;
        private Predicate<T> validator;

        /**
         * Builds the {@link ArgSpec} instance.
         * <p>
         * If no validator is provided, a default validator that accepts all values is used.
         *
         * @return a new {@link ArgSpec}
         * @throws NullPointerException if name, description, or type is null
         */
        public ArgSpec<T> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(description);
            Objects.requireNonNull(type);

            if (validator == null)
                validator = ignored -> true;

            return new ArgSpec<>(
                    name,
                    description,
                    type,
                    validator
            );
        }

        /**
         * Sets the name of the argument.
         * <p>
         * This name is used to retrieve the argument value from the {@link io.github.bagu.executor.api.Context context}.
         *
         * @param name the argument name
         * @return this builder
         * @throws NullPointerException if the name is null
         */
        public Builder<T> name(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Sets the description of the argument.
         *
         * @param description the argument description
         * @return this builder
         * @throws NullPointerException if the description is null
         */
        public Builder<T> description(String description) {
            this.description = Objects.requireNonNull(description);
            return this;
        }

        /**
         * Sets the type of the argument.
         * <p>
         * This type determines which {@link io.github.bagu.executor.api.Converter} will be used to
         * transform the string input into a typed value.
         *
         * @param type the argument type
         * @return this builder
         * @throws NullPointerException if the type is null
         */
        public Builder<T> type(Class<T> type) {
            this.type = Objects.requireNonNull(type);
            return this;
        }

        /**
         * Sets the validator for the argument.
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
    }
}
