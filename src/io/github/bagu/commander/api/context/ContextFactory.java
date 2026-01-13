package io.github.bagu.commander.api.context;

import io.github.bagu.commander.api.argument.Arguments;
import io.github.bagu.commander.api.core.CommandGroup;
import io.github.bagu.commander.api.core.Environment;

/**
 * Factory for creating {@link Context} instances.
 *
 * <p>
 *     This functional interface is used by {@link Environment} to construct
 *     context objects after argument resolution. The use of the factory pattern allows for contexts to be
 *     created with additional dependencies beyond just the arguments.
 * </p>
 *
 * <p>
 *     Simple example using a constructor reference (no additional dependencies):
 *     <pre>
 *         {@code Environment<MyContext> environment = Environment.<MyContext>builder()
 *         .contextFactory(MyContext::new)
 *         .build();}
 *     </pre>
 *
 *     Complex example using additional dependencies:
 *     <pre>
 *         {@code ContextFactory<ServerContext> factory =
 *         (args, group) -> new ServerContext(args, group, server);
 *
 * Environment<ServerContext> environment = Environment.<ServerContext>builder()
 *         .contextFactory(factory)
 *         .build();}
 *     </pre>
 * </p>
 *
 * @param <T> the type of context to create
 *
 * @see Context
 * @see Environment
 */
@FunctionalInterface
public interface ContextFactory<T extends Context<T>> {
    /**
     * Creates a new context instance with the given arguments and command group.
     *
     * @param args the resolved command arguments
     * @param group the command group
     * @return a new context instance
     */
    T create(Arguments args, CommandGroup<T> group);
}
