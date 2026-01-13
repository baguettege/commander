package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link String} values.
 *
 * <p>
 *     This implementation returns the string unchanged. This converter exists to allow {@link String} types
 *     to be used in argument and option definitions.
 * </p>
 *
 * @see Converter
 */
public final class StringConverter implements Converter<String> {
    /**
     * Returns the input string unchanged.
     *
     * @param text the string to convert
     * @return the same string
     */
    @Override
    public String convert(String text) {
        return text;
    }
}
