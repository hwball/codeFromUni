package seng202.group6.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import seng202.group6.DataContainer;
import seng202.group6.model.Airline;
import seng202.group6.model.AirlineList;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.model.RecordList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;
import seng202.group6.utils.RecordType;

/**
 * The controller class to handle the Graphs GUI.
 */
@SuppressWarnings("unchecked")
public class GraphsController implements Observer {
    //graphs
    @FXML
    private BarChart<String, Integer> graphEquipment;
    @FXML
    private BarChart<String, Integer> graphNumberOfRoutes;
    @FXML
    private BarChart<String, Integer> graphAirportCountry;
    @FXML
    private BarChart<String, Integer> graphAirlineCountry;

    private DataContainer dataContainer;
    private MainWindowController mainWindowController;

    /**
     * The maximum number of entries to show in a graph.
     */
    private Integer maxItems;


    /**
     * FX initialization method. Sets up the charts.
     */
    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        graphEquipment.getXAxis().setLabel("Equipment");
        graphEquipment.getYAxis().setLabel("Number of Routes");
        graphEquipment.setLegendVisible(false);

        graphNumberOfRoutes.getXAxis().setLabel("Airports");
        graphNumberOfRoutes.getYAxis().setLabel("Number of Routes");
        graphNumberOfRoutes.setLegendVisible(false);

        graphAirportCountry.getXAxis().setLabel("Countries");
        graphAirportCountry.getYAxis().setLabel("Number of Airports");
        graphAirportCountry.setLegendVisible(false);

