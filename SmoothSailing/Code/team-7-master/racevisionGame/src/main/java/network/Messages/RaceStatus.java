package network.Messages;


import network.Messages.Enums.MessageType;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.Enums.RaceTypeEnum;
import network.Utils.AC35UnitConverter;
import shared.model.Bearing;
import shared.model.Constants;

import java.util.List;

/**
 * Represents the information in a RaceStatus message (AC streaming spec: 4.2).
 */
public class RaceStatus  extends AC35Data {


    /**
     * The current messageVersionNumber according to the API spec.
     */
    public static final byte currentMessageVersionNumber = 2;


    /**
     * Version number of the message.
     */
    private byte messageVersionNumber;

    /**
     * Time the message was generated at.
     * Milliseconds since unix epoch.
     */
    private long currentTime;

    /**
     * ID number of the race.
     */
    private int raceID;

    /**
     * The status of the race.
     */
    private RaceStatusEnum raceStatus;

    /**
     * The expected race start time.
     * Milliseconds since unix epoch.
     */
    private long expectedStartTime;

    /**
     * The wind direction of the course.
     */
    private Bearing windDirection;

    /**
     * The wind speed of the course.
     * Knots.
     */
    private double windSpeed;

    /**
     * The type of race this is.
     */
    private RaceTypeEnum raceType;

    /**
     * A list of boat statuses.
     * One for each boat.
     */
    private List<BoatStatus> boatStatuses;


    /**
     * Constructs a RaceStatus message with the given parameters.
     * @param messageVersionNumber The version number of the message.
     * @param currentTime Time at which the message was generated.
     * @param raceID The ID of the race.
     * @param raceStatus The status of the race.
     * @param expectedStartTime The expected start time of the race.
     * @param windDirection The current wind direction in the race.
     * @param windSpeed The current wind speed in the race, in knots.
     * @param raceType The type of race this is.
     * @param boatStatuses A list of BoatStatuses. One for each boat.
     */
    public RaceStatus(byte messageVersionNumber, long currentTime, int raceID, RaceStatusEnum raceStatus, long expectedStartTime, Bearing windDirection, double windSpeed, RaceTypeEnum raceType, List<BoatStatus> boatStatuses) {

        super(MessageType.RACESTATUS);
        this.messageVersionNumber = messageVersionNumber;
        this.currentTime = currentTime;
        this.raceID = raceID;
        this.raceStatus = raceStatus;
        this.expectedStartTime = expectedStartTime;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.raceType = raceType;
        this.boatStatuses = boatStatuses;
    }



    /**
     * Returns the version number of this message.
     * @return The version number of the message.
     */
    public byte getMessageVersionNumber() {
        return messageVersionNumber;
    }

    /**
     * Returns the current time at which this message was generated. Milliseconds since unix epoch.
     * @return Time this message was generated at.
     */
    public long getCurrentTime()
    {
        return currentTime;
    }

    /**
     * Returns the RaceID.
     * @return The raceID.
     */
    public int getRaceID()
    {
        return raceID;
    }

    /**
     * Returns the race status.
     * @return The current race status.
     */
    public RaceStatusEnum getRaceStatus()
    {
        return raceStatus;
    }

    /**
     * Returns the expected start time for the race. Milliseconds since unix epoch.
     * @return Expected start time for the race.
     */
    public long getExpectedStartTime()
    {
        return expectedStartTime;
    }

    /**
     * Returns the current direction of the wind in the race.
     * @return Current wind direction.
     */
    public Bearing getWindDirection()
    {
        return windDirection;
    }

    /**
     * Returns the wind speed for this race status, in knots.
     * @return Wind speed in knots.
     */
    public double getWindSpeed()
    {
        return windSpeed;
    }

    /**
     * Retrusn the type of race this is.
     * @return The type of race this is.
     */
    public RaceTypeEnum getRaceType()
    {
        return raceType;
    }

    /**
     * Returns the list of BoatStatuses. One for each boat.
     * @return List of BoatStatuses.
     */
    public List<BoatStatus> getBoatStatuses()
    {
        return boatStatuses;
    }


    public boolean isNotActive() {
        return raceStatus == RaceStatusEnum.NOT_ACTIVE;
    }

    public boolean isWarning() {
        return raceStatus == RaceStatusEnum.WARNING;
    }

    public boolean isPreparatory() {
        return raceStatus == RaceStatusEnum.PREPARATORY;
    }

    public boolean isStarted() {
        return raceStatus == RaceStatusEnum.STARTED;
    }

    public boolean isFinished() {
        return raceStatus == RaceStatusEnum.FINISHED;
    }

    public boolean isRetired() {
        return raceStatus == RaceStatusEnum.RETIRED;
    }

    public boolean isAbandoned() {
        return raceStatus == RaceStatusEnum.ABANDONED;
    }

    public boolean isPostponed() {
        return raceStatus == RaceStatusEnum.POSTPONED;
    }

    public boolean isTerminated() {
        return raceStatus == RaceStatusEnum.TERMINATED;
    }

    public boolean isStartTimeSet() {
        return raceStatus != RaceStatusEnum.RACE_START_TIME_NOT_SET;
    }

    public boolean isPrestart() {
        return raceStatus == RaceStatusEnum.PRESTART;
    }

}
