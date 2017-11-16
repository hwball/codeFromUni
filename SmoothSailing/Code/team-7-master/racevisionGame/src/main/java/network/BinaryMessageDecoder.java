package network;


import network.Exceptions.InvalidMessageException;
import network.Exceptions.InvalidMessageTypeException;
import network.MessageDecoders.*;
import network.Messages.*;
import network.Messages.Enums.MessageType;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

import static network.Utils.ByteConverter.bytesToInt;
import static network.Utils.ByteConverter.bytesToLong;

/**
 * This class can be used to decode/convert a byte array into a messageBody object, descended from AC35Data.
 */
public class BinaryMessageDecoder {

    /**
     * Length of the header.
     */
    private static final int headerLength = 15;
    /**
     * Length of the CRC.
     */
    private static final int CRCLength = 4;

    /**
     * The value the first sync byte should have.
     */
    private static final byte syncByte1 = (byte) 0x47;
    /**
     * The value the second sync byte should have.
     */
    private static final byte syncByte2 = (byte) 0x83;

    /**
     * The full message.
     */
    private byte[] fullMessage;
    /**
     * The messageHeader.
     */
    private byte[] messageHeader;
    /**
     * The messageBody.
     */
    private byte[] messageBody;

    /**
     * The sync bytes from the header.
     */
    private byte headerSync1;
    private byte headerSync2;

    /**
     * The message type from the header.
     */
    private byte headerMessageType;

    /**
     * The timestamp from the header.
     */
    private long headerTimeStamp;

    /**
     * The source ID from the header.
     */
    private int headerSourceID;

    /**
     * The message body length from the header.
     */
    private int messageBodyLength;

    /**
     * CRC value read from message header.
     */
    private long messageCRCValue;

    /**
     * Calculated CRC value from message.
     */
    private long calculatedCRCValue;


    /**
     * Ctor.
     * @param fullMessage Entire encoded binary message.
     */
    public BinaryMessageDecoder(byte[] fullMessage) {
        this.fullMessage = fullMessage;

        //Get the messageHeader.
        this.messageHeader = Arrays.copyOfRange(this.fullMessage, 0, 15);

        //Get the sync bytes.
        this.headerSync1 = this.messageHeader[0];
        this.headerSync2 = this.messageHeader[1];

        //Get the message type.
        this.headerMessageType = this.messageHeader[2];

        //Get the header timestamp.
        this.headerTimeStamp = bytesToLong(Arrays.copyOfRange(this.messageHeader, 3, 9));

        //Get the source ID for the message.
        this.headerSourceID = bytesToInt(Arrays.copyOfRange(this.messageHeader, 9, 13));

        //Get the length of the message body.
        this.messageBodyLength = bytesToInt(Arrays.copyOfRange(this.messageHeader, 13, 15));


        //Get the messageBody.
        this.messageBody = Arrays.copyOfRange(this.fullMessage, this.headerLength, this.headerLength + this.messageBodyLength);

        //Get the CRC value.
        this.messageCRCValue = bytesToLong(Arrays.copyOfRange(this.fullMessage, this.fullMessage.length - CRCLength, this.fullMessage.length));

        //Combine the header and body into a single array.
        ByteBuffer headerBodyByteBuffer = ByteBuffer.allocate(messageHeader.length + messageBody.length);
        headerBodyByteBuffer.put(messageHeader);
        headerBodyByteBuffer.put(messageBody);

        //Calculate the CRC value from the header+body array.
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(headerBodyByteBuffer.array());
        this.calculatedCRCValue = crc.getValue();
    }


    /**
     * Decodes the byte array (binary message) this object was initialized with, and returns the corresponding message object.
     * @return Message object corresponding to the binary message.
     * @throws InvalidMessageException If the message cannot be decoded.
     */
    public AC35Data decode() throws InvalidMessageException {

        //Run through the checks to ensure that the message is valid.
        if (messageBody.length != messageBodyLength) {//keep like this - hba65
            //Check the message body length.
            throw new InvalidMessageException("MessageBody length in header does not equal the messageBody length. MessageBody length in header is: " + messageBodyLength + ", should be: " + messageBody.length);

        } else if (headerSync1 != syncByte1) {
            //Check the first sync byte.
            throw new InvalidMessageException("Sync byte 1 is wrong. Sync byte is: " + headerSync1 + ", should be: " + syncByte1);

        } else if (headerSync2 != syncByte2) {
            //Check the second sync byte.
            throw new InvalidMessageException("Sync byte 2 is wrong. Sync byte is: " + headerSync2 + ", should be: " + syncByte2);

        } else if (calculatedCRCValue != messageCRCValue) {
            //Check the CRC value.
            throw new InvalidMessageException("CRC value is wrong. The calculated value is: " + calculatedCRCValue + ", should be: " + messageCRCValue);

        }

        //Now we create the message object based on what is actually in the message body.
        MessageType messageType = MessageType.fromByte(headerMessageType);

        MessageDecoder decoder = null;
        try {
            decoder = DecoderFactory.create(messageType);

        } catch (InvalidMessageTypeException e) {
            throw new InvalidMessageException("Could not create decoder for MessageType: " + messageType, e);

        }

        return decoder.decode(messageBody);

    }


    /**
     * Returns the first sync byte value.
     * @return The first sync byte value.
     */
    public byte getHeaderSync1() {
        return headerSync1;
    }

    /**
     * Returns the second sync byte value.
     * @return The second sync byte value.
     */
    public byte getHeaderSync2() {
        return headerSync2;
    }

    /**
     * Returns the message type.
     * @return The message type.
     */
    public byte getHeaderMessageType() {
        return headerMessageType;
    }

    /**
     * Returns the header timestamp.
     * @return The header timestamp.
     */
    public long getHeaderTimeStamp() {
        return headerTimeStamp;
    }

    /**
     * Returns the header source ID.
     * @return The header source ID.
     */
    public int getHeaderSourceID() {
        return headerSourceID;
    }

    /**
     * Returns the message body length, according to the header.
     * @return The message body length.
     */
    public int getMessageBodyLength() {
        return messageBodyLength;
    }

    /**
     * Returns the message CRC value, according to the header.
     * @return The message CRC value.
     */
    public long getMessageCRCValue() {
        return messageCRCValue;
    }

    /**
     * Returns the calculated CRC value from the message header + body contents.
     * @return The calculated CRC value.
     */
    public long getCalculatedCRCValue() {
        return calculatedCRCValue;
    }


    /**
     * Returns the message body.
     * @return The message body.
     */
    public byte[] getMessageBody() {
        return messageBody;
    }
}

