package io.github.bagu.commander.api.exception.converter;

/**
 * Thrown when attempting to register a duplicate converter for a type.
 *
 * <p>
 *     This exception is thrown during environment building when a converter for a specific type
 *     is registered more than once.
 * </p>
 */
public class ConverterAlreadyExistsException extends ConverterException {
    /**
     * Creates a new exception for the specified converter type.
     *
     * @param converterType the type for which a duplicate converter was attempted
     */
    public ConverterAlreadyExistsException(Class<?> converterType) {
        super("Converter already exists for type \"" + converterType + "\"");
    }
}
