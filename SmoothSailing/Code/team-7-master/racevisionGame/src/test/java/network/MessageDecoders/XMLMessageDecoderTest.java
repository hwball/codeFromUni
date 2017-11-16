package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.Enums.XMLMessageType;
import network.Messages.XMLMessage;
import org.junit.Assert;
import org.junit.Test;
import shared.dataInput.XMLReader;
import shared.exceptions.XMLReaderException;

import javax.xml.transform.TransformerException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.fail;

/**
 * Test for the XMLMessage encoder and decoder
 */
public class XMLMessageDecoderTest {


    /**
     * Creates an XML message of the given type, using the specified filePath, encodes it, decodes it, and checks that the result matches the starting message.
     * @param filePath The file path for xml file.
     * @param type The type of xml file.
     * @throws InvalidMessageException Thrown if message cannot be encoded.
     */
    private void xmlMessageTest(String filePath, XMLMessageType type) throws InvalidMessageException {

        try {
            String xmlString = XMLReader.readXMLFileToString(filePath, StandardCharsets.UTF_8);

            long time = System.currentTimeMillis();

            XMLMessage message = new XMLMessage(
                    (byte)1,
                    1,
                    time,
                    type,
                    (short)1,
                    xmlString    );

            byte[] encodedXML = RaceVisionByteEncoder.encode(message);



            XMLMessageDecoder decoderXML = new XMLMessageDecoder();
            decoderXML.decode(encodedXML);
            XMLMessage decodedMessage = decoderXML.getMessage();


            compareXMLMessages(message, decodedMessage);


        } catch (XMLReaderException e){
            fail("couldn't read file" + e.getMessage());
        }

    }

    /**
     * Compares two XML messages to check that they are the same.
     * @param originalMessage The first message to test.
     * @param decodedMessage The second message to test.
     */
    public static void compareXMLMessages(XMLMessage originalMessage, XMLMessage decodedMessage) {

        Assert.assertEquals(originalMessage.getVersionNumber(), decodedMessage.getVersionNumber());
        Assert.assertEquals(originalMessage.getAckNumber(), decodedMessage.getAckNumber());
        Assert.assertEquals(originalMessage.getTimeStamp(), decodedMessage.getTimeStamp());
        Assert.assertEquals(originalMessage.getXmlMsgSubType(), decodedMessage.getXmlMsgSubType());
        Assert.assertEquals(originalMessage.getSequenceNumber(), decodedMessage.getSequenceNumber());
        Assert.assertEquals(originalMessage.getXmlMsgLength(), decodedMessage.getXmlMsgLength());
        Assert.assertEquals(originalMessage.getXmlMessage(), decodedMessage.getXmlMessage());

    }

    /**
     * Tests if a regatta.xml message can be encoded and decoded.
     * @throws Exception if test fails.
     */
    @Test
    public void regattaXMLMessageTest() throws Exception {
        xmlMessageTest("network/raceXML/Regatta.xml", XMLMessageType.REGATTA);
    }


    /**
     * Tests if a race.xml message can be encoded and decoded.
     * @throws Exception if test fails.
     */
    @Test
    public void raceXMLMessageTest() throws Exception {
        xmlMessageTest("network/raceXML/Race.xml", XMLMessageType.RACE);
    }


    /**
     * Tests if a boat.xml message can be encoded and decoded.
     * @throws Exception if test fails.
     */
    @Test
    public void boatXMLMessageTest() throws Exception {
        xmlMessageTest("network/raceXML/Boats.xml", XMLMessageType.BOAT);
    }
}
