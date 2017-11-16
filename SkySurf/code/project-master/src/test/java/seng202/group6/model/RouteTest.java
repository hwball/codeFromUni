package seng202.group6.model;

import org.junit.Test;

import java.util.ArrayList;

import seng202.group6.model.Route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Route  class
 */
public class RouteTest {

    @Test
    public void equalsTest() {
        ArrayList<String> equipment = new ArrayList<>();
        Route line1 = new Route("airline", "srce", "dest", false, 0, equipment);
        Route line2 = new Route("airline", "srce", "dest", false, 0, equipment);
        assertTrue("The routes are the same", line1.equals(line2));
    }

    @Test
    public void copyTest() {
        ArrayList<String> equipment = new ArrayList<>();
        Route origin = new Route("airline", "srce", "dest", false, 0, equipment);
        assertTrue("The copy is the same as the original",
                origin.copy().equals(origin));
    }

    @Test
    public void getEquipmentAsStringTest() {
        ArrayList<String> equipment = new ArrayList<>();
        String expected = "";
        Route route = new Route("airline", "srce", "dest", false, 0, equipment);

        assertEquals(expected, route.getEquipmentAsString());

        equipment.add("ABC");
        equipment.add("DEF");
        equipment.add("HIJ");
        route = new Route("airline", "srce", "dest", false, 0, equipment);
        expected = "ABC, DEF, HIJ";

        assertEquals(expected, route.getEquipmentAsString());
    }
}