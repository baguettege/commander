package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

/**
 * Converts string tokens to {@link Boolean} values.
 *
 * <p>
 *     Accepts only {@code "true"} or {@code "false"} (case-sensitive). Any other input will
 *     result in a conversion failure.
 * </p>
 *
 * @see Converter
 */
public final class BooleanConverter implements Converter<Boolean> {
    /**
     * Converts a string to a boolean.
     *
     * @param text the string to convert (must be "true" or "false")
     * @return the parsed boolean value
     * @throws ConversionFailedException if the string is not "true" or "false"
     */
    @Override
    public Boolean convert(String text) throws ConversionFailedException {
        boolean isTrue = "true".equals(text);
        boolean isFalse = "false".equals(text);

        if (!isTrue && !isFalse)
            throw new ConversionFailedException("Expected boolean, got " + text, text);

        return isTrue;
    }
}
