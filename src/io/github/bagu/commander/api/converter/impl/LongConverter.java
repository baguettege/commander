package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link Long} values.
 *
 * @see Converter
 */
public final class LongConverter implements Converter<Long> {
    /**
     * Converts a string to a long.
     *
     * @param text the string to convert
     * @return the parsed long
     * @throws ConversionFailedException if the string is not a valid long
     */
    @Override
    public Long convert(String text) throws ConversionFailedException {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new ConversionFailedException("Expected long, got " + text, text);
        }
    }
}
