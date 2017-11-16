package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.CourseWind;
import shared.model.Bearing;

import java.util.Arrays;

import static network.Utils.AC35UnitConverter.*;
import static network.Utils.ByteConverter.*;


/**
 * Decodes {@link CourseWind} messages.
 */
public class CourseWindDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private CourseWind message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public CourseWindDecoder() {
    }


    /**
     * Decodes the contained message.
     * @param encodedMessage The message to decode.
     * @return The decoded message.
     * @throws InvalidMessageException Thrown if the encoded message is invalid in some way, or cannot be decoded.
     */
    public CourseWind decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte[] windId = Arrays.copyOfRange(encodedMessage, 0, 1);

            byte[] timeBytes = Arrays.copyOfRange(encodedMessage, 1, 7);
            long time = bytesToLong(timeBytes);

            byte[] raceIDBytes = Arrays.copyOfRange(encodedMessage, 7, 11);
            int raceIDInt = bytesToInt(raceIDBytes);

            byte[] windDirectionBytes = Arrays.copyOfRange(encodedMessage, 11, 13);
            int windDirectionInt = bytesToInt(windDirectionBytes);
            Bearing windDirection = Bearing.fromDegrees(unpackHeading(windDirectionInt));

            byte[] windSpeedBytes = Arrays.copyOfRange(encodedMessage, 13, 15);
            int windSpeedInt = bytesToInt(windSpeedBytes);
            double windSpeedKnots = unpackMMperSecToKnots(windSpeedInt);

            byte[] bestUpwindAngleBytes = Arrays.copyOfRange(encodedMessage, 15, 17);
            int bestUpwindAngleInt = bytesToInt(bestUpwindAngleBytes);
            Bearing bestUpwindAngle = Bearing.fromDegrees(unpackHeading(bestUpwindAngleInt));

            byte[] bestDownwindAngleBytes = Arrays.copyOfRange(encodedMessage, 17, 19);
            int bestDownwindAngleInt = bytesToInt(bestDownwindAngleBytes);
            Bearing bestDownwindAngle = Bearing.fromDegrees(unpackHeading(bestDownwindAngleInt));

            byte[] flags = Arrays.copyOfRange(encodedMessage, 19, 20);


            message = new CourseWind(
                    windId[0],
                    time,
                    raceIDInt,
                    windDirection,
                    windSpeedKnots,
                    bestUpwindAngle,
                    bestDownwindAngle,
                    flags[0]);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode CourseWind message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public CourseWind getMessage() {
        return message;
    }

}
