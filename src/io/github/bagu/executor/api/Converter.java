package io.github.bagu.executor.api;

import io.github.bagu.executor.api.exception.converter.ConversionException;

/**
 * Converts {@link String} values into a typed value.
 * <p>
 * Any conversion failures must be handled by throwing a {@link ConversionException}. For example,
 * parsing an integer must not throw {@link NumberFormatException} and should instead be caught and
 * throw a new conversion exception.
 *
 * @param <T> the type of the value the string is converted to
 * @see ConversionException
 */
@FunctionalInterface
public interface Converter<T> {
    /**
     * Converts a string into a specific type.
     *
     * @param string the string to convert
     * @return the converted value
     * @throws ConversionException if conversion fails
     */
    T convert(String string) throws ConversionException;
}
