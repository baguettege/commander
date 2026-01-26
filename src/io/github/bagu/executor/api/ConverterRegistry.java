package io.github.bagu.executor.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A registry for {@link Converter} instances, indexed by their target type.
 * <p>
 * Each type can have at most one converter registered. Converters are used to transform string
 * arguments into typed values.
 *
 * @see Converter
 */
public final class ConverterRegistry {
    private final Map<Class<?>, Converter<?>> converters;

    private ConverterRegistry(Map<Class<?>, Converter<?>> converters) {
        this.converters = converters;
    }

    /**
     * Retrieves the converter for the specified type.
     *
     * @param type the target type
     * @return the converter for the type, or null if not registered
     * @param <T> the target type parameter
     * @throws NullPointerException if the type is null
     */
    @SuppressWarnings("unchecked") // safe, validated in builder
    public <T> Converter<T> get(Class<T> type) {
        Objects.requireNonNull(type);
        return (Converter<T>) converters.get(type);
    }

    /**
     * Retrieves all registered converters.
     *
     * @return an immutable list of all converters
     */
    public List<Converter<?>> getAll() {
        return List.copyOf(converters.values());
    }

    @Override
    public String toString() {
        return "ConverterRegistry{" +
                "converters=" + converters +
                '}';
    }

    /**
     * Returns a new builder for creating {@link ConverterRegistry} instances.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating {@link ConverterRegistry} instances.
     */
    public static final class Builder {
        private Builder() {}

        private final Map<Class<?>, Converter<?>> converters = new HashMap<>();

        /**
         * Builds the {@link ConverterRegistry} instance.
         *
         * @return a new {@link ConverterRegistry}
         */
        public ConverterRegistry build() {
            return new ConverterRegistry(
                    Map.copyOf(converters)
            );
        }

        /**
         * Registers a converter for a specific type.
         *
         * @param converter the converter instance
         * @param type the target type
         * @return this builder
         * @param <T> the target type parameter
         * @throws IllegalArgumentException if a converter for this type already exists
         * @throws NullPointerException if any parameter is null
         */
        public <T> Builder register(
                Converter<T> converter,
                Class<T> type
        ) {
            Objects.requireNonNull(converter);
            Objects.requireNonNull(type);

            if (converters.containsKey(type)) {
                throw new IllegalArgumentException("Converter with type \"" + type.getName() + "\" already exists");
            }

            converters.put(type, converter);
            return this;
        }
    }
}
