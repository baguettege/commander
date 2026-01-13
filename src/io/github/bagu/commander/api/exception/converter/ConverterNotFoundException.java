package io.github.bagu.commander.api.exception.converter;

/**
 * Thrown when a required converter is not registered in the environment.
 *
 * <p>
 *     This exception is thrown during argument resolution when an argument or option requires
 *     a converter for a type that has not been registered with the environment.
 * </p>
 */
public class ConverterNotFoundException extends ConverterException {
    /**
     * Creates a new exception for the specified converter type.
     *
     * @param converterType the type for which no converter was found
     */
    public ConverterNotFoundException(Class<?> converterType) {
        super("Converter not found for type \"" + converterType + "\"");
    }
}
