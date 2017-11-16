package network.Messages;


import network.Messages.Enums.MarkRoundingBoatStatusEnum;
import network.Messages.Enums.MarkRoundingSideEnum;
import network.Messages.Enums.MarkRoundingTypeEnum;
import network.Messages.Enums.MessageType;

/**
 * Represents a MarkRound message (see AC35 spec, 4.10).
 */
public class MarkRounding extends AC35Data {

    /**
     * The current messageVersionNumber according to the API spec.
     */
    public static final byte currentMessageVersionNumber = 1;


    /**
     * Version number of the message.
     */
    private byte messageVersionNumber;

    /**
     * The time at which the mark was rounding. Milliseconds since unix epoch.
     */
    private long time;

    /**
     * The ack number of the message.
     */
    private int ackNum;

    /**
     * The raceID this message relates to.
     */
    private int raceID;

    /**
     * The sourceID of the boat this message relates to.
     */
    private int sourceID;

    /**
     * The status of the boat.
     */
    private MarkRoundingBoatStatusEnum boatStatus;

    /**
     * The side around which the boat rounded.
     */
    private MarkRoundingSideEnum roundingSide;

    /**
     * The type of mark that was rounded.
     */
    private MarkRoundingTypeEnum markType;

    /**
     * The ID of the mark. This is not a source ID.
     */
    private byte markID;


    /**
     * Creates a MarkRounding message with the given parameters.
     * @param messageVersionNumber The version number of the message.
     * @param time The time at which the message was created.
     * @param ackNum The ack number of the message.
     * @param raceID The raceID this message relates to.
     * @param sourceID The sourceID of the boat this message relates to.
     * @param boatStatus The status of the boat as it rounded the mark.
     * @param roundingSide The side around which the boat rounded.
     * @param markType The type of mark that was rounded.
     * @param markID The ID number of the mark. Not a sourceID. See {@link network.Messages.Enums.MarkRoundingIDEnum}.
     */
    public MarkRounding(
            byte messageVersionNumber,
            long time,
            int ackNum,
            int raceID,
            int sourceID,
            MarkRoundingBoatStatusEnum boatStatus,
            MarkRoundingSideEnum roundingSide,
            MarkRoundingTypeEnum markType,
            byte markID  ) {

        super(MessageType.MARKROUNDING);

        this.messageVersionNumber = messageVersionNumber;
        this.time = time;
        this.ackNum = ackNum;
        this.raceID = raceID;
        this.sourceID = sourceID;
        this.boatStatus = boatStatus;
        this.roundingSide = roundingSide;
        this.markType = markType;
        this.markID = markID;
    }


    /**
     * Returns the version number of this message.
     * @return Version number of this message.
     */
    public byte getMessageVersionNumber() {
        return messageVersionNumber;
    }


    /**
     * Returns the timestamp for this message.
     * @return Timestamp for this message.
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns the ack number of this message.
     * @return Ack number of this message.
     */
    public int getAckNum() {
        return ackNum;
    }

    /**
     * Returns the raceID this message relates to.
     * @return RaceID this message relates to.
     */
    public int getRaceID() {
        return raceID;
    }

    /**
     * Returns the boat (source) ID for this message.
     * @return Boat ID for this message.
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * Returns the status of the boat as it rounded the mark.
     * @return Status of boat as it rounded mark.
     */
    public MarkRoundingBoatStatusEnum getBoatStatus() {
        return boatStatus;
    }

    /**
     * Returns the side to which the boat rounded the mark.
     * @return Side to which boat rounded mark.
     */
    public MarkRoundingSideEnum getRoundingSide() {
        return roundingSide;
    }

    /**
     * Returns the type of mark that was rounded.
     * @return The type of mark that was rounded.
     */
    public MarkRoundingTypeEnum getMarkType() {
        return markType;
    }

    /**
     * Returns ID number of the mark. This is not a source ID. See {@link network.Messages.Enums.MarkRoundingIDEnum}.
     * @return ID number of the mark.
     */
    public byte getMarkID() {
        return markID;
    }
}
