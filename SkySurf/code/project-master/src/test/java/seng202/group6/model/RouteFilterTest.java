package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.model.Route;
import seng202.group6.model.RouteFilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the class RouteFilter
 */
public class RouteFilterTest {
    private ArrayList<Route> routes;
    private RouteFilter filter;

    @Before
    public void setUp() {
        filter = new RouteFilter();
        routes = new ArrayList<>();
        /*
        routes.add(new Route("airline", "Code share", "dest", "equipment", "src", "stops"));
        routes.add(new Route("airline", "Code share", "dest", "equipment", "src", "stops"));
        routes.add(new Route("airloon", "Code share", "dest", "equipment", "src", "stops"));
        */
        // Need to write test for equipment searching as sole instance of string array matching.
        ArrayList<String> equipment = new ArrayList<>();
        equipment.add("747");
        routes.add(new Route("airline", "src", "dest", false, 1, equipment));
        routes.add(new Route("airline", "src", "dest", false, 1, equipment));
        routes.add(new Route("airloon", "src", "dest", false, 1, equipment));

    }

    @After
    public void tearDown() {
        routes = null;
        filter = null;
    }

    @Test
    public void matchesTestString() {
        filter.addAirline("airline");
        assertTrue ("Route 1 matches filter", filter.matches(routes.get(0)));
        assertTrue ("Route 2 matches filter", filter.matches(routes.get(1)));
        assertFalse("Route 3 does not have airline 'airline'", filter.matches(routes.get(2)));
    }

    @Test
    public void matchesTestEmpty() {
        assertTrue("Any route matches an empty filter", filter.matches(routes.get(0)));
        assertTrue("Any route matches an empty filter", filter.matches(routes.get(1)));
        assertTrue("Any route matches an empty filter", filter.matches(routes.get(2)));
    }
}
