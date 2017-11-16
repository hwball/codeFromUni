package seng202.group6.model;

import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.model.Flight;
import seng202.group6.model.FlightPoint;

import static org.junit.Assert.assertTrue;

/**
 * Tests the class Flight
 */
public class FlightTest {

    @Test
    public void equalsTest() {
        ArrayList<FlightPoint> points = new ArrayList<>();
        points.add(new FlightPoint("APT", "src*", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("type", "id", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("APT", "dest", 1.0, 1.0, 1.0));
        Flight line1 = new Flight(points);
        Flight line2 = new Flight(points);
        assertTrue("The flights are the same", line1.equals(line2));
    }

    @Test
    public void copyTest() {
        ArrayList<FlightPoint> points = new ArrayList<>();
        points.add(new FlightPoint("APT", "src*", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("type", "id", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("APT", "dest", 1.0, 1.0, 1.0));
        Flight origin = new Flight(points);
        assertTrue("The copy is the same as the original",
                origin.copy().equals(origin));
    }
}