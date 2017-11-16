package shared.model;

import javafx.util.Pair;
import org.geotools.referencing.GeodeticCalculator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * GPS Coordinate for the world map, containing a longitude and latitude.
 * Created by esa46 on 15/03/17.
 */
public class GPSCoordinate {

    /**
     * The latitude of the coordinate.
     */
    private double latitude;

    /**
     * The longitude of the coordinate.
     */
    private double longitude;



    /**
     * Constructs a GPSCoordinate from a latitude and longitude value.
     * @param latitude  Latitude the coordinate is located at.
     * @param longitude Longitude that the coordinate is located at.
     */
    public GPSCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }



    /**
     * Gets the Latitude that the Coordinate is at.
     *
     * @return Returns the latitude of the Coordinate.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the Longitude that the Coordinate is at.
     *
     * @return Returns the longitude of the Coordinate.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * To String method of the Coordinate in the form Latitude: $f, Longitude: $f.
     *
     * @return A String representation of the GPSCoordinate Class.
     */
    public String toString() {
        return String.format("Latitude: %f, Longitude: %f", latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GPSCoordinate that = (GPSCoordinate) o;

        if (Math.abs(this.latitude - that.latitude) > 1e-7) return false;
        return (Math.abs(this.longitude - that.longitude) < 1e-7);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Calculates min and max values and passed it to calculate if coordinate is in the boundary
     * @param coordinate coordinate of interest
     * @param boundary List of points which make a boundary
     * @return true if coordinate is in the boundary
     */
    public static boolean isInsideBoundary(GPSCoordinate coordinate, List<GPSCoordinate> boundary) {
        int length = boundary.size();

        boolean inside = false;

        // Check if inside using ray casting algorithm
        for (int i = 0, j = length - 1; i < length; j = i++) {
            if (intersects(boundary.get(i), boundary.get(j), coordinate)) {
                inside = !inside;
            }


        }
        return inside;

    }

    /**
     * Calculates if the coordinate is in the boundary
     * @param coordinate coordinate of interest
     * @param boundary List of points which make a boundary
     * @param minValues max GPS
     * @param maxValues min GPS
     * @return true if coordinate is in the boundary
     */
    public static boolean isInsideBoundary(GPSCoordinate coordinate, List<GPSCoordinate> boundary, GPSCoordinate minValues, GPSCoordinate maxValues) {
        double minLat = minValues.getLatitude();
        double minLon = minValues.getLongitude();
        double maxLat = maxValues.getLatitude();
        double maxLon = maxValues.getLongitude();
        double coordinateLat = coordinate.getLatitude();
        double coordinateLon = coordinate.getLongitude();
        // End computation early
        if (coordinateLat <= minLat || coordinateLat >= maxLat || coordinateLon <= minLon || coordinateLon >= maxLon) {
            return false;
        } else {
            return isInsideBoundary(coordinate, boundary);
        }
    }


    /**
     * Helper function to find if a point is in a boundary
     * @param boundaryA The first coordinate of the boundary.
     * @param boundaryB The second coordinate of the boundary.
     * @param coordinate The coordinate to test.
     * @return true if a line from the point intersects the two boundary points
     */
    public static boolean intersects(GPSCoordinate boundaryA, GPSCoordinate boundaryB, GPSCoordinate coordinate) {
        double boundaryALat = boundaryA.getLatitude();
        double boundaryALon = boundaryA.getLongitude();
        double boundaryBLat = boundaryB.getLatitude();
        double boundaryBLon = boundaryB.getLongitude();
        double coordinateLat = coordinate.getLatitude();
        double coordinateLon = coordinate.getLongitude();

        if (boundaryALat > boundaryBLat) {
            return intersects(boundaryB, boundaryA, coordinate);
        }
        if (coordinateLat == boundaryALat || coordinateLat == boundaryBLat) {
            // Move coordinate off intersection line
            coordinateLat += 1e-9;
        }
        if (coordinateLat > boundaryBLat || coordinateLat < boundaryALat || coordinateLon > Double.max(boundaryALon, boundaryBLon)) {
            return false;
        }
        if (coordinateLon < Double.min(boundaryALon, boundaryBLon)) {
            return true;
        }

        double aRatio = (coordinateLat - boundaryALat) / (coordinateLon - boundaryALon);
        double bRatio = (coordinateLat - boundaryBLat) / (coordinateLon - boundaryBLon);
        return aRatio >= bRatio;

    }

    /**
     * Checks to see if a point passes or lands on a line
     * @param linePointA first point for a line
     * @param linePointB second point for a line
     * @param point point to check
     * @param directionBearing direction of the correct side of line
     * @return true if on the correct side
     */
    public static boolean passesLine(GPSCoordinate linePointA, GPSCoordinate linePointB, GPSCoordinate point, Bearing directionBearing) {
        double d = lineCheck(linePointA, linePointB, point);//this gives a number < 0 for one side and > 0 for an other

        //to find if the side is the correct one
        //compare with point that is known on the correct side
        GPSCoordinate pointForComparison = GPSCoordinate.calculateNewPosition(linePointA,
                250, Azimuth.fromDegrees(directionBearing.degrees()));
        double d2 = lineCheck(linePointA, linePointB, pointForComparison);

        return (d > 0 && d2 > 0) || (d < 0 && d2 < 0) || d == 0;
    }

    /**
     * returns a double that is positive or negative based on which
     * side of the line it is on. returns 0 if it is on the line
     * @param linePointA first point to make up the line
     * @param linePointB second point to make up the line
     * @param point the point to check
     * @return greater than 0 for one side, less than 0 for another
     */
    private static double lineCheck(GPSCoordinate linePointA, GPSCoordinate linePointB, GPSCoordinate point) {
        double linePointALat = linePointA.getLatitude();
        double linePointALon = linePointA.getLongitude();
        double linePointBLat = linePointB.getLatitude();
        double linePointBLon = linePointB.getLongitude();
        double pointLat = point.getLatitude();
        double pointLon = point.getLongitude();

        double d1 = (pointLat - linePointALat) * (linePointBLon - linePointALon);
        double d2 = (pointLon - linePointALon) * (linePointBLat - linePointALat);
        return d1 - d2; //this gives a number < 0 for one side and > 0 for an other
    }


    /**
     * Calculates the azimuth between two points.
     * @param start The starting point.
     * @param end The ending point.
     * @return The azimuth from the start point to the end point.
     */
    public static Azimuth calculateAzimuth(GPSCoordinate start, GPSCoordinate end) {

        GeodeticCalculator calc = new GeodeticCalculator();

        calc.setStartingGeographicPoint(start.getLongitude(), start.getLatitude());
        calc.setDestinationGeographicPoint(end.getLongitude(), end.getLatitude());

        return Azimuth.fromDegrees(calc.getAzimuth());
    }


    /**
     * Calculates the bearing between two points.
     * @param start The starting point.
     * @param end The ending point.
     * @return The Bearing from the start point to the end point.
     */
    public static Bearing calculateBearing(GPSCoordinate start, GPSCoordinate end) {

        //Calculates the azimuth between the two points.
        Azimuth azimuth = GPSCoordinate.calculateAzimuth(start, end);

        //And converts it into a bearing.
        return Bearing.fromAzimuth(azimuth);
    }


    /**
     * Calculates the distance, in meters, between two points.
     * Note: all other distance calculations (e.g., nautical miles between two points) should use this, and convert from meters to their desired unit.
     * @param start The starting point.
     * @param end The ending point.
     * @return The distance, in meters, between the two given points.
     */
    public static double calculateDistanceMeters(GPSCoordinate start, GPSCoordinate end) {

        GeodeticCalculator calc = new GeodeticCalculator();

        calc.setStartingGeographicPoint(start.getLongitude(), start.getLatitude());
        calc.setDestinationGeographicPoint(end.getLongitude(), end.getLatitude());

        double distanceMeters = calc.getOrthodromicDistance();

        return  distanceMeters;
    }


    /**
     * Calculates the distance, in nautical miles, between two points.
     * @param start The starting point.
     * @param end The ending point.
     * @return The distance, in nautical miles, between the two given points.
     */
    public static double calculateDistanceNauticalMiles(GPSCoordinate start, GPSCoordinate end) {
        //Find distance in meters.
        double distanceMeters = GPSCoordinate.calculateDistanceMeters(start, end);

        //Convert to nautical miles.
        double distanceNauticalMiles = distanceMeters / Constants.NMToMetersConversion;

        return distanceNauticalMiles;
    }


    /**
     * Calculates the GPS position an entity will be at, given a starting position, distance (in meters), and an azimuth.
     *
     * @param oldCoordinates GPS coordinates of the entity's starting position.
     * @param distanceMeters The distance in meters.
     * @param azimuth The entity's current azimuth.
     * @return The entity's new coordinate.
     */
    public static GPSCoordinate calculateNewPosition(GPSCoordinate oldCoordinates, double distanceMeters, Azimuth azimuth) {


        GeodeticCalculator calc = new GeodeticCalculator();

        //Set starting position.
        calc.setStartingGeographicPoint(oldCoordinates.getLongitude(), oldCoordinates.getLatitude());

        //Set direction.
        calc.setDirection(azimuth.degrees(), distanceMeters);

        //Get the destination.
        Point2D destinationPoint = calc.getDestinationGeographicPoint();

        return new GPSCoordinate(destinationPoint.getY(), destinationPoint.getX());
    }


    /**
     * Calculates the average coordinate of two coordinates.
     * @param point1 The first coordinate to average.
     * @param point2 The second coordinate to average.
     * @return The average of the two coordinates.
     */
    public static GPSCoordinate calculateAverageCoordinate(GPSCoordinate point1, GPSCoordinate point2) {

        //Calculate distance between them.
        double distanceMeters = GPSCoordinate.calculateDistanceMeters(point1, point2);
        //We want the average, so get half the distance between points.
        distanceMeters = distanceMeters / 2d;

        //Calculate azimuth between them.
        Azimuth azimuth = GPSCoordinate.calculateAzimuth(point1, point2);

        //Calculate the middle coordinate.
        GPSCoordinate middleCoordinate = GPSCoordinate.calculateNewPosition(point1, distanceMeters, azimuth);

        return middleCoordinate;

    }


    /**
     * Takes a list of GPS coordinates describing a course boundary, and "shrinks" it inwards by 50m.
     * @param boundary The boundary of course.
     * @return A copy of the course boundary list, shrunk inwards by 50m.
     */
    public static List<GPSCoordinate> getShrinkBoundary(List<GPSCoordinate> boundary) {

        //TODO shrinkDistance should be a parameter. Also the code should be refactored to be smaller/simpler.

        double shrinkDistance = 50d;
        List<GPSCoordinate> shrunkBoundary = new ArrayList<>(boundary.size());
        //This is a list of edges that have been shrunk/shifted inwards.
        List<Pair<GPSCoordinate, GPSCoordinate>> shrunkEdges = new ArrayList<>();


        //We need to invert some of our operations depending if the boundary is clockwise or anti-clockwise.
        boolean isClockwise = GPSCoordinate.isClockwisePolygon(boundary);
        double clockwiseScaleFactor = 0;

        if (isClockwise) {
            clockwiseScaleFactor = 1;
        } else {
            clockwiseScaleFactor = -1;
        }


        /*
          Starting at a vertex, face anti-clockwise along an adjacent edge.
         Replace the edge with a new, parallel edge placed at distance d to the "left" of the old one.
         Repeat for all edges.
         Find the intersections of the new edges to get the new vertices.
         Detect if you've become a crossed polynomial and decide what to do about it. Probably add a new vertex at the crossing-point and get rid of some old ones. I'm not sure whether there's a better way to detect this than just to compare every pair of non-adjacent edges to see if their intersection lies between both pairs of vertices.
         */

        //For the first (size-1) adjacent pairs.
        for (int i = 0; i < (boundary.size() - 1); i++) {

            //Get the points.
            GPSCoordinate firstPoint = boundary.get(i);
            GPSCoordinate secondPoint = boundary.get(i + 1);

            //Get the bearing between two adjacent points.
            Bearing bearing = GPSCoordinate.calculateBearing(firstPoint, secondPoint);

            //Calculate angle perpindicular to bearing.
            Bearing perpindicularBearing = Bearing.fromDegrees(bearing.degrees() + (90d * clockwiseScaleFactor));

            //Translate both first and second point by 50m, using this bearing. These form our inwards shifted edge.
            GPSCoordinate firstPointTranslated = GPSCoordinate.calculateNewPosition(firstPoint, shrinkDistance, Azimuth.fromBearing(perpindicularBearing));
            GPSCoordinate secondPointTranslated = GPSCoordinate.calculateNewPosition(secondPoint, shrinkDistance, Azimuth.fromBearing(perpindicularBearing));

            //Add edge to list.
            shrunkEdges.add(new Pair<>(firstPointTranslated, secondPointTranslated));

        }

        //For the final adjacent pair, between the last and first point.
        //Get the points.
        GPSCoordinate firstPoint = boundary.get(boundary.size() - 1);
        GPSCoordinate secondPoint = boundary.get(0);

        //Get the bearing between two adjacent points.
        Bearing bearing = GPSCoordinate.calculateBearing(firstPoint, secondPoint);

        //Calculate angle perpindicular to bearing.
        Bearing perpindicularBearing = Bearing.fromDegrees(bearing.degrees() + (90d * clockwiseScaleFactor));

        //Translate both first and second point by 50m, using this bearing. These form our inwards shifted edge.
        GPSCoordinate firstPointTranslated = GPSCoordinate.calculateNewPosition(firstPoint, shrinkDistance, Azimuth.fromBearing(perpindicularBearing));
        GPSCoordinate secondPointTranslated = GPSCoordinate.calculateNewPosition(secondPoint, shrinkDistance, Azimuth.fromBearing(perpindicularBearing));

        //Add edge to list.
        shrunkEdges.add(new Pair<>(firstPointTranslated, secondPointTranslated));


        //We now have a list of edges that have been shifted inwards.
        //We need to find the intersections between adjacent vertices in our edge list. E.g., intersection between edge1-right, and edge2-left.

        //For the first (size-1) adjacent pairs.
        for (int i = 0; i < (shrunkEdges.size() - 1); i++) {

            //Get the pair of adjacent edges.
            Pair<GPSCoordinate, GPSCoordinate> edge1 = shrunkEdges.get(i);
            Pair<GPSCoordinate, GPSCoordinate> edge2 = shrunkEdges.get(i + 1);

            //Get the x and y coordinates of first edge.
            double x1 = edge1.getKey().getLongitude();
            double x2 = edge1.getValue().getLongitude();
            double y1 = edge1.getKey().getLatitude();
            double y2 = edge1.getValue().getLatitude();

            //Get the x and y coordinates of second edge.
            double x3 = edge2.getKey().getLongitude();
            double x4 = edge2.getValue().getLongitude();
            double y3 = edge2.getKey().getLatitude();
            double y4 = edge2.getValue().getLatitude();

            //Find the equations for both edges.
            // y = a*x + b
            //First equation.
            double a1 = (y2 - y1) / (x2 - x1);
            double b1 = y1 - a1 * x1;

            //Second equation.
            double a2 = (y4 - y3) / (x4 - x3);
            double b2 = y3 - a2 * x3;


            //Find intersecting x coordinate.
            // a1 * x + b1 = a2 * x + b2
            double x0 = -(b1 - b2) / (a1 - a2);
            //Find intersecting y coordinate.
            double y0 = x0 * a1 + b1;

            //Add this to shrunk boundary list.
            GPSCoordinate coordinate = new GPSCoordinate(y0, x0);
            shrunkBoundary.add(coordinate);

        }


        //For the final adjacent pair, between the last and first point.
        //Get the pair of adjacent edges.
        Pair<GPSCoordinate, GPSCoordinate> edge1 = shrunkEdges.get(shrunkEdges.size() - 1);
        Pair<GPSCoordinate, GPSCoordinate> edge2 = shrunkEdges.get(0);

        //Get the x and y coordinates of first edge.
        double x1 = edge1.getKey().getLongitude();
        double x2 = edge1.getValue().getLongitude();
        double y1 = edge1.getKey().getLatitude();
        double y2 = edge1.getValue().getLatitude();

        //Get the x and y coordinates of second edge.
        double x3 = edge2.getKey().getLongitude();
        double x4 = edge2.getValue().getLongitude();
        double y3 = edge2.getKey().getLatitude();
        double y4 = edge2.getValue().getLatitude();

        //Find the equations for both edges.
        // y = a*x + b
        //First equation.
        double a1 = (y2 - y1) / (x2 - x1);
        double b1 = y1 - a1 * x1;

        //Second equation.
        double a2 = (y4 - y3) / (x4 - x3);
        double b2 = y3 - a2 * x3;


        //Find intersecting x coordinate.
        // a1 * x + b1 = a2 * x + b2
        double x0 = -(b1 - b2) / (a1 - a2);
        //Find intersecting y coordinate.
        double y0 = x0 * a1 + b1;

        //Add this to shrunk boundary list.
        GPSCoordinate coordinate = new GPSCoordinate(y0, x0);
        shrunkBoundary.add(coordinate);



        return shrunkBoundary;

    }


    /**
     * Determines if a list of coordinates describes a boundary polygon in clockwise or anti-clockwise order.
     * @param boundary The list of coordinates.
     * @return True if clockwise, false if anti-clockwise.
     */
    public static boolean isClockwisePolygon(List<GPSCoordinate> boundary) {

        /* From https://stackoverflow.com/questions/1165647/how-to-determine-if-a-list-of-polygon-points-are-in-clockwise-order
          sum all pairs (x2 − x1)(y2 + y1)
         point[0] = (5,0)   edge[0]: (6-5)(4+0) =   4
         point[1] = (6,4)   edge[1]: (4-6)(5+4) = -18
         point[2] = (4,5)   edge[2]: (1-4)(5+5) = -30
         point[3] = (1,5)   edge[3]: (1-1)(0+5) =   0
         point[4] = (1,0)   edge[4]: (5-1)(0+0) =   0
         ---
         -44  counter-clockwise
         */

        double sum = 0;

        //For the first (size-1) adjacent pairs.
        for (int i = 0; i < (boundary.size() - 1); i++) {

            //Get the points.
            GPSCoordinate firstPoint = boundary.get(i);
            GPSCoordinate secondPoint = boundary.get(i + 1);

            double xDelta = secondPoint.getLongitude() - firstPoint.getLongitude();
            double ySum = secondPoint.getLatitude() + firstPoint.getLatitude();

            double product = xDelta * ySum;

            sum += product;

        }

        //For the final adjacent pair, between the last and first point.
        //Get the points.
        GPSCoordinate firstPoint = boundary.get(boundary.size() - 1);
        GPSCoordinate secondPoint = boundary.get(0);

        double xDelta = secondPoint.getLongitude() - firstPoint.getLongitude();
        double ySum = secondPoint.getLatitude() + firstPoint.getLatitude();

        double product = xDelta * ySum;

        sum += product;


        //sum > 0 is clockwise, sum < 0 is anticlockwise.
        return sum > 0;


    }

}

