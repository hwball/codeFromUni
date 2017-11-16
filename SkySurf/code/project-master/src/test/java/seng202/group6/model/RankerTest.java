package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.Ranker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anton J on 13/09/2016.
 */
public class RankerTest {
    private AirportList airportList;
    private RouteList routeList;
    private Ranker ranker;
    
    @Before
    public void setUp() {
        airportList = new AirportList("name");
        airportList.add(new Airport("name", "city", "country", "1", "icao1", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "2", "icao2", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "3", "icao3", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "4", "icao4", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "5", "icao5", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));

        routeList = new RouteList("name");
        routeList.add(new Route("airline", "3", "1", false, 1, new ArrayList<>()));
        routeList.add(new Route("airline", "3", "1", false, 2, new ArrayList<>()));
        routeList.add(new Route("airline", "3", "4", false, 3, new ArrayList<>()));
        routeList.add(new Route("airline", "4", "2", false, 4, new ArrayList<>()));
        routeList.add(new Route("airline", "2", "4", false, 5, new ArrayList<>()));
        routeList.add(new Route("airline", "*", "1", false, 6, new ArrayList<>()));
        routeList.add(new Route("airline", "+", "-", false, 7, new ArrayList<>()));

        ranker = new Ranker(airportList, routeList);
    }
    
    @After
    public void tearDown() {
        airportList = null;
        routeList = null;
        ranker = null;
    }
    
    @Test
    public void rankerTest() {
        assertEquals(5, airportList.size());
        assertTrue(airportList.containsUniqueIATA("1"));
        assertEquals("Airport 1 has 2 routes", 2, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 2 routes", 2, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 3 routes", 3, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 3 routes", 3, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 0 routes", 0, airportList.get(4).getNumRoutes());
    }

    @Test
    public void setAirportListTest() {
        AirportList airportList = new AirportList("name");
        airportList.add(new Airport("name", "city", "country", "2", "icao6", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "4", "icao7", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "a", "icao8", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        airportList.add(new Airport("name", "city", "country", "b", "icao9", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        ranker.setAirportList(airportList);
        
        assertEquals("Airport 1 has 2 routes", 2, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 2 routes", 2, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 0 routes", 0, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 0 routes", 0, airportList.get(3).getNumRoutes());
    }

    @Test
    public void setRouteListTest() {
        RouteList routeList = new RouteList("name");
        routeList.add(new Route("airline", "1", "2", false, 5, new ArrayList<>()));
        routeList.add(new Route("airline", "2", "5", false, 5, new ArrayList<>()));
        routeList.add(new Route("airline", "2", "*", false, 5, new ArrayList<>()));
        routeList.add(new Route("airline", "+", "-", false, 5, new ArrayList<>()));
        ranker.setRouteList(routeList);

        assertEquals("Airport 1 has 1 routes", 1, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 2 routes", 2, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 0 routes", 0, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 0 routes", 0, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 1 routes", 1, airportList.get(4).getNumRoutes());
    }

    @Test
    public void airportUpdateTest() {
        airportList.get(2).setIata("*");
        assertEquals("Airport 1 has 1 routes", 1, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 2 routes", 2, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 1 routes", 1, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 2 routes", 2, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 0 routes", 0, airportList.get(4).getNumRoutes());
    }

    @Test
    public void routeUpdateTest() {
        routeList.get(0).setSourceAirport("*");
        routeList.get(4).setDestinationAirport("5");
        routeList.get(6).setSourceAirport("3");
        routeList.get(6).setDestinationAirport("2");
        assertEquals("Airport 1 has 1 routes", 1, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 3 routes", 3, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 3 routes", 3, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 2 routes", 2, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 1 routes", 1, airportList.get(4).getNumRoutes());
    }

    @Test
    public void airportListUpdateAddTest() {
        airportList.add(new Airport("name", "city", "country", "*", "icao6", 1.0, 1.0, 1.0, "timezone", "dst", "tzd"));
        assertEquals("Airport 1 has 3 routes", 3, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 2 routes", 2, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 3 routes", 3, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 3 routes", 3, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 0 routes", 0, airportList.get(4).getNumRoutes());
        assertEquals("New airport has 1 routes", 1, airportList.get(5).getNumRoutes());
    }

    @Test
    public void routeListUpdateAddTest() {
        routeList.add(new Route("airline", "2", "5", false, 5, new ArrayList<>()));
        assertEquals("Airport 1 has 2 routes", 2, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 3 routes", 3, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 3 routes", 3, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 3 routes", 3, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 1 routes", 1, airportList.get(4).getNumRoutes());
    }

    @Test
    public void airportListUpdateRemoveTest() {
        airportList.remove(3);
        assertEquals("Airport 1 has 2 routes", 2, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 0 routes", 0, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 2 routes", 2, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 (old 5) has 0 routes", 0, airportList.get(3).getNumRoutes());
    }

    @Test
    public void routeListUpdateRemoveTest() {
        routeList.remove(1);
        assertEquals("Airport 1 has 1 routes", 1, airportList.get(0).getNumRoutes());
        assertEquals("Airport 2 has 2 routes", 2, airportList.get(1).getNumRoutes());
        assertEquals("Airport 3 has 2 routes", 2, airportList.get(2).getNumRoutes());
        assertEquals("Airport 4 has 3 routes", 3, airportList.get(3).getNumRoutes());
        assertEquals("Airport 5 has 0 routes", 0, airportList.get(4).getNumRoutes());
    }
}
