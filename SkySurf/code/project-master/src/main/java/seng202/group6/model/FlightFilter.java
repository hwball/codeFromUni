package seng202.group6.model;

import java.util.HashSet;

/**
 * Flight filter class used to crate filters for flight lists
 */
public class FlightFilter extends Filter<Flight> {

    /**
     * The HashSet of destination airport criteria that will be used in the match method
     * to find Flights that match criteria.
     */
    private final HashSet<String> destinationAirport = new HashSet<>();
    /**
     * The HashSet of source airport criteria that will be used in the match method
     * to find Flights that match criteria.
     */
    private final HashSet<String> sourceAirport = new HashSet<>();


    /**
     * Takes a flight object as a parameter and returns true if it meets the filter's criteria
     * and false if it does not.
     *
     * @param flight The flight object to be checked.
     * @return Returns true if the flight meets the filter criteria and false if it does not.
     */
    public boolean matches(Flight flight) {
        return  stringMatches(destinationAirport, flight.getDestinationAirport()) &&
                stringMatches(sourceAirport, flight.getSourceAirport());
    }

    /* Methods to add criteria */

    /**
     * Adds a Destination Airport criteria to the filter.
     *
     * @param destinationAirport The Destination Airport string to add to the filter
     */
    public void addDestinationAirport(String destinationAirport) {
        this.destinationAirport.add(destinationAirport.toLowerCase());
    }


    /**
     * Adds a Source Airport criteria to the filter.
     *
     * @param sourceAirport The Source Airport string to add to the filter
     */
    public void addSourceAirport(String sourceAirport) {
        this.sourceAirport.add(sourceAirport.toLowerCase());
    }

    /* Getters*/

    /**
     * Returns the HashSet of Destination Airport string criteria.
     *
     * @return The HashSet of Destination Airport string criteria
     */
    public HashSet<String> getDestinationAirport() {
        return destinationAirport;
    }


    /**
     * Returns the HashSet of Source Airport string criteria.
     *
     * @return The HashSet of Source Airport string criteria
     */
    public HashSet<String> getSourceAirport() {
        return sourceAirport;
    }
}
