package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.exception.converter.ConversionException;

import java.util.Objects;

/**
 * Converts string values to {@link Short}.
 *
 * @see Converter
 */
public final class ShortConverter implements Converter<Short> {
    /**
     * Converts a string value into an {@link Short}.
     *
     * @param string the string to convert
     * @return the short
     * @throws ConversionException if the string is not a valid short
     * @throws NullPointerException if the string is null
     */
    @Override
    public Short convert(String string) throws ConversionException {
        Objects.requireNonNull(string);

        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            throw new ConversionException("Expected short, got \"" + string + "\": " + e.getMessage());
        }
    }
}
