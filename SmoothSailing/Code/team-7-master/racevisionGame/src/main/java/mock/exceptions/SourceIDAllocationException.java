package mock.exceptions;

/**
 * An exception thrown when we cannot allocate a source ID.
 */
public class SourceIDAllocationException extends Exception {

    /**
     * Constructs the exception with a given message.
     * @param message Message to store.
     */
    public SourceIDAllocationException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a given message and cause.
     * @param message Message to store.
     * @param cause Cause to store.
     */
    public SourceIDAllocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
