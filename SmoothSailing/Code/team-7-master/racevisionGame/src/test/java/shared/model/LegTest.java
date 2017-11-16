package shared.model;

import org.geotools.referencing.GeodeticCalculator;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LegTest {

    private CompoundMark ORIGIN_COMPOUND_MARKER;


    @Before
    public void setUp() throws Exception {
        ORIGIN_COMPOUND_MARKER = new CompoundMark(
                1,
                "origin",
                new Mark(1, "test mark1", new GPSCoordinate(0, 0))  );
    }


    @Test
    public void calculateDistanceHandles5nmNorth() {
        GeodeticCalculator calc = new GeodeticCalculator();
        calc.setStartingGeographicPoint(0, 0);
        calc.setDirection(0, 5 * Constants.NMToMetersConversion);

        CompoundMark endMarker = getEndMarker(calc.getDestinationGeographicPoint());
        Leg test = new Leg("Test", ORIGIN_COMPOUND_MARKER, endMarker, 0);
        assertEquals(test.getDistanceNauticalMiles(), 5, 1e-8);
    }

    @Test
    public void calculateDistanceHandles12nmEast() {
        GeodeticCalculator calc = new GeodeticCalculator();
        calc.setStartingGeographicPoint(0, 0);
        calc.setDirection(90, 12 * Constants.NMToMetersConversion);

        CompoundMark endMarker = getEndMarker(calc.getDestinationGeographicPoint());
        Leg test = new Leg("Test", ORIGIN_COMPOUND_MARKER, endMarker, 0);
        assertEquals(test.getDistanceNauticalMiles(), 12, 1e-8);
    }

    @Test
    public void calculateDistanceHandlesHalfnmSouth() {
        GeodeticCalculator calc = new GeodeticCalculator();
        calc.setStartingGeographicPoint(0, 0);
        calc.setDirection(180, 0.5 * Constants.NMToMetersConversion);

        CompoundMark endMarker = getEndMarker(calc.getDestinationGeographicPoint());
        Leg test = new Leg("Test", ORIGIN_COMPOUND_MARKER, endMarker, 0);
        assertEquals(test.getDistanceNauticalMiles(), 0.5, 1e-8);
    }

    @Test
    public void calculateDistanceHandlesPoint1nmWest() {
        GeodeticCalculator calc = new GeodeticCalculator();
        calc.setStartingGeographicPoint(0, 0);
        calc.setDirection(-90, 0.1 * Constants.NMToMetersConversion);

        CompoundMark endMarker = getEndMarker(calc.getDestinationGeographicPoint());
        Leg test = new Leg("Test", ORIGIN_COMPOUND_MARKER, endMarker, 0);
        assertEquals(test.getDistanceNauticalMiles(), 0.1, 1e-8);
    }

    @Test
    public void calculateDistanceHandlesZeroDifference() {

        Leg test = new Leg("Test", ORIGIN_COMPOUND_MARKER, ORIGIN_COMPOUND_MARKER, 0);
        assertEquals(test.getDistanceNauticalMiles(), 0, 1e-8);
    }

    private CompoundMark getEndMarker(Point2D point) {

        GPSCoordinate coords = new GPSCoordinate(point.getY(), point.getX());

        return new CompoundMark(
                1,
                "test compound 3",
                new Mark(3, "test mark3", coords)   );

    }
}
