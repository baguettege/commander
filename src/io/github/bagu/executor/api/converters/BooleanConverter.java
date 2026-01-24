package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.exception.converter.ConversionException;

import java.util.Objects;

/**
 * Converts string values to {@link Boolean}.
 * <p>
 * Only accepts the exact strings "true" and "false" (case-sensitive).
 * Throws {@link ConversionException} for any other input.
 */
public final class BooleanConverter implements Converter<Boolean> {
    /**
     * Converts a string value into an {@link Boolean}.
     *
     * @param string the string to convert
     * @return the boolean
     * @throws ConversionException if the string is not "true" or "false"
     * @throws NullPointerException if the string is null
     */
    @Override
    public Boolean convert(String string) throws ConversionException {
        Objects.requireNonNull(string);

        if (!"true".equals(string) && !"false".equals(string)) {
            throw new ConversionException("Expected boolean, got \"" + string + "\"");
        }

        return "true".equals(string);
    }
}
