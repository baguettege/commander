package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.exception.converter.ConversionException;

import java.util.Objects;

/**
 * Converts string values to {@link Integer}.
 *
 * @see Converter
 */
public final class IntegerConverter implements Converter<Integer> {
    /**
     * Converts a string value into an {@link Integer}.
     *
     * @param string the string to convert
     * @return the integer
     * @throws ConversionException if the string is not a valid integer
     * @throws NullPointerException if the string is null
     */
    @Override
    public Integer convert(String string) throws ConversionException {
        Objects.requireNonNull(string);

        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new ConversionException("Expected integer, got \"" + string + "\": " + e.getMessage());
        }
    }
}
