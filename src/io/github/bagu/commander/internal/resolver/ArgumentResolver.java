package io.github.bagu.commander.internal.resolver;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.api.converter.Converter;
import io.github.bagu.commander.api.definition.ArgumentDefinition;
import io.github.bagu.commander.api.definition.CommandDefinition;
import io.github.bagu.commander.api.definition.FlagDefinition;
import io.github.bagu.commander.api.definition.OptionDefinition;
import io.github.bagu.commander.api.exception.argument.InvalidArgumentCountException;
import io.github.bagu.commander.api.exception.converter.ConversionFailedException;
import io.github.bagu.commander.api.exception.converter.ConverterNotFoundException;
import io.github.bagu.commander.internal.invocation.Invocation;

import java.util.*;

/**
 * Resolves string arguments from an {@link Invocation} into typed {@link Arguments}.
 *
 * <p>
 *     This resolver performs type conversion using registered {@link Converter}s and ensures that the correct
 *     number of positional arguments are provided. It handles three types of arguments:<br>
 *     - Positional arguments: converted to their defined types in order<br>
 *     - Options: converted to their defined types, default values or null if not provided<br>
 *     - Flags: only included in the result set if they were specified by the user
 * </p>
 *
 * @see Arguments
 * @see Invocation
 * @see Converter
 */

public final class ArgumentResolver {
    private ArgumentResolver() {}

    /**
     * Resolves an invocation into typed arguments.
     *
     * <p>
     *     Resolution process:<br>
     *     - Validates that the number of positional arguments matches the command definition<br>
     *     - Converts each positional argument using its defined type's converter<br>
     *     - Converts each provided option using its defined type's converter, or its default value<br>
     *     - Includes only the flags that were specified in the command definition
     * </p>
     *
     * <p>
     *     The converter map given as a parameter expects that the {@link Class} and {@link Converter} both have the
     *     same type parameter.
     * </p>
     *
     * @param invocation the parsed invocation with string arguments
     * @param command the command definition specifying the expected arguments
     * @param converters the map of type of converters available for conversion
     * @return the resolved arguments with typed values
     * @throws InvalidArgumentCountException if the number of positional arguments does not match the command definition
     * @throws ConverterNotFoundException if a required converter is not registered
     * @throws ConversionFailedException if type conversion fails
     */
    public static Arguments resolve(
            Invocation invocation,
            CommandDefinition<?> command,
            Map<Class<?>, Converter<?>> converters // assumption that these 2 are the same type
    ) throws InvalidArgumentCountException, ConverterNotFoundException, ConversionFailedException {
        // defined
        List<ArgumentDefinition<?>> definedArgs = command.args();
        List<OptionDefinition<?>> definedOptions = command.options();
        List<FlagDefinition> definedFlags = command.flags();

        // user passed
        List<String> passedArgs = invocation.args();
        Map<String, String> passedOptions = invocation.options();
        Set<String> passedFlags = invocation.flags();

        // building
        Map<String, Object> builtArgs = new HashMap<>();
        Map<String, Object> builtOptions = new HashMap<>();
        Set<String> builtFlags = new HashSet<>();

        // length check
        if (passedArgs.size() != definedArgs.size())
            throw new InvalidArgumentCountException(
                    command.name(),
                    definedArgs.size(),
                    passedArgs.size()
            );

        // args
        for (int i = 0; i < definedArgs.size() ; i++) {
            ArgumentDefinition<?> definedArg = definedArgs.get(i);
            String passedArg = passedArgs.get(i);

            Converter<?> converter = converters.get(definedArg.type());
            if (converter == null)
                throw new ConverterNotFoundException(definedArg.type());

            builtArgs.put(definedArg.name(), converter.convert(passedArg));
        }

        // options
        for (OptionDefinition<?> definedOption : definedOptions) {
            String definedKey = definedOption.key();
            String passedValue = passedOptions.get(definedKey);

            Object value;

            if (passedValue != null) {
                Converter<?> converter = converters.get(definedOption.type());
                if (converter == null)
                    throw new ConverterNotFoundException(definedOption.type());

                value = converter.convert(passedValue);
            } else {
                // put default or null if user not inputted option
                value = definedOption.defaultValue().orElse(null);
            }

            builtOptions.put(definedKey, value);
        }

        // flags
        for (FlagDefinition definedFlag : definedFlags) {
            String definedName = definedFlag.name();
            if (passedFlags.contains(definedName))
                builtFlags.add(definedName);
        }

        return new Arguments(
                builtArgs,
                builtOptions,
                builtFlags
        );
    }
}
