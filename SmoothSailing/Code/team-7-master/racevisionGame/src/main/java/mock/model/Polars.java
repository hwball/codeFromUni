package mock.model;

import javafx.util.Pair;
import shared.model.Bearing;

import java.util.*;

/**
 * Encapsulates an entire polar table. Has a function to calculate VMG.
 */
public class Polars {

    /**
     * Internal store of data. Maps {@literal Pair<windSpeed, windAngle>} to boatSpeed.
     */
    private Map<Pair<Double, Bearing>, Double> polarValues = new HashMap<>();


    /**
     * Stores a list of angles from the polar table - this is used during the calculateVMG function.
     * Maps between windSpeed and a list of angles for that wind speed.
     */
    private HashMap<Double, List<Bearing>> polarAngles = new HashMap<>();




    /**
     * Ctor.
     */
    public Polars() {
    }


    /**
     * Adds an estimated velocity to the polar table object, for a given (windSpeed, windAngle) pair. That is, stores a mapping from (windSpeed, windAngle) to (boatVelocity).
     * Note: an estimate means given a specific wind speed of trueWindSpeed, if the boat travels relativeWindAngle degrees towards the wind, it will move at boatSpeed knots. E.g., trueWindSpeed = 20kn, relativeWindAngle = 45 degrees, boatSpeed = 25kn. If the boat travels towards the wind, plus or minus 45 degrees either side, it will move at 25kn.
     * @param trueWindSpeed The true wind speed of the estimate.
     * @param relativeWindAngle The relative wind angle between the wind direction + 180 degrees and the boat's direction of the estimate.
     * @param boatSpeed The boat speed of the estimate.
     */
    public void addEstimate(double trueWindSpeed, Bearing relativeWindAngle, double boatSpeed) {

        //We also add the same values with a complementary angle (e.g., angle = 50, complement = 360 - 50 = 310). This is because the data file contains angles [0, 180), but we need [0, 360).

        //Create the array to store angles for this wind speed if it doesn't exist.
        if (!this.polarAngles.containsKey(trueWindSpeed)) {
            this.polarAngles.put(trueWindSpeed, new ArrayList<>());
        }

        //Add estimate to map.
        Pair<Double, Bearing> newKeyPositive = new Pair<>(trueWindSpeed, relativeWindAngle);
        this.polarValues.put(newKeyPositive, boatSpeed);

        //Get the "negative" bearing - that is, the equivalent bearing between [180, 360).
        Bearing negativeBearing = Bearing.fromDegrees(360d - relativeWindAngle.degrees());


        //Ensure that the positive and negative angles aren't the same (e.g., pos = 0, neg = 360 - 0 = 0.
        if (!negativeBearing.equals(relativeWindAngle)) {
            Pair<Double, Bearing> newKeyNegative = new Pair<>(trueWindSpeed, negativeBearing);
            this.polarValues.put(newKeyNegative, boatSpeed);
        }


        //Add angle to angle list. Don't add if it already contains them.
        if (!this.polarAngles.get(trueWindSpeed).contains(relativeWindAngle)) {
            this.polarAngles.get(trueWindSpeed).add(relativeWindAngle);
        }

        if (!this.polarAngles.get(trueWindSpeed).contains(negativeBearing)) {
            this.polarAngles.get(trueWindSpeed).add(negativeBearing);
        }


    }

