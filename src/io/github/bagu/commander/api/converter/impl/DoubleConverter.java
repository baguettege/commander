package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link Double} values.
 *
 * @see Converter
 */
public final class DoubleConverter implements Converter<Double> {
    /**
     * Converts a string to a double.
     *
     * @param text the string to convert
     * @return the parsed double
     * @throws ConversionFailedException if the string is not a valid double
     */
    @Override
    public Double convert(String text) throws ConversionFailedException {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new ConversionFailedException("Expected double, got " + text, text);
        }
    }
}