        graphAirlineCountry.getXAxis().setLabel("Countries");
        graphAirlineCountry.getYAxis().setLabel("Number of Airlines");
        graphAirlineCountry.setLegendVisible(false);
        maxItems = 30;
    }


    /**
     * Sets up the controller with the data container used by the program as well as sets a link to
     * the main window controller
     *
     * @param dataContainer        data container being used by the program
     * @param mainWindowController the main window controller
     */
    public void setUp(DataContainer dataContainer, MainWindowController mainWindowController) {
        this.dataContainer = dataContainer;
        this.mainWindowController = mainWindowController;
        this.dataContainer.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == dataContainer) {
            // The update was triggered by the data container changing state.
            if (arg instanceof RecordType || arg instanceof RecordList) {
                // An active list has changed or
                redrawAllGraphs();
            }
        }
    }


    /**
     * Redraws all graphs.
     */
    void redrawAllGraphs() {
        if (mainWindowController.isGraphTabSelected()) {
            updateEquipmentPerRouteGraph();
            updateRoutesPerAirport();
            updateAirportPerCountry();
            updateAirlinesPerCountry();
        }
    }


    /**
     * Updates the equipment per route graph to plot based on the active routes.
     */
    private void updateEquipmentPerRouteGraph() {
        graphEquipment.getData().clear();
        graphEquipment.layout();
        if (dataContainer.getActiveRouteList() != null) {
            XYChart.Series<String, Integer> equipPerRoute = equipmentPerRoute(dataContainer.getActiveRouteList(), maxItems);
            graphEquipment.getData().addAll(equipPerRoute);
        }
    }


    /**
     * Updates the routes per airport graph to plot based on the active routes.
     */
    private void updateRoutesPerAirport() {
        graphNumberOfRoutes.getData().clear();
        graphNumberOfRoutes.layout();
        if (dataContainer.getActiveRouteList() != null && dataContainer.getActiveAirportList() != null) {
            XYChart.Series<String, Integer> routesPerAirport =
                    routePerAirport(dataContainer.getActiveAirportList(), maxItems);
            graphNumberOfRoutes.getData().addAll(routesPerAirport);
        }
    }


    /**
     * Updates the airports per country graph to plot based on the active airports.
     */
    private void updateAirportPerCountry() {
        graphAirportCountry.getData().clear();
        graphAirportCountry.layout();
        if (dataContainer.getActiveAirportList() != null) {
            XYChart.Series<String, Integer> airportCountry =
                    airportsPerCountry(dataContainer.getActiveAirportList(), maxItems);
            graphAirportCountry.getData().addAll(airportCountry);
        }
    }


    /**
     * Updates the airlines per country graph to plot based on the active airlines.
     */
    private void updateAirlinesPerCountry() {
        graphAirlineCountry.getData().clear();
        graphAirlineCountry.layout();
        if (dataContainer.getActiveAirlineList() != null) {
            XYChart.Series<String, Integer> airlineCountry =
                    airlinesPerCountry(dataContainer.getActiveAirlineList(), maxItems);
            graphAirlineCountry.getData().addAll(airlineCountry);
        }
    }


    /**
     * Returns a XYChart.Series plotting the top x number of routes per airport where routes are on
     * the Y axis and airports are on the X axis using the information stored in an RouteList.
     *
     * @param airportList the airportList to plot data from
     * @param top         an integer representing the top x number of routes to display
     * @return the series containing the data
     */
    static XYChart.Series<String, Integer> routePerAirport(AirportList airportList, Integer top) {
        Map<String, Integer> freq = new HashMap<>();
        airportList.getRecords().forEach(airport -> freq.put(airport.getIata(), airport.getNumRoutes()));

        Map<String, Integer> orderedFreq = sortMapByValues(freq);
        XYChart.Series<String, Integer> plot = new XYChart.Series<>();

        int i = 0;
        for (Map.Entry<String, Integer> entry : orderedFreq.entrySet()) {
            plot.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            i++;
            if (i >= top) {
                break;
            }
        }
        return plot;
    }


    /**
     * Returns a XYChart.Series plotting the top x number of routes per equipment where routes are
     * on the Y axis and equipment are on the X axis using the information stored in an RouteList
     *
     * @param routeList the routeList to plot data from
     * @param top       an integer representing the top x number of routes to display
     * @return the series containing the data
     */
    static XYChart.Series<String, Integer> equipmentPerRoute(RouteList routeList, Integer top) {
        Map<String, Integer> freq = new HashMap<>();

        for (Route route : routeList.getRecords()) {
            route.getEquipment().forEach(equipment -> {
                if (freq.putIfAbsent(equipment, 1) != null) {
                    // putIfAbsent() returns null if key initially absent
                    freq.replace(equipment, freq.get(equipment) + 1);
                }
            });
        }
        Map<String, Integer> orderedFreq = sortMapByValues(freq);
        XYChart.Series<String, Integer> plot = new XYChart.Series<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : orderedFreq.entrySet()) {
            plot.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            i++;
            if (i >= top) {
                break;
            }
        }
        return plot;
    }


    /**
     * Returns a XYChart.Series plotting the top x number of airports per country where airports are
     * on the Y axis and country are on the X axis using the information stored in an AirportList
     *
     * @param airportList the airportList to plot data from
     * @param top         an integer representing the top x number of airports to display
     * @return the series containing the data
     */
    static XYChart.Series<String, Integer> airportsPerCountry(AirportList airportList, Integer top) {
        Map<String, Integer> freq = new HashMap<>();

        for (Airport airport : airportList.getRecords()) {
            String country = airport.getCountry();
            if (freq.putIfAbsent(country, 1) != null) {
                // putIfAbsent() returns null if key initially absent
                freq.replace(country, freq.get(country) + 1);
            }
        }
        Map<String, Integer> orderedFreq = sortMapByValues(freq);
        XYChart.Series<String, Integer> plot = new XYChart.Series<>();
        int counter = 0;
        for (Map.Entry<String, Integer> entry : orderedFreq.entrySet()) {
            counter++;
            plot.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            if (counter >= top) {
                break;
            }
        }
        return plot;
    }


    /**
     * Returns a XYChart.Series plotting the top x number of airline per country where airline are
     * on the Y axis and country are on the X axis using the information stored in an AirlineList
     *
     * @param airlineList the airlineList to plot data from.
     * @param top         an integer representing the top x number of airlines to display
     * @return the series containing the data
     */
    static XYChart.Series<String, Integer> airlinesPerCountry(AirlineList airlineList, Integer top) {
        Map<String, Integer> freq = new HashMap<>();

        for (Airline airline : airlineList.getRecords()) {
            String country = airline.getCountry();
            if (freq.putIfAbsent(country, 1) != null) {
                // putIfAbsent() returns null if key initially absent
                freq.replace(country, freq.get(country) + 1);
            }
        }
        Map<String, Integer> orderedFreq = sortMapByValues(freq);
        XYChart.Series<String, Integer> plots = new XYChart.Series<>();
        int counter = 0;
        for (Map.Entry<String, Integer> entry : orderedFreq.entrySet()) {
            counter++;
            plots.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            if (counter >= top) {
                break;
            }
        }
        return plots;
    }


    /**
     * Sorts a mapping based on its values.
     *
     * @param originalMap the mapping to sort
     * @return the sorted mapping
     */
    private static LinkedHashMap<String, Integer> sortMapByValues(Map<String, Integer> originalMap) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(originalMap.entrySet());
        Collections.sort(entries, (a, b) -> -a.getValue().compareTo(b.getValue()));
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


    /**
     * Sets the max number of items that are shown on the map.
     *
     * @param maxItems the max number of items that will be shown on the map
     */
    void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
        redrawAllGraphs();
    }

    Integer getMaxItems() {
        return this.maxItems;
    }
}
