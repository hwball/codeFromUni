package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import seng202.group6.model.Route;
import seng202.group6.model.RouteFilter;
import seng202.group6.model.RouteList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests the class RouteList
 */
public class RouteListTest {
    private RouteList routes;
    private ArrayList<Route> list;
    private ArrayList<String> equipment;

    @Before
    public void setUp() {
        list = new ArrayList<>();

        equipment = new ArrayList<>();
        equipment.add("747");

        list.add(new Route("airline", "srce", "dest", true, 1, equipment));
        list.add(new Route("airline", "srce", "dest", false, 1, equipment));
        list.add(new Route("airline", "srce", "dust", false, 1, equipment));

        routes = new RouteList("name");
        routes.addAll(list);
    }

    @After
    public void tearDown() {
        routes = null;
        list = null;
    }

    @Test
    public void searchByCriteriaTest() {
        RouteFilter filter = new RouteFilter();
        filter.setCodeShare(true);
        filter.addDestinationAirport("dest");
        List<Route> results = routes.searchByCriteria(filter);
        assertTrue("Route 1 matches criteria", results.contains(list.get(0)));
        assertFalse("Route 2 does not match code share criteria", results.contains(list.get(1)));
        assertFalse("Route 3 does not match destination criteria", results.contains(list.get(2)));
    }

    @Test
    public void removeRouteTest() {
        routes.remove(list.get(2));
        assertFalse("The third route should no longer be present",
                routes.getRecords().contains(list.get(2)));
    }
}