package seng202.group6.utils;

/**
 * Class for storing analysis methods that calculate
 * distance between two points for the user to see
 */
public class DistanceCalculator {
    /**
     * Calculates distance between two points
     *
     * @param srcLongitude Source Longitude
     * @param srcLatitude  Source Latitude
     * @param srcHeight    Source Height
     * @param dstLongitude Destination Longitude
     * @param dstLatitude  Destination Latitude
     * @param dstHeight    Destination Height
     * @return A double representing the distance in meters
     */
    public static double calculateAirpDist(double srcLongitude, double srcLatitude, double srcHeight,
                                           double dstLongitude, double dstLatitude, double dstHeight) {
        final int RADIUS = 6371; // radius of the earth
        Double latDistance = Math.toRadians(dstLatitude - srcLatitude);
        Double lonDistance = Math.toRadians(dstLongitude - srcLongitude);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(srcLatitude)) * Math.cos(Math.toRadians(dstLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = RADIUS * c * 1000; // convert to meters
        double height = srcHeight - dstHeight;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
