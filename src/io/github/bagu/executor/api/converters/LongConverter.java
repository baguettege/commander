package io.github.bagu.executor.api.converters;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.exception.converter.ConversionException;

import java.util.Objects;

/**
 * Converts string values to {@link Long}.
 *
 * @see Converter
 */
public final class LongConverter implements Converter<Long> {
    /**
     * Converts a string value into an {@link Long}.
     *
     * @param string the string to convert
     * @return the long
     * @throws ConversionException if the string is not a valid long
     * @throws NullPointerException if the string is null
     */
    @Override
    public Long convert(String string) throws ConversionException {
        Objects.requireNonNull(string);

        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            throw new ConversionException("Expected long, got \"" + string + "\": " + e.getMessage());
        }
    }
}
