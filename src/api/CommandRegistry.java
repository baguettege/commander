package api;

import exceptions.CommandRegisterException;
import exceptions.UnknownCommandException;

import java.util.*;
import java.util.function.Supplier;

/**
 * Represents a registry of commands.
 *
 * @param <T> concrete implementation of {@link CommandContext}
 */

public class CommandRegistry<T extends CommandContext> {

    /**
     * Maps command names to their instances.
     */
    private final LinkedHashMap<String, Command<T>> registry = new LinkedHashMap<>();

    /**
     * Constructs a registry with specified commands.
     * <p>
     *     May throw {@link CommandRegisterException} if a command with the same name has already been registered.
     * </p>
     * <p>
     *
     * </p>
     *
     * @param constructors var args of the no-arg constructors for the commands
     */
    @SafeVarargs
    public CommandRegistry(Supplier<Command<T>>... constructors) {
        for (Supplier<Command<T>> constructor : constructors)
            register(constructor);
    }

    /**
     * Registers a command by instantiating it and mapping the constructed instance to its name.
     * <p>
     *     May throw {@link CommandRegisterException} if a command with the same name has already been registered.
     * </p>
     *
     * @param constructor no-arg constructor of the command
     */
    private void register(Supplier<Command<T>> constructor) {
        Command<T> instance = constructor.get();
        String name = instance.getName();

        if (registry.get(name) != null)
            throw new CommandRegisterException("Command already registered with name: " + name);
        registry.put(name, instance);
    }

    /**
     * Returns the registered instance for a specified command name.
     * <p>
     *     May throw {@link UnknownCommandException} if the command name is unregistered.
     * </p>
     *
     * @param commandName name of the command
     * @return the instance of the command
     */
    public Command<T> get(String commandName) {
        Command<T> instance = registry.get(commandName);
        if (instance == null)
            throw new UnknownCommandException(commandName);
        return instance;
    }

    /**
     * Returns the registered commands in an unmodifiable {@link Collection}.
     *
     * @return registered commands
     */
    public Collection<Command<T>> list() {
        return Collections.unmodifiableCollection(registry.values());
    }
}
