package io.github.bagu.executor.internal;

import io.github.bagu.executor.api.Converter;
import io.github.bagu.executor.api.ConverterRegistry;
import io.github.bagu.executor.api.exception.arg.ArgCountException;
import io.github.bagu.executor.api.exception.arg.ArgValidationException;
import io.github.bagu.executor.api.exception.converter.ConverterNotFoundException;
import io.github.bagu.executor.api.internal.TypedArgs;
import io.github.bagu.executor.api.spec.ArgSpec;
import io.github.bagu.executor.api.spec.CommandSpec;
import io.github.bagu.executor.api.spec.OptionSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Utility class for binding parsed arguments to typed values.
 * <p>
 * This binder uses converters to transform string arguments into their specified types, validates
 * them using the configured validators, and creates a {@link TypedArgs} instance containing
 * the typed values ready for use in command execution.
 * <p>
 * The binding process:
 * <ol>
 * <li>Validates the number of positional arguments matches the spec</li>
 * <li>For each argument: converts to type, validates, stores in map</li>
 * <li>For each option: converts to type if provided, uses default is no, validates, stores in map</li>
 * <li>Returns a {@link TypedArgs} instance containing all typed values</li>
 * </ol>
 *
 * @see ParsedArgs
 * @see TypedArgs
 * @see ConverterRegistry
 * @see CommandSpec
 */
public final class ArgBinder {
    private ArgBinder() {}

    /**
     * Binds parsed arguments to typed values according to the command specification.
     * <p>
     * This method performs type conversion and validation for both positional arguments
     * and named options.
     *
     * @param parsedArgs the parsed arguments and options as strings
     * @param converterRegistry the registry of type converters
     * @param spec the command specification defining expected arguments and options
     * @return a {@link TypedArgs} instance containing the typed and validated values
     * @throws ArgCountException if the number of arguments doesn't match the spec
     * @throws ConverterNotFoundException if no converter exists for a required type
     * @throws ArgValidationException if a value fails validation
     * @throws io.github.bagu.executor.api.exception.converter.ConversionException if type conversion fails
     */
    public static TypedArgs bind(
            ParsedArgs parsedArgs,
            ConverterRegistry converterRegistry,
            CommandSpec<?> spec
    ) {
        if (parsedArgs.args().size() != spec.argSpecs().size()) {
            throw new ArgCountException(
                    spec.argSpecs().size(),
                    parsedArgs.args().size()
            );
        }

        Map<String, Object> typedArgs = new HashMap<>();
        Map<String, Object> typedOptions = new HashMap<>();

        for (int i = 0; i < spec.argSpecs().size(); i++) {
            ArgSpec<?> argSpec = spec.argSpecs().get(i);
            String value = parsedArgs.args().get(i);

            Converter<?> converter = converterRegistry.get(argSpec.type());
            if (converter == null) {
                throw new ConverterNotFoundException(argSpec.type());
            }

            Object typedValue = converter.convert(value);

            @SuppressWarnings("unchecked")
            Predicate<Object> validator = (Predicate<Object>) argSpec.validator();
            if (!validator.test(typedValue)) {
                throw new ArgValidationException(argSpec.name(), String.valueOf(typedValue));
            }

            typedArgs.put(
                    argSpec.name(),
                    typedValue
            );
        }

        for (OptionSpec<?> optionSpec : spec.optionSpecs()) {
            String key = optionSpec.name();
            String value = parsedArgs.options().get(key);

            if (value == null) {
                typedOptions.put(
                        key,
                        optionSpec.defaultValue().orElse(null)
                );
            } else {
                Converter<?> converter = converterRegistry.get(optionSpec.type());
                if (converter == null) {
                    throw new ConverterNotFoundException(optionSpec.type());
                }

                Object typedValue = converter.convert(value);

                @SuppressWarnings("unchecked")
                Predicate<Object> validator = (Predicate<Object>) optionSpec.validator();
                if (!validator.test(typedValue)) {
                    throw new ArgValidationException(key, String.valueOf(typedValue));
                }

                typedOptions.put(
                        key,
                        typedValue
                );
            }
        }

        return new TypedArgs(
                typedArgs,
                typedOptions
        );
    }
}
