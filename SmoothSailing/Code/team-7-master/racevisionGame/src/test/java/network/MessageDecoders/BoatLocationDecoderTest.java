package network.MessageDecoders;

import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.BoatLocation;
import network.Messages.Enums.BoatLocationDeviceEnum;
import org.junit.Assert;
import org.junit.Test;
import shared.model.Azimuth;
import shared.model.Bearing;


/**
 * Test for the BoatLocation encoder and decoder
 */
public class BoatLocationDecoderTest {


    /**
     * Creates a BoatLocation message, encodes it, decodes it, and checks that the result matches the starting message.
     * @throws Exception if test fails.
     */
    @Test
    public void boatLocationEncodeDecodeTest() throws Exception {

        //Create message.
        long time = System.currentTimeMillis();

        BoatLocation testMessage = new BoatLocation(
                BoatLocation.currentMessageVersionNumber,
                time,
                2,
                3,
                BoatLocationDeviceEnum.RacingYacht,
                180,
                -180,
                4,
                Bearing.fromDegrees(45),
                (short) 6,
                (short) 7,
                8,
                Bearing.fromDegrees(40),
                10,
                11,
                Azimuth.fromDegrees(35),
                13,
                Bearing.fromDegrees(80),
                Azimuth.fromDegrees(80),
                16,
                Bearing.fromDegrees(80),
                Azimuth.fromDegrees(22)  );

        //Encode.
        byte [] testEncodedMessage = RaceVisionByteEncoder.encode(testMessage);

        //Decode.
        BoatLocationDecoder testDecoder = new BoatLocationDecoder();
        testDecoder.decode(testEncodedMessage);
        BoatLocation decodedTest = testDecoder.getMessage();

        //Check if valid.
        Assert.assertEquals(testMessage.getMessageVersionNumber(), decodedTest.getMessageVersionNumber());
        Assert.assertEquals(testMessage.getTime(), decodedTest.getTime());
        Assert.assertEquals(testMessage.getSequenceNumber(), decodedTest.getSequenceNumber());
        Assert.assertEquals(testMessage.getDeviceType(), decodedTest.getDeviceType());
        Assert.assertEquals(testMessage.getLatitude(), decodedTest.getLatitude(), 0.01);
        Assert.assertEquals(testMessage.getLongitude(), decodedTest.getLongitude(), 0.01);
        Assert.assertEquals(testMessage.getAltitude(), decodedTest.getAltitude());
        Assert.assertEquals(testMessage.getHeading().degrees(), decodedTest.getHeading().degrees(), 0.01);
        Assert.assertEquals(testMessage.getPitch(), decodedTest.getPitch());
        Assert.assertEquals(testMessage.getRoll(), decodedTest.getRoll());
        Assert.assertEquals(testMessage.getBoatSpeedKnots(), decodedTest.getBoatSpeedKnots(), 0.01);

        Assert.assertEquals(testMessage.getBoatCOG().degrees(), decodedTest.getBoatCOG().degrees(), 0.01);
        Assert.assertEquals(testMessage.getBoatSOGKnots(), decodedTest.getBoatSOGKnots(), 0.01);
        Assert.assertEquals(testMessage.getApparentWindSpeedKnots(), decodedTest.getApparentWindSpeedKnots(), 0.01);
        Assert.assertEquals(testMessage.getTrueWindSpeedKnots(), decodedTest.getTrueWindSpeedKnots(), 0.01);
        Assert.assertEquals(testMessage.getTrueWindDirection().degrees(), decodedTest.getTrueWindDirection().degrees(), 0.01);
        Assert.assertEquals(testMessage.getTrueWindAngle().degrees(), decodedTest.getTrueWindAngle().degrees(), 0.01);
        Assert.assertEquals(testMessage.getCurrentDriftKnots(), decodedTest.getCurrentDriftKnots(), 0.01);
        Assert.assertEquals(testMessage.getCurrentSet().degrees(), decodedTest.getCurrentSet().degrees(), 0.01);
        Assert.assertEquals(testMessage.getRudderAngle().degrees(), decodedTest.getRudderAngle().degrees(), 0.01);
    }
}
