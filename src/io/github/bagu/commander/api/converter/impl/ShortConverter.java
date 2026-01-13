package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link Short} values.
 *
 * @see Converter
 */
public final class ShortConverter implements Converter<Short> {
    /**
     * Converts a string to a short.
     *
     * @param text the string to convert
     * @return the parsed short
     * @throws ConversionFailedException if the string is not a valid short
     */
    @Override
    public Short convert(String text) throws ConversionFailedException {
        try {
            return Short.parseShort(text);
        } catch (NumberFormatException e) {
            throw new ConversionFailedException("Expected short, got " + text, text);
        }
    }
}
