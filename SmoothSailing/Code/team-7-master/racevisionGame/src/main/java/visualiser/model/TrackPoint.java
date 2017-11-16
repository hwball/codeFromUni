package visualiser.model;


import shared.model.GPSCoordinate;

/**
 * A TrackPoint is a point plotted to display the track a
 * {@link VisualiserBoat Boat} has travelled in a race. <br>
 * TrackPoints are displayed on a
 * {@link ResizableRaceCanvas}, via the
 * {@link visualiser.Controllers.RaceViewController}. <br>
 * Track points can be made visible or hidden via the RaceViewController's
 * {@link Annotations}.
 */
public class TrackPoint {

    /**
     * The {@link GPSCoordinate} this {@link TrackPoint} corresponds to.
     */
    private final GPSCoordinate coordinate;

    /**
     * The time the track point was created at, in milliseconds since unix epoch.
     */
    private final long timeAdded;

    /**
     * The period of time, in milliseconds, over which the track point's alpha should diminish to a floor value of {@link #minAlpha}.
     */
    private final long expiry;

    /**
     * The minimum alpha to draw the track point with.
     */
    private final double minAlpha;


    /**
     * Creates a new track point with fixed GPS coordinates and time, to reach minimum opacity on expiry.
     *
     * @param coordinate position of point on physical race map
     * @param timeAdded  system clock at time of addition
     * @param expiry     time to minimum opacity after added
     */
    public TrackPoint(GPSCoordinate coordinate, long timeAdded, long expiry) {
        this.coordinate = coordinate;
        this.timeAdded = timeAdded;
        this.expiry = expiry;

        this.minAlpha = 0.1;
    }


    /**
     * Gets the position of the point on physical race map.
     *
     * @return GPS coordinate of point
     */
    public GPSCoordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Gets opacity of point scaled by age in proportion to expiry, between 1 and minimum opacity inclusive.
     *
     * @return Greater of minimum opacity and scaled opacity.
     */
    public double getAlpha() {

        //Calculate how much the alpha should be attenuated by elapsed time.

        //Elapsed time.
        long elapsedTime = System.currentTimeMillis() - this.timeAdded;

        //Proportion of expiry period that has elapsed. (E.g., 2.5 means that 5 times the period has elapsed.)
        double elapsedProportion = ((double) elapsedTime) / this.expiry;

        //As the alpha diminishes from 1 down to a floor, we take the complement of this value. This may be negative.
        double calculatedAlpha = 1.0 - elapsedProportion;

        //We then take the max of the minAlpha and calculatedAlpha so that it doesn't go past our floor value.
        return Double.max(this.minAlpha, calculatedAlpha);
    }

    /**
     * Gets time point was added to track.
     *
     * @return system clock at time of addition
     */
    public long getTimeAdded() {
        return timeAdded;
    }

}
