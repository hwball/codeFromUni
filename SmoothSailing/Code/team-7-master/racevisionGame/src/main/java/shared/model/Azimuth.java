package shared.model;



/**
 * Represents an azimuth.
 * If treated as an absolute azimuth this is the angle between north and a target point.
 * If treated as a relative azimuth, this is the angle between from one target point to the other.
 * It has the interval [-180, 180) degrees, and clockwise is positive.
 */
public class Azimuth extends Angle{


    /**
     * Ctor.
     * This is protected because you need to use the static helper functions {@link #fromDegrees(double)} and {@link #fromRadians(double)} to construct an Azimuth object.
     *
     * @param degrees The value, in degrees, to initialize this Azimuth object with.
     */
    protected Azimuth(double degrees) {
        super(degrees);
    }


    /**
     * Converts an angle in degrees into an angle in degrees in the correct interval for an azimuth - [-180, 180).
     * E.g., converts -183 to 177, or converts 250 to -110, or converts 180 to -180.
     * @param degrees Degree value to convert.
     * @return Degree value in interval [-180, 180).
     */
    public static double toAzimuthInterval(double degrees) {

        return Angle.toPeriodicInterval(degrees, -180d, 180d, 360d);
    }

    /**
     * Constructs an Azimuth object from an angle value in degrees.
     * @param degrees Azimuth value in degrees.
     * @return Azimuth object.
     */
    public static Azimuth fromDegrees(double degrees) {
        //Ensure the angle is in the correct interval.
        double degreesInInterval = Azimuth.toAzimuthInterval(degrees);

        Azimuth azimuth = new Azimuth(degreesInInterval);
        return azimuth;
    }

    /**
     * Constructs an Azimuth object from an angle value in radians.
     * @param radians Azimuth value in radians.
     * @return Azimuth object.
     */
    public static Azimuth fromRadians(double radians) {
        return Azimuth.fromDegrees(Math.toDegrees(radians));
    }


    /**
     * Constructs an Azimuth object from a Bearing object.
     * @param bearing Bearing object to read value from.
     * @return Azimuth object.
     */
    public static Azimuth fromBearing(Bearing bearing) {
        return Azimuth.fromDegrees(bearing.degrees());
    }


    /**
     * Constructs an Azimuth object from another Azimuth object.
     * @param azimuth Azimuth object to read value from.
     * @return Azimuth object.
     */
    public static Azimuth fromAzimuth(Azimuth azimuth) {
        return Azimuth.fromDegrees(azimuth.degrees());
    }


    /**
     * Sets the degrees value of the azimuth.
     * @param degrees New value of the azimuth.
     */
    public void setDegrees(double degrees) {
        //Put degree value in correct interval.
        degrees = Azimuth.toAzimuthInterval(degrees);
        //Update.
        super.setDegrees(degrees);
    }

    /**
     * Sets the radians value of the azimuth.
     * @param radians New value of the azimuth.
     */
    public void setRadians(double radians) {
        this.setDegrees(Math.toDegrees(radians));
    }


    /**
     * Sets the value of this azimuth from another azimuth.
     * @param azimuth Azimuth to copy the value from.
     */
    public void setAzimuth(Azimuth azimuth) {
        this.setDegrees(azimuth.degrees());
    }



}
