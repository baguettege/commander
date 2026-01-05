package api;

import exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reads and executes commands from an input source.
 *
 * <p>
 *     {@link CommandReader} handles parsing, environment selection, argument parsing and execution of commands,
 *     registered via {@link CommandRegistry} and {@link CommandEnvironment}. it supports both single and multiple environment
 *     setups.
 * </p>
 *
 * <p>
 *     Setups:
 *     <li>Multiple environment: {@code [environment] [command] [args...]}</li>
 *     <li>Single environment: {@code [command] [args...]} or {@code [environment] [command] [args...]}</li>
 * </p>
 *
 * <p>
 *     Environments are registered using a unique name, a {@link CommandRegistry} and a {@link CommandContextFactory}.
 *     When reading inputs, the first token is checked against registered environments:
 *     <li>If a matching environment exists, the second token is read as the command name and the remaining tokens
 *     are read as the command arguments.</li>
 *     <li>If no environment matches, and only one environment is registered, the first token is read as the
 *     command name, and is executed within the only registered environment.</li>
 *     <li>If no environment matches, and multiple environments exist, a {@link UnknownCommandEnvironmentException}
 *     is thrown.</li>
 *     <li>If no environments are registered, then a {@link NoCommandEnvironmentsException} will be thrown.</li>
 * </p>
 *
 * <p>
 *     Double quotes {@code "} can be used to group arguments containing spaces. Unterminated quotes will result
 *     in the throwing of a {@link CommandParseException}.
 * </p>
 *
 */

public final class CommandReader {
    private final Scanner scanner;
    private final ConcurrentHashMap<String, CommandEnvironment<?>> environmentRegistry = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@link Scanner} that reads input from a given {@link Scanner}.
     *
     * @param scanner scanner to read lines from
     */
    public CommandReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Registers a new command environment.
     *
     * @param name unique name of the environment
     * @param registry the {@link CommandRegistry} containing the commands for this environment
     * @param contextFactory the {@link CommandContextFactory} used to create command contexts
     * @param <C> the type of {@link CommandContext} for this environment
     * @throws CommandRegisterException if an environment with the same name has been registered already
     */
    public <C extends CommandContext<C>> void registerEnvironment(
            String name,
            CommandRegistry<C> registry,
            CommandContextFactory<C> contextFactory
    ) throws CommandRegisterException {
        if (environmentRegistry.get(name) != null)
            throw new CommandRegisterException("Command already registered with name: " + name);
        CommandEnvironment<C> meta = new CommandEnvironment<>(registry, contextFactory);
        environmentRegistry.put(name, meta);
    }

    /**
     * Unregisters a command environment from the reader.
     *
     * <p>
     *     If the environment does not exist with the given name, it is ignored.
     * </p>
     *
     * @param name name of the command environment
     * @return {@code true} if the environment with the given name existed, otherwise {@code false}
     */
    public boolean unregisterEnvironment(String name) {
        CommandEnvironment<?> environment = environmentRegistry.remove(name);
        return environment != null;
    }

    /**
     * Returns whether the reader has another command ready.
     *
     * @return if the underlying {@link Scanner} has another line
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Stops the reader by closing the underlying {@link Scanner}.
     *
     * <p>
     *     No more commands can be read after this has been called.
     * </p>
     */
    public void stop() {
        scanner.close();
    }

    /**
     * Reads the next line of input, parses it, and executes the corresponding command within the given environment
     * (or the only environment if there is only one).
     *
     * @throws NoCommandEnvironmentsException if no environments are registered with this reader
     * @throws UnknownCommandEnvironmentException if the given environment is unknown
     * @throws CommandParseException if parsing of the command fails
     * @throws UnknownCommandException if the command name is unregistered within the given {@link CommandRegistry}
     * @throws CommandArgumentException if the arguments for the specified command are invalid
     * @throws CommandExecutionException if an error occurs during command execution
     * @throws CommandException if any error occurs during the process
     */
    public void nextCommand() throws CommandException {
        String input = scanner.nextLine();
        if (input.isEmpty()) return;

        if (environmentRegistry.isEmpty())
            throw new NoCommandEnvironmentsException("No command environments exist for command reader: " + System.identityHashCode(this));


        String[] tokens = parse(input);

        String commandName;
        String[] args;

        // get env name, cmd name & args
        String environmentName = tokens[0];
        CommandEnvironment<?> environment = environmentRegistry.get(environmentName);
        if (environment == null) {
            if (environmentRegistry.size() == 1) { // only 1 env exists
                environment = environmentRegistry.values().iterator().next();
                commandName = environmentName; // 1st token is cmd name not env name
                args = Arrays.copyOfRange(tokens, 1, tokens.length);
            } else {
                throw new UnknownCommandEnvironmentException(environmentName);
            }
        } else { // env exists
            if (tokens.length < 2)
                throw new CommandParseException("Expected command");

            commandName = tokens[1];
            args = Arrays.copyOfRange(tokens, 2, tokens.length);
        }

        executeCommand(
                environment,
                commandName,
                args
        );
    }

    /**
     * Executes a command within a given environment.
     *
     * @param environment the command environment
     * @param commandName the name of the command to execute
     * @param args the command arguments
     * @param <C> the type of {@link CommandContext} for this environment
     * @throws UnknownCommandException if the command name is unregistered within the given {@link CommandRegistry}
     * @throws CommandArgumentException if the arguments for the specified command are invalid
     * @throws CommandExecutionException if an error occurs during command execution
     * @throws CommandException if any error occurs during the process
     */
    private static <C extends CommandContext<C>> void executeCommand(
            CommandEnvironment<C> environment,
            String commandName,
            String[] args
    ) throws CommandException {
        CommandRegistry<C> registry = environment.registry();
        CommandContextFactory<C> contextFactory = environment.contextFactory();

        Command<C> command = registry.get(commandName);
        CommandContext<C> context = contextFactory.create(registry, args);
        command.execute(context);
    }

    /**
     * Parses a line of input into command tokens, allowing for grouping of tokens with double quotes {@code "}.
     *
     * @param text the input line
     * @return an array of tokens
     * @throws CommandParseException if quotes are unterminated
     */
    private static String[] parse(String text) throws CommandParseException {
        if (text == null || text.isBlank())
            return new String[0];

        ArrayList<String> args = new ArrayList<>();
        StringBuilder argBuilder = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '"') {
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

        if (!argBuilder.isEmpty())
            args.add(argBuilder.toString());

        return args.toArray(String[]::new);
    }

    /**
     * An internal record representing a registered command environment
     *
     * @param registry {@link CommandRegistry} for the environment
     * @param contextFactory {@link CommandContextFactory} for the environment
     * @param <C> type of {@link CommandContext} for the environment
     */
    private record CommandEnvironment<C extends CommandContext<C>>(
            CommandRegistry<C> registry,
            CommandContextFactory<C> contextFactory
    ) {}
}
