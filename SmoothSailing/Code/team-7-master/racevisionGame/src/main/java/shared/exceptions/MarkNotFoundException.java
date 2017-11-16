package shared.exceptions;

/**
 * An exception thrown when a specific mark cannot be found.
 */
public class MarkNotFoundException extends Exception {

    public MarkNotFoundException(String message) {
        super(message);
    }

    public MarkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
