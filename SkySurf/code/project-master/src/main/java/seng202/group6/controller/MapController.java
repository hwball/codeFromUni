package seng202.group6.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group6.DataContainer;
import seng202.group6.model.AirlineList;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.model.Flight;
import seng202.group6.model.FlightList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;
import seng202.group6.utils.RecordType;

/**
 * The controller class to handle the Map GUI.
 */
public class MapController implements Observer {
    // Web view
    @FXML
    private WebView webView;
    private WebEngine webEngine;

    /**
     * The controller for the fxml-including main view.
     */
    private MainWindowController mainWindowController;
    /**
     * The container of data to map.
     */
    private DataContainer dataContainer;
    /**
     * The path to the map.
     */
    private String mapURL;
    /**
     * The maximum number of each type of record to map.
     */
    private Integer maxItems = Integer.MAX_VALUE;


    /**
     * Sets the data container, main window controller and the map url.
     *
     * @param mainWindowController the controller of the main window
     * @param dataContainer        the data container being used by the program
     * @param mapURL               the url of the map to display in the program
     */
    public void setUp(MainWindowController mainWindowController, DataContainer dataContainer, String mapURL) {
        this.mainWindowController = mainWindowController;
        this.dataContainer = dataContainer;
        dataContainer.addObserver(this);

        dataContainer.getAirportData().addListener((ListChangeListener<Airport>) (c -> updateDisplay(RecordType.AIRPORT)));
        dataContainer.getFlightData().addListener((ListChangeListener<Flight>) (c -> updateDisplay(RecordType.FLIGHT)));
        dataContainer.getRouteData().addListener((ListChangeListener<Route>) (c -> updateDisplay(RecordType.ROUTE)));

        this.mapURL = mapURL;
        initialiseMap();
    }


