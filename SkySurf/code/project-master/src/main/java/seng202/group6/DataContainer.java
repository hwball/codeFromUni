package seng202.group6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group6.model.*;
import seng202.group6.utils.RecordType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * The DataContainer class holds the instances of the model classes containing the data loaded into
 * the program, and provides the controllers access to them.
 */
public class DataContainer extends Observable implements Observer {
    /*
     * Notifies observers:
     * +--------Event---------------------------Argument----------------------------------------+
     * | Active list notified observers:                                                        |
     * |        activeAirlineList               RecordType.AIRLINE                              |
     * |        activeAirportList               RecordType.AIRPORT                              |
     * |        activeFlightList                RecordType.FLIGHT                               |
     * |        activeRouteList                 RecordType.ROUTE                                |
     * |                                                                                        |
     * | Active list selection changed:                                                         |
     * |        activeAirlineList               AirlineList  (activeAirlineList, may be null)   |
     * |        activeAirportList               AirportList  (activeAirportList, may be null)   |
     * |        activeFlightList                FlightList   (activeFlightList, may be null)    |
     * |        activeRouteList                 RouteList    (activeRouteList, may be null)     |
     * |                                                                                        |
     * | Lists reset/added/deleted:                           Notification.LISTS_CHANGED        |
     * +----------------------------------------------------------------------------------------+
     *
     * Observers:
     * MainWindowController: - list addition/deletion
     * Ranker:               - active list change & update (airport, route)
     * BasicFilterController - active list change & update
     *
     */

    // List of RecordLists
    // Reference to active lists
    /**
     * List of Airline lists.
     */
    private ArrayList<AirlineList> airlineLists = new ArrayList<>();

    /**
     * List of Airport lists.
     */
    private ArrayList<AirportList> airportLists = new ArrayList<>();

    /**
     * List of Route lists.
     */
    private ArrayList<RouteList> routeLists = new ArrayList<>();

    /**
     * List of Flight lists.
     */
    private ArrayList<FlightList> flightLists = new ArrayList<>();


    /**
     * Reference to active Airline list, can be null.
     */
    private AirlineList activeAirlineList = null;

    /**
     * Reference to active Airport list, can be null.
     */
    private AirportList activeAirportList = null;

    /**
     * Reference to active Route list, can be null.
     */
    private RouteList activeRouteList = null;

    /**
     * Reference to active Flight list, can be null.
     */
    private FlightList activeFlightList = null;

    /**
     * Currently applied Airport filter.
     */
    private AirportFilter airportFilter = new AirportFilter();

    /**
     * Currently applied Airline filter.
     */
    private AirlineFilter airlineFilter = new AirlineFilter();

    /**
     * Currently applied Route filter.
     */
    private RouteFilter routeFilter = new RouteFilter();

    /**
     * Currently applied Flight filter.
     */
    private FlightFilter flightFilter = new FlightFilter();

    /**
     * List containing filtered active Airports.
     */
    private final ObservableList<Airport> airportData = FXCollections.observableArrayList();

    /**
     * List containing filtered active Airlines.
     */
    private final ObservableList<Airline> airlineData = FXCollections.observableArrayList();

    /**
     * List containing filtered active Routes.
     */
    private final ObservableList<Route> routeData = FXCollections.observableArrayList();

    /**
     * List containing filtered active Flights.
     */
    private final ObservableList<Flight> flightData = FXCollections.observableArrayList();

    /**
     * A variable to track whether the DataContainer has been modified or not.
     */
    private boolean modified = false;


    /**
     * update() method as required by observable interface.
     * Delegates the updating of the observable lists for the four datatypes.
     *
     * @param obs the observable object
     * @param obj an argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable obs, Object obj) {
        modified = true;
        if (obs == activeAirlineList) {
            updateAirlines();
            setChanged();
            notifyObservers(RecordType.AIRLINE);

        } else if (obs == activeAirportList) {
            updateAirports();
            setChanged();
            notifyObservers(RecordType.AIRPORT);

        } else if (obs == activeFlightList) {
            updateFlights();
            setChanged();
            notifyObservers(RecordType.AIRPORT);

        } else if (obs == activeRouteList) {
            updateRoutes();
            setChanged();
            notifyObservers(RecordType.ROUTE);
        }
    }


    /**
     * Gets the modified property.
     *
     * @return true if the DataContainer has been modified, false otherwise
     */
    public boolean getModified() {
        return modified;
    }


