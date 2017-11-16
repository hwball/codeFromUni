package visualiser.model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import network.Messages.Enums.BoatStatusEnum;
import shared.model.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Boat on the visualiser side of a race.
 * This adds visualiser specific functionality to a boat.
 * This class is used to represent and store information about a boat which may
 * travel around in a race. It is displayed on the
 * {@link ResizableRaceCanvas ResizableRaceCanvas} via the
 * {@link visualiser.Controllers.RaceViewController RaceViewController}.
 */
public class VisualiserBoat extends Boat {


    /**
     * The collection of trackpoints generated for the boat.
     */
    private final List<TrackPoint> track = new ArrayList<>();

    /**
     * The next time, in milliseconds since unix epoch, at which we may create a new track point.
     */
    private long nextValidTime = 0;

    /**
     * The minimum period of time, in milliseconds, between the creation of each track point.
     */
    private static final long trackPointTimeInterval = 1000;

    /**
     * The number of track points that should be created before fully diminishing the alpha of a given track point.
     */
    private static final int trackPointLimit = 50;


    /**
     * The boat's color.
     */
    private Color color;


    /**
     * Scalar used to scale the boat's wake.
     */
    private static final double wakeScale = 25;

    /**
     * If true then this boat has been allocated to the client.
     */
    private boolean isClientBoat = false;


    private ObjectProperty<GPSCoordinate> positionProperty;
    private ObjectProperty<Bearing> bearingProperty;
    private BooleanProperty hasCollided;
    private DoubleProperty healthProperty;


    /**
     * Constructs a boat object from a given boat and color.
     *
     * @param boat The boat to convert into a MockBoat.
     * @param color  The color of the boat.
     */
    public VisualiserBoat(Boat boat, Color color) {
        super(boat.getSourceID(), boat.getName(), boat.getCountry());

        this.color = color;
        this.hasCollided = new SimpleBooleanProperty(false);
        this.healthProperty = new SimpleDoubleProperty(100);
    }


    /**
     * Returns the position of the end of the boat's wake, which is 180 degrees
     * from the boat's heading, and whose length is proportional to the boat's
     * speed.
     *
     * @return GPSCoordinate of wake endpoint.
     */
    public GPSCoordinate getWake() {


        //Calculate the reverse bearing of the boat, and convert it to an azimuth.
        Azimuth reverseAzimuth = Azimuth.fromDegrees(getBearing().degrees() - 180d);

        //Calculate the distance, in meters, of the wake.
        //We currently use boat's speed, in meters per second, to calculate the wake length. Could maybe move the knot -> m/s calculation somewhere else.
        double speedKnots = getCurrentSpeed();
        double speedMetersPerHour = speedKnots * Constants.NMToMetersConversion;
        double speedMetersPerSecond = speedMetersPerHour / Constants.OneHourSeconds;

        double wakeDistanceMeters = speedMetersPerSecond * this.wakeScale;


        //Calculate the new coordinate.
        GPSCoordinate wakeCoordinate = GPSCoordinate.calculateNewPosition(getPosition(), wakeDistanceMeters, reverseAzimuth);

        return wakeCoordinate;
    }

    /**
     * Attempts to add a new point to boat's track.
     * It only adds a new point if the boat is still racing (see {@link #getStatus()} and {@link BoatStatusEnum#RACING}), and if at least {@link #trackPointTimeInterval} milliseconds have occurred, using race time.
     * @param coordinate The {@link GPSCoordinate} of the trackpoint.
     * @param currentTime The current race time.
     * @see TrackPoint
     */
    public void addTrackPoint(GPSCoordinate coordinate, ZonedDateTime currentTime) {

        //Get current time.
        long currentTimeMilli = currentTime.toInstant().toEpochMilli();

        //Check if enough time has passed to create a new track point.
        Boolean canBeAdded = currentTimeMilli >= nextValidTime;

        //If it has, and if we are still racing, create the point.
        if (canBeAdded && (getStatus() == BoatStatusEnum.RACING)) {

            //Calculate the period of time that it should take the track point to diminish over. We essentially allow for trackPointLimit number of track points to be created before it should fade out.
            long expiryPeriod = trackPointTimeInterval * trackPointLimit;

            //Create and add point.
            TrackPoint trackPoint = new TrackPoint(coordinate, currentTimeMilli, expiryPeriod);
            track.add(trackPoint);

            //Update the nextValidTime for the next track point.
            nextValidTime = currentTimeMilli + trackPointTimeInterval;

            if (track.size() > trackPointLimit) {
                track.remove(0);
            }
        }
    }

