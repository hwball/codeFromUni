package network.MessageDecoders;

import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.Enums.RaceStartTypeEnum;
import network.Messages.RaceStartStatus;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the RaceStartStatus encoder and decoder.
 */
public class RaceStartStatusDecoderTest {


    /**
     * Tests if a RaceStartStatus message can be encoded and decoded correctly.
     * @throws Exception Thrown when an error occurs.
     */
    @Test
    public void raceStartStatusEncodeDecodeTest() throws Exception {

        long timestamp = System.currentTimeMillis();

        long startTime = System.currentTimeMillis() + 10 * 1000;

        RaceStartStatus raceStartStatus = new RaceStartStatus(
                RaceStartStatus.currentMessageVersionNumber,
                timestamp,
                55,
                startTime,
                35,
                RaceStartTypeEnum.SET_RACE_START
        );

        byte[] encodedRaceStartStatus = RaceVisionByteEncoder.encode(raceStartStatus);

        RaceStartStatusDecoder testDecoder = new RaceStartStatusDecoder();
        testDecoder.decode(encodedRaceStartStatus);
        RaceStartStatus raceStartStatusDecoded = testDecoder.getMessage();

        compareRaceStartStatusMessages(raceStartStatus, raceStartStatusDecoded);

    }

    /**
     * Compares two RaceStartStatus messages to check that they are the same.
     * @param original The original message.
     * @param decoded The decoded message.
     */
    public static void compareRaceStartStatusMessages(RaceStartStatus original, RaceStartStatus decoded) {

        Assert.assertEquals(original.getMessageVersionNumber(), decoded.getMessageVersionNumber());
        Assert.assertEquals(original.getTimestamp(), decoded.getTimestamp());
        Assert.assertEquals(original.getAckNum(), decoded.getAckNum());
        Assert.assertEquals(original.getRaceStartTime(), decoded.getRaceStartTime());
        Assert.assertEquals(original.getRaceID(), decoded.getRaceID());
        Assert.assertEquals(original.getNotificationType(), decoded.getNotificationType());

    }


}
