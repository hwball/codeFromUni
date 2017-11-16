package seng202.group6.controller;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.XYChart;
import seng202.group6.model.Airline;
import seng202.group6.model.AirlineList;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 9/15/16.
 */
public class GraphsControllerTest {

    @Test
    public void routePerAirportTest() {
        AirportList airportList = setUpAirports();

        XYChart.Series<String, Integer> results = GraphsController.routePerAirport(airportList, 29);

        assertEquals(29, results.getData().size());
        for (int i = 0; i < 29; i++) {
            assertEquals(i + "th entry should have IATA of " + (30 - i), Integer.toString(30 - i),
                    results.getData().get(i).getXValue());
            assertEquals(i + "th entry should have value of " + (30 - i), new Integer(30 - i),
                    results.getData().get(i).getYValue());
        }
    }

    @Test
    public void equipmentPerRouteTest() {
        RouteList routes = this.setUpRoutes();
        XYChart.Series<String, Integer> results = GraphsController.equipmentPerRoute(routes, 4);
        List<XYChart.Data<String, Integer>> expected = new ArrayList<>();
        expected.add(new XYChart.Data<>("111", 5));
        expected.add(new XYChart.Data<>("112", 4));
        expected.add(new XYChart.Data<>("113", 3));
        expected.add(new XYChart.Data<>("114", 2));

        assertEquals(expected.size(), results.getData().size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getXValue(), results.getData().get(i).getXValue());
            assertEquals(expected.get(i).getYValue(), results.getData().get(i).getYValue());
        }
    }

    @Test
    public void airportsPerCountryTest() {
        AirportList airports = setUpAirports();
        addDuplicateAirports(airports);
        XYChart.Series<String, Integer> results = GraphsController.airportsPerCountry(airports, 30);

        assertEquals(30, results.getData().size());

        assertEquals("0", results.getData().get(0).getXValue());
        assertEquals((Integer) 4, results.getData().get(0).getYValue());

        for (int i = 1; i <= 29; i++) {
            assertEquals((Integer) 2, results.getData().get(i).getYValue());
        }

    }

    @Test
    public void airlinesPerCountryTest() {
        AirlineList airline = setUpAirlines();
        XYChart.Series<String, Integer> results = GraphsController.airlinesPerCountry(airline, 4);
        List<XYChart.Data<String, Integer>> expected = new ArrayList<>();
        expected.add(new XYChart.Data<>("country3", 4));
        expected.add(new XYChart.Data<>("country1", 3));
        expected.add(new XYChart.Data<>("country2", 1));

        assertEquals(expected.size(), results.getData().size());
        for (int i = 0; i < expected.size(); i++) {
            System.out.println(expected.get(i));
            System.out.println(results.getData().get(i));
            assertEquals(expected.get(i).getXValue(), results.getData().get(i).getXValue());
            assertEquals(expected.get(i).getYValue(), results.getData().get(i).getYValue());
        }
    }

    private RouteList setUpRoutes() {
        ArrayList<Route> list = new ArrayList<>();
        ArrayList<ArrayList<String>> equip = new ArrayList<>();
        ArrayList<String> equipment1 = new ArrayList<>(); // [111, 112, 113, 114, 115]
        ArrayList<String> equipment2 = new ArrayList<>(); // [111]
        ArrayList<String> equipment3 = new ArrayList<>(); // [111, 112]
        ArrayList<String> equipment4 = new ArrayList<>(); // [111, 112, 113]
        ArrayList<String> equipment5 = new ArrayList<>(); // [111, 112, 113, 114]
        equip.add(equipment2);
        equip.add(equipment3);
        equip.add(equipment4);
        equip.add(equipment5);
        equipment1.add("111");
        equipment1.add("112");
        equipment1.add("113");
        equipment1.add("114");
        equipment1.add("115");
        for (int i = 0; i < equipment1.size(); i++) {
            for (int j = i; j < equip.size(); j++) {
                equip.get(j).add(equipment1.get(i));
            }
        }
        list.add(new Route("airline1", "srce1", "dest1", false, 1, equipment1));
        list.add(new Route("airline2", "srce1", "dest1", false, 1, equipment2));
        list.add(new Route("airline3", "srce2", "dest1", false, 1, equipment3));
        list.add(new Route("airline4", "srce2", "dest1", false, 1, equipment4));
        list.add(new Route("airline5", "srce2", "dest1", false, 1, equipment5));
        return new RouteList("name", list);
    }

    private AirportList setUpAirports() {
        AirportList airportList = new AirportList("");
        // the 30 airports with the greatest number of routes
        airportList.add(new Airport("q", "", "1", "1", "1", 0, 0, 0, "", "", "", 1));
        airportList.add(new Airport("w", "", "2", "2", "2", 0, 0, 0, "", "", "", 2));
        airportList.add(new Airport("e", "", "3", "3", "3", 0, 0, 0, "", "", "", 3));
        airportList.add(new Airport("r", "", "4", "4", "4", 0, 0, 0, "", "", "", 4));
        airportList.add(new Airport("t", "", "5", "5", "5", 0, 0, 0, "", "", "", 5));
        airportList.add(new Airport("y", "", "6", "6", "6", 0, 0, 0, "", "", "", 6));
        airportList.add(new Airport("u", "", "7", "7", "7", 0, 0, 0, "", "", "", 7));
        airportList.add(new Airport("i", "", "8", "8", "8", 0, 0, 0, "", "", "", 8));
        airportList.add(new Airport("o", "", "9", "9", "9", 0, 0, 0, "", "", "", 9));
        airportList.add(new Airport("p", "", "10", "10", "10", 0, 0, 0, "", "", "", 10));
        airportList.add(new Airport("[", "", "11", "11", "11", 0, 0, 0, "", "", "", 11));
        airportList.add(new Airport("]", "", "12", "12", "12", 0, 0, 0, "", "", "", 12));
        airportList.add(new Airport("|", "", "13", "13", "13", 0, 0, 0, "", "", "", 13));
        airportList.add(new Airport("a", "", "14", "14", "14", 0, 0, 0, "", "", "", 14));
        airportList.add(new Airport("s", "", "15", "15", "15", 0, 0, 0, "", "", "", 15));
        airportList.add(new Airport("d", "", "16", "16", "16", 0, 0, 0, "", "", "", 16));
        airportList.add(new Airport("f", "", "17", "17", "17", 0, 0, 0, "", "", "", 17));
        airportList.add(new Airport("g", "", "18", "18", "18", 0, 0, 0, "", "", "", 18));
        airportList.add(new Airport("h", "", "19", "19", "19", 0, 0, 0, "", "", "", 19));
        airportList.add(new Airport("j", "", "20", "20", "20", 0, 0, 0, "", "", "", 20));
        airportList.add(new Airport("k", "", "21", "21", "21", 0, 0, 0, "", "", "", 21));
        airportList.add(new Airport("l", "", "22", "22", "22", 0, 0, 0, "", "", "", 22));
        airportList.add(new Airport("z", "", "23", "23", "23", 0, 0, 0, "", "", "", 23));
        airportList.add(new Airport("x", "", "24", "24", "24", 0, 0, 0, "", "", "", 24));
        airportList.add(new Airport("c", "", "25", "25", "25", 0, 0, 0, "", "", "", 25));
        airportList.add(new Airport("v", "", "26", "26", "26", 0, 0, 0, "", "", "", 26));
        airportList.add(new Airport("b", "", "27", "27", "27", 0, 0, 0, "", "", "", 27));
        airportList.add(new Airport("n", "", "28", "28", "28", 0, 0, 0, "", "", "", 28));
        airportList.add(new Airport("m", "", "29", "29", "29", 0, 0, 0, "", "", "", 29));
        airportList.add(new Airport(",", "", "30", "30", "30", 0, 0, 0, "", "", "", 30));

        return airportList;
    }

    private AirlineList setUpAirlines() {
        ArrayList<Airline> list = new ArrayList<>();
        list.add(new Airline("name1", "alias", "iata1", "icao1", "callSign", "country1", true));
        list.add(new Airline("name2", "alias", "iata2", "icao2", "callSign", "country1", true));
        list.add(new Airline("name3", "alias", "iata3", "icao3", "callSign", "country1", true));
        list.add(new Airline("name4", "alias", "iata4", "icao4", "callSign", "country2", true));
        list.add(new Airline("name5", "alias", "iata5", "icao5", "callSign", "country3", true));
        list.add(new Airline("name6", "alias", "iata6", "icao6", "callSign", "country3", true));
        list.add(new Airline("name7", "alias", "iata7", "icao7", "callSign", "country3", true));
        list.add(new Airline("name8", "alias", "iata8", "icao8", "callSign", "country3", true));

        return new AirlineList("name", list);
    }

    private void addDuplicateAirports(AirportList airportList) {
        // country "0" four times and countries "1" - "29" a second time
        airportList.add(new Airport(".", "", "0", "q", "q0", 0, 0, 0, "", "", "", 0));
        airportList.add(new Airport("/", "", "0", "w", "0w", 0, 0, 0, "", "", "", 0));
        airportList.add(new Airport("?", "", "0", "e0", "e0", 0, 0, 0, "", "", "", 0));
        airportList.add(new Airport(">", "", "0", "r0", "r0", 0, 0, 0, "", "", "", 0));
        airportList.add(new Airport("1", "", "1", "t1", "t1", 0, 0, 0, "", "", "", 1));
        airportList.add(new Airport("2", "", "2", "y2", "y2", 0, 0, 0, "", "", "", 2));
        airportList.add(new Airport("3", "", "3", "u3", "u3", 0, 0, 0, "", "", "", 3));
        airportList.add(new Airport("4", "", "4", "i4", "i4", 0, 0, 0, "", "", "", 4));
        airportList.add(new Airport("5", "", "5", "o5", "o5", 0, 0, 0, "", "", "", 5));
        airportList.add(new Airport("6", "", "6", "p6", "p6", 0, 0, 0, "", "", "", 6));
        airportList.add(new Airport("7", "", "7", "[7", "[7", 0, 0, 0, "", "", "", 7));
        airportList.add(new Airport("8", "", "8", "]8", "]8", 0, 0, 0, "", "", "", 8));
        airportList.add(new Airport("9", "", "9", "a9", "a31", 0, 0, 0, "", "", "", 9));
        airportList.add(new Airport("0", "", "10", "1s0", "1s0", 0, 0, 0, "", "", "", 10));
        airportList.add(new Airport("-", "", "11", "1d1", "d11", 0, 0, 0, "", "", "", 11));
        airportList.add(new Airport("=", "", "12", "1f2", "1f2", 0, 0, 0, "", "", "", 12));
        airportList.add(new Airport("!", "", "13", "1g3", "g13", 0, 0, 0, "", "", "", 13));
        airportList.add(new Airport("@", "", "14", "h14", "h14", 0, 0, 0, "", "", "", 14));
        airportList.add(new Airport("#", "", "15", "1j5", "1j5", 0, 0, 0, "", "", "", 15));
        airportList.add(new Airport("$", "", "16", "1k6", "1k6", 0, 0, 0, "", "", "", 16));
        airportList.add(new Airport("%", "", "17", "1k7", "1k7", 0, 0, 0, "", "", "", 17));
        airportList.add(new Airport("^", "", "18", "1k8", "1k8", 0, 0, 0, "", "", "", 18));
        airportList.add(new Airport("&", "", "19", "1k9", "k19", 0, 0, 0, "", "", "", 19));
        airportList.add(new Airport("*", "", "20", "2k0", "k20", 0, 0, 0, "", "", "", 20));
        airportList.add(new Airport("(", "", "21", "2k1", "2k1", 0, 0, 0, "", "", "", 21));
        airportList.add(new Airport(")", "", "22", "2k2", "2k2", 0, 0, 0, "", "", "", 22));
        airportList.add(new Airport("`", "", "23", "k23", "2k3", 0, 0, 0, "", "", "", 23));
        airportList.add(new Airport("~", "", "24", "2k4", "2k4", 0, 0, 0, "", "", "", 24));
        airportList.add(new Airport("<", "", "25", "2k5", "2k5", 0, 0, 0, "", "", "", 25));
        airportList.add(new Airport("{", "", "26", "26k", "2k6", 0, 0, 0, "", "", "", 26));
        airportList.add(new Airport("}", "", "27", "2k7", "2k7", 0, 0, 0, "", "", "", 27));
        airportList.add(new Airport(":", "", "28", "2k8", "k28", 0, 0, 0, "", "", "", 28));
        airportList.add(new Airport(";", "", "29", "2k9", "k29", 0, 0, 0, "", "", "", 29));
    }


}
