package network;

import network.Exceptions.InvalidMessageException;
import network.MessageDecoders.XMLMessageDecoder;
import network.MessageDecoders.XMLMessageDecoderTest;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.AC35Data;
import network.Messages.Enums.MessageType;
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
 * Tests the binary message decoder and encoder for a variety of messages.
 */
public class BinaryMessageDecoderTest {


    /**
     * Tests if an XMLMessage can be encoded and decoded correctly.
     * @throws Exception if test fails.
     */
    @Test
    public void xmlMessageTest() throws Exception {

        try {

            String filePath = "network/raceXML/Regatta.xml";
            XMLMessageType messageType = XMLMessageType.REGATTA;

            String xmlString = XMLReader.readXMLFileToString(filePath, StandardCharsets.UTF_8);

            long time = System.currentTimeMillis();

            XMLMessage xmlMessage = new XMLMessage(
                    (byte)1,
                    1,
                    time,
                    messageType,
                    (short)1,
                    xmlString    );

            byte[] encodedMessage = RaceVisionByteEncoder.encode(xmlMessage);


            BinaryMessageEncoder encoder = new BinaryMessageEncoder(
                    xmlMessage.getType(),
                    time,
                    1,
                    (short)encodedMessage.length,
                    encodedMessage  );

            BinaryMessageDecoder decoder = new BinaryMessageDecoder(encoder.getFullMessage());


            AC35Data message = null;
            try {
                message = decoder.decode();
            }
            catch (InvalidMessageException e) {
                Assert.assertFalse(e.getMessage(), true);
            }

            if (!(message instanceof XMLMessage)){
                Assert.assertFalse(true);
            }
            XMLMessage xmlMessageDecoded = (XMLMessage) message;


            //message length
            Assert.assertEquals((short) encodedMessage.length, decoder.getMessageBodyLength());
            //time stamp
            Assert.assertEquals(time, decoder.getHeaderTimeStamp());
            //source ID
            Assert.assertEquals((short) 1, decoder.getHeaderSourceID());
            //message type
            Assert.assertEquals(MessageType.XMLMESSAGE.getValue(), decoder.getHeaderMessageType());


            XMLMessageDecoderTest.compareXMLMessages(xmlMessage, xmlMessageDecoded);


        } catch (XMLReaderException e){
            fail("couldn't read file" + e.getMessage());
        }
    }

    //TODO add some tests for more messages types.

}
