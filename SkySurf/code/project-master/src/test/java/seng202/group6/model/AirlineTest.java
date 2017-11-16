package seng202.group6.model;

import org.junit.Test;

import seng202.group6.model.Airline;

import static org.junit.Assert.assertTrue;

/**
 * Created by afj19 on 24/08/16.
 */
public class AirlineTest {

    @Test
    public void equalsTest() {
        Airline line1 = new Airline("name", "alias", "iata", "icao", "callSign", "country", false);
        Airline line2 = new Airline("name", "alias", "iata", "icao", "callSign", "country", false);
        assertTrue("The airlines are the same", line1.equals(line2));
    }

    @Test
    public void copyTest() {
        Airline origin = new Airline("name", "alias", "iata", "icao", "callSign", "country", false);
        assertTrue("The copy is the same as the original",
                origin.copy().equals(origin));
    }
}