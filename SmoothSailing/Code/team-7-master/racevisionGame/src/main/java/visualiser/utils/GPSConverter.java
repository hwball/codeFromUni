package visualiser.utils;

import shared.dataInput.RaceDataSource;
import shared.model.GPSCoordinate;
import visualiser.model.GraphCoordinate;

/**
 * Converts GPS coordinates to view volume coordinates. Longitudes are equally spaced at all latitudes,
 * which leads to inaccurate distance measurements close to the poles. This is acceptable as races are
 * not likely to be set there.
 */
public class GPSConverter {
    private double longRight;
    private double longLeft;
    private double latBottom;
    private double latTop;
    /**
     * Conversion factor from longitude to view units
     */
    private double longitudeFactor;
    /**
     * Conversion factor from latitude to view units
     */
    private double latitudeFactor;

    /**
     * Set up projection with default view boundaries from RaceDataSource
     * @param source for view boundaries
     * @param longitudeFactor separation of a degree of longitude in view units
     * @param latitudeFactor separation of a degree of latitude in view units
     */
    public GPSConverter(RaceDataSource source, double longitudeFactor, double latitudeFactor) {
        this.latTop = source.getMapTopLeft().getLatitude();
        this.longLeft = source.getMapTopLeft().getLongitude();
        this.latBottom = source.getMapBottomRight().getLatitude();
        this.longRight = source.getMapBottomRight().getLongitude();
        this.longitudeFactor = longitudeFactor;
        this.latitudeFactor = latitudeFactor;
    }

    /**
     * Converts GPS coordinates to coordinates for container.
     * It is assumed that the provided GPSCoordinate will always be within the GPSCoordinate boundaries of the RaceMap.
     *
     * @param lat GPS latitude
     * @param lon GPS longitude
     * @return GraphCoordinate (pair of doubles)
     * @see GraphCoordinate
     */
    private GraphCoordinate convertGPS(double lat, double lon) {

        //Calculate the width/height, in gps coordinates, of the map.
        double longWidth = longRight - longLeft;
        double latHeight = latBottom - latTop;

        //Calculate the distance between the specified coordinate and the edge of the map.
        double longDelta = lon - longLeft;
        double latDelta = lat - latTop;

        //Calculate the proportion along horizontally, from the left, the coordinate should be.
        double longProportion = longDelta / longWidth;
        //Calculate the proportion along vertically, from the top, the coordinate should be.
        double latProportion = latDelta / latHeight;

        //Check which metric dimension of our map is smaller. We use this to ensure that any rendered stuff retains its correct aspect ratio, and that everything is visible on screen.
        double smallerDimension = Math.min(longitudeFactor, latitudeFactor);

        //Calculate the x and y pixel coordinates.
        //We take the complement of latProportion to flip it.
        double x = (longProportion * smallerDimension);
        double y = (latProportion * smallerDimension);

        //Because we try to maintain the correct aspect ratio, we will end up with "spare" pixels along the larger dimension (e.g., width 800, height 600, 200 extra pixels along width).
        double extraDistance = Math.abs(longitudeFactor - latitudeFactor);
        //We therefore "center" the coordinates along this larger dimension, by adding half of the extra pixels.
        if (longitudeFactor > latitudeFactor) {
            x += extraDistance / 2;
        } else {
            y += extraDistance / 2;
        }


        //Finally, create the GraphCoordinate.
        GraphCoordinate graphCoordinate = new GraphCoordinate(x, y);


        return graphCoordinate;

    }

    /**
     * Converts the GPS Coordinate to GraphCoordinate.
     * It is assumed that the provided GPSCoordinate will always be within the GPSCoordinate boundaries of the RaceMap.
     *
     * @param coordinate GPSCoordinate representation of Latitude and Longitude.
     * @return GraphCoordinate that the GPS is coordinates are to be displayed on the map.
     * @see GraphCoordinate
     * @see GPSCoordinate
     */
    public GraphCoordinate convertGPS(GPSCoordinate coordinate) {
        return convertGPS(coordinate.getLatitude(), coordinate.getLongitude());
    }

    /**
     * Gets the bearing between two coordinates
     * @param coord1 coordinate 1
     * @param coord2 coordinate 2
     * @return return the bearing between the two.
     */
    public static double getAngle(GraphCoordinate coord1, GraphCoordinate coord2){
        return Math.atan2(coord2.getX() - coord1.getX(), coord2.getY() - coord1.getY());
    }

    public double getLongitudeFactor() {
        return longitudeFactor;
    }

    public double getLatitudeFactor() {
        return latitudeFactor;
    }
}
