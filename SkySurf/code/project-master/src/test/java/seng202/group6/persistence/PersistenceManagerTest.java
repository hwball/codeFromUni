package seng202.group6.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng202.group6.DataContainer;
import seng202.group6.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 2/10/16.
 */
public class PersistenceManagerTest {
    private DataContainer dataContainer;
    private PersistenceManager persistenceManager;
    private File saveTo;
    private File loadFrom;
    
    @Before
    public void setUp() throws FileNotFoundException {
        dataContainer = new DataContainer();
        persistenceManager = new PersistenceManager(dataContainer);
        String path = new File("").getAbsolutePath();
        loadFrom = new File(path + "/src/test/testFiles/persistence-test-loadFrom.sky");
        saveTo = new File(path + "/src/test/testFiles/persistence-test-saveTo.sky");
        PrintWriter writer = new PrintWriter(saveTo);
        writer.close();
    }

    @After
    public void tearDown() {
        dataContainer = null;
        persistenceManager = null;
        saveTo = null;
        loadFrom = null;
    }

    @Test
    public void saveToFileTest() throws IOException {
        setUpDataContainer(dataContainer);
        persistenceManager.saveToFile(saveTo);
        byte[] result = Files.readAllBytes(saveTo.toPath());
        byte[] expected = Files.readAllBytes(loadFrom.toPath());
    }


    @Test
    public void loadFromFileTest() throws IOException, FileFormatException {
        DataContainer expected = new DataContainer();
        setUpDataContainer(expected);
        persistenceManager.loadFromFile(loadFrom);
        // airlines
        assertEquals(expected.getAirlineLists().size(), dataContainer.getAirlineLists().size());
        assertEquals(expected.getAirlineLists().get(0).size(), dataContainer.getAirlineLists().get(0).size());
        for (int i = 0; i < expected.getAirlineLists().get(0).size(); i++) {
            assertEquals(expected.getAirlineLists().get(0).get(i), dataContainer.getAirlineLists().get(0).get(i));
        }
        // airports
        assertEquals(expected.getAirportLists().size(), dataContainer.getAirportLists().size());
        assertEquals(expected.getAirportLists().get(0).size(), dataContainer.getAirportLists().get(0).size());
        for (int i = 0; i < expected.getAirportLists().get(0).size(); i++) {
            assertEquals(expected.getAirportLists().get(0).get(i), dataContainer.getAirportLists().get(0).get(i));
        }
        // routes
        assertEquals(expected.getRouteLists().size(), dataContainer.getRouteLists().size());
        assertEquals(expected.getRouteLists().get(0).size(), dataContainer.getRouteLists().get(0).size());
        for (int i = 0; i < expected.getRouteLists().get(0).size(); i++) {
            assertEquals(expected.getRouteLists().get(0).get(i), dataContainer.getRouteLists().get(0).get(i));
        }
        // flights
        assertEquals(expected.getFlightLists().size(), dataContainer.getFlightLists().size());
        assertEquals(expected.getFlightLists().get(0).size(), dataContainer.getFlightLists().get(0).size());
        for (int i = 0; i < expected.getFlightLists().get(0).size(); i++) {
            assertEquals(expected.getFlightLists().get(0).get(i), dataContainer.getFlightLists().get(0).get(i));
        }
    }


