package api;

import exceptions.CommandRegisterException;
import exceptions.UnknownCommandException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * A registry that holds and manages {@link Command} instances.
 *
 * <p>
 *     This class provides a mapping from command names to their corresponding {@link Command} instances.
 *     It supports registration of commands, getting by names and listing of all registered commands.
 * </p>
 *
 * <p>
 *     This registry ensures that all command names are unique.
 * </p>
 *
 * <p>
 *     All commands must be stateless, and have a no-arg constructor for registration.
 * </p>
 *
 * @param <C> type of {@link CommandContext} that each {@link Command} takes
 */

public class CommandRegistry<C extends CommandContext<C>> {
    private final HashMap<String, Command<C>> registry = new HashMap<>();

    /**
     * Constructs a new {@link CommandRegistry} and registers the provided commands.
     *
     * <p>
     *     Commands are made from their no-arg constructors provided, and each command must be stateless.
     * </p>
     *
     * <p>
     *     If multiple commands with the same name are registered, a {@link CommandRegisterException} is thrown.
     * </p>
     *
     * @param constructors command no-arg constructors to create the commands for registry
     */
    @SafeVarargs
    public CommandRegistry(Supplier<Command<C>>... constructors) {
        for (Supplier<Command<C>> constructor : constructors) {
            Command<C> instance = constructor.get();
            String name = instance.name();

            if (registry.get(name) != null)
                throw new CommandRegisterException("Command with name '" + name + "' has already been registered");
            registry.put(name, instance);
        }
    }

    /**
     * Retrieves a {@link Command} instance from this registry by its name.
     *
     * @param name name of the command
     * @return corresponding {@link Command} instance
     * @throws UnknownCommandException if no command is registered with the given name
     */
    public final Command<C> get(String name) throws UnknownCommandException {
        Command<C> instance = registry.get(name);
        if (instance == null)
            throw new UnknownCommandException(name);
        return instance;
    }

    /**
     * Returns an unmodifiable collection of all registered commands.
     *
     * @return a collection of all {@link Command} instances in this registry
     */
    public final Collection<Command<C>> list() {
        return Collections.unmodifiableCollection(registry.values());
    }
}
