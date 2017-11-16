package network;


import network.Messages.Enums.MessageType;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import static network.Utils.ByteConverter.*;


/**
 * This class can be used to encode/convert a byte array message body, plus header data into a byte array containing the entire message, ready to send.
 */
public class BinaryMessageEncoder {

    ///Length of the header.
    private static final int headerLength = 15;
    ///Length of the CRC.
    private static final int CRCLength = 4;//TODO these should probably be static defined somewhere else to be shared.

    ///The full message.
    private byte[] fullMessage;
    ///The message header.
    private byte[] messageHeader;
    ///The message body.
    private byte[] messageBody;

    ///First sync byte value.
    private byte headerSync1 = (byte)0x47;
    ///Second sync byte value.
    private byte headerSync2 = (byte)0x83;

    ///The message type to place in header.
    private byte headerMessageType;
    ///The timestamp to place in header.
    private long headerTimeStamp;
    ///The source ID to place in header.
    private int headerSourceID;
    ///The message length to place in header.
    private short bodyMessageLength;

    ///The calculated CRC value.
    private long calculatedCRCValue;

    /**
     * Ctor. Constructs a encoder and encodes the full message. Retrieve it with encoder.getFullMessage().
     * @param headerMessageType The message type to send.
     * @param headerTimeStamp Timestamp of the message.
     * @param headerSourceID Source ID of the message.
     * @param bodyMessageLength The length of the body of the message.
     * @param messageBody The body of the message (that is, the payload).
     */
    public BinaryMessageEncoder(MessageType headerMessageType, long headerTimeStamp, int headerSourceID, short bodyMessageLength, byte[] messageBody) {
        //Set the header parameters.
        this.headerMessageType = headerMessageType.getValue();
        this.headerTimeStamp = headerTimeStamp;
        this.headerSourceID = headerSourceID;
        this.bodyMessageLength = bodyMessageLength;

        //Place the header parameters into a buffer.
        ByteBuffer tempHeaderByteBuffer = ByteBuffer.allocate(this.headerLength);
        tempHeaderByteBuffer.put(this.headerSync1);
        tempHeaderByteBuffer.put(this.headerSync2);
        tempHeaderByteBuffer.put(this.headerMessageType);
        tempHeaderByteBuffer.put(longToBytes(this.headerTimeStamp, 6));
        tempHeaderByteBuffer.put(intToBytes(this.headerSourceID));
        tempHeaderByteBuffer.put(shortToBytes(this.bodyMessageLength));

        this.messageHeader = tempHeaderByteBuffer.array();

        //Set the message body.
        this.messageBody = messageBody;


        //Place header and body into a buffer.
        ByteBuffer tempHeaderBodyByteBuffer = ByteBuffer.allocate(this.messageHeader.length + this.bodyMessageLength);
        tempHeaderBodyByteBuffer.put(this.messageHeader);
        tempHeaderBodyByteBuffer.put(this.messageBody);

        //Calculate the CRC from header + body.
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(tempHeaderBodyByteBuffer.array());
        this.calculatedCRCValue = crc.getValue();

        //Place header, body, and CRC value in buffer.
        ByteBuffer tempFullMessageByteBuffer = ByteBuffer.allocate(this.messageHeader.length + this.messageBody.length + this.CRCLength);
        tempFullMessageByteBuffer.put(this.messageHeader);
        tempFullMessageByteBuffer.put(this.messageBody);
        tempFullMessageByteBuffer.put(intToBytes((int) this.calculatedCRCValue));

        //Set the full message.
        this.fullMessage = tempFullMessageByteBuffer.array();
    }

    /**
     * Construct a binary message from message type and message body.
     * @param headerMessageType of message
     * @param messageBody of message
     */
    public BinaryMessageEncoder(MessageType headerMessageType, byte[] messageBody) {
        this(headerMessageType, System.currentTimeMillis(), 69, (short)messageBody.length, messageBody);
    }

    /**
     * Returns the full encoded message. This includes the header, body, and CRC.
     * @return Full encoded message.
     */
    public byte[] getFullMessage() {
        return fullMessage;
    }
}
