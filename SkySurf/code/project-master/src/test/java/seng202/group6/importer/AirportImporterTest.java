package seng202.group6.importer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng202.group6.importer.AirportImporter;
import seng202.group6.model.Airport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 24/08/16.
 */
public class AirportImporterTest {

    AirportImporter ai;

    @Before
    public void setUp() {
        try {
            String path = new File("").getAbsolutePath() + "/src/test/testFiles/airports.dat";
            ai = new AirportImporter(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        ai = null;
    }


    @Test
    public void getAllAirportsTest() {

        ArrayList<Airport> expected = new ArrayList<>();
        expected.add(new Airport("Goroka", "Goroka", "Papua New Guinea", "GKA", "AYGA", -6.081689, 145.391881, 5282.0,
                "10", "U", "Pacific/Port_Moresby", 0));
        expected.add(new Airport("Madang", "Madang", "Papua New Guinea", "MAG", "AYMD", -5.207083, 145.7887, 20.0, "10",
                "U", "Pacific/Port_Moresby", 0));
        expected.add(new Airport("Mount Hagen", "Mount Hagen", "Papua New Guinea", "HGU", "AYMH", -5.826789, 144.295861,
                5388.0, "10", "U", "Pacific/Port_Moresby", 0));
        expected.add(new Airport("Nadzab", "Nadzab", "Papua New Guinea", "LAE", "AYNZ", -6.569828, 146.726242, 239.0,
                "10", "U", "Pacific/Port_Moresby", 0));
        expected.add(new Airport("Port Moresby Jacksons Intl", "Port Moresby", "Papua New Guinea", "POM", "AYPY",
                -9.443383, 147.22005, 146.0, "10", "U", "Pacific/Port_Moresby", 0));
        expected.add(new Airport("Wewak Intl", "Wewak", "Papua New Guinea", "WWK", "AYWK", -3.583828, 143.669186, 19.0,
                "10", "U", "Pacific/Port_Moresby", 0));
        expected.add(new Airport("Narsarsuaq", "Narssarssuaq", "Greenland", "UAK", "BGBW", 61.160517, -45.425978, 112.0,
                "-3", "E", "America/Godthab", 0));
        expected.add(new Airport("Nuuk", "Godthaab", "Greenland", "GOH", "BGGH", 64.190922, -51.678064, 283.0, "-3",
                "E", "America/Godthab", 0));
        expected.add(new Airport("Sondre Stromfjord", "Sondrestrom", "Greenland", "SFJ", "BGSF", 67.016969, -50.689325,
                165.0, "-3", "E", "America/Godthab", 0));
        expected.add(new Airport("Robin Hood Doncaster Sheffield Airport","Doncaster, Sheffield","United Kingdom",
                "DSA","EGCN",53.474722,-1.004444,55,"0","E","Europe/London", 0));

        ArrayList<Airport> result = ai.getAll();

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void getInvalidRecordsTest() {
        ArrayList<String[]> expected = new ArrayList<>();
        expected.add(new String[] { "Expected 12 parameters", "" });
        // invalid latitude
        expected.add(new String[] { "Expected 90 >= Latitude >= -90",
            "10,\"Thule Air Base\",\"Thule\",\"Greenland\",\"THU\",\"BGTL\",76.5312a03,-68.703161,251,-4,\"E\"," +
                    "\"America/Thule\"" });
        // invalid longitude
        expected.add(new String[] { "Expected 180 >= Longitude >= -180",
            "10,\"Thule Air Base\",\"Thule\",\"Greenland\",\"THU\",\"BGTL\",76.531203,-68.703b161,251,-4,\"E\"," +
                    "\"America/Thule\"" });
        // invalid dst
        expected.add(new String[] { "Expected one of E, A, S, O, Z, N, or U (DST)",
            "10,\"Thule Air Base\",\"Thule\",\"Greenland\",\"THU\",\"BGTL\",76.531203,-68.703161,251,-4,\"Q\"," +
                    "\"America/Thule\"" });
        // invalid iata
        expected.add(new String[] { "Expected three letter code or blank (IATA/FAA)",
            "10,\"Thule Air Base\",\"Thule\",\"Greenland\",\"B00B\",\"BGTL\",76.531203,-68.703161,251,-4,\"E\"," +
                    "\"America/Thule\"" });
        // invalid icao
        expected.add(new String[] { "Expected four letter code or blank (ICAO)",
            "10,\"Thule Air Base\",\"Thule\",\"Greenland\",\"THU\",\"69\",76.531203,-68.703161,251,-4,\"E\"," +
                    "\"America/Thule\"" });

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
