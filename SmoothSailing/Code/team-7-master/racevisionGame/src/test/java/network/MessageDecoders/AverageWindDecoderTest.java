package network.MessageDecoders;

import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.AverageWind;
import static org.junit.Assert.*;
import org.junit.Test;
import shared.model.Bearing;

import java.util.ArrayList;

/**
 * Test for the AverageWind encoder and decoder
 */
public class AverageWindDecoderTest {


    /**
     * Creates a AverageWind message, encodes it, decodes it, and checks that the result matches the starting message.
     * @throws Exception if test fails.
     */
    @Test
    public void averageWindEncodeDecodeTest() throws Exception {

        AverageWind averageWind = new AverageWind(
                AverageWind.currentMessageVersionNumber,
                System.currentTimeMillis(),
                3000,
                12.5,
                4050,
                12.6,
                3055,
                12.7,
                6051,
                13.37
        );

        byte[] encodedMessage = RaceVisionByteEncoder.encode(averageWind);

        AverageWindDecoder averageWindDecoder = new AverageWindDecoder();
        averageWindDecoder.decode(encodedMessage);
        AverageWind averageWindDecoded = averageWindDecoder.getMessage();

        compareAverageWindMessages(averageWind, averageWindDecoded);

    }


    /**
     * Compares two AverageWind messages to check that they are equal.
     * @param original The original AverageWind message.
     * @param decoded The decoded AverageWind message.
     */
    public static void compareAverageWindMessages(AverageWind original, AverageWind decoded) {


        assertEquals(original.getMessageVersionNumber(), decoded.getMessageVersionNumber());
        assertEquals(original.getTime(), decoded.getTime());

        assertEquals(original.getRawPeriod(), decoded.getRawPeriod(), 100);
        assertEquals(original.getRawSpeedKnots(), decoded.getRawSpeedKnots(), 0.01);

        assertEquals(original.getSampleTwoPeriod(), decoded.getSampleTwoPeriod(), 100);
        assertEquals(original.getSampleTwoSpeedKnots(), decoded.getSampleTwoSpeedKnots(), 0.01);

        assertEquals(original.getSampleThreePeriod(), decoded.getSampleThreePeriod(), 100);
        assertEquals(original.getSampleThreeSpeedKnots(), decoded.getSampleThreeSpeedKnots(), 0.01);

        assertEquals(original.getSampleFourPeriod(), decoded.getSampleFourPeriod(), 100);
        assertEquals(original.getSampleFourSpeedKnots(), decoded.getSampleFourSpeedKnots(), 0.01);


    }


}
