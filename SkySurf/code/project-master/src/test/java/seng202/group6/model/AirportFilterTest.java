package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.model.Airport;
import seng202.group6.model.AirportFilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anton J on 19/08/2016.
 */
public class AirportFilterTest {
    ArrayList<Airport> airports;
    AirportFilter filter;

    @Before
    public void setUp() {
        filter = new AirportFilter();
        airports = new ArrayList<>();
        airports.add(new Airport("Alpha", "Citya", "Countrya", "aaaa", "aaaa", 10, 10, 100, "1", "who knows", "yay", 1));
        airports.add(new Airport("Alpha", "Citya", "Countrya", "aaaa", "aaaa", 10, 10, 100, "1", "who knows", "yay", 20));
        airports.add(new Airport("Alpha", "Citya", "Countryx", "aaaa", "aaaa", 10, 10, 101, "1", "who knows", "yay", 1));
    }

    @After
    public void tearDown() {
        airports = null;
        filter = null;
    }

    @Test
    public void matchesTestString() {
        filter.addCountry("Countrya");
        assertTrue ("Airport 1 matches filter", filter.matches(airports.get(0)));
        assertTrue ("Airport 2 matches filter", filter.matches(airports.get(1)));
        assertFalse("Airport 3 does not have country 'countrya'", filter.matches(airports.get(2)));
    }

    @Test
    public void matchesTestEmpty() {
        assertTrue("Any airport matches an empty filter", filter.matches(airports.get(0)));
        assertTrue("Any airport matches an empty filter", filter.matches(airports.get(1)));
        assertTrue("Any airport matches an empty filter", filter.matches(airports.get(2)));
    }

    @Test
    public void matchesTestInt() {
        filter.setRoutes(null, 5);
        assertTrue ("Airport 1 matches filter", filter.matches(airports.get(0)));
        assertFalse("Airport 2 does not have rank <= 5", filter.matches(airports.get(1)));
        assertTrue ("Airport 3 matches filter", filter.matches(airports.get(2)));
    }

    @Test
    public void matchesTestDouble() {
        filter.setAltitude(100.0, 100.0);
        assertTrue ("Airport 1 matches filter", filter.matches(airports.get(0)));
        assertTrue ("Airport 2 matches filter", filter.matches(airports.get(1)));
        assertFalse("Airport 3 does not have altitude == 100.0", filter.matches(airports.get(2)));
    }
}
