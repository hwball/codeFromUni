package mock.model;

import shared.model.Bearing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * New Polars are the revampe of the old Polars class which interpolates the data after being parsed from the Polar Parser
 * There can only be one NewPolars instance stored statically however if a boat does happen to have a special case it can be assigned.
 */
public class NewPolars {


    //true wind speed, <true wind angle, best boat angle>
    private static Map<Double, TreeMap<Double, Double>> polars = new TreeMap<>();

    public static NewPolars newPolars = null;

    public NewPolars(){
        newPolars = this;
    }

    /**
     * Add polars from the polar table
     * @param trueWindSpeed True Wind Speed that the true wind angle and speed corresponds to
     * @param trueWindAngle True Wind Angle of the race
     * @param boatSpeed The speed the boat should be going at given the true wind angle
     */
    public static void addPolars(double trueWindSpeed, Bearing trueWindAngle, double boatSpeed){
        double tws = trueWindSpeed;
        double bs = boatSpeed;
        double twa = trueWindAngle.degrees() + 180;
        if (!polars.containsKey(tws)){
            polars.put(tws, new TreeMap<>());
        }
        polars.get(tws).putIfAbsent(twa, bs);
        polars.get(tws).putIfAbsent(360d - twa, bs);
    }

    /**
     * Linearly Interpolates this should only be called once per parsing of a polar table
     */
    public static void linearInterpolatePolars(){
        TreeMap<Double, Double> prevTWS = null;
        TreeMap<Double, TreeMap<Double, Double>> iterablePolars = new TreeMap<>(polars);
        //this loop averages out the speed between tow angles
        //Example: Pair one: 0 degrees, 0 knots
        //         Pair two: 3 degrees, 6 knots
        //This loop will add
        //Pair one: 0 degrees, 0 knots
        //Pair two: 1 degrees, 2 knots
        //Pair three: 2 degrees, 4 knots
        //Pair four: 3 degrees, 6 knots
        for (double windSpeed: iterablePolars.keySet()){
            TreeMap<Double, Double> tws = iterablePolars.get(windSpeed);

            if (prevTWS == null){
                prevTWS = tws;
                continue;
            }

            double previousTWA = -1;
            TreeMap<Double, Double> iterableTWS = new TreeMap<>(tws);

            for (double twa: iterableTWS.keySet()){
                if (previousTWA == -1){
                    previousTWA = twa;
                    continue;
                }
                double twaDiff = twa - previousTWA;
                double speedDiff = iterableTWS.get(twa) - iterableTWS.get(previousTWA);
                double prevSpeed  = iterableTWS.get(previousTWA);
                double diff = speedDiff/twaDiff;

                for (double i = previousTWA; i < twa; i ++){
                    double mult = i - previousTWA;
                    double newSpeed = diff * mult + prevSpeed;
                    tws.put(i, newSpeed);
                }
                previousTWA = twa;
            }
        }
    }

    private static double getClosest(double value, Set<Double> set){
        double closestVal = 0;
        double smallestDiff = Double.MAX_VALUE;
        for (double d: set){
            double diff = Math.abs(value - d);
            if (diff < smallestDiff){
                closestVal = d;
                smallestDiff = diff;
            }
        }
        return closestVal;
    }

    /**
     * Determines which quadrant degrees are in
     *           0/360 Degrees
     * Quadrant 4 | Quadrant 1
     * -----------------------
     * Quadrant 3 | Quadrant 2
     * @param degrees
     * @return
     */
    private static int getQuadrant(double degrees){
        return (int) modulateAngle(degrees) / 90 + 1;
    }

    private static double getBestSpeedInQuadrant(int quad, Map<Double, Double> set){
        double min = (quad - 1)* 90;
        double max = quad * 90;
        double maxAngle = 0;
        double maxSpeed = 0;
        double dupAngle = 0;
        //DupAngle will average the angle between maxAngles that have the same speed
        //Example: if 150 degrees, 180 degrees, and 210 degrees all go at 10 knots
        //then the average will be taken as (150 + 210) / 2 and the angle will be returned on that.
        for (Double s: set.keySet()){
            if (s >= min && s < max){
                if (set.get(s) > maxSpeed){
                    dupAngle = 0;
                    maxAngle = s;
                    maxSpeed = set.get(s);
                } else if (set.get(s) == maxSpeed){
                    dupAngle = s;
                }
            }
        }
        if (dupAngle != 0 ){
            return getClosest((dupAngle + maxAngle) / 2, set.keySet());
        }
        return maxAngle;
    }

    /**
     * Returns the best VMG that the boat can change to given it's current diagonal heading direction.
     * @param trueWindAngle True wind angle of the race
     * @param trueWindSpeed True wind speed of the race
     * @param boatAngle Angle that the boat is currently at
     * @return the best vmg that the boat can change to
     */
    public static VMG setBestVMG(Bearing trueWindAngle, double trueWindSpeed, Bearing boatAngle){
        double closestSpeed = getClosest(trueWindSpeed, polars.keySet());

        double angle = modulateAngle(boatAngle.degrees() - trueWindAngle.degrees());
        int quad = getQuadrant(angle);
        double bestAngle = getBestSpeedInQuadrant(quad, polars.get(closestSpeed));

        double boatSpeed = polars.get(closestSpeed).get(bestAngle);

        double newAngle = modulateAngle(bestAngle + trueWindAngle.degrees());

        return new VMG(boatSpeed, Bearing.fromDegrees(newAngle));
    }

    /**
     * Calculates the speed that a certain angle should be doing
     * @param trueWindAngle TrueWind Angle of the race
     * @param trueWindSpeed True Wind Speed of the race
     * @param boatAngle Angle that the boat is current at
     * @return the speed that the boat should be traveling at.
     */
    public static double calculateSpeed(Bearing trueWindAngle, double trueWindSpeed, Bearing boatAngle){
        //speed
        double closestSpeed = getClosest(trueWindSpeed, polars.keySet());

        double angleDiff = modulateAngle(boatAngle.degrees() - trueWindAngle.degrees());
        double closestAngle = getClosest(angleDiff, polars.get(closestSpeed).keySet());

        double boatSpeed = polars.get(closestSpeed).get(closestAngle);

        return boatSpeed;


    }

    /**
     * gets the angle bound between 0 and 360 following modular arithmetic
     * @param angle angle to modulate
     * @return resultant angle after modulation.
     */
    public static double modulateAngle(double angle){
        return (angle % 360 + 360) % 360;
    }

    /**
     * DO NOT DELETE THIS FUNCTIONS THEY ARE USED FOR TESTING PURPOSES
     * @return current polars map
     */
    @SuppressWarnings("unused")
    private Map<Double, TreeMap<Double, Double>> getPolars(){
        //this function is just for testing so therefore it is private
        return polars;
    }

    /**
     * DO NOT DELETE THESE FUNCTIONS THEY ARE USED FOR TESTING PURPOSES
     */
    @SuppressWarnings("unused")
    private void printOutLinearInterpolated(){
        for (double tws: polars.keySet()){
            System.out.println("==================================================");
            System.out.println("Speed: " + tws);
            System.out.println("==================================================");
            for (double twa: polars.get(tws).keySet()){
                System.out.println("TWA: " + twa + ", Boat Speed: " + polars.get(tws).get(twa));
            }
        }
    }

}
