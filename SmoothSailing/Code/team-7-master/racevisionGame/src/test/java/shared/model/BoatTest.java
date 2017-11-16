package shared.model;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by esa46 on 22/03/17.
 */
public class BoatTest {


    private GPSCoordinate ORIGIN_COORDS;
    private Boat TEST_BOAT;

    @Before
    public void setUp() {
        ORIGIN_COORDS = new GPSCoordinate(0, 0);
        TEST_BOAT = new Boat(1, "Test", "tt");
        TEST_BOAT.setPosition(ORIGIN_COORDS);
    }

    //TODO these bearing tests could be tidied up to reduce code repetition.
    //TODO also, most of these tests may be more appropriate in GPSCoordinateTest

    @Test
    public void calculateDueNorthAzimuthReturns0() {

        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(50, 0))    );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(startMarker.getAverageGPSCoordinate(), endMarker.getAverageGPSCoordinate()).degrees(), 0, 1e-8);
    }

    @Test
    public void calculateDueSouthAzimuthReturns180() {
        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(-50, 0))   );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(startMarker.getAverageGPSCoordinate(), endMarker.getAverageGPSCoordinate()).degrees(), -180, 1e-8);
    }


    @Test
    public void calculateDueEastAzimuthReturns90() {

        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(0, 50))    );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(startMarker.getAverageGPSCoordinate(), endMarker.getAverageGPSCoordinate()).degrees(), 90, 1e-8);
    }


    @Test
    public void calculateDueWestAzimuthReturnsNegative90() {
        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(0, -50))   );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(startMarker.getAverageGPSCoordinate(), endMarker.getAverageGPSCoordinate()).degrees(), -90, 1e-8);

    }

    @Test
    public void calculateDueNorthHeadingReturns0() {

        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(50, 0))    );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(TEST_BOAT.getPosition(), endMarker.getAverageGPSCoordinate()).degrees(), 0, 1e-8);
    }


    @Test
    public void calculateDueEastHeadingReturns90() {
        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(0, 50))    );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(TEST_BOAT.getPosition(), endMarker.getAverageGPSCoordinate()).degrees(), 90, 1e-8);
    }

    @Test
    public void calculateDueSouthHeadingReturns180() {
        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                new Mark(2, "test mark 2", new GPSCoordinate(-50, 0))   );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateAzimuth(TEST_BOAT.getPosition(), endMarker.getAverageGPSCoordinate()).degrees(), -180, 1e-8);
    }

    @Test
    public void calculateDueWestHeadingReturns270() {
        CompoundMark startMarker = new CompoundMark(
                1,
                "start",
                new Mark(1, "test origin 1", ORIGIN_COORDS) );

        CompoundMark endMarker = new CompoundMark(
                2,
                "end",
                    new Mark(2, "test mark 2", new GPSCoordinate(0, -50))   );

        Leg start = new Leg("Start", startMarker, endMarker, 0);
        TEST_BOAT.setCurrentLeg(start);
        assertEquals(GPSCoordinate.calculateBearing(TEST_BOAT.getPosition(), endMarker.getAverageGPSCoordinate()).degrees(), 270, 1e-8);
    }


    //TODO test Boat#setCurrentLeg and it's effects.
}
