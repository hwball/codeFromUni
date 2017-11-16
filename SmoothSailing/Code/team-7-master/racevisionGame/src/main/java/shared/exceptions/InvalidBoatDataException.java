package shared.exceptions;

/**
 * An exception thrown when we cannot generate Boats.xml and send an XML message, or we cannot parse a Boats.xml file.
 */
public class InvalidBoatDataException extends Exception {

    public InvalidBoatDataException(String message) {
        super(message);
    }

    public InvalidBoatDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
