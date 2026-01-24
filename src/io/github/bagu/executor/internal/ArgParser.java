package io.github.bagu.executor.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for parsing raw argument strings into {@link ParsedArgs}.
 * <p>
 * This parser separates positional arguments from named options. Options must be
 * in the format {@code --key=value}.
 * Any token that is not in this format is treated as a positional argument.
 * <p>
 * Example input: {@code ["arg1", "--opt=val:", "arg2"]} becomes:
 * <ol>
 * <li>args: {@code ["arg1", "arg2"]}</li>
 * <li>options: {@code {"opt": "val"}}</li>
 * </ol>
 *
 * @see ParsedArgs
 */
public final class ArgParser {
    private ArgParser() {}

    /**
     * Parses a list of argument strings into positional arguments and options.
     * <p>
     * Options must be in the format {@code --key=value}. An option without a value will be
     * treated as a positional argument.
     *
     * @param args the list of argument strings to parse
     * @return a {@link ParsedArgs} instance containing separated arguments and options
     */
    public static ParsedArgs parse(List<String> args) {
        List<String> parsedArgs = new ArrayList<>();
        Map<String, String> parsedOptions = new HashMap<>();

        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=", 2);

                if (parts.length == 2) {
                    String key = parts[0].substring(2);
                    String value = parts[1];
                    parsedOptions.put(key, value);
                } else {
                    parsedArgs.add(arg);
                }
            } else {
                parsedArgs.add(arg);
            }
        }

        return new ParsedArgs(
                parsedArgs,
                parsedOptions
        );
    }
}
