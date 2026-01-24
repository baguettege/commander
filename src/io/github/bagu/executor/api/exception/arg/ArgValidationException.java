package io.github.bagu.executor.api.exception.arg;

import java.util.function.Predicate;

/**
 * Exception thrown when an argument or option fails validation.
 * <p>
 * This exception is thrown when a typed value does not pass the validator predicate defined in its
 * {@link io.github.bagu.executor.api.spec.ArgSpec} or {@link io.github.bagu.executor.api.spec.OptionSpec}.
 *
 * @see io.github.bagu.executor.api.spec.ArgSpec.Builder#validator(Predicate)
 * @see io.github.bagu.executor.api.spec.OptionSpec.Builder#validator(Predicate)
 */
public class ArgValidationException extends ArgException {
    /**
     * Constructs a new exception for the specified argument and value.
     *
     * @param argName the name of the argument or option that failed validation
     * @param value the string representation of the value that failed
     */
    public ArgValidationException(String argName, String value) {
        super("Invalid value for arg \"" + argName + "\": " + value);
    }
}
