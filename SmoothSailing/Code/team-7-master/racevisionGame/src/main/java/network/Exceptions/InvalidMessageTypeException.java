package network.Exceptions;


/**
 * An exception thrown when we encounter a message type that isn't recognised.
 */
public class InvalidMessageTypeException extends Exception {


    public InvalidMessageTypeException(String message) {
        super(message);
    }

    public InvalidMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