    /**
     * Sets the modified property.
     *
     * @param modified used to determine if the data container has been modified
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }


    /**
     * Deletes all current lists, resets observers and observables,
     * then resets the lists of RecordLists to the given lists.
     *
     * @param airlineLists the new AirlinesLists
     * @param airportLists the new AirportLists
     * @param flightLists the new FlightLists
     * @param routeLists the new RoutesLists
     */
    public void reset(List<AirlineList> airlineLists, List<AirportList> airportLists,
                      List<FlightList> flightLists, List<RouteList> routeLists) {

        // Remove observers for outgoing lists to allow garbage collection
        this.airlineLists.forEach(RecordList::cleanUp);
        this.airportLists.forEach(RecordList::cleanUp);
        this.flightLists.forEach(RecordList::cleanUp);
        this.routeLists.forEach(RecordList::cleanUp);

        // Drop all the lists in memory.
        this.airlineLists = null;
        this.airportLists = null;
        this.flightLists = null;
        this.routeLists = null;

        setActiveAirlineList(null);
        setActiveAirportList(null);
        setActiveFlightList(null);
        setActiveRouteList(null);

        setFilters(new AirportFilter(), new AirlineFilter(), new RouteFilter(), new FlightFilter());
        this.airlineLists = (ArrayList<AirlineList>) airlineLists;
        this.airportLists = (ArrayList<AirportList>) airportLists;
        this.flightLists = (ArrayList<FlightList>) flightLists;
        this.routeLists = (ArrayList<RouteList>) routeLists;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
        modified = false;
    }


    /**
     * Returns the currently active AirlineList.
     *
     * @return the currently active AirlineList
     */
    public AirlineList getActiveAirlineList() {
        return activeAirlineList;
    }


    /**
     * Returns the currently active AirportList.
     *
     * @return the currently active AirportList
     */
    public AirportList getActiveAirportList() {
        return activeAirportList;
    }


    /**
     * Returns the currently active RouteList.
     *
     * @return the currently active RouteList
     */
    public RouteList getActiveRouteList() {
        return activeRouteList;
    }


    /**
     * Returns the currently active FlightList.
     *
     * @return the currently active FlightList
     */
    public FlightList getActiveFlightList() {
        return activeFlightList;
    }


    /**
     * Returns the list of Airports being observed by FX.
     *
     * @return airportData
     */
    public ObservableList<Airport> getAirportData() {
        return airportData;
    }


    /**
     * Returns the list of Airlines being observed by FX.
     *
     * @return airlineData
     */
    public ObservableList<Airline> getAirlineData() {
        return airlineData;
    }


    /**
     * Returns the list of Routes being observed by FX.
     *
     * @return routeData
     */
    public ObservableList<Route> getRouteData() {
        return routeData;
    }


    /**
     * Returns the list of Flights being observed by FX.
     *
     * @return flightData
     */
    public ObservableList<Flight> getFlightData() {
        return flightData;
    }


    /**
     * Returns the Airport Filter.
     *
     * @return airport filter
     */
    public AirportFilter getAirportFilter() {
        return airportFilter;
    }


    /**
     * Returns the Airline Filter.
     *
     * @return airline filter
     */
    public AirlineFilter getAirlineFilter() {
        return airlineFilter;
    }


    /**
     * Returns the Route Filter.
     *
     * @return route filter
     */
    public RouteFilter getRouteFilter() {
        return routeFilter;
    }


    /**
     * Returns the Flight Filter.
     *
     * @return flight filter
     */
    public FlightFilter getFlightFilter() {
        return flightFilter;
    }


    /**
     * Sets the active FlightList.
     *
     * @param index the index of the active list in the list of FlightLists
     */
    public void setActiveFlightList(Integer index) {
        if (activeFlightList != null) {
            activeFlightList.deleteObserver(this);
        }
        activeFlightList = null;
        if (index != null) {
            activeFlightList = flightLists.get(index);
            activeFlightList.addObserver(this);
        }
        setChanged();
        notifyObservers(activeFlightList);
        updateFlights();
    }


