package seng202.group6.model;


import java.util.Collection;

/**
 * Defines methods specific to lists of airlines.
 */
public class AirlineList extends IdentifiedRecordList<Airline> {
    /**
     * Creates a AirlineList with the specified name and no airlines.
     *
     * @param name the name of the AirlineList
     */
    public AirlineList(String name) {
        super(name);
    }

    /**
     * Creates an AirlineList with the specified name and holds the given airlines.
     *
     * @param name     the name of the AirlineList
     * @param airlines the airlines to store in the internal list
     */
    public AirlineList(String name, Collection<Airline> airlines) {
        super(name, airlines);
    }

}
