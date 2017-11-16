package network.Messages;


import network.Messages.Enums.MessageType;
import shared.model.Bearing;

/**
 * Contains a single CourseWind record.
 * A CourseWinds message contains one or more CourseWind messages.
 */
public class CourseWind extends AC35Data {

    /**
     * The ID for this wind source.
     */
    private int ID;

    /**
     * The time the wind was captured at. Milliseconds since unix epoch.
     */
    private long time;

    /**
     * The ID of the race this applies to.
     * 0 means it isn't race specific.
     */
    private int raceID;

    /**
     * Direction of the wind.
     */
    private Bearing windDirection;

    /**
     * The speed of the wind, in knots.
     */
    private double windSpeedKnots;

    /**
     * Optimum upwind sailing angle.
     */
    private Bearing bestUpwindAngle;

    /**
     * Optimum downwind sailing angle.
     */
    private Bearing bestDownwindAngle;

    /**
     * Various flags which determine which values are valid.
     */
    private short flags;



    public CourseWind(int ID, long time, int raceID, Bearing windDirection, double windSpeedKnots, Bearing bestUpwindAngle, Bearing bestDownwindAngle, short flags) {
        super(MessageType.COURSEWIND);
        this.ID = ID;
        this.time = time;
        this.raceID = raceID;
        this.windDirection = windDirection;
        this.windSpeedKnots = windSpeedKnots;
        this.bestUpwindAngle = bestUpwindAngle;
        this.bestDownwindAngle = bestDownwindAngle;
        this.flags = flags;
    }


    /**
     * Returns the ID of the wind source.
     * @return ID of the wind source.
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the time that this was captured at. Milliseconds since unix epoch.
     * @return Time this wind was captured at.
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns the ID of the race this wind source belongs to. 0 means any race.
     * @return ID of the race this belongs to.
     */
    public int getRaceID() {
        return raceID;
    }

    /**
     * Returns the direction of the wind.
     * @return The direction of the wind.
     */
    public Bearing getWindDirection() {
        return windDirection;
    }

    /**
     * Returns the wind speed, in knots.
     * @return Wind speed, in knots.
     */
    public double getWindSpeedKnots() {
        return windSpeedKnots;
    }

    /**
     * Returns the best upwind sailing angle.
     * @return Best upwind sailing angle.
     */
    public Bearing getBestUpwindAngle() {
        return bestUpwindAngle;
    }

    /**
     * Returns the best downwind sailing angle.
     * @return The best downwind sailing angle.
     */
    public Bearing getBestDownwindAngle() {
        return bestDownwindAngle;
    }

    /**
     * Returns various flags which determine which values are valid.
     * @return Flag which determines which values are valid.
     */
    public int getFlags() {
        return flags;
    }

}
