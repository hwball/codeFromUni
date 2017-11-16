package network.Messages;


import network.Messages.Enums.BoatStatusEnum;
import network.Utils.ByteConverter;

/**
 * Represents the information in a BoatStatus message, which is contained inside a RaceStatus message (AC streaming spec: 4.2).
 */
public class BoatStatus {

    /**
     * The sourceID of the boat.
     */
    private int sourceID;

    /**
     * The status of the boat.
     */
    private BoatStatusEnum boatStatus;

    /**
     * The leg number that the boat is on.
     */
    private byte legNumber;

    /**
     * The number of penalties awarded to the boat.
     */
    private byte numPenaltiesAwarded;

    /**
     * The number of penalties served by the boat.
     */
    private byte numPenaltiesServed;

    /**
     * The time at which it is estimated the boat will reach the next mark.
     * Milliseconds since unix epoch.
     */
    private long estTimeAtNextMark;

    /**
     * The time at which it is estimated the boat will finish the race.
     * Milliseconds since unix epoch.
     */
    private long estTimeAtFinish;


    /**
     * Constructs a BoatStatus message with the given parameters.
     * @param sourceID The sourceID of the boat.
     * @param boatStatus The status of the boat.
     * @param legNumber The leg number the boat is on.
     * @param numPenaltiesAwarded The number of penalties awarded to the boat.
     * @param numPenaltiesServed The number of penalties served by the boat.
     * @param estTimeAtNextMark The estimated time at which the boat will reach the next mark.
     * @param estTimeAtFinish The estimated time at which the boat will finish the race.
     */
    public BoatStatus(int sourceID, BoatStatusEnum boatStatus, int legNumber, byte numPenaltiesAwarded, byte numPenaltiesServed, long estTimeAtNextMark, long estTimeAtFinish) {
        this.sourceID = sourceID;
        this.boatStatus = boatStatus;
        this.legNumber = ByteConverter.intToBytes(legNumber, 1)[0];
        this.numPenaltiesAwarded = numPenaltiesAwarded;
        this.numPenaltiesServed = numPenaltiesServed;
        this.estTimeAtNextMark = estTimeAtNextMark;
        this.estTimeAtFinish = estTimeAtFinish;
    }


    /**
     * Constructs a BoatStatus message with the given parameters. Sets penalties to zero, and time at finish to zero.
     * @param sourceID The sourceID of the boat.
     * @param boatStatus The status of the boat.
     * @param legNumber The leg number the boat is on.
     * @param estTimeAtNextMark The estimated time at which the boat will reach the next mark.
     */
    public BoatStatus(int sourceID, BoatStatusEnum boatStatus, int legNumber, long estTimeAtNextMark) {
        this(
                sourceID,
                boatStatus,
                legNumber,
                (byte) 0,
                (byte) 0,
                estTimeAtNextMark,
                0   );
    }


    /**
     * Returns the sourceID of the boat.
     * @return The sourceID of the boat.
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * Returns the status of the boat.
     * @return The status of the boat.
     */
    public BoatStatusEnum getBoatStatus() {
        return boatStatus;
    }

    /**
     * Returns the leg number of boat is on.
     * @return The leg number of boat is on.
     */
    public byte getLegNumber() {
        return legNumber;
    }

    /**
     * Returns the number of penalties awarded to the boat.
     * @return Number of penalties awarded to boat.
     */
    public byte getNumPenaltiesAwarded() {
        return numPenaltiesAwarded;
    }

    /**
     * Returns the number of penalties served by the boat.
     * @return The number of penalties served by the boat.
     */
    public byte getNumPenaltiesServed() {
        return numPenaltiesServed;
    }

    /**
     * Returns the time at which it is estimated the boat will reach the next mark. Milliseconds since unix epoch.
     * @return Time at which boat will reach next mark.
     */
    public long getEstTimeAtNextMark() {
        return estTimeAtNextMark;
    }

    /**
     * Returns the time at which it is estimated the boat will finish the race. Milliseconds since unix epoch.
     * @return Time at which boat will finish the race.
     */
    public long getEstTimeAtFinish() {
        return estTimeAtFinish;
    }
}
