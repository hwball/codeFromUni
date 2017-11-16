package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anton J on 29/09/2016.
 */
public class IdentifiedRecordListTest {
    private IdentifiedRecordList<Airline> identifiedRecordList;
    private ArrayList<Airline> airlines;
    private Airline blank;

    @Before
    public void setUp() throws Exception {
        identifiedRecordList = new IdentifiedRecordList<>("");
        airlines = new ArrayList<>();

        Airline a1 = new Airline("name", "alias", "iata1", "icao1", "callSign", "country", false);
        Airline a2 = new Airline("name", "alias", "iata2", "icao2", "callSign", "country", false);
        Airline a3 = new Airline("name", "alias", "iata3", "icao3", "callSign", "country", false);
        airlines.add(a1);
        airlines.add(a2);
        airlines.add(a3);

        blank = new Airline("", "", "", "", "", "", true);
    }

    @After
    public void tearDown() throws Exception {
        identifiedRecordList = null;
        airlines = null;
        blank = null;
    }

    @Test
    public void add() throws Exception {
        boolean added;
        for (Airline airline : airlines) {
            added = identifiedRecordList.add(airline);

            assertTrue(added);
            assertTrue(identifiedRecordList.getRecords().contains(airline));
            assertTrue(identifiedRecordList.containsUniqueIATA(airline.getIata()));
            assertTrue(identifiedRecordList.containsUniqueICAO(airline.getIcao()));
            assertEquals(airline, identifiedRecordList.getById(airline.getIata()));
            assertEquals(airline, identifiedRecordList.getById(airline.getIcao()));
        }

        added = identifiedRecordList.add(blank);
        assertTrue(added);
        assertTrue(identifiedRecordList.getRecords().contains(blank));
        assertFalse(identifiedRecordList.containsUniqueIATA(blank.getIata()));
        assertFalse(identifiedRecordList.containsUniqueICAO(blank.getIcao()));
        assertEquals(null, identifiedRecordList.getById(blank.getIata()));
        assertEquals(null, identifiedRecordList.getById(blank.getIcao()));
    }

    @Test
    public void addAll() throws Exception {
        airlines.add(blank);
        airlines.add(airlines.get(0));
        identifiedRecordList.addAll(airlines);

        assertEquals(4, identifiedRecordList.getRecords().size());

        Airline airline;
        for (int i = 0; i < 3; i++) {
            airline = airlines.get(i);

            assertTrue(identifiedRecordList.getRecords().contains(airline));
            assertTrue(identifiedRecordList.containsUniqueIATA(airline.getIata()));
            assertTrue(identifiedRecordList.containsUniqueICAO(airline.getIcao()));
            assertEquals(airline, identifiedRecordList.getById(airline.getIata()));
            assertEquals(airline, identifiedRecordList.getById(airline.getIcao()));
        }

        assertTrue(identifiedRecordList.getRecords().contains(blank));
        assertFalse(identifiedRecordList.containsUniqueIATA(blank.getIata()));
        assertFalse(identifiedRecordList.containsUniqueICAO(blank.getIcao()));
        assertEquals(null, identifiedRecordList.getById(blank.getIata()));
        assertEquals(null, identifiedRecordList.getById(blank.getIcao()));
    }

    @Test
    public void remove() throws Exception {
        identifiedRecordList.addAll(airlines);
        Airline airline = airlines.get(0);

        assertTrue(identifiedRecordList.getRecords().contains(airline));
        assertTrue(identifiedRecordList.containsUniqueIATA(airline.getIata()));
        assertTrue(identifiedRecordList.containsUniqueICAO(airline.getIcao()));
        assertEquals(airline, identifiedRecordList.getById(airline.getIata()));
        assertEquals(airline, identifiedRecordList.getById(airline.getIcao()));

        identifiedRecordList.remove(airline);

        assertFalse(identifiedRecordList.getRecords().contains(airline));
        assertFalse(identifiedRecordList.containsUniqueIATA(airline.getIata()));
        assertFalse(identifiedRecordList.containsUniqueICAO(airline.getIcao()));
        assertEquals(null, identifiedRecordList.getById(airline.getIata()));
        assertEquals(null, identifiedRecordList.getById(airline.getIcao()));
    }

    @Test
    public void remove1() throws Exception {
        identifiedRecordList.addAll(airlines);
        Airline airline = airlines.get(0);

        assertTrue(identifiedRecordList.getRecords().contains(airline));
        assertTrue(identifiedRecordList.containsUniqueIATA(airline.getIata()));
        assertTrue(identifiedRecordList.containsUniqueICAO(airline.getIcao()));
        assertEquals(airline, identifiedRecordList.getById(airline.getIata()));
        assertEquals(airline, identifiedRecordList.getById(airline.getIcao()));

        identifiedRecordList.remove(0);

        assertFalse(identifiedRecordList.getRecords().contains(airline));
        assertFalse(identifiedRecordList.containsUniqueIATA(airline.getIata()));
        assertFalse(identifiedRecordList.containsUniqueICAO(airline.getIcao()));
        assertEquals(null, identifiedRecordList.getById(airline.getIata()));
        assertEquals(null, identifiedRecordList.getById(airline.getIcao()));
    }

    @Test
    public void recordPresent() throws Exception {
        identifiedRecordList.addAll(airlines);
        identifiedRecordList.add(blank);

        assertTrue(identifiedRecordList.recordUniquelyPresent(airlines.get(0).getIcao()));

        assertTrue(identifiedRecordList.recordUniquelyPresent(airlines.get(0).getIata()));
        assertTrue(identifiedRecordList.recordUniquelyPresent(airlines.get(1).getIcao()));

        assertFalse(identifiedRecordList.recordUniquelyPresent("fake"));
        assertFalse(identifiedRecordList.recordUniquelyPresent(""));
    }


    @Test
    public void update() throws Exception {
        identifiedRecordList.addAll(airlines);
        Airline airline = identifiedRecordList.get(0);
        String oldIata = identifiedRecordList.get(0).getIata();
        airline.setIata("newIata");
        String newIata = identifiedRecordList.get(0).getIata();

        assertTrue(identifiedRecordList.getRecords().contains(airline));
        assertTrue(identifiedRecordList.containsUniqueIATA(newIata));
        assertFalse(identifiedRecordList.containsUniqueIATA(oldIata));
    }


    @Test
    public void getById() throws Exception {
        identifiedRecordList.addAll(airlines);
        identifiedRecordList.add(blank);

        assertEquals(null, identifiedRecordList.getById(blank.getIata()));
        assertEquals(null, identifiedRecordList.getById("FAKE"));

        for (Airline airline : airlines) {
            assertTrue(identifiedRecordList.getById(airline.getIata()) == airline);
            assertTrue(identifiedRecordList.getById(airline.getIcao()) == airline);
        }
    }
}