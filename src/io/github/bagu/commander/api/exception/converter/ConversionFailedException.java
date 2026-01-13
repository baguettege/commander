package io.github.bagu.commander.api.exception.converter;

/**
 * Thrown when a converter fails to convert a string to its target type.
 *
 * <p>
 *     This exception is thrown during argument resolution when the input string cannot be
 *     converted to the required type (e.g., "abc" cannot be converted to an integer).
 * </p>
 */
public class ConversionFailedException extends ConverterException {
    /**
     * Creates a new exception with the reason and received value.
     *
     * @param reason the reason why conversion failed
     * @param received the value that could not be converted
     */
    public ConversionFailedException(String reason, String received) {
        super("Type conversion failed for \"" + received + "\": " + reason);
    }
}