    /**
     * Sets the active RouteList.
     *
     * @param index the index of active route list in the list of RouteLists
     */
    public void setActiveRouteList(Integer index) {
        if (activeRouteList != null) {
            activeRouteList.deleteObserver(this);
        }
        activeRouteList = null;
        if (index != null) {
            activeRouteList = routeLists.get(index);
            activeRouteList.addObserver(this);
        }
        setChanged();
        notifyObservers(activeRouteList);
        updateRoutes();
    }


    /**
     * Sets the active AirportList.
     *
     * @param index the index of the active list in the list of AirportList
     */
    public void setActiveAirportList(Integer index) {
        if (activeAirportList != null) {
            activeAirportList.deleteObserver(this);
        }
        activeAirportList = null;
        if (index != null) {
            activeAirportList = airportLists.get(index);
            activeAirportList.addObserver(this);
        }
        setChanged();
        notifyObservers(activeAirportList);
        updateAirports();
    }


    /**
     * Sets the active AirlineList.
     *
     * @param index the index of the active list in the list of AirlineLists
     */
    public void setActiveAirlineList(Integer index) {
        if (activeAirlineList != null) {
            activeAirlineList.deleteObserver(this);
        }
        activeAirlineList = null;
        if (index != null) {
            activeAirlineList = airlineLists.get(index);
            activeAirlineList.addObserver(this);
        }
        setChanged();
        notifyObservers(activeAirlineList);
        updateAirlines();
    }


