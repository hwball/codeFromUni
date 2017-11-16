package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.XMLMessageType;
import network.Messages.XMLMessage;

import java.util.Arrays;

import static network.Utils.ByteConverter.bytesToLong;
import static network.Utils.ByteConverter.bytesToShort;


/**
 * Decodes {@link network.Messages.XMLMessage} messages.
 */
public class XMLMessageDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private XMLMessage message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public XMLMessageDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte[] messageVersionNumberBytes = Arrays.copyOfRange(encodedMessage, 0, 1);
            byte[] ackNumberBytes = Arrays.copyOfRange(encodedMessage, 1, 3);
            byte[] timeStampBytes = Arrays.copyOfRange(encodedMessage, 3, 9);
            byte[] xmlMsgSubTypeBytes = Arrays.copyOfRange(encodedMessage, 9, 10);
            byte[] sequenceNumberBytes = Arrays.copyOfRange(encodedMessage, 10, 12);
            byte[] xmlMsgLengthBytes = Arrays.copyOfRange(encodedMessage, 12, 14);
            byte[] xmlMessagebytes = Arrays.copyOfRange(encodedMessage, 14, encodedMessage.length);


            byte messageVersionNumber = messageVersionNumberBytes[0];
            short ackNumber = bytesToShort(ackNumberBytes);
            long timeStamp = bytesToLong(timeStampBytes);
            XMLMessageType xmlMsgSubType = XMLMessageType.fromByte(xmlMsgSubTypeBytes[0]);
            short sequenceNumber = bytesToShort(sequenceNumberBytes);
            short xmlMsgLength = bytesToShort(xmlMsgLengthBytes);
            String xmlMessage = new String(xmlMessagebytes);


            message = new XMLMessage(
                    messageVersionNumber,
                    ackNumber,
                    timeStamp,
                    xmlMsgSubType,
                    sequenceNumber,
                    xmlMessage);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode XMLMessage message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public XMLMessage getMessage() {
        return message;
    }
}
