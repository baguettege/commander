package io.github.bagu.commander.internal.parser;

import io.github.bagu.commander.api.exception.parser.TokenizationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizes raw command input text into a list of string tokens.
 *
 * <p>
 *     Tokenization rules:<br>
 *     - Tokens are separated by spaces<br>
 *     - Text within double quotes is treated as a single token (with quotes removed)<br>
 *     - Escape sequences: {@code \n} = newline, {@code \\} = backslash, {@code \"} double quote<br>
 *     - Multiple consecutive spaces are treated as a single separator
 * </p>
 *
 * <p>
 *     Example:<br>
 *     Input: {@code git commit "fix: bug in \"parser\"" --message=test}<br>
 *     Results in: {@code ["git", "commit", "fix: bug in "parser"", "--message=test"]}
 * </p>
 *
 * @see InvocationParser
 */
public final class Tokenizer {
    private Tokenizer() {}

    /**
     * Tokenizes the input text into a list of strings.
     *
     * @param text the raw command text to tokenize
     * @return an immutable list of tokens
     * @throws TokenizationException if quotes are unterminated, an escape sequence is invalid, or a
     * backslash appears at the end of input
     */
    public static List<String> tokenize(String text) throws TokenizationException {
        List<String> tokens = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();

        boolean isInQuotes = false;
        boolean isEscaped = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (isEscaped) {
                String escapeSequence;
                switch (c) {
                    case 'n':
                        escapeSequence = "\n";
                        break;
                    case '\\':
                        escapeSequence = "\\";
                        break;
                    case '"':
                        escapeSequence = "\"";
                        break;
                    default:
                        throw new TokenizationException("Unknown escape sequence: " + c);
                }
                tokenBuilder.append(escapeSequence);
                isEscaped = false;
            } else if (c == '\\') {
                isEscaped = true;
            } else if (c == '"') {
                isInQuotes = !isInQuotes;
            } else if (!isInQuotes && c == ' ') {
                if (tokenBuilder.length() != 0) {
                    tokens.add(tokenBuilder.toString());
                    tokenBuilder.setLength(0);
                }
            } else {
                tokenBuilder.append(c);
            }
        }

        if (isInQuotes)
            throw new TokenizationException("Unterminated quotes");
        if (isEscaped)
            throw new TokenizationException("Trailing escape");

        if (tokenBuilder.length() != 0)
            tokens.add(tokenBuilder.toString());

        return List.copyOf(tokens);
    }
}