    /**
     * Calculates the VMG for a given wind angle, wind speed, and angle to destination. Will only return VMGs that have a true bearing (angle) within a given bound - this is to ensure that you can calculate VMGs without going out of bounds.
     * <br>
     * If you don't care about bearing bounds, simply pass in lower = 0, upper = 359.9.
     * <br>
     * Passing in lower = 0, upper = 0, or lower = 0, upper = 360 will both be treated the same as lower = 0, upper = 359.99999.
     * <br><br>
     * The resulting angle of the VMG will be within the interval [bearingLowerBound, bearingUpperBound].
     * <br><br>
     * If the lower bound is greater than the upper bound (e.g., lower = 70, upper = 55), then it checks that {@literal VMGAngle >= lower OR VMGAngle <= upper} (e.g., {@literal [70, 55] means angle >= 70, OR angle =< 55}).
     * <br><br>
     * Returns a VMG with 0 speed and 0 bearing if there are no VMGs with {@literal velocity > 0} in the acceptable bearing bounds.
     * @param trueWindAngle The current true wind angle.
     * @param trueWindSpeed The current true wind speed. Knots.
     * @param destinationAngle The angle between the boat and the destination point.
     * @param bearingLowerBound The lowest bearing (angle) that the boat may travel on.
     * @param bearingUpperBound The highest bearing (angle) that the boat may travel on.
     * @return The VMG.
     */
    public VMG calculateVMG(Bearing trueWindAngle, double trueWindSpeed, Bearing destinationAngle, Bearing bearingLowerBound, Bearing bearingUpperBound) {

        //Sorts polar angles.
        for (List<Bearing> angles : this.polarAngles.values()) {
            angles.sort(null);
        }


        //If the user enters [0, 360] for their bounds, there won't be any accepted angles, as Bearing(360) turn into Bearing(0) (it has the interval [0, 360)).
        //So if both bearing bounds are zero, we assume that the user wanted [0, 360) for the interval.
        //So, we give them Bearing(359.99999) as the upper bound.
        if ((bearingLowerBound.degrees() == 0d) && (bearingUpperBound.degrees() == 0d)) {
            bearingUpperBound = Bearing.fromDegrees(359.99999d);
        }




        //If the lower bound is greater than the upper bound, we have a "flipped" interval. That is for, e.g., [70, 55] the lower bound is greater than the upper bound, and so it checks that (VMGAngle >= 70 OR VMGAngle =< 55), instead of (VMGAngle >= 70 AND VMGAngle =< 55).
        boolean flippedInterval = Polars.isFlippedInterval(bearingLowerBound, bearingUpperBound);



        //We need to find the upper and lower wind speeds from the Polars table, for a given current wind speed (e.g., current wind speed is 11kn, therefore lower = 8kn, upper = 12kn).
        double polarWindSpeedLowerBound = 0d;
        double polarWindSpeedUpperBound = 9999999d;//Start this off with a value larger than any in the Polars table so that it actually works.
        //This indicates whether or not we've managed to find a wind speed larger than the current wind speed (the upper bound) in the Polars table (in cases where the current wind speed is larger than any in the file we will never find an upper bound).
        boolean foundUpperBoundWindSpeed = false;
        boolean foundLowerBoundWindSpeed = false;
        for (Pair<Double, Bearing> key : this.polarValues.keySet()) {

            //The key is Pair<windSpeed, windAngle>, so pair.key is windSpeed.
            double currentPolarSpeed = key.getKey();

            //Lower bound.
            if ((currentPolarSpeed >= polarWindSpeedLowerBound) && (currentPolarSpeed <= trueWindSpeed)) {
                polarWindSpeedLowerBound = currentPolarSpeed;
                foundLowerBoundWindSpeed = true;
            }

            //Upper bound.
            if ((currentPolarSpeed < polarWindSpeedUpperBound) && (currentPolarSpeed > trueWindSpeed)) {
                polarWindSpeedUpperBound = currentPolarSpeed;
                foundUpperBoundWindSpeed = true;
            }

        }



        //Find the angle with the best VMG.
        //We need to find the VMGs for both lower and upper bound wind speeds, and interpolate between them.
        List<VMG> vmgs = new ArrayList<>();

        //Put wind speed bounds we found above into a list.
        List<Double> windSpeedBounds = new ArrayList<>(2);

        if (foundLowerBoundWindSpeed) {
            windSpeedBounds.add(polarWindSpeedLowerBound);
        }
        if (foundUpperBoundWindSpeed) {
            windSpeedBounds.add(polarWindSpeedUpperBound);
        }


        //Calculate VMG for any wind speed bounds we found.
        for (double polarWindSpeed : windSpeedBounds) {

            //The list of polar angles for this wind speed.
            List<Bearing> polarAngles = this.polarAngles.get(polarWindSpeed);


            double bestVMGVelocity = 0;
            double bestVMGSpeed = 0;
            Bearing bestVMGAngle = Bearing.fromDegrees(0d);

            //Calculate the VMG for all possible angles at this wind speed.
            for (double angleDegree = 0; angleDegree < 360; angleDegree += 1) {
                Bearing angle = Bearing.fromDegrees(angleDegree);

                //This is the true bearing of the boat, if it went at the angle against the wind.
                //For angle < 90 OR angle > 270, it means that the boat is going into the wind (tacking).
                //For angle > 90 AND angle < 270, it means that the boat is actually going with the wind (gybing).
                double trueBoatBearingDegrees = trueWindAngle.degrees() + angle.degrees() + 180d;
                Bearing trueBoatBearing = Bearing.fromDegrees(trueBoatBearingDegrees);


                //Check that the boat's bearing would actually be acceptable.
                //We continue (skip to next iteration) if it is outside of the interval.
                if (!Polars.isBearingInsideInterval(trueBoatBearing, bearingLowerBound, bearingUpperBound)) {
                    continue;
                }


                //Basic linear interpolation. Find the nearest two angles from the table, and interpolate between them.

                //Check which pair of adjacent angles the angle is between.
                boolean foundAdjacentAngles = false;
                Bearing lowerBound = Bearing.fromDegrees(0d);
                Bearing upperBound = Bearing.fromDegrees(0d);
                for (int i = 0; i < polarAngles.size() - 1; i++) {
                    Bearing currentAngle = polarAngles.get(i);
                    Bearing nextAngle = polarAngles.get(i + 1);
                    //Check that angle is in interval [lower, upper).
                    if ((angle.degrees() >= currentAngle.degrees()) && (angle.degrees() < nextAngle.degrees())) {
                        foundAdjacentAngles = true;
                        lowerBound = currentAngle;
                        upperBound = nextAngle;
                        break;
                    }
                }

                if (!foundAdjacentAngles) {
                    //If we never found the interval, then it must be the "last" interval, between the i'th and 0'th values - angles are periodic, so they wrap around.
                    lowerBound = polarAngles.get(polarAngles.size() - 1);
                    upperBound = polarAngles.get(0);
                }


                //Calculate how far between those points the angle is.


                //This is how far between the lower and upper bounds the angle is, as a proportion (e.g., 0.5 = half-way, 0.9 = close to upper).
                double interpolationScalar = calculatePeriodicLinearInterpolateScalar(lowerBound.degrees(), upperBound.degrees(), 360, angle.degrees());

                //Get the estimated boat speeds for the lower and upper angles.
                Pair<Double, Bearing> lowerKey = new Pair<>(polarWindSpeed, lowerBound);
                Pair<Double, Bearing> upperKey = new Pair<>(polarWindSpeed, upperBound);
                double lowerSpeed = this.polarValues.get(lowerKey);
                double upperSpeed = this.polarValues.get(upperKey);

                //Calculate the speed at the interpolated angle.
                double interpolatedSpeed = calculateLinearInterpolation(lowerSpeed, upperSpeed, interpolationScalar);


                //This is the delta angle between the boat's true bearing and the destination.
                double angleBetweenDestAndTackDegrees = trueBoatBearing.degrees() - destinationAngle.degrees();
                Bearing angleBetweenDestAndTack = Bearing.fromDegrees(angleBetweenDestAndTackDegrees);

                //This is the estimated velocity towards the target (e.g., angling away from the target reduces velocity).
                double interpolatedVelocity = Math.cos(angleBetweenDestAndTack.radians()) * interpolatedSpeed;


                //Check that the velocity is better, if so, update our best VMG so far, for this wind speed.
                if (interpolatedVelocity > bestVMGVelocity) {
                    bestVMGVelocity = interpolatedVelocity;
                    bestVMGSpeed = interpolatedSpeed;
                    bestVMGAngle = trueBoatBearing;
                }

            }
            //Angle iteration loop is finished.

            //Create the VMG, and add to list.
            VMG vmg = new VMG(bestVMGSpeed, bestVMGAngle);
            vmgs.add(vmg);

        }


        //If we never found an upper bound for the wind speed, we will only have one VMG (for the lower bound), so we can't interpolate/extrapolate anything.
        if (!foundUpperBoundWindSpeed) {
            return vmgs.get(0);
        } else {
            //We may have more than one VMG. If we found an upper and lower bound we will have two, if we only found an upper bound (e.g., wind speed = 2kn, upper = 4kn, lower = n/a) we will only have one VMG, but must interpolate between that and a new VMG with 0kn speed.

            //We do a simple linear interpolation.

            VMG vmg1 = vmgs.get(0);
            VMG vmg2;
            if (vmgs.size() > 1) {
                //If we have a second VMG use it.
                vmg2 = vmgs.get(1);
            } else {
                //Otherwise create a VMG with zero speed, but the same angle. This is what our VMG would be with 0 knot wind speed (boats don't move at 0 knots).
                //We also need to swap them around, as vmg1 needs to be the vmg for the lower bound wind speed, and vmg2 is the upper bound wind speed.
                vmg2 = vmg1;
                vmg1 = new VMG(0, vmg1.getBearing());
            }


            //Get the interpolation scalar for the current wind speed.
            double interpolationScalar = calculateLinearInterpolateScalar(polarWindSpeedLowerBound, polarWindSpeedUpperBound, trueWindSpeed);

            //We then calculate the interpolated VMG speed and angle using the interpolation scalar.
            double interpolatedSpeed = calculateLinearInterpolation(vmg1.getSpeed(), vmg2.getSpeed(), interpolationScalar);
            double interpolatedAngleDegrees = calculateLinearInterpolation(vmg1.getBearing().degrees(), vmg2.getBearing().degrees(), interpolationScalar);

            Bearing interpolatedAngle = Bearing.fromDegrees(interpolatedAngleDegrees);


            //Return the interpolated VMG.
            return new VMG(interpolatedSpeed, interpolatedAngle);

        }


    }


