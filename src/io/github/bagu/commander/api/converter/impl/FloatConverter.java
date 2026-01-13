package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link Float} values.
 *
 * @see Converter
 */
public final class FloatConverter implements Converter<Float> {
    /**
     * Converts a string to a float.
     *
     * @param text the string to convert
     * @return the parsed float
     * @throws ConversionFailedException if the string is not a valid float
     */
    @Override
    public Float convert(String text) throws ConversionFailedException {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            throw new ConversionFailedException("Expected float, got " + text, text);
        }
    }
}
