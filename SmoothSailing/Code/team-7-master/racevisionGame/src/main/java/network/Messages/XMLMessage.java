package network.Messages;


import network.Messages.Enums.MessageType;
import network.Messages.Enums.XMLMessageType;

import java.nio.charset.StandardCharsets;

/**
 * Created by fwy13 on 25/04/17.
 */
public class XMLMessage extends AC35Data {


    /**
     * The current version number for xml messages is 1.
     */
    public static byte currentVersionNumber = 1;

    /**
     * The version number of the message.
     */
    private byte versionNumber;

    /**
     * The ack number of the message.
     */
    private int ackNumber;

    /**
     * The timestamp of the message.
     * Milliseconds since unix epoch.
     */
    private long timeStamp;

    /**
     * The subtype of the xml message (e.g., race xml message).
     */
    private XMLMessageType xmlMsgSubType;

    /**
     * The sequence number of this specific xml subtype.
     * Increments whenever the xml contents for a specific xml subtype changes.
     */
    private int sequenceNumber;

    /**
     * The length of the xml message.
     * Number of bytes.
     */
    private int xmlMsgLength;

    /**
     * The contents of the xml message.
     */
    private String xmlMessage;


    /**
     * Constructor for an XML Message
     * @param versionNumber The version number of the xml message.
     * @param ackNumber Number for acknowledgement inherited for the AC35Data Packet
     * @param timeStamp Time received
     * @param xmlMsgSubType Type of XML message
     * @param sequenceNumber Order that it has arrived in
     * @param xmlMessage XML message
     */
    public XMLMessage(byte versionNumber, int ackNumber, long timeStamp, XMLMessageType xmlMsgSubType, int sequenceNumber, String xmlMessage) {
        super(MessageType.XMLMESSAGE);
        this.versionNumber = versionNumber;
        this.ackNumber = ackNumber;
        this.timeStamp = timeStamp;
        this.xmlMsgSubType = xmlMsgSubType;
        this.sequenceNumber = sequenceNumber;
        this.xmlMsgLength = xmlMessage.getBytes(StandardCharsets.UTF_8).length;
        this.xmlMessage = xmlMessage;
    }

    /**
     * Get the XML Message.
     * @return the XML message as string.
     */
    public String getXmlMessage() {
        return xmlMessage;
    }

    /**
     * Get the type of message
     * @return Gets the type of message the XML message is
     */
    public XMLMessageType getXmlMsgSubType() {
        return xmlMsgSubType;
    }


    /**
     * Returns the version number of this xml message.
     * @return The version number of this xml message.
     */
    public byte getVersionNumber() {
        return versionNumber;
    }

    /**
     * Returns the ack number of this xml message.
     * @return The ack number of this xml message.
     */
    public int getAckNumber() {
        return ackNumber;
    }

    /**
     * Returns the timestamp of this xml message.
     * @return The timestamp of this xml message.
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Returns the sequence number of this xml message. This is specific to each message subtype.
     * @return The sequence number of this xml message.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Returns the length, in number of bytes, of the xml message.
     * @return The length, in bytes, of the xml message.
     */
    public int getXmlMsgLength() {
        return xmlMsgLength;
    }
}
