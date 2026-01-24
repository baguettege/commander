package io.github.bagu.executor.api.spec;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Abstract base class for parameter specifications (arguments and options).
 * <p>
 * A parameter spec defines a type of validator for command parameters.
 *
 * @param <T> the type of parameter value
 * @see ArgSpec
 * @see OptionSpec
 */
public abstract class ParameterSpec<T> extends AbstractSpec {
    private final Class<T> type;
    private final Predicate<T> validator;

    /**
     * Constructs a new parameter specification.
     *
     * @param name the parameter name
     * @param description the parameter description
     * @param type the parameter type
     * @param validator the validator predicate
     * @throws NullPointerException if any parameter is null
     */
    protected ParameterSpec(
            String name,
            String description,
            Class<T> type,
            Predicate<T> validator
    ) {
        super(name, description);
        this.type = Objects.requireNonNull(type);
        this.validator = Objects.requireNonNull(validator);
    }

    /**
     * Returns the type of this parameter.
     * <p>
     * The type determines which converter will be used for type conversion.
     *
     * @return the parameter type
     */
    public Class<T> type() {
        return type;
    }

    /**
     * Returns the validator for this parameter.
     * <p>
     * The validator is applied after type conversion  to ensure the value meets any additional constraints.
     *
     * @return the validator predicate
     */
    public Predicate<T> validator() {
        return validator;
    }

    @Override
    public String toString() {
        return "ParameterSpec{" +
                "name=" + super.name() +
                ", description=" + super.description() +
                ", type=" + type +
                ", validator=" + validator +
                "}";
    }
}
