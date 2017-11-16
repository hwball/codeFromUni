package mock.model.wind;


import shared.model.Bearing;
import shared.model.Wind;

import java.util.Random;

/**
 * This class generates Wind objects for use in a MockRace.
 * Bounds on bearing and speed can be specified.
 * Wind can be completely random, or random incremental change.
 */
public class RandomWindGenerator implements WindGenerator {

    /**
     * The bearing the wind direction starts at.
     */
    private Bearing windBaselineBearing;

    /**
     * The lower bearing angle that the wind may have.
     */
    private Bearing windBearingLowerBound;

    /**
     * The upper bearing angle that the wind may have.
     */
    private Bearing windBearingUpperBound;



    /**
     * The speed the wind starts at, in knots.
     */
    private double windBaselineSpeed;

    /**
     * The lower speed that the wind may have, in knots.
     */
    private double windSpeedLowerBound;

    /**
     * The upper speed that the wind may have, in knots.
     */
    private double windSpeedUpperBound;


    /**
     * Creates a wind generator, with a baseline, lower bound, and upper bound, for the wind speed and direction.
     * @param windBaselineBearing Baseline wind direction.
     * @param windBearingLowerBound Lower bound for wind direction.
     * @param windBearingUpperBound Upper bound for wind direction.
     * @param windBaselineSpeed Baseline wind speed, in knots.
     * @param windSpeedLowerBound Lower bound for wind speed, in knots.
     * @param windSpeedUpperBound Upper bound for wind speed, in knots.
     */
    public RandomWindGenerator(Bearing windBaselineBearing, Bearing windBearingLowerBound, Bearing windBearingUpperBound, double windBaselineSpeed, double windSpeedLowerBound, double windSpeedUpperBound) {

        this.windBaselineBearing = windBaselineBearing;
        this.windBearingLowerBound = windBearingLowerBound;
        this.windBearingUpperBound = windBearingUpperBound;
        this.windBaselineSpeed = windBaselineSpeed;
        this.windSpeedLowerBound = windSpeedLowerBound;
        this.windSpeedUpperBound = windSpeedUpperBound;

    }


    @Override
    public Wind generateBaselineWind() {
        return new Wind(windBaselineBearing, windBaselineSpeed);
    }

    /**
     * Generates a random Wind object, that is within the provided bounds.
     * @return Generated wind object.
     */
    public Wind generateRandomWind() {

        double windSpeed = generateRandomWindSpeed();
        Bearing windBearing = generateRandomWindBearing();

        return new Wind(windBearing, windSpeed);

    }

    /**
     * Generates a random wind speed within the specified bounds. In knots.
     * @return Wind speed, in knots.
     */
    private double generateRandomWindSpeed() {

        double randomSpeedKnots = generateRandomValueInBounds(windSpeedLowerBound, windSpeedUpperBound);

        return randomSpeedKnots;
    }


    /**
     * Generates a random wind bearing within the specified bounds.
     * @return Wind bearing.
     */
    private Bearing generateRandomWindBearing() {

        double randomBearingDegrees = generateRandomValueInBounds(windBearingLowerBound.degrees(), windBearingUpperBound.degrees());

        return Bearing.fromDegrees(randomBearingDegrees);
    }


    /**
     * Generates a random value within a specified interval.
     * @param lowerBound The lower bound of the interval.
     * @param upperBound The upper bound of the interval.
     * @return A random value within the interval.
     */
    private static double generateRandomValueInBounds(double lowerBound, double upperBound) {

        float proportion = new Random().nextFloat();

        double delta = upperBound - lowerBound;

        double amount = delta * proportion;

        double finalAmount = amount + lowerBound;

        return finalAmount;

    }


    /**
     * Generates a new value within an interval, given a start value, chance to change, and change amount.
     * @param lowerBound Lower bound of interval.
     * @param upperBound Upper bound of interval.
     * @param currentValue The current value to change.
     * @param changeAmount The amount to change by.
     * @param chanceToChange The change to actually change the value.
     * @return The new value.
     */
    private static double generateNextValueInBounds(double lowerBound, double upperBound, double currentValue, double changeAmount, double chanceToChange) {

        float chance = new Random().nextFloat();


        if (chance <= chanceToChange) {
            currentValue += changeAmount;

        } else if (chance <= (2 * chanceToChange)) {
            currentValue -= changeAmount;

        }

        currentValue = clamp(lowerBound, upperBound, currentValue);

        return currentValue;

    }


    @Override
    public Wind generateNextWind(Wind currentWind) {

        double windSpeed = generateNextWindSpeed(currentWind.getWindSpeed());
        Bearing windBearing = generateNextWindBearing(currentWind.getWindDirection());

        return new Wind(windBearing, windSpeed);

    }


    /**
     * Generates the next wind speed to use.
     * @param windSpeed Current wind speed, in knots.
     * @return Next wind speed, in knots.
     */
    private double generateNextWindSpeed(double windSpeed) {

        double chanceToChange = 0.2;
        double changeAmount = 0.1;

        double nextWindSpeed = generateNextValueInBounds(
                windSpeedLowerBound,
                windSpeedUpperBound,
                windSpeed,
                changeAmount,
                chanceToChange);

        return nextWindSpeed;
    }


    /**
     * Generates the next wind speed to use.
     * @param windBearing Current wind bearing.
     * @return Next wind speed.
     */
    private Bearing generateNextWindBearing(Bearing windBearing) {

        double chanceToChange = 0.2;
        double changeAmount = 0.5;

        double nextWindBearingDegrees = generateNextValueInBounds(
                windBearingLowerBound.degrees(),
                windBearingUpperBound.degrees(),
                windBearing.degrees(),
                changeAmount,
                chanceToChange);

        return Bearing.fromDegrees(nextWindBearingDegrees);
    }





    /**
     * Clamps a value to be within an interval.
     * @param lower Lower bound of the interval.
     * @param upper Upper bound of the interval.
     * @param value Value to clamp.
     * @return The clamped value.
     */
    private static double clamp(double lower, double upper, double value) {

        if (value > upper) {
            value = upper;

        } else if (value < lower) {
            value = lower;

        }

        return value;
    }





}
