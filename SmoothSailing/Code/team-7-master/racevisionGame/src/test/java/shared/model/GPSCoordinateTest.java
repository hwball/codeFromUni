package shared.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by jjg64 on 11/05/17.
 */
public class GPSCoordinateTest {
    List<GPSCoordinate> boundary;

    @Before
    public void init() {
        boundary = new ArrayList<>();
    }

    /**
     * -------
     * |     |
     * |   * |
     * -------
     */
    @Test
    public void insideSquareTest() {
        boundary.add(new GPSCoordinate(0, 0));
        boundary.add(new GPSCoordinate(10, 0));
        boundary.add(new GPSCoordinate(10, 10));
        boundary.add(new GPSCoordinate(0, 10));

        GPSCoordinate coordinate = new GPSCoordinate(2, 8);
        boolean inside = GPSCoordinate.isInsideBoundary(coordinate, boundary);
        assertTrue(inside);
    }

    /**
     *   -------
     *   |     |
     * * |     |
     *   -------
     */
    @Test
    public void outsideSquareTest() {
        boundary.add(new GPSCoordinate(0, 0));
        boundary.add(new GPSCoordinate(10, 0));
        boundary.add(new GPSCoordinate(10, 10));
        boundary.add(new GPSCoordinate(0, 10));

        GPSCoordinate coordinate = new GPSCoordinate(-2, 8);
        boolean inside = GPSCoordinate.isInsideBoundary(coordinate, boundary);
        assertFalse(inside);
    }

    @Test
    public void insideShapeWithObtuseAnglesTest() {
        boundary.add(new GPSCoordinate(0, 0));
        boundary.add(new GPSCoordinate(4, 4));
        boundary.add(new GPSCoordinate(7, 2));
        boundary.add(new GPSCoordinate(9, 5));
        boundary.add(new GPSCoordinate(10, 10));
        boundary.add(new GPSCoordinate(6, 6));
        boundary.add(new GPSCoordinate(2, 10));

        GPSCoordinate coordinate = new GPSCoordinate(5, 5);
        boolean inside = GPSCoordinate.isInsideBoundary(coordinate, boundary);
        assertTrue(inside);
    }

    @Test
    public void outsideShapeWithObtuseAnglesTest() {
        boundary.add(new GPSCoordinate(0, 0));
        boundary.add(new GPSCoordinate(4, 4));
        boundary.add(new GPSCoordinate(7, 2));
        boundary.add(new GPSCoordinate(9, 5));
        boundary.add(new GPSCoordinate(10, 10));
        boundary.add(new GPSCoordinate(6, 6));
        boundary.add(new GPSCoordinate(2, 10));

        GPSCoordinate coordinate = new GPSCoordinate(4, 3);
        boolean inside = GPSCoordinate.isInsideBoundary(coordinate, boundary);
        assertFalse(inside);
    }

    /**
     *   -------
     *   |     |
     * * |     |
     *   -------
     */
    @Test
    public void earlyTerminationTest() {
        boundary.add(new GPSCoordinate(0, 0));
        boundary.add(new GPSCoordinate(10, 0));
        boundary.add(new GPSCoordinate(10, 10));
        boundary.add(new GPSCoordinate(0, 10));

        GPSCoordinate coordinate = new GPSCoordinate(-2, 8);
        boolean inside = GPSCoordinate.isInsideBoundary(coordinate, boundary, new GPSCoordinate(0, 0), new GPSCoordinate(10, 10));
        assertFalse(inside);
    }
}
