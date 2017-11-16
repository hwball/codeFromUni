package shared.exceptions;

/**
 * An exception thrown when we the client-server handshake fails.
 */
public class HandshakeException extends Exception {

    /**
     * Constructs the exception with a given message.
     * @param message Message to store.
     */
    public HandshakeException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a given message and cause.
     * @param message Message to store.
     * @param cause Cause to store.
     */
    public HandshakeException(String message, Throwable cause) {
        super(message, cause);
    }
}
