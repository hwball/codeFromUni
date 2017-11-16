package network.Exceptions;

/**
 * Exception which is thrown when a message is read, but it is invalid in some way (CRC is wrong, sync bytes, etc...).
 */
public class InvalidMessageException extends Exception
{

    /**
     * Ctor.
     * @param message String message.
     */
    public InvalidMessageException(String message) {
        super(message);
    }

    /**
     * Ctor.
     * @param message String message.
     * @param cause Cause of the exception.
     */
    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
