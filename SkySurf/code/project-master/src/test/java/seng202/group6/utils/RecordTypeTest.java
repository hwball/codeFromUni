package seng202.group6.utils;

import org.junit.Test;
import seng202.group6.utils.RecordType;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 9/16/16.
 */
public class RecordTypeTest {

    @Test
    public void parseNameTest() {
        assertEquals(RecordType.AIRLINE, RecordType.parseName("Airline"));
        assertEquals(RecordType.AIRPORT, RecordType.parseName("Airport"));
        assertEquals(RecordType.ROUTE, RecordType.parseName("Route"));
        assertEquals(RecordType.FLIGHT, RecordType.parseName("Flight"));
    }

    @Test
    public void getSearchTypesAirportTest() {
        String[] expected = { "Name", "City", "Country", "IATA/FAA", "ICAO", "Latitude", "Longitude", "Altitude",
                "Time zone", "DST (Daylight Savings Time)", "Tz database", "Number of Routes" };
        String[] result = RecordType.AIRPORT.getSearchTypes();
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void getSearchTypesAirlineTest() {
        String[] expected = { "Name", "Alias", "IATA", "ICAO", "Call sign", "Country", "Status" };
        String[] result = RecordType.AIRLINE.getSearchTypes();
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }


    @Test
    public void getSearchTypesRouteTest() {
        String[] expected =
                { "Airline", "Source Airport", "Destination Airport", "Codeshare", "Number of stops", "Equipment" };
        String[] result = RecordType.ROUTE.getSearchTypes();
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }


    @Test
    public void getSearchTypesFlightTest() {
        String[] expected = { "Source Airport", "Destination Airport" };
        String[] result = RecordType.FLIGHT.getSearchTypes();
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    
}
