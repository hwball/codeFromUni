package mock.exceptions;

/**
 * An exception thrown when we cannot parse a polar data file.
 */
public class InvalidPolarFileException extends RuntimeException {

    /**
     * Constructs the exception with a given message.
     * @param message Message to store.
     */
    public InvalidPolarFileException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a given message and cause.
     * @param message Message to store.
     * @param cause Cause to store.
     */
    public InvalidPolarFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
