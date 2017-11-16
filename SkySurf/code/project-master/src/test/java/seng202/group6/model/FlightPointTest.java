package seng202.group6.model;

import org.junit.Test;

import seng202.group6.model.FlightPoint;

import static org.junit.Assert.assertTrue;

/**
 * Tests the class FlightPoint
 */
public class FlightPointTest {

    @Test
    public void equalsTest() {
        FlightPoint line1 = new FlightPoint("type", "id", 1.0, 1.0, 1.0);
        FlightPoint line2 = new FlightPoint("type", "id", 1.0, 1.0, 1.0);
        assertTrue("The flight points are the same", line1.equals(line2));
    }

    @Test
    public void copyTest() {
        FlightPoint origin = new FlightPoint("type", "id", 1.0, 1.0, 1.0);
        assertTrue("The copy is the same as the original",
                origin.copy().equals(origin));
    }
}