    /**
     * Sets up the map pane, and adds listener such that the map is redrawn whenever the page is
     * reloaded.
     */
    private void initialiseMap() {
        webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                mapRedrawAll();
            }
        });
        reloadPage();
    }


    /**
     * Reloads the html file. Used to force a reload in case the web view navigates away from the
     * correct page.
     */
    void reloadPage() {
        webEngine.load(mapURL);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == dataContainer) {
            // The update was triggered by the data container changing state.
            if (arg instanceof RecordType) {
                // An active list changed state
                if (mainWindowController.isMapTabSelected()) {
                    updateDisplay((RecordType) arg);
                }
            } else if (arg instanceof AirportList) {
                // Active airport list changed, redraw airports and routes
                updateDisplay(RecordType.AIRPORT);
                updateDisplay(RecordType.ROUTE);
            } else if (arg instanceof FlightList) {
                // Active flight list changed, redraw flights
                updateDisplay(RecordType.FLIGHT);
            } else if (arg instanceof RouteList) {
                // Active route list changed, redraw routes
                updateDisplay(RecordType.ROUTE);
            }
        }
    }


    /**
     * Calls the functions required to update the map.
     *
     * @param type The type of record to update (AIRPORT, ROUTE, or FLIGHT).
     */
    private void updateDisplay(RecordType type) {
        if (mainWindowController.isMapTabSelected()) {
            switch (type) {
                case AIRPORT:
                    if (maxItems < dataContainer.getAirportData().size()) {
                        mapAirports(parseAirports(dataContainer.getAirportData().subList(0, maxItems)));
                    } else {
                        mapAirports(parseAirports(dataContainer.getAirportData()));
                    }
                    break;
                case ROUTE:
                    HashMap<String, Route> routes = new HashMap<>(20000);
                    for (int i = 0; i < dataContainer.getRouteData().size(); i++) {
                        if (routes.size() >= maxItems) {
                            break;
                        }
                        String src = dataContainer.getRouteData().get(i).getSourceAirport();
                        String dest = dataContainer.getRouteData().get(i).getDestinationAirport();
                        routes.put(src.compareTo(dest) < 0
                                ? src + dest : dest + src, dataContainer.getRouteData().get(i));
                    }
                    mapRoutes(parseRoutes(routes.values()));
                    break;
                case FLIGHT:
                    if (maxItems < dataContainer.getFlightData().size()) {
                        mapFlights(parseFlights(dataContainer.getFlightData().subList(0, maxItems)));
                    } else {
                        mapFlights(parseFlights(dataContainer.getFlightData()));
                    }
                    break;
            }
        }
    }


    /**
     * Calls on the MapDataManager to update the display of airports, routes and flights.
     */
    private void mapRedrawAll() {
        if (mainWindowController.isMapTabSelected()) {
            updateDisplay(RecordType.AIRPORT);
            updateDisplay(RecordType.FLIGHT);
            updateDisplay(RecordType.ROUTE);
        }
    }


    /**
     * Redraws the airports displayed on the map.
     *
     * @param airports A javascript array of airports to display, in the format ["name", latitude,
     *                 longitude].
     */
    private void mapAirports(String airports) {
        webEngine.executeScript("deleteAirports()");
        webEngine.executeScript("showAirports(" + airports + ")");
    }


    /**
     * Redraws the flights displayed on the map.
     *
     * @param flights A javascript array of flights to display.
     */
    private void mapFlights(String flights) {
        webEngine.executeScript("deleteFlights()");
        webEngine.executeScript("showFlights(" + flights + ")");
    }


    /**
     * Redraws the routes displayed on the map.
     *
     * @param routes A javascript array of routes to display.
     */
    private void mapRoutes(String routes) {
        webEngine.executeScript("deleteRoutes()");
        webEngine.executeScript("showRoutes(" + routes + ")");
    }


    /**
     * Parses an airportList into a list string that the map javascript can accept.
     *
     * @param airports airportList to be parsed.
     * @return String list of airports.
     */
    private String parseAirports(Collection<Airport> airports) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (Airport airport : airports) {
            stringBuilder.append(airport.getMappingJSON());
            stringBuilder.append(",");
        }
        int lastComma = stringBuilder.lastIndexOf(",");
        if (lastComma > 0) {
            stringBuilder.deleteCharAt(lastComma);
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }


    /**
     * Parses a flightList into a list string that the map javascript can accept.
     *
     * @param flights flightList to be parsed.
     * @return String list of flights.
     */
    private String parseFlights(Collection<Flight> flights) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        flights.forEach(flight -> {
            stringBuilder.append(flight.getMappingJSON());
            stringBuilder.append(",");
        });
        int lastComma = stringBuilder.lastIndexOf(",");
        if (lastComma > 0) {
            stringBuilder.deleteCharAt(lastComma);
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    /**
     * Parses a routeList into a list string that the map javascript can accept.
     *
     * @param routes routeList to be parsed.
     * @return String list of routes.
     */
    private String parseRoutes(Collection<Route> routes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        AirportList airportList = dataContainer.getActiveAirportList();
        AirlineList airlineList = dataContainer.getActiveAirlineList();

        if (airportList == null) {
            return "[]";
        }
        routes.forEach(route -> {
            Airport dst = airportList.getById(route.getSourceAirport());
            Airport src = airportList.getById(route.getDestinationAirport());

            if (dst != null && src != null) {
                // Extract coordinates from airports.
                Double[] source = {src.getLatitude(), src.getLongitude()};
                Double[] destination = {dst.getLatitude(), dst.getLongitude()};

                // Get airline name if possible
                String airlineName = route.getAirline();
                try {
                    airlineName = airlineList.getById(route.getAirline()).getName();
                } catch (NullPointerException e) {
                    // either no active list or airline not in active list. No big deal, just
                    // display code instead of airline name.
                }

                stringBuilder.append(
                        route.getMappingJSON(airlineName, source, destination)
                );
                stringBuilder.append(",");
            }
        });
        int lastComma = stringBuilder.lastIndexOf(",");
        if (lastComma > 0) {
            stringBuilder.deleteCharAt(lastComma);
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }


    /**
     * Sets the max number of items the map will show.
     *
     * @param maxItems the max number of items that the map will show
     */
    void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
        reloadPage();
        mapRedrawAll();
    }


    /**
     * Gets the maximum number of records the map will show of each type.
     *
     * @return the maximum number of items
     */
    Integer getMaxItems() {
        return this.maxItems;
    }
}
