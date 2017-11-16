package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.Enums.JoinAcceptanceEnum;
import network.Messages.JoinAcceptance;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test for the {@link network.Messages.JoinAcceptance} encoder and decoder
 */
public class JoinAcceptanceDecoderTest {


    /**
     * Encodes and decodes a given message.
     * @param message Message to encode/decode.
     * @return The decoded message.
     * @throws InvalidMessageException If the message cannot be encoded.
     */
    private JoinAcceptance encodeDecodeMessage(JoinAcceptance message) throws InvalidMessageException {

        //Encode.
        byte [] testEncodedMessage = RaceVisionByteEncoder.encode(message);

        //Decode.
        JoinAcceptanceDecoder testDecoder = new JoinAcceptanceDecoder();
        testDecoder.decode(testEncodedMessage);
        JoinAcceptance decodedMessage = testDecoder.getMessage();

        return decodedMessage;
    }


    /**
     * Tests if a specific acceptance type message can be encoded and decoded correctly.
     * @param type The type of acceptance response.
     * @param sourceID The source ID to assign.
     * @throws Exception if test fails.
     */
    private void responseTypeTest(JoinAcceptanceEnum type, int sourceID) throws Exception {

        //Prepare message.
        JoinAcceptance beforeMessage = new JoinAcceptance(type, sourceID);


        //Encode/decode it.
        JoinAcceptance afterMessage = encodeDecodeMessage(beforeMessage);


        //Compare.
        assertEquals(beforeMessage.getAcceptanceType().getValue(), afterMessage.getAcceptanceType().getValue());
        assertEquals(beforeMessage.getSourceID(), afterMessage.getSourceID());

    }


    /**
     * Tests if a join success message, with a source ID, can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void joinSuccessSourceIDTest() throws Exception {
        responseTypeTest(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, 12345);
    }

    /**
     * Tests if a join success message, with no source ID, can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void joinSuccessNoSourceIDTest() throws Exception {
        responseTypeTest(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, 0);
    }


    /**
     * Tests if a server full message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void serverFullTest() throws Exception {
        responseTypeTest(JoinAcceptanceEnum.SERVER_FULL, 0);
    }



}
