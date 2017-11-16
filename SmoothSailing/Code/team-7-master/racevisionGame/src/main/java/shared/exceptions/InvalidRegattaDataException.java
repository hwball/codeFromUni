package shared.exceptions;

/**
 * An exception thrown when we cannot generate Regatta.xml and send an XML message, or we cannot parse a Regatta.xml file.
 */
public class InvalidRegattaDataException extends Exception {

    public InvalidRegattaDataException(String message) {
        super(message);
    }

    public InvalidRegattaDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
