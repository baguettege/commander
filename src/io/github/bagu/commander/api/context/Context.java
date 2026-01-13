package io.github.bagu.commander.api.context;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.api.core.CommandGroup;
import io.github.bagu.commander.api.core.Environment;

import java.util.Objects;

/**
 * Base class for command execution contexts.
 *
 * <p>
 *     A context provides access to resolved command arguments and command groups and should be extended to
 *     include additional dependencies required by command handlers.
 * </p>
 *
 * <p>
 *     Each {@link Environment} is bound to a specific context type, ensuring type
 *     safety across all commands within that environment.
 * </p>
 *
 * <p>
 *     Subclasses can add specific objects or resources that can be used by the command:
 *     <pre>
 *         {@code public class ServerContext extends Context<ServerContext> {
 *     private final Server server;
 *
 *     public ServerContext(Arguments args, CommandGroup<ServerContext> group, Server server) {
 *         super(args, group);
 *         this.server = server;
 *     }
 *
 *     public Server server() {
 *         return server;
 *     }
 * }}
 *     </pre>
 * </p>
 *
 * @see Arguments
 * @see ContextFactory
 * @see Environment
 */
public abstract class Context<T extends Context<T>> {
    private final Arguments args;
    private final CommandGroup<T> group;

    /**
     * Creates a new context with the specified arguments and command group.
     *
     * @param args the resolved command arguments
     * @param group the command group that the environment using this context is using
     */
    protected Context(Arguments args, CommandGroup<T> group) {
        this.args = Objects.requireNonNull(args);
        this.group = Objects.requireNonNull(group);
    }

    /**
     * Returns the resolved command arguments.
     *
     * <p>
     *     This provides access to the positional arguments, options and flags that were
     *     passed to the command, converted to the correct type.
     * </p>
     *
     * @return the command arguments
     */
    public Arguments args() {
        return args;
    }

    /**
     * Returns the command group used by the environment using this context.
     *
     * <p>
     *     This provides access to currently registered commands, allowing for viewing commands at runtime,
     *     such as {@code help} commands.
     * </p>
     *
     * @return the command group
     */
    public CommandGroup<T> group() {
        return group;
    }
}
