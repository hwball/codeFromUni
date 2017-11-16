package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.model.Flight;
import seng202.group6.model.FlightFilter;
import seng202.group6.model.FlightPoint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests class FlightFilter
 */
public class FlightFilterTest {
    ArrayList<Flight> flights;
    FlightFilter filter;
    ArrayList<FlightPoint> points;

    @Before
    public void setUp() {
        filter = new FlightFilter();
        flights = new ArrayList<>();
        points = new ArrayList<>();
        points.add(new FlightPoint("APT", "Source", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("VOR", "abcd123", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("APT", "Dest", 1.0, 1.0, 1.0));
        flights.add(new Flight(points));
        flights.add(new Flight(points));

        points.get(0).setId("Soorse");
        flights.add(new Flight(points));
    }

    @After
    public void tearDown() {
        flights = null;
        filter = null;
        points = null;
    }

    @Test
    public void matchesTestString() {
        filter.addSourceAirport("Source");
        assertTrue("Flight 1 matches filter", filter.matches(flights.get(0)));
        assertTrue("Flight 2 matches filter", filter.matches(flights.get(1)));
        assertFalse("Flight 3 does not have source 'source'", filter.matches(flights.get(2)));
    }

    @Test
    public void matchesTestEmpty() {
        assertTrue("Any flight matches an empty filter", filter.matches(flights.get(0)));
        assertTrue("Any flight matches an empty filter", filter.matches(flights.get(1)));
        assertTrue("Any flight matches an empty filter", filter.matches(flights.get(2)));
    }
}