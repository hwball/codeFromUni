package shared.model;

/**
 * This represents an angle.
 * Has functions to return angle as either degrees or radians.
 */
public class Angle implements Comparable<Angle> {

    /**
     * The angle stored in this object.
     * Degrees.
     */
    private double degrees;


    /**
     * Ctor.
     * Don't use this.
     * This is protected because you need to use the static helper functions {@link #fromDegrees(double)} and {@link #fromRadians(double)} to construct an Angle object.
     *
     * @param degrees The value, in degrees, to initialize this Angle object with.
     */
    protected Angle(double degrees) {
        this.degrees = degrees;
    }


    /**
     * Constructs an Angle object from an angle value in degrees.
     * @param degrees Angle value in degrees.
     * @return Angle object.
     */
    public static Angle fromDegrees(double degrees) {
        Angle angle = new Angle(degrees);
        return angle;
    }

    /**
     * Constructs an Angle object from an angle value in radians.
     * @param radians Angle value in radians.
     * @return Angle object.
     */
    public static Angle fromRadians(double radians) {
        return Angle.fromDegrees(Math.toDegrees(radians));
    }



    /**
     * Returns the value of this Angle object, in degrees.
     * @return The value of this Angle object, in degrees.
     */
    public double degrees() {
        return this.degrees;
    }


    /**
     * Returns the value of this Angle object, in radians.
     * @return The value of this Angle object, in radians.
     */
    public double radians() {
        return Math.toRadians(this.degrees);
    }



    /**
     * Returns true if two Angle objects have equal values.
     * @param obj Other angle object to compare.
     * @return True if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        //Cast other side.
        Angle other = (Angle) obj;

        //Compare values.
        if (this.degrees() == other.degrees()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return Double.hashCode(this.degrees);
    }



    /**
     * Returns an int describing the ordering between this angle object, and another.
     * @param o Other angle to compare to.
     * @return {@literal int < 0} if this angle is less than the other angle, {@literal int > 0} if this angle is greater than the other angle, and {@literal int = 0} if this angle is equal to the other angle,
     */
    @Override
    public int compareTo(Angle o) {

        if (this.degrees() < o.degrees()) {
            return -1;
        } else if (this.degrees() > o.degrees()) {
            return 1;
        } else {
            return 0;
        }

    }


    /**
     * Converts an angle to an angle in a given periodic interval (e.g., degrees have a periodic interval of 360, radians have a periodic interval of 2Pi) of [lowerBound, upperBound).
     * @param angle The angle to convert.
     * @param lowerBound The lower bound of the interval.
     * @param upperBound The upper bound of the interval.
     * @param period The period of the interval.
     * @return The angle in the desired periodic interval.
     */
    public static double toPeriodicInterval(double angle, double lowerBound, double upperBound, double period) {


        while (angle >= upperBound) {
            //Too large.
            angle -= period;
        }

        while (angle < lowerBound) {
            //Too small.
            angle += period;
        }

        return angle;

    }



    /**
     * Sets the degrees value of the angle.
     * @param degrees New value of the angle.
     */
    public void setDegrees(double degrees) {
        this.degrees = degrees;
    }

    /**
     * Sets the radians value of the angle.
     * @param radians New value of the angle.
     */
    public void setRadians(double radians) {
        this.setDegrees(Math.toDegrees(radians));
    }

}
