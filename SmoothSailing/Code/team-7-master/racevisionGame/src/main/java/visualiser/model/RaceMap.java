package visualiser.model;

import shared.model.GPSCoordinate;

/**
 * The base size of the map to be used for the
 * resizable race map
 */
public class RaceMap {

    /**
     * The longitude of the left side of the map.
     */
    private double longLeft;

    /**
     * The longitude of the right side of the map.
     */
    private double longRight;

    /**
     * The latitude of the top side of the map.
     */
    private double latTop;

    /**
     * The latitude of the bottom side of the map.
     */
    private double latBottom;


    /**
     * The width, in pixels, of the map.
     */
    private int width;

    /**
     * The height, in pixels, of the map.
     */
    private int height;



    /**
     * Constructor Method.
     *
     * @param latTop     Latitude of the top left point.
     * @param longLeft     Longitude of the top left point.
     * @param latBottom     Latitude of the top right point.
     * @param longRight     Longitude of the top right point.
     * @param width  width that the Canvas the race is to be drawn on is.
     * @param height height that the Canvas the race is to be drawn on is.
     */
    public RaceMap(double latTop, double longLeft, double latBottom, double longRight, int width, int height) {

        //Long/lat edges.
        this.longLeft = longLeft;
        this.longRight = longRight;
        this.latTop = latTop;
        this.latBottom = latBottom;

        //Pixel sizes.
        this.width = width;
        this.height = height;
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


        //Check which pixel dimension of our map is smaller. We use this to ensure that any rendered stuff retains its correct aspect ratio, and that everything is visible on screen.
        int smallerDimension = Math.min(width, height);

        //Calculate the x and y pixel coordinates.
        //We take the complement of latProportion to flip it.
        int x = (int) (longProportion * smallerDimension);
        int y = (int) ((1 - latProportion) * smallerDimension);

        //Because we try to maintain the correct aspect ratio, we will end up with "spare" pixels along the larger dimension (e.g., width 800, height 600, 200 extra pixels along width).
        int extraPixels = Math.abs(width - height);
        //We therefore "center" the coordinates along this larger dimension, by adding half of the extra pixels.
        if (width > height) {
            x += extraPixels / 2;
        } else {
            y += extraPixels / 2;
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
     * Sets the width, in pixels, of our RaceMap.
     * @param width The new width, in pixels, of the RaceMap.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the height, in pixels, of our RaceMap.
     * @param height The new height, in pixels, of the RaceMap.
     */
    public void setHeight(int height) {
        this.height = height;
    }


    /**
     * Updates the bottom right GPS coordinates of the RaceMap.
     * @param bottomRight New bottom right GPS coordinates.
     */
    public void setGPSBotRight(GPSCoordinate bottomRight) {
        this.latBottom = bottomRight.getLatitude();
        this.longRight = bottomRight.getLongitude();
    }


    /**
     * Updates the top left GPS coordinates of the RaceMap.
     * @param topLeft New top left GPS coordinates.
     */
    public void setGPSTopLeft(GPSCoordinate topLeft) {
        this.latTop = topLeft.getLatitude();
        this.longLeft = topLeft.getLongitude();
    }



}