    /**
     * Determines whether an interval is "flipped". This means that the lower bound is greater than the upper bound (e.g., [290, 43] degrees).
     * @param lowerBound The lower bound.
     * @param upperBound The upper bound.
     * @return True if the interval is flipped, false otherwise.
     */
    public static boolean isFlippedInterval(Bearing lowerBound, Bearing upperBound) {

        //If the lower bound is greater than the upper bound, we have a "flipped" interval.
        boolean flippedInterval = false;
        if (lowerBound.degrees() > upperBound.degrees()) {
            flippedInterval = true;
        }

        return flippedInterval;
    }


    /**
     * Determines if a bearing is inside an interval.
     * @param bearing The bearing to check.
     * @param lowerBound The lower bound of the interval.
     * @param upperBound The upper bound of the interval.
     * @return True if the bearing is inside the interval, false otherwise.
     */
    public static boolean isBearingInsideInterval(Bearing bearing, Bearing lowerBound, Bearing upperBound) {

        //Check if it's a flipped interval.
        boolean flippedInterval = Polars.isFlippedInterval(lowerBound, upperBound);

        if (flippedInterval) {
            //Bearing must be inside [lower, upper], where lower > upper. So, bearing must be >= lower, or bearing < upper. We use inverted logic since we are skipping if it is true.
            if ((bearing.degrees() >= lowerBound.degrees()) || (bearing.degrees() <= upperBound.degrees())) {
                return true;
            } else {
                return false;
            }

        } else {
            //Bearing must be inside [lower, upper].
            if ((bearing.degrees() >= lowerBound.degrees()) && (bearing.degrees() <= upperBound.degrees())) {
                return true;
            } else {
                return false;
            }

        }


    }


