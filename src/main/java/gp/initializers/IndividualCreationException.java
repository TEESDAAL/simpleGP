package gp.initializers;

/**
 * An error type which states that the creation of an individual failed for some reason
 */
public class IndividualCreationException extends RuntimeException {
    public IndividualCreationException(String message) {
        super(message);
    }

    public IndividualCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndividualCreationException(Throwable cause) {
        super(cause);
    }
}
