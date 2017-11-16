package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.BoatAction;
import network.Messages.Enums.BoatActionEnum;
import network.Messages.HeartBeat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test for the HeartBeat encoder and decoder
 */
public class HeartBeatDecoderTest {


    /**
     * Encodes and decodes a given message.
     * @param message Message to encode/decode.
     * @return The decoded message.
     * @throws InvalidMessageException if the message cannot be encoded.
     */
    private HeartBeat encodeDecodeMessage(HeartBeat message) throws InvalidMessageException {

        //Encode.
        byte [] testEncodedMessage = RaceVisionByteEncoder.encode(message);

        //Decode.
        HeartBeatDecoder testDecoder = new HeartBeatDecoder();
        testDecoder.decode(testEncodedMessage);
        HeartBeat decodedMessage = testDecoder.getMessage();

        return decodedMessage;
    }


    /**
     * Tests if a heartbeat message with a given sequence number can be encoded and decoded correctly.
     * @param sequenceNumber The sequenceNumber to use.
     * @throws Exception if test fails.
     */
    private void heartBeatTest(long sequenceNumber) throws Exception {

        //Prepare message.
        HeartBeat beforeMessage = new HeartBeat(sequenceNumber);


        //Encode/decode it.
        HeartBeat afterMessage = encodeDecodeMessage(beforeMessage);


        //Compare.
        assertEquals(beforeMessage.getSequenceNumber(), afterMessage.getSequenceNumber());

    }


    /**
     * Tests if a heartbeat message with a sequence number of zero can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void heartBeatZeroTest() throws Exception {
        heartBeatTest(0);
    }

    /**
     * Tests if a heartbeat message with a sequence number of 1234512 can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void heartBeatNonZeroTest() throws Exception {
        heartBeatTest(1234512);
    }


}
