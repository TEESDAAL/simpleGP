package gp.initializers;

/**
 * An error type stating that individual creation failed.
 */
public class IndividualCreationException extends RuntimeException {
    /**
     * Creates an exception with a message.
     * @param message The error message
     */
    public IndividualCreationException(final String message) {
        super(message);
    }

    /**
     * Creates an exception with a message and cause.
     * @param message The error message
     * @param cause The underlying cause
     */
    public IndividualCreationException(
            final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an exception with a cause.
     * @param cause The underlying cause
     */
    public IndividualCreationException(final Throwable cause) {
        super(cause);
    }
}
