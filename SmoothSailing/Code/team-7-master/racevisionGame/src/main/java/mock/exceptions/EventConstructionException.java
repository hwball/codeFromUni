package mock.exceptions;

/**
 * An exception thrown when we cannot create an {@link mock.app.Event}.
 */
public class EventConstructionException extends Exception {

    /**
     * Constructs the exception with a given message.
     * @param message Message to store.
     */
    public EventConstructionException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a given message and cause.
     * @param message Message to store.
     * @param cause Cause to store.
     */
    public EventConstructionException(String message, Throwable cause) {
        super(message, cause);
    }
}
