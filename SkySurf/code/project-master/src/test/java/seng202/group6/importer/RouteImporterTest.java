package seng202.group6.importer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng202.group6.importer.RouteImporter;
import seng202.group6.model.Route;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 24/08/16.
 */
public class RouteImporterTest {

    RouteImporter ri;

    @Before
    public void setUp() {
        try {
            String path = new File("").getAbsolutePath() + "/src/test/testFiles/routes.dat"; // so bad matthias plz fix
            ri = new RouteImporter(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        ri = null;
    }

    @Test
    public void getAllRoutesTest() {
        //String airline, String codeShare, String destinationAirport, String equipment, String sourceAirport, String stops
        ArrayList<Route> expected = new ArrayList<>();
        expected.add(new Route("2B", "AER", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "ASF", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "ASF", "MRV", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "CEK", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "CEK", "OVB", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "DME", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "DME", "NBC", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        expected.add(new Route("2B", "DME", "TGK", false, 0, new ArrayList<>(Arrays.asList("CR2", "123"))));
        expected.add(new Route("2B", "DME", "UUA", false, 0, new ArrayList<>(Arrays.asList("CR2", "123"))));
        expected.add(new Route("2B", "EGO", "KGD", false, 0, new ArrayList<>()));
        expected.add(new Route("2B", "EGO", "KGD", false, 0, new ArrayList<>(Arrays.asList("CR2"))));

        ArrayList<Route> result = ri.getAll();

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getAirline(), result.get(i).getAirline());
            assertEquals(expected.get(i).getCodeShare(), result.get(i).getCodeShare());
            assertEquals(expected.get(i).getDestinationAirport(), result.get(i).getDestinationAirport());
            assertEquals(expected.get(i).getSourceAirport(), result.get(i).getSourceAirport());
            assertEquals(expected.get(i).getStops(), result.get(i).getStops());
            for (int j = 0; j < expected.get(i).getEquipment().size(); j++) {
                assertEquals(expected.get(i).getEquipment().get(j), result.get(i).getEquipment().get(j) );
            }

        }

    }


    @Test
    public void getInvalidRecordsTest() {
        ArrayList<String[]> expected = new ArrayList<>();
        // just an empty line
        expected.add(new String[] { "Expected 9 parameters", "" });
        // stops is not a number
        expected.add(new String[] { "Expected a integer (Stops)", "2B,410,EGO,6156,KGD,2952,,a0,CR2" });
        // invalid source airport
        expected.add(new String[] { "Expected 3 letter IATA or 4 letter ICAO (Source Airport)",
                "2B,410,SENG202,6156,KGD,2952,,0,CR2" });
        //invalid airline
        expected.add(new String[] { "Expected 2 letter IATA or 3-letter ICAO (Airline)",
                "2,410,EGO,6156,KGD,2952,Y,0,CR2" });

        ri.getAll();
        ArrayList<String[]> result = ri.getInvalidRecords();
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).length, result.get(i).length);
            for (int j = 0; j < expected.get(i).length; j++) {
                assertEquals(expected.get(i)[j], result.get(i)[j]);
            }
        }
    }


}
