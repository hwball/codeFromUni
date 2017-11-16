package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AirlineListTest {
    private ArrayList<Airline> list;
    private AirlineList airlines;

    @Before
    public void setUp() {
        list = new ArrayList<>();
        list.add(new Airline("name", "alias", "iata1", "icao1", "callSign", "country", false));
        list.add(new Airline("name", "alias", "iata2", "icao2", "callSign", "poultry", true));
        list.add(new Airline("name", "alien", "iota", "icao3", "callSign", "country", true));
        airlines = new AirlineList("Test");
        airlines.addAll(list);
    }

    @After
    public void tearDown() {
        list = null;
        airlines = null;
    }

    @Test
    public void addAllTest() {
        for (int i=0; i < list.size(); i++) {
            assertEquals(list.get(i), airlines.get(i));
        }
    }

    @Test
    public void searchByCriteriaTest() {
        AirlineFilter filter = new AirlineFilter();
        filter.setActive(true);
        filter.addAlias("alias");
        List<Airline> results = airlines.searchByCriteria(filter);
        assertFalse("Airline 1 is not active", results.contains(list.get(0)));
        assertTrue("Airline 2 matches filter", results.contains(list.get(1)));
        assertFalse("Airline 3 does not match alias criteria", results.contains(list.get(2)));
    }

    @Test
    public void recordPresentTest() {
        assertTrue("Airline with IATA or ICAO code 'iata' is present",
                airlines.recordUniquelyPresent("iata1"));
        assertFalse("Airline with IATA code 'fake' is not present",
                airlines.recordUniquelyPresent("fake"));
    }

    @Test
    public void removeAirlineTest() {
        airlines.remove(list.get(2));
        assertFalse("The third airline should no longer be present",
                airlines.getRecords().contains(list.get(2)));

    }
}
