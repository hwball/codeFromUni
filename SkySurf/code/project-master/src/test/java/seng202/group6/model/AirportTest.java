package seng202.group6.model;

import org.junit.Test;

import seng202.group6.model.Airport;

import static org.junit.Assert.assertTrue;

/**
 * Tests class Airport
 */
public class AirportTest {

    @Test
    public void equalsTest() {
        Airport line1 = new Airport("Alpha", "Citya", "Countrya", "aaaa", "aaaa", 10, 10, 100, "1", "who knows", "yay", 1);
        Airport line2 = new Airport("Alpha", "Citya", "Countrya", "aaaa", "aaaa", 10, 10, 100, "1", "who knows", "yay", 1);
        assertTrue("The airports are the same", line1.equals(line2));
    }

    @Test
    public void copyTest() {
        Airport origin = new Airport("Alpha", "Citya", "Countrya", "aaaa", "aaaa", 10, 10, 100, "1", "who knows", "yay", 1);
        assertTrue("The copy is the same as the original",
                origin.copy().equals(origin));
    }
}