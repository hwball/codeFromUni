package seng202.group6.model;


import java.util.Collection;

/**
 * Defines methods specific to lists of routes
 */
public class RouteList extends RecordList<Route> {

    /**
     * Creates a RouteList with the specified name and no routes.
     *
     * @param name the name of the RouteList
     */
    public RouteList(String name) {
        super(name);
    }


    /**
     * Creates a RouteList with the specified name that holds the given routes
     *
     * @param name   the name of the RouteList
     * @param routes the routes to store in the internal list
     */
    public RouteList(String name, Collection<Route> routes) {
        super(name, routes);
    }
}
