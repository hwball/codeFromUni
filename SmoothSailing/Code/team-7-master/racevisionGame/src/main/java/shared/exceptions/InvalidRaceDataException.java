package shared.exceptions;

/**
 * Exception thrown when we cannot generate Race.xml data, and send an XML message, or we cannot parse a Race.xml file.
 */
public class InvalidRaceDataException extends Exception {

    public InvalidRaceDataException(String message) {
        super(message);
    }

    public InvalidRaceDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
