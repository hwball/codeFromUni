package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.CourseWindEncoder;
import network.Messages.BoatStatus;
import network.Messages.CourseWind;
import network.Messages.Enums.BoatStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import shared.model.Bearing;

/**
 * Test for the CourseWind encoder and decoder
 */
public class CourseWindDecoderTest {


    /**
     * Creates a CourseWind message, encodes it, decodes it, and checks that the result matches the starting message.
     * @throws Exception if test fails.
     */
    @Test
    public void courseWindEncodeDecodeTest() throws Exception {

        long time = System.currentTimeMillis();
        CourseWind courseWind = new CourseWind(
                1,
                time,
                2,
                Bearing.fromDegrees(45),
                4,
                Bearing.fromDegrees(70),
                Bearing.fromDegrees(160),
                (byte) 0x13   );


        CourseWind courseWindDecoded = encodeDecodeCourseWind(courseWind);

        compareCourseWindMessages(courseWind, courseWindDecoded);

    }

    /**
     * Encodes and decodes a CourseWind, and returns it.
     * @param courseWind The CourseWind to encode and decode.
     * @return The decoded CourseWind.
     * @throws InvalidMessageException Thrown if message cannot be encoded or decoded.
     */
    private static CourseWind encodeDecodeCourseWind(CourseWind courseWind) throws InvalidMessageException {

        CourseWindEncoder courseWindEncoder = new CourseWindEncoder();
        byte[] courseWindEncoded = courseWindEncoder.encode(courseWind);

        CourseWindDecoder courseWindDecoder = new CourseWindDecoder();
        CourseWind courseWindDecoded = courseWindDecoder.decode(courseWindEncoded);

        return courseWindDecoded;
    }


    /**
     * Compares two CourseWind messages to check that they are equal.
     * @param original The original CourseWind message.
     * @param decoded The decoded CourseWind message.
     */
    public static void compareCourseWindMessages(CourseWind original, CourseWind decoded) {

        Assert.assertEquals(original.getID(), decoded.getID());
        Assert.assertEquals(original.getTime(), decoded.getTime());
        Assert.assertEquals(original.getRaceID(), decoded.getRaceID());
        Assert.assertEquals(original.getWindDirection().degrees(), decoded.getWindDirection().degrees(), 0.01);
        Assert.assertEquals(original.getWindSpeedKnots(), decoded.getWindSpeedKnots(), 0.01);
        Assert.assertEquals(original.getBestUpwindAngle().degrees(), decoded.getBestUpwindAngle().degrees(), 0.01);
        Assert.assertEquals(original.getBestDownwindAngle().degrees(), decoded.getBestDownwindAngle().degrees(), 0.01);
        Assert.assertEquals(original.getFlags(), decoded.getFlags());

    }

}
