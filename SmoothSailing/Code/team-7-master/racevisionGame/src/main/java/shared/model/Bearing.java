package shared.model;

/**
 * Represents a bearing. Also known as a heading.
 * If treated as an absolute bearing this is the angle between north and a target point.
 * If treated as a relative bearing, this is the angle between from one target point to the other.
 * Has the interval [0, 360) degrees, and clockwise is positive.
 */
public class Bearing extends Angle {


    /**
     * Ctor.
     * This is protected because you need to use the static helper functions {@link #fromDegrees(double)} and {@link #fromRadians(double)} to construct a Bearing object.
     *
     * @param degrees The value, in degrees, to initialize this Bearing object with.
     */
    protected Bearing(double degrees) {
        super(degrees);
    }


    /**
     * Converts an angle in degrees into an angle in degrees in the correct interval for a bearing - [0, 360).
     * E.g., converts -183 to 177, or converts 425 to 65.
     * @param degrees Degree value to convert.
     * @return Degree value in interval [0, 360).
     */
    public static double toBearingInterval(double degrees) {

        return Angle.toPeriodicInterval(degrees, 0d, 360d, 360d);
    }

    /**
     * Constructs a Bearing object from an angle value in degrees.
     * @param degrees Bearing value in degrees.
     * @return Bearing object.
     */
    public static Bearing fromDegrees(double degrees) {
        //Ensure the angle is in the correct interval.
        double degreesInInterval = Bearing.toBearingInterval(degrees);

        Bearing bearing = new Bearing(degreesInInterval);
        return bearing;
    }

    /**
     * Constructs a Bearing object from an angle value in radians.
     * @param radians Bearing value in radians.
     * @return Bearing object.
     */
    public static Bearing fromRadians(double radians) {
        return Bearing.fromDegrees(Math.toDegrees(radians));
    }


    /**
     * Constructs a Bearing object from an Azimuth object.
     * @param azimuth Azimuth object to read value from.
     * @return Bearing object.
     */
    public static Bearing fromAzimuth(Azimuth azimuth) {
        return Bearing.fromDegrees(azimuth.degrees());
    }

    /**
     * Constructs a Bearing object from another Bearing object.
     * This can be used to copy a bearing.
     * @param bearing Bearing object to read value from.
     * @return Bearing object.
     */
    public static Bearing fromBearing(Bearing bearing) {
        return Bearing.fromDegrees(bearing.degrees());
    }


    /**
     * Sets the degrees value of the bearing.
     * @param degrees New value of the bearing.
     */
    public void setDegrees(double degrees) {
        //Put degree value in correct interval.
        degrees = Bearing.toBearingInterval(degrees);
        //Update.
        super.setDegrees(degrees);
    }

    /**
     * Sets the radians value of the bearing.
     * @param radians New value of the bearing.
     */
    public void setRadians(double radians) {
        this.setDegrees(Math.toDegrees(radians));
    }


    /**
     * Sets the value of this bearing from another bearing.
     * @param bearing Bearing to copy the value from.
     */
    public void setBearing(Bearing bearing) {
        this.setDegrees(bearing.degrees());
    }



}