    public void setUpDataContainer(DataContainer dataContainer) {
        // populate airlines
        List<Airline> airlines = new ArrayList<>();
        airlines.add(new Airline("135 Airways", "", "", "GNL", "GENERAL", "United States", false));
        airlines.add(new Airline("1Time Airline", "", "1T", "RNX", "NEXTIME", "South Africa", true));
        airlines.add(new Airline("2 Sqn No 1 Elementary Flying Training School", "", "", "WYT", "",
                "United Kingdom", false));
        airlines.add(new Airline("213 Flight Unit", "", "", "TFU", "", "Russia", false));
        airlines.add(new Airline("223 Flight Unit State Airline", "", "", "CHD", "CHKALOVSK-AVIA", "Russia", false));
        airlines.add(new Airline("224th Flight Unit", "", "", "TTF", "CARGO UNIT", "Russia", false));
        airlines.add(new Airline("247 Jet Ltd", "", "", "TWF", "CLOUD RUNNER", "United Kingdom", false));
        airlines.add(new Airline("3D Aviation", "", "", "SEC", "SECUREX", "United States", false));
        airlines.add(new Airline("40-Mile Air", "", "Q5", "MLA", "MILE-AIR", "United States", true));
        AirlineList airlineList = new AirlineList("airlines", airlines);
        dataContainer.addAirlineList(airlineList);

        // populate airports
        List<Airport> airports = new ArrayList<>();
        airports.add(new Airport("Goroka", "Goroka", "Papua New Guinea", "GKA", "AYGA", -6.081689, 145.391881, 5282.0,
                "10", "U", "Pacific/Port_Moresby", 0));
        airports.add(new Airport("Madang", "Madang", "Papua New Guinea", "MAG", "AYMD", -5.207083, 145.7887, 20.0, "10",
                "U", "Pacific/Port_Moresby", 0));
        airports.add(new Airport("Mount Hagen", "Mount Hagen", "Papua New Guinea", "HGU", "AYMH", -5.826789, 144.295861,
                5388.0, "10", "U", "Pacific/Port_Moresby", 0));
        airports.add(new Airport("Nadzab", "Nadzab", "Papua New Guinea", "LAE", "AYNZ", -6.569828, 146.726242, 239.0,
                "10", "U", "Pacific/Port_Moresby", 0));
        airports.add(new Airport("Port Moresby Jacksons Intl", "Port Moresby", "Papua New Guinea", "POM", "AYPY",
                -9.443383, 147.22005, 146.0, "10", "U", "Pacific/Port_Moresby", 0));
        airports.add(new Airport("Wewak Intl", "Wewak", "Papua New Guinea", "WWK", "AYWK", -3.583828, 143.669186, 19.0,
                "10", "U", "Pacific/Port_Moresby", 0));
        airports.add(new Airport("Narsarsuaq", "Narssarssuaq", "Greenland", "UAK", "BGBW", 61.160517, -45.425978, 112.0,
                "-3", "E", "America/Godthab", 0));
        airports.add(new Airport("Nuuk", "Godthaab", "Greenland", "GOH", "BGGH", 64.190922, -51.678064, 283.0, "-3",
                "E", "America/Godthab", 0));
        airports.add(new Airport("Sondre Stromfjord", "Sondrestrom", "Greenland", "SFJ", "BGSF", 67.016969, -50.689325,
                165.0, "-3", "E", "America/Godthab", 0));
        airports.add(new Airport("Robin Hood Doncaster Sheffield Airport","Doncaster, Sheffield","United Kingdom",
                "DSA","EGCN",53.474722,-1.004444,55,"0","E","Europe/London", 0));
        AirportList airportList = new AirportList("airports", airports);
        dataContainer.addAirportList(airportList);

        // populate routes
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("2B", "AER", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "ASF", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "ASF", "MRV", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "CEK", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "CEK", "OVB", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "DME", "KZN", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "DME", "NBC", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        routes.add(new Route("2B", "DME", "TGK", false, 0, new ArrayList<>(Arrays.asList("CR2", "123"))));
        routes.add(new Route("2B", "DME", "UUA", false, 0, new ArrayList<>(Arrays.asList("CR2", "123"))));
        routes.add(new Route("2B", "EGO", "KGD", false, 0, new ArrayList<>(Arrays.asList(""))));
        routes.add(new Route("2B", "EGO", "KGD", false, 0, new ArrayList<>(Arrays.asList("CR2"))));
        RouteList routeList = new RouteList("routes", routes);
        dataContainer.addRouteList(routeList);

        // populating flights
        ArrayList<FlightPoint> flightPoints =  new ArrayList<>();
        flightPoints.add(new FlightPoint("APT","NZCH",0.0,-43.48664019,172.53368221));
        flightPoints.add(new FlightPoint("APT","ESSH",0.0,58.978615,14.666105));
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(flightPoints));
        dataContainer.addFlightList(new FlightList("flights", flights));

    }



}
