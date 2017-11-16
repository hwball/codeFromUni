package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.XMLMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link XMLMessage} message.
 */
public class XMLMessageEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public XMLMessageEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            XMLMessage xmlMessage = (XMLMessage) message;


            byte[] messageBytes = xmlMessage.getXmlMessage().getBytes(StandardCharsets.UTF_8);

            //Message is 14 + xmlMessage.length bytes.
            ByteBuffer tempOutputByteBuffer = ByteBuffer.allocate(14 + messageBytes.length);

            //ackNumber converted to bytes
            byte[] ackNumberBytes = intToBytes(xmlMessage.getAckNumber(), 2);

            //Timestamp converted to bytes.
            byte[] timestampBytes = longToBytes(xmlMessage.getTimeStamp(), 6);

            //sequenceNumber converted to bytes
            byte[] sequenceNumberBytes = intToBytes(xmlMessage.getSequenceNumber(), 2);

            //xmlMsgLength converted to bytes
            byte[] xmlMsgLengthBytes = intToBytes(xmlMessage.getXmlMsgLength(), 2);


            tempOutputByteBuffer.put(xmlMessage.getVersionNumber());
            tempOutputByteBuffer.put(ackNumberBytes);
            tempOutputByteBuffer.put(timestampBytes);
            tempOutputByteBuffer.put(xmlMessage.getXmlMsgSubType().getValue());
            tempOutputByteBuffer.put(sequenceNumberBytes);
            tempOutputByteBuffer.put(xmlMsgLengthBytes);
            tempOutputByteBuffer.put(messageBytes);

            return tempOutputByteBuffer.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode XMLMessage message.", e);
        }

    }
}