    /**
     * Returns the boat's sampled track between start of race and current time.
     * @return The list of track points.
     * @see TrackPoint
     */
    public List<TrackPoint> getTrack() {
        return track;
    }

    /**
     * Returns the color of the boat.
     * @return The color of the boat.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Print method prints the name of the boat
     *
     * @return Name of the boat.
     */
    public String toString() {
        return getName();
    }




    /**
     * Returns the time until the boat will reach the next mark, as a string.
     * @param currentTime The current race time.
     * @return The time delta until the boat reaches the next mark.
     */
    public String getTimeToNextMarkFormatted(ZonedDateTime currentTime) {

        if ((getTimeAtLastMark() != null) && (currentTime != null)) {
            //Calculate time delta.
            Duration timeUntil = Duration.between(currentTime, getEstimatedTimeAtNextMark());

            //Convert to seconds.
            long secondsUntil = timeUntil.getSeconds();

            //This means the estimated time is in the past, or not racing.
            if ((secondsUntil < 0) || (getStatus() != BoatStatusEnum.RACING)) {
                return " -";
            }


            if (secondsUntil <= 60) {
                //If less than 1 minute, display seconds only.
                return " " + secondsUntil + "s";

            } else {
                //Otherwise display minutes and seconds.
                long seconds = secondsUntil % 60;
                long minutes = (secondsUntil - seconds) / 60;
                return String.format(" %dm %ds", minutes, seconds);

            }

        } else {
            return " -";
        }

    }


    /**
     * Returns the time since the boat passed the previous mark, as a string.
     * @param currentTime The current race time.
     * @return The time delta since the boat passed the previous mark.
     */
    public String getTimeSinceLastMarkFormatted(ZonedDateTime currentTime) {

        if ((getTimeAtLastMark() != null) && (currentTime != null)) {
            //Calculate time delta.
            Duration timeSince = Duration.between(getTimeAtLastMark(), currentTime);

            //Format it.
            return String.format(" %ds ", timeSince.getSeconds());

        } else {
            return " -";
        }
    }


    /**
     * Returns whether or not this boat has been assigned to the client.
     * @return True if this is the client's boat.
     */
    public boolean isClientBoat() {
        return isClientBoat;
    }

    /**
     * Sets whether or not this boat has been assigned to the client.
     * @param clientBoat True if this is the client's boat.
     */
    public void setClientBoat(boolean clientBoat) {
        isClientBoat = clientBoat;
    }

    @Override
    public GPSCoordinate getPosition() {
        return positionProperty.get();
    }

    @Override
    public void setPosition(GPSCoordinate position) {
        if(this.positionProperty == null) {
            this.positionProperty = new SimpleObjectProperty<>();
        }
        this.positionProperty.set(position);
    }

    @Override
    public Bearing getBearing() {
        return bearingProperty.get();
    }

    @Override
    public void setBearing(Bearing bearing) {
        if(this.bearingProperty == null) {
            this.bearingProperty = new SimpleObjectProperty<>();
        }
        this.bearingProperty.set(bearing);
    }

    public boolean hasCollided() {
        return hasCollided.get();
    }

    public BooleanProperty hasCollidedProperty() {
        return hasCollided;
    }

    public void setHasCollided(boolean hasCollided) {
        this.hasCollided.set(hasCollided);
    }

    public DoubleProperty healthProperty() {
        return healthProperty;
    }

    @Override
    public double getHealth() {
        return healthProperty.get();
    }

    @Override
    public void setHealth(double healthProperty) {
        this.healthProperty.set((int)healthProperty);
    }
}
