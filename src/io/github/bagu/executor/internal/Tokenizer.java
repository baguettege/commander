package io.github.bagu.executor.internal;

import io.github.bagu.executor.api.exception.parser.TokenizationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for tokenizing command input strings.
 * <p>
 * Handles quoted strings, escape sequences, and whitespace separation. The tokenizer splits
 * input on whitespace but respects quoted strings as single tokens.
 * <p>
 * Supported escape sequences:
 * <ol>
 * <li>{@code \\} - Backslash</li>
 * <li>{@code \"} - Double quote</li>
 * <li>{@code ]n} - Newline</li>
 * <li>{@code \r} - Carriage return</li>
 * <li>{@code \t} - Tab</li>
 * <li>{@code \b} - Backspace</li>
 * </ol>
 * <p>
 * For example, {@code hello "world test" --opt=val} becomes:
 * {@code ["hello", "world test", "--opt=val"]}
 *
 * @see TokenizationException
 */
public final class Tokenizer {
    private Tokenizer() {}

    private static final Map<Character, Character> escapeSequences = Map.of(
            '\\', '\\',
            '"', '"',
            'n', '\n',
            'r', '\r',
            't', '\t',
            'b', '\b'
    );

    /**
     * Tokenizes a command input string into a list of tokens.
     * <p>
     * Tokens are separated by whitespace unless enclosed in double quotes. Escape sequences are
     * processed within both quoted and unquoted strings.
     *
     * @param string the command string to tokenize
     * @return a list of tokens
     * @throws TokenizationException if there are unterminated quotes, unknown escape sequences, or a trailing
     * escape character
     */
    public static List<String> tokenize(String string) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean inQuotes = false;
        boolean isEscaped = false;

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (isEscaped) {
                isEscaped = false;
                Character sequence = escapeSequences.get(c);
                if (sequence == null) {
                    throw new TokenizationException("Unknown escape sequence: '\\" + c + "'");
                } else {
                    sb.append(sequence);
                }
            } else if (c == '"') {
                if (inQuotes) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                inQuotes = !inQuotes;
            } else if (c == '\\') {
                isEscaped = true;
            } else if (!inQuotes && Character.isWhitespace(c)) {
                if (!sb.isEmpty()) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }

        if (inQuotes) {
            throw new TokenizationException("Unterminated quotes");
        } else if (isEscaped) {
            throw new TokenizationException("Trailing escape");
        }

        if (!sb.isEmpty()) {
            tokens.add(sb.toString());
        }

        return tokens;
    }

    public static void main(String[] args) {
        System.out.println(tokenize("command \"argument with spaces\" --text=\"quoted value\""));
    }
}
