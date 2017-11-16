package network.Messages;


import network.Messages.Enums.MessageType;
import network.Messages.Enums.RaceStartTypeEnum;


/**
 * Represents a RaceStartStatus message from the API, section 4.5.
 */
public class RaceStartStatus extends AC35Data {

    /**
     * The current version number of this message type.
     */
    public static final byte currentMessageVersionNumber = 1;


    /**
     * The version number of this message.
     */
    private byte messageVersionNumber;

    /**
     * The time at which this message was created. Milliseconds since unix epoch.
     */
    private long timestamp;

    /**
     * Sequence number of message.
     */
    private int ackNum;

    /**
     * The time the race is expected to start at. Milliseconds since unix epoch.
     */
    private long raceStartTime;

    /**
     * The ID of the race this message relates to.
     */
    private int raceID;

    /**
     * The type of notification this is.
     */
    private RaceStartTypeEnum notificationType;


    /**
     * Constructs a RaceStartStatus message with the given parameters.
     * @param messageVersionNumber Version number of the message.
     * @param timestamp The timestamp at which this message was generated.
     * @param ackNum The sequence number of this message.
     * @param raceStartTime The expected race start time.
     * @param raceID The ID of the race this message relates to.
     * @param notificationType The type of notification this is.
     */
    public RaceStartStatus(byte messageVersionNumber, long timestamp, int ackNum, long raceStartTime, int raceID, RaceStartTypeEnum notificationType) {
        super(MessageType.RACESTARTSTATUS);
        this.messageVersionNumber = messageVersionNumber;
        this.timestamp = timestamp;
        this.ackNum = ackNum;
        this.raceStartTime = raceStartTime;
        this.raceID = raceID;
        this.notificationType = notificationType;
    }


    /**
     * Returns the version number of this message.
     * @return Version number of this message.
     */
    public byte getMessageVersionNumber() {
        return messageVersionNumber;
    }

    /**
     * Return the time at which this message was generated. Milliseconds since unix epoch.
     * @return Time at which this message was generated.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the sequence number of this message.
     * @return Sequence number of this message.
     */
    public int getAckNum() {
        return ackNum;
    }

    /**
     * Returns the expected race start time. Milliseconds since unix epoch.
     * @return Expected race start time.
     */
    public long getRaceStartTime() {
        return raceStartTime;
    }

    /**
     * Returns the race ID this message relates to.
     * @return Race ID this message relates to.
     */
    public int getRaceID() {
        return raceID;
    }

    /**
     * Returns the type of start status notification this message is.
     * @return The type of notification this is.
     */
    public RaceStartTypeEnum getNotificationType() {
        return notificationType;
    }
}
