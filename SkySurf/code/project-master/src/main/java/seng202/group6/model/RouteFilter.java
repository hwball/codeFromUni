package seng202.group6.model;

import java.util.HashSet;

/**
 * Route filter class used to crate filters for route lists
 */
public class RouteFilter extends Filter<Route> {

    /* These contain the active criteria.
     * !? Change to list-ish types rather tha sets - only likely to be short anyway.
     */
    private final HashSet<String> airline = new HashSet<>();
    private Boolean codeShare = null;
    private final HashSet<String> destinationAirport = new HashSet<>();
    private final HashSet<String> equipment = new HashSet<>();
    private final HashSet<String> sourceAirport = new HashSet<>();
    private Integer[] stops = new Integer[2];

    /**
     * Takes a route object as a parameter and returns true if it meets the filter's criteria and
     * false if it does not.
     *
     * @param route The flight object to be checked.
     * @return Returns true if the route meets the filter criteria and false if it does not.
     */
    public boolean matches(Route route) {
        return stringMatches(airline, route.getAirline()) &&
                booleanMatches(codeShare, route.getCodeShare()) &&
                stringMatches(destinationAirport, route.getDestinationAirport()) &&
                stringMatches(equipment, route.getEquipment()) &&
                stringMatches(sourceAirport, route.getSourceAirport()) &&
                inRange(stops, route.getStops());
    }

    /* Methods to add criteria */

    /**
     * Adds a airline criteria to the filter.
     *
     * @param airline The airline string to add to the filter.
     */
    public void addAirline(String airline) {
        this.airline.add(airline.toLowerCase());
    }


    /**
     * Adds a codeShare criteria to the filter.
     *
     * @param codeShare The codeShare  boolean to add to the filter.
     */
    public void setCodeShare(boolean codeShare) {
        this.codeShare = codeShare;
    }


    /**
     * Adds a Destination Airport criteria to the filter.
     *
     * @param destinationAirport The Destination Airport string to add to the filter.
     */
    public void addDestinationAirport(String destinationAirport) {
        this.destinationAirport.add(destinationAirport.toLowerCase());
    }


    /**
     * Adds a equipment criteria to the filter.
     *
     * @param equipment The equipment string to add to the filter.
     */
    public void addEquipment(String equipment) {
        this.equipment.add(equipment.toLowerCase());
    }


    /**
     * Adds a Source Airport criteria to the filter.
     *
     * @param sourceAirport The Source Aiport string to add to the filter.
     */
    public void addSourceAirport(String sourceAirport) {
        this.sourceAirport.add(sourceAirport.toLowerCase());
    }


    /**
     * Sets the Stops range criteria for the filter.
     *
     * @param min The minimum stops.
     * @param max The maximum stops.
     */
    public void setStops(Integer min, Integer max) {
        Integer[] range = {min, max};
        assert rangeIsValid(range);
        this.stops = range;
    }

    /* Getters*/

    /**
     * Returns the HashSet of airline string criteria.
     *
     * @return The HashSet of airline string criteria
     */
    public HashSet<String> getAirline() {
        return airline;
    }


    /**
     * Returns the HashSet of codeShare string criteria.
     *
     * @return The HashSet of codeShare string criteria
     */
    public Boolean getCodeShare() {
        return codeShare;
    }


    /**
     * Returns the HashSet of Destination Airport string criteria.
     *
     * @return The HashSet of Destination Airport string criteria
     */
    public HashSet<String> getDestinationAirport() {
        return destinationAirport;
    }


    /**
     * Returns the HashSet of equipment string criteria.
     *
     * @return The HashSet of equipment string criteria
     */
    public HashSet<String> getEquipment() {
        return equipment;
    }


    /**
     * Returns the HashSet of Source Airport string criteria.
     *
     * @return The HashSet of Source Airport string criteria
     */
    public HashSet<String> getSourceAirport() {
        return sourceAirport;
    }


    /**
     * Returns a Integer array containing the minimum and maximum stops.
     *
     * @return Integer array containing the minimum and maximum stops
     */
    public Integer[] getStops() {
        return stops;
    }

}
