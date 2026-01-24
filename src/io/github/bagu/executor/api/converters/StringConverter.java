package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;

import java.util.Objects;

/**
 * A pass-through converter that returns the string value unchanged.
 * <p>
 * This converter is useful for explicitly defining string arguments.
 *
 * @see Converter
 */
public final class StringConverter implements Converter<String> {
    /**
     * Returns the string unchanged.
     *
     * @param string the string to convert
     * @return the string unchanged
     * @throws NullPointerException if the string is null
     */
    @Override
    public String convert(String string) {
        Objects.requireNonNull(string);
        return string;
    }
}
