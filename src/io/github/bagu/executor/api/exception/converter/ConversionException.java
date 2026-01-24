package io.github.bagu.executor.api.exception.converter;

/**
 * Exception thrown when type conversion fails.
 * <p>
 * This exception occurs when a {@link io.github.bagu.executor.api.Converter} is unable to
 * convert a string value to its target type.
 *
 * @see io.github.bagu.executor.api.Converter
 */
public class ConversionException extends ConverterException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ConversionException(String message) {
        super(message);
    }
}
