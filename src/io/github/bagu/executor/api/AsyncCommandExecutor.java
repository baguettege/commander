package io.github.bagu.executor.api;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * A command executor that executes commands asynchronously using a thread pool.
 * <p>
 * This executor wraps another {@link CommandExecutor} and delegates command execution to a fixed thread
 * pool. Any exceptions thrown during execution are handled by the configured {@link ExceptionHandler}.
 * <p>
 * Instances must be closed when no longer needed to properly shut down the thread pool.
 *
 * @see CommandExecutor
 * @see ExceptionHandler
 */
public final class AsyncCommandExecutor implements AutoCloseable, CommandExecutor {
    private final CommandExecutor executor;
    private final ExecutorService service;
    private final ExceptionHandler exceptionHandler;

    private AsyncCommandExecutor(
            CommandExecutor executor,
            ExceptionHandler exceptionHandler,
            int nThreads
    ) {
        this.executor = executor;
        this.service = Executors.newFixedThreadPool(nThreads);
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Executes a command string asynchronously.
     *
     * @param string the command string
     * @throws NullPointerException if the string is null
     * @throws IllegalStateException if this executor has been closed
     */
    @Override
    public void execute(String string) {
        Objects.requireNonNull(string);
        try {
            service.execute(() -> {
                try {
                    executor.execute(string);
                } catch (Exception e) {
                    exceptionHandler.handle(e);
                }
            });
        } catch (RejectedExecutionException e) {
            throw new IllegalStateException("Async runtime is closed", e);
        }
    }

    /**
     * Initiates an orderly shutdown of the thread pool. Previously submitted tasks are executed,
     * but no new tasks will be accepted.
     */
    @Override
    public void close() {
        service.shutdown();
    }

    /**
     * Attempts to stop all actively executing tasks and halts the processing of waiting tasks.
     */
    public void closeNow() {
        service.shutdownNow();
    }

    /**
     * Returns whether this executor has been shut down.
     *
     * @return {@code true} if shutdown has been initiated, otherwise {@code false}
     */
    public boolean isClosed() {
        return service.isShutdown();
    }

    /**
     * Returns whether all tasks have completed following shutdown.
     *
     * @return {@code true} if all tasks have completed after shutdown, otherwise {@code false}
     */
    public boolean isTerminated() {
        return service.isTerminated();
    }

    @Override
    public String toString() {
        return "AsyncCommandExecutor{" +
                "executor=" + executor +
                ", service=" + service +
                ", exceptionHandler=" + exceptionHandler +
                "}";
    }

    /**
     * Returns a new builder for creating {@link AsyncCommandExecutor} instances.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating {@link AsyncCommandExecutor} instances.
     */
    public static final class Builder {
        private Builder() {}

        private CommandExecutor executor;
        private ExceptionHandler exceptionHandler;
        private Integer nThreads;

        /**
         * Builds the {@link AsyncCommandExecutor} instance.
         *
         * @return a new {@link AsyncCommandExecutor}
         * @throws NullPointerException if any required field null
         */
        public AsyncCommandExecutor build() {
            Objects.requireNonNull(executor);
            Objects.requireNonNull(exceptionHandler);
            Objects.requireNonNull(nThreads);

            return new AsyncCommandExecutor(
                    executor,
                    exceptionHandler,
                    nThreads
            );
        }

        /**
         * Sets the underlying command executor to delegate to.
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
         * @throws NullPointerException if the handler is null
         */
        public Builder exceptionHandler(ExceptionHandler exceptionHandler) {
            this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
            return this;
        }

        /**
         * Sets the number of threads in the thread pool.
         *
         * @param nThreads the thread count
         * @return this builder
         * @throws IllegalArgumentException if the count is {@code < 1}
         */
        public Builder nThreads(int nThreads) {
            if (nThreads < 1) {
                throw new IllegalArgumentException("Thread count < 1: " + nThreads);
            }

            this.nThreads = nThreads;
            return this;
        }
    }
}
