package network.MessageDecoders;

import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.Enums.YachtEventEnum;
import network.Messages.YachtEvent;
import org.junit.Before;
import org.junit.Test;

import static org.testng.Assert.*;

/**
 * Tests for the YachtEvent decoder and encoder
 */
public class YachtEventCodeDecoderTest {
    private YachtEvent decodedMessage;
    private YachtEvent originalMessage;

    @Before
    public void setUp() throws Exception {
        long timestamp = System.currentTimeMillis();

        originalMessage = new YachtEvent(
                timestamp,
                55,
                35,
                0,
                1,
                YachtEventEnum.COLLISION
        );

        byte[] encodedMessage = RaceVisionByteEncoder.encode(originalMessage);

        YachtEventCodeDecoder testDecoder = new YachtEventCodeDecoder();
        testDecoder.decode(encodedMessage);
        decodedMessage = testDecoder.getMessage();
    }

    @Test
    public void decodingEqualsOriginal() {
        assertEquals(originalMessage.getCurrentTime(), decodedMessage.getCurrentTime());
        assertEquals(originalMessage.getAckNum(), decodedMessage.getAckNum());
        assertEquals(originalMessage.getRaceID(), decodedMessage.getRaceID());
        assertEquals(originalMessage.getSourceID(), decodedMessage.getSourceID());
        assertEquals(originalMessage.getIncidentID(), decodedMessage.getIncidentID());
        assertEquals(originalMessage.getYachtEvent(), decodedMessage.getYachtEvent());
    }
}