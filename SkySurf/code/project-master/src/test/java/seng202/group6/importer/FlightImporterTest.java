package seng202.group6.importer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng202.group6.importer.FlightImporter;
import seng202.group6.model.FlightPoint;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FlightImporterTest {

    FlightImporter fi;
    ArrayList<FlightPoint> expected;
    ArrayList<FlightPoint> result;



    @Before
    public void setUp() {
        expected = new ArrayList<>();
        result = new ArrayList<>();

        try {
            String path = new File("").getAbsolutePath() + "/src/test/testFiles/flight_NZCH-WSSS_invalid.csv";
            fi = new FlightImporter(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        fi = null;
    }

    @Test
    public void getFlightTest() {
        expected.add(new FlightPoint("APT", "NZCH", 0.0, -43.48664019, 172.53368221));
        expected.add(new FlightPoint("VOR", "CH", 400.0, -43.50411111, 172.51463889000001));
        expected.add(new FlightPoint("FIX", "VANDA", 34000.0, -42.421806, 169.34450000000004));
        expected.add(new FlightPoint("FIX", "UKLAK", 34000.0, -41.4245, 166.71119399999998));
        expected.add(new FlightPoint("FIX", "SULON", 34000.0, -39.858528, 163.0));
        expected.add(new FlightPoint("FIX", "PLUGA", 34000.0, -35.6075, 154.68888900000002));
        expected.add(new FlightPoint("FIX", "CAWLY", 34000.0, -34.326111, 151.95777799999996));
        expected.add(new FlightPoint("VOR", "SY", 34000.0, -33.94277778, 151.18055556000002));
        expected.add(new FlightPoint("NDB", "KAT", 34000.0, -33.710806, 150.299622));
        expected.add(new FlightPoint("APT", "WSSS", 0.0, 1.3519171399999976, 103.99560303999999));

        result = new ArrayList<>(fi.getFlight().getFlightPoints());

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

    }

    @Test
    public void getInvalidRecordsTest() {
        ArrayList<String[]> expected = new ArrayList<>();
        expected.add(new String[] { "Expected 5 parameters", "" });
        // invalid altitude
        expected.add(new String[] { "Expected a number (Altitude)",
                "APT,WSSS,c,1.3519171399999976,103.99560303999999" });
        // invalid latitude
        expected.add(new String[] { "Expected a number (Latitude)",
                "APT,WSSS,0,1.35191713999b9976,103.99560303999999" });
        // invalid longitude
        expected.add(new String[] { "Expected a number (Longitude)",
                "APT,WSSS,0,1.3519171399999976,103.9956030399a999" });

        fi.getFlight();
        ArrayList<String[]> result = fi.getInvalidRecords();

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).length, result.get(i).length);
            for (int j = 0; j < expected.get(i).length; j++) {
                assertEquals(expected.get(i)[j], result.get(i)[j]);
            }
        }
    }

}
