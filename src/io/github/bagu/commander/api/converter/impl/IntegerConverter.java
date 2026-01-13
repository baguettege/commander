package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link Integer} values.
 *
 * @see Converter
 */
public final class IntegerConverter implements Converter<Integer> {
    /**
     * Converts a string to an integer.
     *
     * @param text the string to convert
     * @return the parsed integer
     * @throws ConversionFailedException if the string is not a valid integer
     */
    @Override
    public Integer convert(String text) throws ConversionFailedException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ConversionFailedException("Expected integer, got " + text, text);
        }
    }
}
