package seng202.group6.persistence;

/**
 * Exception class for file format
 */
public class FileFormatException extends Exception {

    /**
     * Constructor for FileFormatException without a message.
     */
    public FileFormatException() {
        super();
    }


    /**
     * Constructor for FileFormatException with a message.
     *
     * @param message the message shown when this error is thrown
     */
    public FileFormatException(String message) {
        super(message);
    }


    /**
     * Constructor for FileFormatException with a message and a cause.
     *
     * @param message the message shown when this error is thrown
     * @param cause   the cause of the error
     */
    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
