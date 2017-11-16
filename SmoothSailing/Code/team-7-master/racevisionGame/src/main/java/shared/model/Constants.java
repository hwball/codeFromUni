package shared.model;

/**
 * Constants that are used throughout the program
 * Created by Erika on 19-Mar-17.
 */
public class Constants {
    /**
    * Multiply by this factor to convert nautical miles to meters.
    * <br>
    * Divide by this factor to convert meters to nautical miles.
     * <br>
    * 1 nautical mile = 1852 meters.
    */
    public static final int NMToMetersConversion = 1852;

    /**
    * Multiply by this factor to convert Knots to millimeters per second.
     * <br>
    * Divide by this factor to convert millimeters per second to Knots.
     * <br>
    * 1 knot = 514.444 millimeters per second.
    */
    public static final double KnotsToMMPerSecond = 514.444;

    /**
     * The scale factor of the race.
     * Frame periods are multiplied by this to get the amount of time a single frame represents.
     * E.g., frame period = 20ms, scale = 5, frame represents 20 * 5 = 100ms, and so boats are simulated for 100ms, even though only 20ms actually occurred.
     */
    public static final int RaceTimeScale = 1;

    /**
     * The race pre-start time, in milliseconds. 30 seconds.
     * Official time is 3 minutes.
     */
    public static final long RacePreStartTime = 30 * 1000;

    /**
     * The race preparatory time, in milliseconds. 10 seconds.
     * Official time is 1 minute.
     */
    public static final long RacePreparatoryTime = 10 * 1000;
    /**
     * The number of milliseconds in one hour.
     * <br>
     * Multiply by this factor to convert milliseconds to hours.
     * <br>
     * Divide by this factor to convert hours to milliseconds.
     */
    public static long OneHourMilliseconds = 1 * 60 * 60 * 1000;

    /**
     * The number of seconds in one hour.
     * <br>
     * Multiply by this factor to convert seconds to hours.
     * <br>
     * Divide by this factor to convert hours to seconds.
     */
    public static long OneHourSeconds = 1 * 60 * 60;
}
