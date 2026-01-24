package io.github.bagu.executor.api;

import java.util.Objects;
import java.util.Scanner;

/**
 * An interactive command shell that continuously reads input and executes commands.
 * <p>
 * The shell runs on a daemon thread and reads lines from a {@link Scanner}, executing each
 * line through the configured {@link CommandExecutor}. Any exceptions during execution are handled
 * by the {@link ExceptionHandler}.
 * <p>
 * Instances must be explicitly started via {@link #start()} and can be closed to stop accepting input.
 *
 * @see CommandExecutor
 * @see ExceptionHandler
 */
public final class CommandShell implements AutoCloseable {
    private final Scanner scanner;
    private final CommandExecutor executor;
    private final ExceptionHandler exceptionHandler;

    private volatile Thread workerThread;
    private volatile boolean closed = false;

    private CommandShell(
            Scanner scanner,
            CommandExecutor executor,
            ExceptionHandler exceptionHandler
    ) {
        this.scanner = Objects.requireNonNull(scanner);
        this.executor = Objects.requireNonNull(executor);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
    }

    private void loop() {
        while (!closed) {
            try {
                String input = scanner.nextLine();
                executor.execute(input);
            } catch (IllegalStateException ignored) {
                break; // closing
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        }
    }

    /**
     * Starts the command shell on a daemon thread.
     * <p>
     * The shell will continuously read input until {@link #close} is called.
     *
     * @throws IllegalStateException if the shell has already been started
     */
    public void start() {
        synchronized (this) {
            if (workerThread != null) {
                throw new IllegalStateException("Command shell has already been started");
            }

            workerThread = new Thread(
                    this::loop,
                    "Command-shell-executor-" + System.identityHashCode(executor)
            );
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    /**
     * Closes the command shell and stops accepting input.
     */
    @Override
    public void close() {
        synchronized (this) {
            if (closed) return;
            closed = true;
            scanner.close();
        }
    }

    /**
     * Returns whether this shell has been closed.
     *
     * @return {@code true} if closed, otherwise {@code false}
     */
    public boolean isClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return "CommandShell{" +
                "scanner=" + scanner +
                ", executor=" + executor +
                ", exceptionHandler=" + exceptionHandler +
                ", closed=" + closed +
                "}";
    }

    /**
     * Returns a new builder for creating {@link CommandShell} instances.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating {@link CommandShell} instances.
     */
    public static final class Builder {
        private Builder() {}

        private Scanner scanner;
        private CommandExecutor executor;
        private ExceptionHandler exceptionHandler;

        /**
         * Builds the {@link CommandShell}.
         * <p>
         * Note that the user retains ownership of the {@link Scanner} used, meaning if this method
         * throws, the scanner will remain open.
         *
         * @return a new {@link CommandShell}
         * @throws NullPointerException if any parameter is null
         */
        public CommandShell build() {
            Objects.requireNonNull(scanner);
            Objects.requireNonNull(executor);
            Objects.requireNonNull(exceptionHandler);

            return new CommandShell(
                    scanner,
                    executor,
                    exceptionHandler
            );
        }

        /**
         * Sets the scanner for reading input.
         *
         * @param scanner the scanner
         * @return this builder
         * @throws NullPointerException if the scanner is null
         */
        public Builder scanner(Scanner scanner) {
            this.scanner = Objects.requireNonNull(scanner);
            return this;
        }

        /**
         * Sets the command executor for executing commands.
         *
         * @param executor the command executor
         * @return this builder
         * @throws NullPointerException if the executor is null
         */
        public Builder executor(CommandExecutor executor) {
            this.executor = Objects.requireNonNull(executor);
            return this;
        }

        /**
         * Sets the exception handler for handling execution errors.
         *
         * @param exceptionHandler the exception handler
         * @return this builder
         */
        public Builder exceptionHandler(ExceptionHandler exceptionHandler) {
            this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
            return this;
        }
    }
}
