package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.BoatAction;
import network.Messages.Enums.BoatActionEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test for the BoatAction encoder and decoder
 */
public class BoatActionDecoderTest {


    /**
     * Encodes and decodes a given message.
     * @param message Message to encode/decode.
     * @return The decoded message.
     * @throws InvalidMessageException If the message cannot be encoded.
     */
    private BoatAction encodeDecodeMessage(BoatAction message) throws InvalidMessageException {

        //Encode.
        byte [] testEncodedMessage = RaceVisionByteEncoder.encode(message);

        //Decode.
        BoatActionDecoder testDecoder = new BoatActionDecoder();
        testDecoder.decode(testEncodedMessage);

        BoatAction decodedMessage = testDecoder.getMessage();

        return decodedMessage;
    }


    /**
     * Tests if a specific boat action type message can be encoded and decoded correctly.
     * @param type The type of boat action.
     * @throws Exception if test fails.
     */
    private void boatActionTypeTest(BoatActionEnum type) throws Exception {

        //Prepare message.
        BoatAction beforeMessage = new BoatAction(type);


        //Encode/decode it.
        BoatAction afterMessage = encodeDecodeMessage(beforeMessage);


        //Compare.
        assertEquals(beforeMessage.getBoatAction(), afterMessage.getBoatAction());

    }


    /**
     * Tests if an autopilot message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void autoPilotTest() throws Exception {
        boatActionTypeTest(BoatActionEnum.VMG);
    }

    /**
     * Tests if a sails in message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void sailsInTest() throws Exception {
        boatActionTypeTest(BoatActionEnum.SAILS_IN);
    }

    /**
     * Tests if a sails out message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void sailsOutTest() throws Exception {
        boatActionTypeTest(BoatActionEnum.SAILS_OUT);
    }

    /**
     * Tests if a tack/gybe message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void tackGybeTest() throws Exception {
        boatActionTypeTest(BoatActionEnum.TACK_GYBE);
    }

    /**
     * Tests if an upwind message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void upwindTest() throws Exception {
        boatActionTypeTest(BoatActionEnum.UPWIND);
    }

    /**
     * Tests if a downwind message can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void downwindTest() throws Exception {
        boatActionTypeTest(BoatActionEnum.DOWNWIND);
    }


}
