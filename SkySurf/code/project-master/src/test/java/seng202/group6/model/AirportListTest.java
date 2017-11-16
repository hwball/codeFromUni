package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests class AirportList
 */
public class AirportListTest {
    private AirportList airports;
    private ArrayList<Airport> list;

    @Before
    public void setUp() {
        list = new ArrayList<>();
        list.add(new Airport("Alpha", "Citya", "Countrya", "aaaa", "aaa", 10, 10, 100, "1", "who knows", "yay", 1));
        list.add(new Airport("Alpha", "Citya", "Countrya", "bbbb", "bbb", 10, 10, 100, "1", "who knows", "yay", 20));
        list.add(new Airport("Alpha", "Citya", "Countrya", "cccc", "ccc", 10, 10, 101, "1", "who knows", "yay", 1));

        airports = new AirportList("name");
        airports.addAll(list);
    }

    @After
    public void tearDown() {
        airports = null;
        list = null;
    }

    @Test
    public void searchByCriteriaTest() {
        AirportFilter filter = new AirportFilter();
        filter.setAltitude(100.0, null);
        filter.setRoutes(1, 1);
        filter.addName("Alpha");
        List<Airport> results = airports.searchByCriteria(filter);
        assertTrue("Airport 1 matches criteria", results.contains(list.get(0)));
        assertFalse("Airport 2 does not match rank criteria", results.contains(list.get(1)));
        assertTrue("Airport 3 matches criteria", results.contains(list.get(2)));
    }

    @Test
    public void recordPresentTest() {
        assertTrue("Airports with IATA 'aaaa' is present", airports.recordUniquelyPresent("aaaa"));
        assertFalse("No airport with IATA 'fake' is present", airports.recordUniquelyPresent("fake"));
    }

    @Test
    public void removeAirportTest() {
        airports.remove(list.get(2));
        assertFalse("The third airport should no longer be present",
                airports.getRecords().contains(list.get(2)));
    }

    @Test
    public void setNumberOfRoutesTest() {
        HashMap<String, Integer> nRoutes = new HashMap<>();
        nRoutes.put("aaaa", 5);
        nRoutes.put("bbbb", 0);
        airports.setNumberOfRoutes(nRoutes, true);
        assertEquals("Airport 1 has 5 routes", 5, list.get(0).getNumRoutes());
        assertEquals("Airport 2 has 0 routes", 0, list.get(1).getNumRoutes());
        assertEquals("Airport 3 has 0 routes", 0, list.get(2).getNumRoutes());
    }
}
