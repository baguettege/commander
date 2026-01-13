package io.github.bagu.commander.api.converter.impl;

import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Implementation of {@link Converter}. Converts string tokens to {@link Path} values.
 *
 * <p>
 *     Converts file system path strings to Path objects using {@link Path#of(String, String...)}.
 *     Accepts both absolute and relative paths in platform-specific format.
 * </p>
 *
 * @see Converter
 */
public final class PathConverter implements Converter<Path> {
    /**
     * Converts a string to a path.
     *
     * @param text the string to convert
     * @return the parsed path object
     * @throws ConversionFailedException if the string is not a valid path
     */
    @Override
    public Path convert(String text) throws ConversionFailedException {
        try {
            return Path.of(text);
        } catch (InvalidPathException e) {
            throw new ConversionFailedException("Could not convert string to path", text);
        }
    }
}
