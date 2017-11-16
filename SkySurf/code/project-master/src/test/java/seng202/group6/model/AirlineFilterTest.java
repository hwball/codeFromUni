package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.model.Airline;
import seng202.group6.model.AirlineFilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests AirlineFilter class
 */
public class AirlineFilterTest {
    ArrayList<Airline> airlines;
    AirlineFilter filter;

    @Before
    public void setUp() {
        filter = new AirlineFilter();
        airlines = new ArrayList<>();
        airlines.add(new Airline("name", "alias", "iata", "icao", "call sign", "country", true));
        airlines.add(new Airline("name", "alias", "iata", "icao", "call sign", "country", false));
        airlines.add(new Airline("name", "alias", "iata", "icao", "call sign", "poultry", true));
    }

    @After
    public void tearDown() {
        airlines = null;
        filter = null;
    }

    @Test
    public void matchesTestString() {
        filter.addCountry("country");
        assertTrue ("Airline 1 matches filter", filter.matches(airlines.get(0)));
        assertTrue ("Airline 2 matches filter", filter.matches(airlines.get(1)));
        assertFalse("Airline 3 matches does not have country 'country'", filter.matches(airlines.get(2)));
    }

    @Test
    public void matchesTestStringCaseInsensitive() {
        filter.addCountry("Country");
        assertTrue ("Airline 1 matches filter", filter.matches(airlines.get(0)));
        assertTrue ("Airline 2 matches filter", filter.matches(airlines.get(1)));
        assertFalse("Airline 3 matches does not have country 'country'", filter.matches(airlines.get(2)));
    }

    @Test
    public void matchesTestEmpty() {
        assertTrue("Any route matches an empty filter", filter.matches(airlines.get(0)));
        assertTrue("Any route matches an empty filter", filter.matches(airlines.get(1)));
        assertTrue("Any route matches an empty filter", filter.matches(airlines.get(2)));
    }

    @Test
    public void matchesTestBoolean() {
        filter.setActive(true);
        assertTrue ("Airline 1 matches filter", filter.matches(airlines.get(0)));
        assertFalse("Airline 1 is not active", filter.matches(airlines.get(1)));
        assertTrue ("Airline 1 matches filter", filter.matches(airlines.get(2)));
    }
}
