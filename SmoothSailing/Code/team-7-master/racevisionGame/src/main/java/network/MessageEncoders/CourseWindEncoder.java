package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.CourseWind;
import shared.model.Bearing;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static network.Utils.AC35UnitConverter.*;
import static network.Utils.ByteConverter.*;
import static network.Utils.ByteConverter.bytesToInt;

/**
 * This encoder can encode a {@link CourseWind} message.
 */
public class CourseWindEncoder {


    /**
     * Constructor.
     */
    public CourseWindEncoder() {
    }


    /**
     * Encodes a given CourseWind message.
     * @param message The message to encode.
     * @return The encoded message.
     * @throws InvalidMessageException Thrown if the message is invalid in some way, or cannot be encoded.
     */
    public byte[] encode(CourseWind message) throws InvalidMessageException {

        try {

            CourseWind courseWind = message;


            //CourseWind is 20 bytes.
            ByteBuffer courseWindBuffer = ByteBuffer.allocate(20);


            byte[] windId = intToBytes(courseWind.getID(), 1);

            byte[] timeBytes = longToBytes(courseWind.getTime(), 6);

            byte[] raceIDBytes = intToBytes(courseWind.getRaceID(), 4);

            int windDirectionInt = packHeading(courseWind.getWindDirection().degrees());
            byte[] windDirectionBytes = intToBytes(windDirectionInt, 2);

            int windSpeedInt = packKnotsToMMperSec(courseWind.getWindSpeedKnots());
            byte[] windSpeedBytes = intToBytes(windSpeedInt, 2);

            int bestUpwindAngleInt = packHeading(courseWind.getBestUpwindAngle().degrees());
            byte[] bestUpwindAngleBytes = intToBytes(bestUpwindAngleInt, 2);

            int bestDownwindAngleInt = packHeading(courseWind.getBestDownwindAngle().degrees());
            byte[] bestDownwindAngleBytes = intToBytes(bestDownwindAngleInt, 2);

            byte[] flags = intToBytes(courseWind.getFlags(), 1);

            courseWindBuffer.put(windId);
            courseWindBuffer.put(timeBytes);
            courseWindBuffer.put(raceIDBytes);
            courseWindBuffer.put(windDirectionBytes);
            courseWindBuffer.put(windSpeedBytes);
            courseWindBuffer.put(bestUpwindAngleBytes);
            courseWindBuffer.put(bestDownwindAngleBytes);
            courseWindBuffer.put(flags);

            return courseWindBuffer.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode CourseWind message.", e);
        }

    }
}
