package network.Utils;

import shared.model.Constants;

/**
 * Contains various unit conversion for encoding/decoding messages.
 * Our program uses the "unpacked" units, and the over-the-wire format uses "packed" units (e.g., degrees stored as ints).
 */
public class AC35UnitConverter {


    /**
     * Converts a packed GPSCoordinate (latitude or longitude) into the unpacked unit.
     * @param value Packed lat/long value.
     * @return Unpacked lat/long angle, in degrees.
     */
    public static double unpackGPS(int value) {
        return (double) value * 180.0 / 2147483648.0;//2^31 = 2147483648
    }

    /**
     * Converts a latitude or longitude angle into a packed unit.
     * @param value The lat/long angle, in degrees, to convert.
     * @return The packed value.
     */
    public static int packGPS(double value) {
        return (int) (value * 2147483648.0 / 180.0);//2^31 = 2147483648
    }


    /**
     * Unpacks a heading from an int to an angle in degrees (this is a bearing).
     * @param value The packed value to unpack.
     * @return The unpacked value in degrees.
     */
    public static double unpackHeading(int value) {
        return (value * 360.0 / 65536.0);//2^15
    }

    /**
     * Packs a heading (this is a bearing), in degrees, to a packed int value.
     * @param value The heading in degrees.
     * @return The packed value.
     */
    public static int packHeading(double value) {
        return (int) (value / 360.0 * 65536.0);//2^15
    }


    /**
     * Unpacks a true wind angle from a short to an angle in degrees (this is an azimuth).
     * @param value The packed value to unpack.
     * @return The unpacked value in degrees.
     */
    public static double unpackTrueWindAngle(short value) {
        return (value * 180.0 / 32768.0);//-2^15 to 2^15
    }

    /**
     * Packs a true wind angle (this is an azimuth) from an angle in degrees to a packed short value.
     * @param value The unpacked value in degrees.
     * @return The packed value.
     */
    public static short packTrueWindAngle(double value) {
        return (short) (value / 180.0 * 32768.0);//-2^15 to 2^15
    }


    /**
     * Unpacks a speed, in millimeters per second, to a double, in knots.
     * @param millimetersPerSec Speed in millimeters per second.
     * @return Speed in knots.
     */
    public static double unpackMMperSecToKnots(int millimetersPerSec) {
        return (millimetersPerSec / Constants.KnotsToMMPerSecond);
    }

    /**
     * Packs a speed, in knots, into an int, in millimeters per second.
     * @param speedKnots Speed in knots.
     * @return Speed in millimeters per second.
     */
    public static int packKnotsToMMperSec(double speedKnots) {
        return (int) (speedKnots * Constants.KnotsToMMPerSecond);
    }


    /**
     * Packs an average wind period, in milliseconds, into an int, in tenths of a second.
     * @param periodMilliseconds Period in milliseconds.
     * @return Period in tenths of a second.
     */
    public static int packAverageWindPeriod(long periodMilliseconds) {
        return (int) (periodMilliseconds / 100);
    }

    /**
     * Unpacks an average wind period, in tenths of a second, into an long, in milliseconds.
     * @param periodTenthsOfSecond Period in tenths of a second.
     * @return Period in milliseconds
     */
    public static long unpackAverageWindPeriod(int periodTenthsOfSecond) {
        return (periodTenthsOfSecond * 100);
    }


}
