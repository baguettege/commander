package io.github.bagu.commander.api.core;

import io.github.bagu.commander.api.context.Context;
import io.github.bagu.commander.api.definition.CommandDefinition;
import io.github.bagu.commander.api.exception.command.CommandAlreadyExistsException;
import io.github.bagu.commander.api.exception.command.CommandNotFoundException;

import java.util.*;

/**
 * A container for command definitions within an environment.
 *
 * <p>
 *     A command group encapsulates the command registry for an {@link Environment},
 *     providing controlled access to command definitions. It is exposed within {@link Context} to allow commands,
 *     such as {@code help} commands, to view available commands at runtime.
 * </p>
 *
 * <p>
 *     The group is immutable after construction and provides two access patterns:<br>
 *     - {@link #get(String)} retrieves a specific command by name<br>
 *     - {@link #getAll()} retrieves all commands for listing/help display
 * </p>
 *
 * @param <T> the type of context for commands in this group
 *
 * @see Environment
 * @see Context
 * @see CommandDefinition
 */
public final class CommandGroup<T extends Context<T>> {
    private final Map<String, CommandDefinition<T>> commands;

    /**
     * Creates a new command group with the specified map of command definitions.
     *
     * @param commands command definitions to contain
     */
    private CommandGroup(Map<String, CommandDefinition<T>> commands) {
        this.commands = Map.copyOf(commands);
    }

    /**
     * Retrieves a command definition by name.
     *
     * <p>
     *     This method returns an {@link Optional} to allow callers to handle missing commands gracefully.
     *     The {@link Environment} uses this method and throws {@link CommandNotFoundException}
     *     if the command is not found.
     * </p>
     *
     * @param name the command name
     * @return an Optional containing the command definition, or empty if not found
     */
    public Optional<CommandDefinition<T>> get(String name) {
        CommandDefinition<T> command = commands.get(name);
        return Optional.ofNullable(command);
    }

    /**
     * Retrieves all command definitions in this group.
     *
     * <p>
     *     This method is mainly used for displaying help information, such as listing available commands.
     *     The returned list is immutable and the order of insertion is guaranteed.
     * </p>
     *
     * @return an immutable list of all command definitions
     */
    public List<CommandDefinition<T>> getAll() {
        return List.copyOf(commands.values());
    }

    /**
     * Creates a new builder for constructing a command group.
     *
     * @return a new builder instance
     * @param <T> the type of context for the command group
     */
    public static <T extends Context<T>> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for constructing {@link CommandGroup} instances.
     *
     * <p>
     *     Commands are added via the {@link #command(CommandDefinition)} method. Each command name
     *     must be unique within the group.
     * </p>
     *
     * <p>
     *     Order of insertion for commands is guaranteed.
     * </p>
     *
     * @param <T> the type of context for commands in the group being built
     */
    public static final class Builder<T extends Context<T>> {
        private final Map<String, CommandDefinition<T>> commands = new LinkedHashMap<>();

        /**
         * Builds the command group.
         *
         * @return a new immutable command group instance
         */
        public CommandGroup<T> build() {
            return new CommandGroup<>(commands);
        }

        /**
         * Adds a command to this group.
         *
         * <p>
         *     The command's name must be unique within the group. Duplicate names will throw a
         *     {@link CommandAlreadyExistsException}.
         * </p>
         *
         * <p>
         *     The order of insertion is guaranteed.
         * </p>
         *
         * @param command the command definition to add
         * @return this builder
         * @throws CommandAlreadyExistsException if a command with this name already exists
         */
        public Builder<T> command(CommandDefinition<T> command) throws CommandAlreadyExistsException {
            String name = command.name();
            if (commands.containsKey(name))
                throw new CommandAlreadyExistsException(name);
            commands.put(name, command);
            return this;
        }
    }
}
