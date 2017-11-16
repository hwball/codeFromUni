package shared.exceptions;

/**
 * An exception thrown when an XMLReader cannot be constructed for some reason.
 */
public class XMLReaderException extends Exception {

    public XMLReaderException(String message) {
        super(message);
    }

    public XMLReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
