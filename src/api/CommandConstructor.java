package api;

/**
 * Represents a no-arg constructor for a {@link Command}.
 *
 * @param <C> type of {@link CommandContext} for the command
 */

@FunctionalInterface
public interface CommandConstructor<C extends CommandContext<C>> {
    Command<C> create();
}
