package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.exception.converter.ConversionException;

import java.util.Objects;

/**
 * Converts string values to {@link Double}.
 *
 * @see Converter
 */
public final class DoubleConverter implements Converter<Double> {
    /**
     * Converts a string value into an {@link Double}.
     *
     * @param string the string to convert
     * @return the double
     * @throws ConversionException if the string is not a valid double
     * @throws NullPointerException if the string is null
     */
    @Override
    public Double convert(String string) throws ConversionException {
        Objects.requireNonNull(string);

        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            throw new ConversionException("Expected double, got \"" + string + "\": " + e.getMessage());
        }
    }
}