    /**
     * Calculate the linear interpolation scalar for a value between two bounds. E.g., lower = 7, upper = 10, value = 8, therefore the scalar (or the proportion between the bounds) is 0.333.
     * Also assumes that the bounds are periodic - e.g., for angles a lower bound of 350deg and upper bound of 5deg is in interval of 15 degrees.
     * @param lowerBound The lower bound to interpolate between.
     * @param upperBound The upper bound to interpolate between.
     * @param value The value that sits between the lower and upper bounds.
     * @param period The period of the bounds (e.g., for angles, they have a period of 360 degrees).
     * @return The interpolation scalar for the value between two bounds.
     */
    public static double calculatePeriodicLinearInterpolateScalar(double lowerBound, double upperBound, double period, double value) {

        //This is the "distance" between the value and its lower bound.
        //I.e., L----V-----------U
        //      <----> is lowerDelta.
        double lowerDelta = value - lowerBound;

        //This is the "distance" between the upper and lower bound.
        //I.e., L----V-----------U
        //      <----------------> is intervalDelta.
        //This can potentially be negative if we have, e.g., lower = 340deg, upper = 0deg, delta = -340deg.
        double intervalDelta = upperBound - lowerBound;
        //If it _is_ negative, modulo it to make it positive.
        //E.g., -340deg = +20deg.
        while (intervalDelta < 0) {
            intervalDelta += period;
        }

        //This is how far between the lower and upper bounds the value is, as a proportion (e.g., 0.5 = half-way, 0.9 = close to upper).
        double interpolationScalar = lowerDelta / intervalDelta;

        return interpolationScalar;
    }

