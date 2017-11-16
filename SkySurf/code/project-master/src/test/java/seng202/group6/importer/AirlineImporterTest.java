package seng202.group6.importer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng202.group6.importer.AirlineImporter;
import seng202.group6.model.Airline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 24/08/16.
 */
public class AirlineImporterTest {

    AirlineImporter ai;

    @Before
    public void setUp() {
        try {
            String path = new File("").getAbsolutePath() + "/src/test/testFiles/airlines.dat";
            ai = new AirlineImporter(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        ai = null;
    }


    @Test
    public void getAllAirlinesTest() {

        ArrayList<Airline> expected = new ArrayList<>();
        // 3 4 1 2 5 6 7
        expected.add(new Airline("135 Airways", "", "", "GNL", "GENERAL", "United States", false));
        expected.add(new Airline("1Time Airline", "", "1T", "RNX", "NEXTIME", "South Africa", true));
        expected.add(new Airline("2 Sqn No 1 Elementary Flying Training School", "", "", "WYT", "",
                "United Kingdom", false));
        expected.add(new Airline("213 Flight Unit", "", "", "TFU", "", "Russia", false));
        expected.add(new Airline("223 Flight Unit State Airline", "", "", "CHD", "CHKALOVSK-AVIA", "Russia", false));
        expected.add(new Airline("224th Flight Unit", "", "", "TTF", "CARGO UNIT", "Russia", false));
        expected.add(new Airline("247 Jet Ltd", "", "", "TWF", "CLOUD RUNNER", "United Kingdom", false));
        expected.add(new Airline("3D Aviation", "", "", "SEC", "SECUREX", "United States", false));
        expected.add(new Airline("40-Mile Air", "", "Q5", "MLA", "MILE-AIR", "United States", true));

        ArrayList<Airline> result = ai.getAll();

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

    }

    @Test
    public void getInvalidRecordsTest() {
        ArrayList<String[]> expected = new ArrayList<>();
        // invalid iata
        expected.add(new String[] { "Expected two letter code or blank (IATA/FAA)",
                "1,\"Private flight\",\\N,\"-\",\"N/A\",\"\",\"\",\"Y\"" });
        // empty string
        expected.add(new String[] { "Expected 9 parameters", "" });
        // invalid active
        expected.add(new String[] { "Expected either \"Y\" or \"N\" (Active)",
                "10,\"40-Mile Air\",\\N,\"Q5\",\"MLA\",\"MILE-AIR\",\"United States\",\"A\"" });
        // invalid iata
        expected.add(new String[] { "Expected two letter code or blank (IATA/FAA)",
                "10,\"40-Mile Air\",\\N,\"I69\",\"MLA\",\"MILE-AIR\",\"United States\",\"Y\"" });
        // invalid icao
        expected.add(new String[] { "Expected three letter code or blank (ICAO)",
                "10,\"40-Mile Air\",\\N,\"Q5\",\"6969\",\"MILE-AIR\",\"United States\",\"Y\"" });
        // duplicate
        expected.add(new String[] { "Duplicate line",
                "10,\"40-Mile Air\",\\N,\"Q5\",\"MLA\",\"MILE-AIR\",\"United States\",\"Y\"" });

        ai.getAll();
        ArrayList<String[]> result = ai.getInvalidRecords();

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).length, result.get(i).length);
            for (int j = 0; j < expected.get(i).length; j++) {
                assertEquals(expected.get(i)[j], result.get(i)[j]);
            }
        }
    }


}
