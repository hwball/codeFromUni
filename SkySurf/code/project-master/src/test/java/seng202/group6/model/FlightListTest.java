package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests the class FlightList
 */
public class FlightListTest {
    private FlightList flights;
    private ArrayList<Flight> list;

    @Before
    public void setUp() {
        list = new ArrayList<>();
        ArrayList<FlightPoint> points = new ArrayList<>();
        points.add(new FlightPoint("APT", "src", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("VOR", "ab12", 1.0, 1.0, 1.0));
        points.add(new FlightPoint("APT", "dest", 1.0, 1.0, 1.0));

        points.get(0).setId("src*");
        list.add(new Flight(points));

        points.get(0).setId("src+");
        list.add(new Flight(points));

        points.get(0).setId("src-");
        list.add(new Flight(points));

        flights = new FlightList("name");
        flights.addAll(list);
    }

    @After
    public void tearDown() {
        flights = null;
        list = null;
    }

    @Test
    public void searchByCriteriaTest() {
        FlightFilter filter = new FlightFilter();
        filter.addSourceAirport("src*");
        filter.addSourceAirport("src-");
        List<Flight> results = flights.searchByCriteria(filter);
        assertTrue("Flight 1 matches criteria", results.contains(list.get(0)));
        assertFalse("Flight 2 does not match rank criteria", results.contains(list.get(1)));
        assertTrue("Flight 3 matches criteria", results.contains(list.get(2)));
    }

    @Test
    public void removeFlightTest() {
        flights.remove(list.get(2));
        assertFalse("The third flight should no longer be present",
                flights.getRecords().contains(list.get(2)));
    }
}