package seng202.group6.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Route class that represents an route.
 */
public class Route extends Record {

    /**
     * Either the ICAO or IATA for the airline that flies this route.
     */
    private String airline;
    /**
     * The codeshare the airline.
     */
    private Boolean codeShare;
    /**
     * The 3-letter (IATA) or 4-letter (ICAO) code of the destination airport.
     */
    private String destinationAirport;
    /**
     * 3-letter codes for plane type(s) generally used on this route.
     */
    private ArrayList<String> equipment;
    /**
     * 3-letter (IATA) or 4-letter (ICAO) code of the source airport.
     */
    private String sourceAirport;
    /**
     * The number of stops this route has.
     */
    private int stops;

    /**
     * Constructor for route class.
     *
     * @param airline            2-letter (IATA) or 3-letter (ICAO) code of the airline
     * @param sourceAirport      3-letter (IATA) or 4-letter (ICAO) code of the source airport
     * @param destinationAirport 3-letter (IATA) or 4-letter (ICAO) code of the destination airport
     * @param codeShare          "Y" if this flight is a codeshare (that is, not operated by
     *                               Airline, but another carrier), empty otherwise
     * @param stops              Number of stops on this flight ("0" for direct)
     * @param equipment          3-letter codes for plane type(s) generally used on this flight
     */
    public Route(String airline, String sourceAirport, String destinationAirport,
                 Boolean codeShare, int stops, ArrayList<String> equipment) {
        this.airline = airline;
        this.codeShare = codeShare;
        this.destinationAirport = destinationAirport;
        ArrayList<String> equip = new ArrayList<>();
        equip.addAll(equipment);
        this.equipment = equip;
        this.sourceAirport = sourceAirport;
        this.stops = stops;
    }


    /**
     * Getter for airline.
     *
     * @return airline code
     */
    public String getAirline() {
        return airline;
    }


    /**
     * Setter for airline.
     *
     * @param airline new route airline code
     */
    public void setAirline(String airline) {
        this.airline = airline;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for codeshare.
     *
     * @return route codeshare
     */
    public Boolean getCodeShare() {
        return codeShare;
    }


    /**
     * Setter for codeshare.
     *
     * @param codeShare new route codeshare value
     */
    public void setCodeShare(Boolean codeShare) {
        this.codeShare = codeShare;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for destination airport.
     *
     * @return route destination airport
     */
    public String getDestinationAirport() {
        return destinationAirport;
    }


    /**
     * Setter for destination airport.
     *
     * @param destinationAirport new route destination airport
     */
    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for equipment.
     *
     * @return equipment used on route
     */
    public ArrayList<String> getEquipment() {
        return equipment;
    }


    /**
     * Setter for equipment.
     *
     * @param equipment new equipment used on route
     */
    public void setEquipment(ArrayList<String> equipment) {
        this.equipment = equipment;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for source aiport.
     *
     * @return route source airport
     */
    public String getSourceAirport() {
        return sourceAirport;
    }


    /**
     * Setter for source aiport.
     *
     * @param sourceAirport new route source airport
     */
    public void setSourceAirport(String sourceAirport) {
        this.sourceAirport = sourceAirport;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for stops.
     *
     * @return number of stops on route
     */
    public int getStops() {
        return stops;
    }


    /**
     * Setter for stops.
     *
     * @param stops new number of stops on route
     */
    public void setStops(int stops) {
        this.stops = stops;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for equipment returns as string.
     *
     * @return equipment used on routes as string
     */
    public String getEquipmentAsString() {
        String string = "";
        if (equipment.size() > 0) {
            string = string + equipment.get(0);
        }
        for (int i = 1; i < equipment.size(); i++) {
            string = string + ", " + equipment.get(i);
        }
        return string;
    }


    /**
     * Checks for for equality.
     *
     * @param object object to check
     * @return true if equal
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        Boolean result = false;
        if (object instanceof Route) {
            Route that = (Route) object;
//            Boolean equipIsEqual = that.getEquipment().size() == equipment.size();
//            if (equipIsEqual && equipment.size() != 0) {
//                for (int i = 0; i < equipment.size(); i++) {
//                    if (!that.getEquipment().get(i).equals(equipment.get(i))) {
//                        equipIsEqual = false;
//                        break;
//                    }
//                }
//            }
            result = that.canEqual(this) &&
                    airline.equals(that.getAirline()) &&
                    codeShare.equals(that.getCodeShare()) &&
                    destinationAirport.equals(that.getDestinationAirport()) &&
                    sourceAirport.equals(that.getSourceAirport()) &&
                    stops == that.getStops() &&
                    equipment.equals(that.getEquipment());
        }
        return result;
    }


    /**
     * Checks for typing.
     *
     * @param object object
     * @return true if object is instance of route
     */
    @Override
    protected boolean canEqual(Object object) {
        return object instanceof Route;
    }


    /**
     * Makes a new copy of the route.
     *
     * @return new copy of route
     */
    public Route copy() {
        return new Route(airline, sourceAirport, destinationAirport, codeShare, stops, equipment);
    }


    /**
     * Used to get the hash code for the route.
     *
     * @return hashcode of route
     */
    @Override
    public int hashCode() {
        int result = airline.hashCode();
        result = 31 * result + codeShare.hashCode();
        result = 31 * result + destinationAirport.hashCode();
        result = 31 * result + equipment.hashCode();
        result = 31 * result + sourceAirport.hashCode();
        result = 31 * result + stops;
        return result;
    }


    /**
     * To string method for routes.
     *
     * @return to sting of route
     */
    @Override
    public String toString() {
        return "Route{" +
                "airline='" + airline + '\'' +
                ", codeShare='" + codeShare + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", equipment='" + equipment + '\'' +
                ", sourceAirport='" + sourceAirport + '\'' +
                ", stops='" + stops + '\'' +
                '}';
    }


    /**
     * Getter for JSON.
     *
     * @param airlineName the airline name to display, may be null
     * @param source the source airport latitude and longitude
     * @param destination the destination airport latitude and longitude
     * @return the JSON representation necessary to map the route.
     */
    public String getMappingJSON(String airlineName, Double[] source, Double[] destination) {
        if (airlineName == null) {
            airlineName = airline;
        }
        return String.format("[\"%s\",%f,%f,%f,%f]",
                airlineName, source[0], source[1], destination[0], destination[1]);
    }


    /**
     * Checks to see if route is valid.
     *
     * @param route    route to check
     * @param airports hash map of airports
     * @return true if route is valid
     */
    public static boolean isValid(Route route, HashMap<String, ?> airports) {
        boolean valid = false;
        if (airports.containsKey(route.getDestinationAirport()) &&
                airports.containsKey(route.getSourceAirport())) {
            valid = true;
        }
        return valid;
    }
}
