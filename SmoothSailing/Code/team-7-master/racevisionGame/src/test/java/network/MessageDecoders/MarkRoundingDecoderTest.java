package network.MessageDecoders;

import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.Enums.MarkRoundingBoatStatusEnum;
import network.Messages.Enums.MarkRoundingSideEnum;
import network.Messages.Enums.MarkRoundingTypeEnum;
import network.Messages.MarkRounding;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for the MarkRounding encoder and decoder
 */
public class MarkRoundingDecoderTest {


    /**
     * Creates a MarkRounding message, encodes it, decodes it, and checks that the result matches the starting message.
     * @throws Exception if test fails.
     */
    @Test
    public void markRoundingEncodeDecodeTest() throws Exception {

        MarkRounding markRounding = new MarkRounding(
                MarkRounding.currentMessageVersionNumber,
                System.currentTimeMillis(),
                567,
                42,
                125,
                MarkRoundingBoatStatusEnum.RACING,
                MarkRoundingSideEnum.PORT,
                MarkRoundingTypeEnum.MARK,
                (byte)45
        );

        byte[] encodedMessage = RaceVisionByteEncoder.encode(markRounding);

        MarkRoundingDecoder markRoundingDecoder = new MarkRoundingDecoder();
        markRoundingDecoder.decode(encodedMessage);
        MarkRounding markRoundingDecoded = markRoundingDecoder.getMessage();

        compareMarkRoundingMessages(markRounding, markRoundingDecoded);

    }


    /**
     * Compares two MarkRounding messages to check that they are equal.
     * @param original The original MarkRounding message.
     * @param decoded The decoded MarkRounding message.
     */
    public static void compareMarkRoundingMessages(MarkRounding original, MarkRounding decoded) {


        assertEquals(original.getMessageVersionNumber(), decoded.getMessageVersionNumber());
        assertEquals(original.getTime(), decoded.getTime());
        assertEquals(original.getAckNum(), decoded.getAckNum());
        assertEquals(original.getRaceID(), decoded.getRaceID());
        assertEquals(original.getSourceID(), decoded.getSourceID());
        assertEquals(original.getBoatStatus(), decoded.getBoatStatus());
        assertEquals(original.getRoundingSide(), decoded.getRoundingSide());
        assertEquals(original.getMarkType(), decoded.getMarkType());
        assertEquals(original.getMarkID(), decoded.getMarkID());

    }


}
