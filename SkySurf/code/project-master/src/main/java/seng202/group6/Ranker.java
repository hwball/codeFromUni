package seng202.group6;

import seng202.group6.model.AirportList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Calculates the number of routes for each airport
 */
public class Ranker implements Observer {

    private HashMap<String, Integer> numberOfRoutes = new HashMap<>();
    private AirportList airportList;
    private RouteList routeList;

    private final AirportList AIRPORT_LIST_EMPTY = new AirportList("");
    private final RouteList ROUTE_LIST_EMPTY = new RouteList("");

    /**
     * Constructor for the ranker
     *
     * @param airportList airport list to rank
     * @param routeList   route list to rank by
     */
    public Ranker(AirportList airportList, RouteList routeList) {
        this.airportList = (airportList != null) ? airportList : AIRPORT_LIST_EMPTY;
        this.airportList.addObserver(this);
        this.routeList = (routeList != null) ? routeList : ROUTE_LIST_EMPTY;
        this.routeList.addObserver(this);

        countRoutes();
    }


    /**
     * Sets the active airport list.
     *
     * @param airportList airport list to rank
     */
    public void setAirportList(AirportList airportList) {
        this.airportList.deleteObserver(this);
        this.airportList = (airportList != null) ? airportList : AIRPORT_LIST_EMPTY;
        this.airportList.addObserver(this);

        countRoutes();
    }


    /**
     * Sets the active route list.
     *
     * @param routeList route list to rank by
     */
    public void setRouteList(RouteList routeList) {
        this.routeList.deleteObserver(this);
        this.routeList = (routeList != null) ? routeList : ROUTE_LIST_EMPTY;
        this.routeList.addObserver(this);

        countRoutes();
    }


    /**
     * Triggers an update of the route counts. Tries to
     * only do the minimum to keep the counts up to date.
     *
     * @param obs the observable object
     * @param obj an argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof DataContainer) {
            if (obj instanceof AirportList) {
                // The active airline list in the data container has changed.
                setAirportList((AirportList) obj);

            } else if (obj instanceof RouteList) {
                // The active airline list in the data container has changed.
                setRouteList((RouteList) obj);
            }
        } else if (obs instanceof RouteList) {
            if (obj instanceof Route) {
                // A single route has been added, so add it to the count.
                countRoute((Route) obj);
                setNumberOfRoutes((Route) obj);
            } else {
                // A more complex change occurred so recalculate everything
                countRoutes();
            }
        } else if (obs instanceof AirportList) {
            countRoutes();
        }
    }


    /**
     * Resets the number of routes for each airport and counts all the routes in the active route
     * list.
     */
    private void countRoutes() {
        numberOfRoutes.clear();
        routeList.getRecords().forEach(this::countRoute);
        setNumberOfRoutes();
    }


    /**
     * Counts the given route if valid.
     *
     * @param route individual route to count
     */
    private void countRoute(Route route) {
        String destination = route.getDestinationAirport();
        String source = route.getSourceAirport();
        if (airportList.recordUniquelyPresent(source) && airportList.recordUniquelyPresent(destination)) {
            numberOfRoutes.putIfAbsent(source, 0);
            numberOfRoutes.putIfAbsent(destination, 0);
            numberOfRoutes.replace(source, numberOfRoutes.get(source) + 1);
            numberOfRoutes.replace(destination, numberOfRoutes.get(destination) + 1);
        }
    }


    /**
     * Sets the number of routes for each airport in the active list. Temporarily removes this class
     * as an observer of the list while doing so as to not cause an update here.
     */
    private void setNumberOfRoutes() {
        airportList.deleteObserver(this);
        airportList.setNumberOfRoutes(numberOfRoutes, true);
        airportList.addObserver(this);
    }


    /**
     * Sets the number of routes only for the terminals of the given route.
     *
     * @param route the route who's terminals need updating.
     */
    private void setNumberOfRoutes(Route route) {
        airportList.deleteObserver(this);

        HashMap<String, Integer> subset = new HashMap<>();
        String source = route.getSourceAirport();
        String destination = route.getDestinationAirport();
        subset.put(source, numberOfRoutes.get(source));
        subset.put(destination, numberOfRoutes.get(destination));
        airportList.setNumberOfRoutes(subset, false);

        airportList.addObserver(this);
    }

}
