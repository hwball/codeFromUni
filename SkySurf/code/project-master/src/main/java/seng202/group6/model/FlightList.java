package seng202.group6.model;


import java.util.Collection;

/**
 * Holds methods specific to lists of flights
 */
public class FlightList extends RecordList<Flight> {

    /**
     * Creates a FlightList with the specified name and no flights.
     *
     * @param name the name of the FlightList
     */
    public FlightList(String name) {
        super(name);
    }


    /**
     * Creates a RecordList with the specified name that holds the given flights.
     *
     * @param name    the name of the FlightList
     * @param flights the flights to store in the internal list
     */
    public FlightList(String name, Collection<Flight> flights) {
        super(name, flights);
    }
}
