package api;

/**
 * Represents the context used by {@link Command}s during execution.
 *
 * <p>
 *     A {@link CommandContext} provides access to the command arguments and registry of available commands. It is
 *     intended to be subclassed to allow environment-specific data to be accessible.
 * </p>
 *
 * <p>
 *     This class is generic over its own type to allow type-safety for the {@link CommandRegistry} it is provided
 *     with in its constructor.
 * </p>
 *
 * @param <C> the type of {@link CommandContext} subclass
 */

public abstract class CommandContext<C extends CommandContext<C>> {
    private final CommandRegistry<C> registry;
    private final String[] args;

    /**
     * Constructs a new {@link CommandContext} with a given registry and arguments
     *
     * @param registry the registry containing all available commands for this context
     * @param args the arguments passed into the command
     */
    protected CommandContext(CommandRegistry<C> registry, String[] args) {
        this.registry = registry;
        this.args = args;
    }

    /**
     * Returns the registry containing all available commands for this context.
     *
     * @return the command registry
     */
    public CommandRegistry<C> registry() {
        return registry;
    }

    /**
     * Returns a copy of the arguments passed into the command.
     *
     * <p>
     *     Defensive copying is used to prevent external modification of the array.
     * </p>
     *
     * @return copy of the command arguments
     */
    public String[] args() {
        return args.clone();
    }
}
