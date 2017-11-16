package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.BoatState;
import org.junit.Before;
import org.junit.Test;

import static org.testng.Assert.*;

/**
 * Test for the {@link network.Messages.BoatState} encoder and decoder.
 */
public class BoatStateDecoderTest {
    private BoatState originalMessage;
    private BoatState decodedMessage;

    @Before
    public void setUp() throws InvalidMessageException {
        originalMessage = new BoatState(0, 100);

        byte[] encodedMessage = RaceVisionByteEncoder.encode(originalMessage);
        BoatStateDecoder decoder = new BoatStateDecoder();
        decoder.decode(encodedMessage);
        decodedMessage = decoder.getMessage();
    }

    @Test
    public void decodingEqualsOriginal() {
        assertEquals(originalMessage.getSourceID(), decodedMessage.getSourceID());
        assertEquals(originalMessage.getBoatHealth(), decodedMessage.getBoatHealth());
    }
}