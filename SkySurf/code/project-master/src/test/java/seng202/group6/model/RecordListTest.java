package seng202.group6.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class RecordListTest {
    private ArrayList<Airline> arrayList;
    private RecordList<Airline> recordList;

    @Before
    public void setUp() {
        arrayList = new ArrayList<>();
        arrayList.add(new Airline("name", "alias", "iata", "icao", "callSign", "country", false));
        arrayList.add(new Airline("name", "alias", "iata", "icao", "callSign", "poultry", true));
        arrayList.add(new Airline("name", "alien", "iota", "icao", "callSign", "country", true));
        recordList = new RecordList<>("Test");
        recordList.addAll(arrayList);
    }

    @After
    public void tearDown() {
        arrayList = null;
        recordList = null;
    }

    @Test
    public void searchByCriteriaTest() {
        AirlineFilter filter = new AirlineFilter();
        filter.setActive(true);
        filter.addAlias("alias");
        List<Airline> results = recordList.searchByCriteria(filter);
        assertFalse("Airline 1 is not active", results.contains(arrayList.get(0)));
        assertTrue("Airline 2 matches filter", results.contains(arrayList.get(1)));
        assertFalse("Airline 3 does not match alias criteria", results.contains(arrayList.get(2)));
    }

    @Test
    public void removeRecordTest() {
        recordList.remove(arrayList.get(2));
        assertFalse("The third airline should no longer be present",
                recordList.getRecords().contains(arrayList.get(2)));

    }

    @Test
    public void removeIndexTest() {
        recordList.remove(2);
        assertFalse("The third airline should no longer be present",
                recordList.getRecords().contains(arrayList.get(2)));

    }

    @Test
    public void nameTest() {
        recordList.setName("ABCD");
        assertTrue("The name should be \"ABCD\"", recordList.getName().equals("ABCD"));
    }

    @Test
    public void addAllDuplicatesTest() {
        int startSize = recordList.getRecords().size();
        recordList.addAll(arrayList);
        assertEquals("No records are added", startSize, recordList.getRecords().size());
    }

    @Test
    public void addSingleDuplicateTest() {
        int startSize = recordList.getRecords().size();
        recordList.add(arrayList.get(0));
        assertEquals("Duplicate record is not added", startSize, recordList.getRecords().size());
    }

    @Test
    public void AddAllObservingTest() {
        for (Airline airline : arrayList) {
            assertEquals("All airlines have one observer", 1, airline.countObservers());
        }
    }

    @Test
    public void addSingleObservingTest() {
        Airline airline = new Airline("name", "alias", "IATA", "icao", "call_sign", "country", true);
        assertEquals("Airline has no observers", 0, airline.countObservers());
        recordList.add(airline);
        assertEquals("Airlines has one observer", 1, airline.countObservers());
    }

    @Test
    public void removeObservingTest() {
        assertEquals("Airline has one observer", 1, arrayList.get(0).countObservers());
        recordList.remove(arrayList.get(0));
        assertEquals("Airline has no observers", 0, arrayList.get(0).countObservers());
    }

    @Test
    public void getByIndexTest() {
        assertEquals("The aiports are the same", arrayList.get(0), recordList.get(0));
    }

    @Test
    public void updateTest() {
        class ListObserver implements Observer {
            boolean updateTriggered = false;
            @Override
            public void update(Observable o, Object arg) {
                updateTriggered = true;
            }
        }
        ListObserver listObserver = new ListObserver();
        recordList.addObserver(listObserver);
        arrayList.get(0).setIata("TRIGGERED");
        assertTrue("An update is triggered", listObserver.updateTriggered);
    }

    @Test
    public void removeIndexOutOfBoundsIndexTest() {
        int startSize = recordList.getRecords().size();
        boolean thrown = false;
        try {
            recordList.remove(startSize);
        } catch (IndexOutOfBoundsException e) {
            thrown = true;
        }
        assertTrue(thrown);
        assertEquals("Duplicate record is not added", startSize, recordList.getRecords().size());
    }

    @Test
    public void nameAndRecordsConstructorTest() {
        RecordList<Airline> newList = new RecordList<>("name", arrayList);
        assertEquals("Contains all records", arrayList, newList.getRecords());
    }
}
