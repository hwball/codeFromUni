package mock.exceptions;

/**
 * An exception thrown when we cannot create a command for some reasn (e.g., uknown action type).
 */
public class CommandConstructionException extends Exception {

    /**
     * Constructs the exception with a given message.
     * @param message Message to store.
     */
    public CommandConstructionException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a given message and cause.
     * @param message Message to store.
     * @param cause Cause to store.
     */
    public CommandConstructionException(String message, Throwable cause) {
        super(message, cause);
    }
}
