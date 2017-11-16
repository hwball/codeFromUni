package visualiser.model;

/**
 * It is a coordinate representing a location
 * resizable race canvas
 */
public class GraphCoordinate {

    /**
     * X (horizontal) coordinate.
     */
    private final double x;

    /**
     * Y (vertical) coordinate.
     */
    private final double y;

    /**
     * Constructor method.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public GraphCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Returns the X coordinate.
     *
     * @return x axis Coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return y axis Coordinate.
     */
    public double getY() {
        return y;
    }

}
