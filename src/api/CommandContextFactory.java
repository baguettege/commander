package api;

/**
 * Factory interface for creating {@link CommandContext} instances.
 *
 * <p>
 *     This functional interface is used by {@link CommandReader} when executing a command. It provides a way
 *     to construct a {@link CommandContext} given a command registry and the arguments passed by the user.
 * </p>
 *
 * <p>
 *     Implementations can be provided as lambdas or method references.
 * </p>
 *
 * @param <C> the type of {@link CommandContext} created by this factory
 */

@FunctionalInterface
public interface CommandContextFactory<C extends CommandContext<C>> {

    /**
     * Creates a new {@link CommandContext} for a command execution.
     *
     * @param registry the registry containing all available commands for this context
     * @param args the arguments passed to the command
     * @return a new {@link CommandContext} instance
     */
    CommandContext<C> create(CommandRegistry<C> registry, String[] args);
}
