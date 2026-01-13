package io.github.bagu.commander.api.converter;

import io.github.bagu.commander.api.core.Environment;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;
import io.github.bagu.commander.internal.resolver.ArgumentResolver;

/**
 * Converts string tokens into typed objects.
 *
 * <p>
 *     Converters are used to convert string arguments from the command line into strongly-typed values.
 *     Each {@link Environment} maintains a registry of converters, mapping types to
 *     the converter.
 * </p>
 *
 * <p>
 *     Converters are used during argument resolution after invocation parsing and before command execution.
 *     They should be stateless and thread-safe, as a single converter instance may be used for multiple conversions.
 * </p>
 *
 * <p>
 *     Example implementation:
 *     <pre>
 *         {@code public final class IntegerConverter implements Converter<Integer> {
 *     @Override
 *     public Integer convert(String text) throws ConversionFailedException {
 *         try {
 *             return Integer.parseInt(text);
 *         } catch (NumberFormatException e) {
 *             throw new ConversionFailedException("Expected integer, got " + text, text);
 *         }
 *     }
 * }}
 *     </pre>
 * </p>
 *
 * <p>
 *     Registering a converter to an environment:
 *     <pre>
 *         {@code Environment<MyContext> environment = Environment.<MyContext>builder()
 *         .converter(new IntegerConverter(), Integer.class)
 *         .converter(new StringConverter(), String.class)
 *         .build();}
 *     </pre>
 * </p>
 *
 * @param <T> the type that this converter produces
 *
 * @see Environment
 * @see ArgumentResolver
 */
@FunctionalInterface
public interface Converter<T> {
    /**
     * Converts the given string into the target type.
     *
     * <p>
     *     Implementations should throw {@link ConversionFailedException} with a clear message
     *     when the input cannot be converted. The message should say what was expected and what was
     *     received.
     * </p>
     *
     * @param text the string to convert
     * @return the converted value
     * @throws ConversionFailedException if the string cannot be converted to the target type
     */
    T convert(String text) throws ConversionFailedException;
}