    /**
     * Calculate the linear interpolation scalar for a value between two bounds. E.g., lower = 7, upper = 10, value = 8, therefore the scalar (or the proportion between the bounds) is 0.333.
     * Assumes that the upper bound is larger than the lower bound.
     * @param lowerBound The lower bound to interpolate between.
     * @param upperBound The upper bound to interpolate between.
     * @param value The value that sits between the lower and upper bounds.
     * @return The interpolation scalar for the value between two bounds.
     */
    public static double calculateLinearInterpolateScalar(double lowerBound, double upperBound, double value) {

        //This is the "distance" between the value and its lower bound.
        //I.e., L----V-----------U
        //      <----> is lowerDelta.
        double lowerDelta = value - lowerBound;

        //This is the "distance" between the upper and lower bound.
        //I.e., L----V-----------U
        //      <----------------> is intervalDelta.
        double intervalDelta = upperBound - lowerBound;

        //This is how far between the lower and upper bounds the value is, as a proportion (e.g., 0.5 = half-way, 0.9 = close to upper).
        double interpolationScalar = lowerDelta / intervalDelta;

        return interpolationScalar;
    }


    /**
     * Does a linear interpolation between two bounds, using an interpolation scalar - i.e., value = lower + (scalar * delta).
     * @param lowerBound Lower bound to interpolate from.
     * @param upperBound Upper bound to interpolate to.
     * @param interpolationScalar Interpolation scalar - the proportion the target value sits between the two bounds.
     * @return The interpolated value.
     */
    public static double calculateLinearInterpolation(double lowerBound, double upperBound, double interpolationScalar) {

        //Get the delta between upper and lower bounds.
        double boundDelta = upperBound - lowerBound;

        //Calculate the speed at the interpolated angle.
        double interpolatedValue = lowerBound + (boundDelta * interpolationScalar);

        return interpolatedValue;
    }

}
