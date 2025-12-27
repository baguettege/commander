package api;

import exceptions.CommandException;
import exceptions.CommandParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;

/**
 * CLI reader which reads, gets and executes commands based on a specified command registry and context.
 *
 * @param <T> concrete implementation of {@link CommandContext}
 */

public class CommandReader<T extends CommandContext> {
    private final Scanner scanner;
    private final CommandRegistry<T> registry;

    /**
     * Represents the constructor of a concrete {@link CommandContext} implementation
     * without the {@code String[] args} parameter applied to it.
     */
    private final Function<String[], T> contextFactory;

    /**
     * Constructs a reader with a specified registry and context factory.
     * <p>
     *     The context factory represents the constructor of a concrete {@link CommandContext} implementation
     *     without the {@code String[] args} parameter applied to it.
     * </p>
     * <p>
     *     An example usage is:
     *     <pre>
     *         {@code         CommandReader<TestCommandContext> reader = new CommandReader<>(
     *                 new Scanner(System.in),
     *                 new TestCommandRegistry(),
     *                 arguments -> new TestCommandContext(arguments, 1)
     *         );}
     *     </pre>
     *     The {@code 1} here is an example parameter for {@code TestCommandContext}.
     * </p>
     *
     * @param registry command registry to use
     * @param contextFactory the constructor of a concrete {@link CommandContext} implementation
     *                       without the {@code String[] args} parameter applied to it.
     */
    public CommandReader(
            Scanner scanner,
            CommandRegistry<T> registry,
            Function<String[], T> contextFactory
    ) {
        this.scanner = scanner;
        this.registry = registry;
        this.contextFactory = contextFactory;
    }

    /**
     * Blocks until the user inputs into the CLI, parses the input and executes the corresponding command.
     * <p>
     *     The first token of the user input is used as the name of the command.
     * </p>

     * @throws CommandException if any error occurs during the parsing, creation and execution of the command.
     */
    public void nextCommand() throws CommandException {
        String input = scanner.nextLine();
        String[] tokens = parse(input);
        if (tokens.length == 0) return;

        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        Command<T> command = registry.get(tokens[0]);
        T context = contextFactory.apply(args);

        command.execute(context);
    }

    /**
     * Returns whether the reader has another command ready.
     *
     * @return {@code true} if there is another command, else {@code false}
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Parses a string of text into its tokens.
     * <p>
     *     Tokens are split by spaces, or grouped up if surrounded by double quotes.
     * </p>
     * <p>
     *     Supports escape with {@code \} allowing for {@code \n} with {@code \\n}, {@code \} with {@code \\} and {@code "} with {@code \"}.
     * </p>
     * <p>
     *     May throw {@link CommandParseException} if the text has unterminated quotes, or a trailing
     *     escape {@code \}.
     * </p>
     *
     * @param text user text
     * @return array of the tokens
     */
    private static String[] parse(String text) {
        if (text == null || text.isBlank())
            return new String[0];

        ArrayList<String> args = new ArrayList<>();
        StringBuilder argBuilder = new StringBuilder();
        boolean inQuotes = false;
        boolean escape = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (escape) {
                switch (c) {
                    case 'n' -> argBuilder.append("\n");
                    case '\\' -> argBuilder.append("\\");
                    case '"' -> argBuilder.append('"');
                    default -> throw new CommandParseException("Unknown escape: " + c);
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && c == ' ') {
                if (!argBuilder.isEmpty()) {
                    args.add(argBuilder.toString());
                    argBuilder.setLength(0);
                }
            } else {
                argBuilder.append(c);
            }
        }

        if (inQuotes)
            throw new CommandParseException("Unterminated quotes");
        else if (escape)
            throw new CommandParseException("Trailing escape at text end");

        if (!argBuilder.isEmpty())
            args.add(argBuilder.toString());

        return args.toArray(String[]::new);
    }
}
