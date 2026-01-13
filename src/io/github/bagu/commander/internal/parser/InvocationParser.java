package io.github.bagu.commander.internal.parser;

import io.github.bagu.commander.api.exception.parser.InvocationFormatException;
import io.github.bagu.commander.internal.invocation.Invocation;

import java.util.*;

/**
 * Parses tokenized command input into an {@link Invocation} object.
 *
 * <p>
 *     This parser expects tokens in the format:<br>
 *     {@code environment} {@code command} {@code [positional args]} {@code [--option=value]} {@code [--flag]}
 * </p>
 *
 * <p>
 *     Parsing rules:<br>
 *     - First token is the environment name<br>
 *     - Second token is the command name<br>
 *     - Tokens starting with {@code --} and containing {@code =} are options ({@code --key=value})<br>
 *     - Tokens starting with {@code --} without {@code =} are flags ({@code --flag})<br>
 *     - Remaining tokens are positional arguments
 * </p>
 *
 * <p>
 *     The {@code --} prefix is stripped from all option keys and flag names in the resulting invocation.
 * </p>
 *
 * @see Invocation
 * @see Tokenizer
 */
public final class InvocationParser {
    private InvocationParser() {}

    /**
     * Parses a list of tokens into an invocation.
     *
     * @param tokens the tokens to parse (must contain at least 2 tokens)
     * @return the parsed invocation
     * @throws InvocationFormatException if fewer than 2 tokens are provided
     */
    public static Invocation parse(List<String> tokens) throws InvocationFormatException {
        if (tokens.size() < 2)
            throw new InvocationFormatException("<environment> <command> [args] [options]");

        String environment = tokens.get(0);
        String command = tokens.get(1);

        List<String> builtArgs = new ArrayList<>();
        Map<String, String> builtOptions = new HashMap<>();
        Set<String> builtFlags = new HashSet<>();

        // start at 2, ignore environment and cmd
        for (int i = 2; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.startsWith("--")) {
                // option OR flag
                String[] parts = token.split("=", 2);

                if (parts.length == 2) {
                    // option
                    String key = parts[0].substring(2);
                    String value = parts[1];
                    builtOptions.put(key, value);
                } else {
                    // flag
                    String flag = token.substring(2);
                    builtFlags.add(flag);
                }

            } else {
                // arg
                builtArgs.add(token);
            }
        }

        return new Invocation(
                environment,
                command,
                builtArgs,
                builtOptions,
                builtFlags
        );
    }
}