    /**
     * Removes an AirlineList from the internal collection.
     *
     * @param index the index of the AirlineList to remove
     */
    public void deleteAirlineList(int index) {
        if (activeAirlineList.equals(airlineLists.get(index))) {
            setActiveAirlineList(null);
        }
        airlineLists.remove(index);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Removes an AirportList from the internal collection.
     *
     * @param index the index of the AirportList to remove
     */
    public void deleteAirportList(int index) {
        if (activeAirportList.equals(airportLists.get(index))) {
            setActiveAirportList(null);
        }
        airportLists.remove(index);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Removes a FlightLists from the internal collection.
     *
     * @param index the index of the FlightLists to remove
     */
    public void deleteFlightList(int index) {
        if (activeFlightList.equals(flightLists.get(index))) {
            setActiveFlightList(null);
        }
        flightLists.remove(index);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Removes a RouteList from the internal collection.
     *
     * @param index the index of the RouteList to remove
     */
    public void deleteRouteList(int index) {
        if (activeRouteList.equals(routeLists.get(index))) {
            setActiveRouteList(null);
        }
        routeLists.remove(index);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Sets the filters. Does not act on them.
     *
     * @param airportFilter the filter for airports
     * @param airlineFilter the filter for airlines
     * @param routeFilter   the filter for routes
     * @param flightFilter  the filter for flights
     */
    public void setFilters(AirportFilter airportFilter, AirlineFilter airlineFilter,
                           RouteFilter routeFilter, FlightFilter flightFilter) {
        this.airportFilter = airportFilter;
        this.airlineFilter = airlineFilter;
        this.routeFilter = routeFilter;
        this.flightFilter = flightFilter;
    }


    /**
     * Applies the filters to all the active RecordLists, updating the observable lists with the results.
     */
    public void applyFilters() {
        updateAirlines();
        updateAirports();
        updateFlights();
        updateRoutes();
    }


    /**
     * Applies the filters to the active airline list, updating the observable list with the
     * results.
     */
    private void updateAirlines() {
        airlineData.clear();
        if (activeAirlineList != null) {
            airlineData.addAll(activeAirlineList.searchByCriteria(airlineFilter));
        }
    }


    /**
     * Applies the filters to the active airport list, updating the observable list with the
     * results.
     */
    private void updateAirports() {
        airportData.clear();
        if (activeAirportList != null) {
            airportData.addAll(activeAirportList.searchByCriteria(airportFilter));
        }
    }


    /**
     * Applies the filters to the active flight list, updating the observable list with the results.
     */
    private void updateFlights() {
        flightData.clear();
        if (activeFlightList != null) {
            flightData.addAll(activeFlightList.searchByCriteria(flightFilter));
        }
    }


    /**
     * Applies the filters to the active route list, updating the observable list with the results.
     */
    private void updateRoutes() {
        routeData.clear();
        if (activeRouteList != null) {
            routeData.addAll(activeRouteList.searchByCriteria(routeFilter));
        }
    }


    /**
     * Returns the AirlineList at the index given.
     *
     * @param index of wanted list
     * @return AirlineList
     */
    public AirlineList getAirlineList(int index) {
        return airlineLists.get(index);
    }


    /**
     * Returns the AirportList at the index given.
     *
     * @param index of wanted list
     * @return AirportList
     */
    public AirportList getAirportList(int index) {
        return airportLists.get(index);
    }


    /**
     * Returns the FlightList at the index given.
     *
     * @param index of wanted list
     * @return FlightList
     */
    public FlightList getFlightList(int index) {
        return flightLists.get(index);
    }


    /**
     * Returns the RouteList at the index given.
     *
     * @param index of wanted list
     * @return RouteList
     */
    public RouteList getRouteList(int index) {
        return routeLists.get(index);
    }


    /**
     * Returns all the currently stored AirportLists.
     *
     * @return AirlineList
     */
    public List<AirportList> getAirportLists() {
        return airportLists;
    }


    /**
     * Returns all the currently stored FlightLists.
     *
     * @return AirlineList
     */
    public List<AirlineList> getAirlineLists() {
        return airlineLists;
    }


    /**
     * Returns all the currently stored RouteLists.
     *
     * @return RouteList
     */
    public List<RouteList> getRouteLists() {
        return routeLists;
    }


    /**
     * Returns all the currently stored FlightLists.
     *
     * @return FlightLists
     */
    public List<FlightList> getFlightLists() {
        return flightLists;
    }


    /**
     * Adds airport list to list of AirportList.
     *
     * @param airports AirportList to be added
     */
    public void addAirportList(AirportList airports) {
        this.airportLists.add(airports);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Adds an AirlineList to the list of AirlineLists.
     *
     * @param airlines AirlineList to be added
     */
    public void addAirlineList(AirlineList airlines) {
        this.airlineLists.add(airlines);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Adds FlightList to the list of FlightLists.
     *
     * @param flights FlightList to be added
     */
    public void addFlightList(FlightList flights) {
        this.flightLists.add(flights);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Adds RouteList to the list of RouteList.
     *
     * @param routes RouteList to be added
     */
    public void addRouteList(RouteList routes) {
        this.routeLists.add(routes);
        modified = true;
        setChanged();
        notifyObservers(Notification.LISTS_CHANGED);
    }


    /**
     * Test to see if active AirlineList is at an index.
     *
     * @param index index of AirlineList
     * @return boolean stating if active
     */
    public boolean isActiveAirlineList(int index) {
        boolean isActive = false;
        try {
            isActive = activeAirlineList == airlineLists.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Tested by illegal index for AirlineList activeness");
            e.printStackTrace();
        }
        return isActive;
    }


    /**
     * Test to see if active AirportList is at the specified index.
     *
     * @param index index of AirportList
     * @return boolean stating if active
     */
    public boolean isActiveAirportList(int index) {
        boolean isActive = false;
        try {
            isActive = activeAirportList == airportLists.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Tested by illegal index for AirportList activeness");
            e.printStackTrace();
        }
        return isActive;
    }


    /**
     * Test to see if active FlightList is at the specified index.
     *
     * @param index index of FlightList
     * @return boolean stating if active
     */
    public boolean isActiveFlightList(int index) {
        boolean isActive = false;
        try {
            isActive = activeFlightList == flightLists.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Tested by illegal index for FlightList activeness");
            e.printStackTrace();
        }
        return isActive;
    }


    /**
     * Test to see if active RouteList is at the specified index.
     *
     * @param index index of RouteList
     * @return boolean stating if active
     */
    public boolean isActiveRouteList(int index) {
        boolean isActive = false;
        try {
            isActive = activeRouteList == routeLists.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Tested by illegal index for RouteList activeness");
            e.printStackTrace();
        }
        return isActive;
    }


    /**
     * TODO
     */
    public enum Notification {
        LISTS_CHANGED
    }
}
