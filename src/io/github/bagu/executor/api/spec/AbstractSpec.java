package io.github.bagu.executor.api.spec;

import java.util.Objects;

/**
 * Abstract base class for all specifications.
 * <p>
 * Provides common properties shared by all spec types: a name and description.
 *
 * @see CommandSpec
 * @see ParameterSpec
 */
public abstract class AbstractSpec {
    private final String name;
    private final String description;

    /**
     * Constructs a new abstract specification.
     *
     * @param name the name of this specification
     * @param description the description of this specification
     * @throws NullPointerException if any parameter is null
     */
    protected AbstractSpec(
            String name,
            String description
    ) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    /**
     * Returns the name of this specification.
     *
     * @return the name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the description of this specification.
     *
     * @return the description
     */
    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return "AbstractSpec{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
