package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.exception.converter.ConversionException;

import java.util.Objects;

/**
 * Converts string values to {@link Float}.
 *
 * @see Converter
 */
public final class FloatConverter implements Converter<Float> {
    /**
     * Converts a string value into an {@link Float}.
     *
     * @param string the string to convert
     * @return the float
     * @throws ConversionException if the string is not a valid float
     * @throws NullPointerException if the string is null
     */
    @Override
    public Float convert(String string) throws ConversionException {
        Objects.requireNonNull(string);

        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            throw new ConversionException("Expected float, got \"" + string + "\": " + e.getMessage());
        }
    }
}
