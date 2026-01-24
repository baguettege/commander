package io.github.bagu.executor.api;

/**
 * Represents a command executor that can execute a string input.
 */
@FunctionalInterface
public interface CommandExecutor {
    /**
     * Executes a command string.
     *
     * @param string the command string
     */
    void execute(String string);
}
