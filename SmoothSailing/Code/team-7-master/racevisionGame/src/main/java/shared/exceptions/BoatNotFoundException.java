package shared.exceptions;

/**
 * An exception thrown when a specific boat cannot be found.
 */
public class BoatNotFoundException extends Exception {

    public BoatNotFoundException(String message) {
        super(message);
    }

    public BoatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
