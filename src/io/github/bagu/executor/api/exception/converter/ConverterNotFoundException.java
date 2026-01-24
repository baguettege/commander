package io.github.bagu.executor.api.exception.converter;

/**
 * Exception thrown when no converter is registered for a required type.
 * <p>
 * This exception is thrown during argument binding when the framework attempts to convert
 * a string argument to a type for which no converter has been registered in the {@link io.github.bagu.executor.api.ConverterRegistry}.
 * <p>
 * To resolve this exception, register a converter for the required type using
 * {@link io.github.bagu.executor.api.ConverterRegistry.Builder#register(io.github.bagu.executor.api.Converter, Class)}
 *
 * @see io.github.bagu.executor.api.ConverterRegistry
 * @see io.github.bagu.executor.api.Converter
 */
public class ConverterNotFoundException extends ConverterException {
    /**
     * Constructs a new exception for the specified type.
     *
     * @param type the type for which no converter was found
     */
    public ConverterNotFoundException(Class<?> type) {
        super("Converter for type \"" + type + "\" not found");
    }
}
