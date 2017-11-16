package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.Enums.RequestToJoinEnum;
import network.Messages.RequestToJoin;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test for the RequestToJoin encoder and decoder
 */
public class RequestToJoinDecoderTest {


    /**
     * Encodes and decodes a given message.
     * @param message Message to encode/decode.
     * @return The decoded message.
     * @throws InvalidMessageException If the message cannot be encoded.
     */
    private RequestToJoin encodeDecodeMessage(RequestToJoin message) throws InvalidMessageException {

        //Encode.
        byte [] testEncodedMessage = RaceVisionByteEncoder.encode(message);

        //Decode.
        RequestToJoinDecoder testDecoder = new RequestToJoinDecoder();
        testDecoder.decode(testEncodedMessage);
        RequestToJoin decodedMessage = testDecoder.getMessage();

        return decodedMessage;
    }


    /**
     * Tests if a specific request type message can be encoded and decoded correctly.
     * @param type The type of join request.
     * @throws Exception if test fails.
     */
    private void requestTypeTest(RequestToJoinEnum type) throws Exception {

        //Prepare message.
        RequestToJoin beforeMessage = new RequestToJoin(type);


        //Encode/decode it.
        RequestToJoin afterMessage = encodeDecodeMessage(beforeMessage);


        //Compare.
        assertEquals(beforeMessage.getRequestType().getValue(), afterMessage.getRequestType().getValue());

    }


    /**
     * Tests if a spectator request message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void spectatorTest() throws Exception {
        requestTypeTest(RequestToJoinEnum.SPECTATOR);
    }

    /**
     * Tests if a participant request message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void participantTest() throws Exception {
        requestTypeTest(RequestToJoinEnum.PARTICIPANT);
    }

    /**
     * Tests if a ghost request message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void ghostTest() throws Exception {
        requestTypeTest(RequestToJoinEnum.GHOST);
    }



}
