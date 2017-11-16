package seng202.group6.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a flight path between several points.
 * The first and last FlightPoints in the internal collection are expected to represent airports.
 */
public class Flight extends Record implements Observer {

    private ArrayList<FlightPoint> flightPoints;

    /**
     * Creates a Flight from a ArrayList of FlightPoint.
     *
     * @param flightPoints the FlightPoints that define the Flight
     */
    public Flight(ArrayList<FlightPoint> flightPoints) {
        ArrayList<FlightPoint> points = new ArrayList<>();
        for (FlightPoint point : flightPoints) {
            FlightPoint copy = point.copy();
            copy.addObserver(this);
            points.add(copy);
        }
        this.flightPoints = points;
    }


    /**
     * Gets the IATA code of the source airport of the Flight.
     *
     * @return the source airport of the Flight
     */
    public String getSourceAirport() {
        return flightPoints.get(0).getId();
    }


    /**
     * Gets the IATA code of the Flight's destination airport.
     *
     * @return the destination airport of the Flight
     */
    public String getDestinationAirport() {
        return flightPoints.get(flightPoints.size() - 1).getId();
    }


    /**
     * Get this Flight's FlightPoints.
     *
     * @return the FlightPoints of the Flight
     */
    public Collection<FlightPoint> getFlightPoints() {
        return Collections.unmodifiableCollection(flightPoints);
    }


    /**
     * Set the Flight's FlightPoints.
     *
     * @param flightPoints the FlightPoints to set for the Flight
     */
    @SuppressWarnings("unused")
    public void setFlightPoints(ArrayList<FlightPoint> flightPoints) {
        this.flightPoints = flightPoints;
        for (FlightPoint point : this.flightPoints) {
            point.addObserver(this);
        }
        setChanged();
        notifyObservers();
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        Boolean result = false;
        if (object instanceof Flight) {
            Flight that = (Flight) object;
            result = that.canEqual(this);
            ArrayList<FlightPoint> thosePoints = new ArrayList<>(that.getFlightPoints());
            if (flightPoints.size() != thosePoints.size()) {
                result = false;
            } else {
                ArrayList<FlightPoint> ourPoints = new ArrayList<>(flightPoints);
                for (int i = 0; i < ourPoints.size(); i++) {
                    if (!ourPoints.get(i).equals(thosePoints.get(i))) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }


    /**
     * Checks whether given object is a Flight.
     *
     * @param object object to check
     * @return true if object is a Flight
     */
    @Override
    protected boolean canEqual(Object object) {
        return object instanceof Flight;
    }


    /**
     * Copies the Flight into a new Flight.
     *
     * @return copy of original Flight
     */
    public Flight copy() {
        return new
                Flight(flightPoints.stream().map(FlightPoint::copy).collect(Collectors.toCollection(ArrayList::new)));
    }


    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }


    @Override
    public String toString() {
        return getSourceAirport() + "->" + getDestinationAirport();
    }


    @Override
    public int hashCode() {
        return getFlightPoints().hashCode();
    }


    /**
     * Returns a string ready to be mapped into Google maps.
     *
     * @return a string formatted to show the Flight in Google maps
     */
    public String getMappingJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        flightPoints.forEach(point -> {
            builder.append(point.getMappingJSON());
            builder.append(",");
        });
        int lastComma = builder.lastIndexOf(",");
        if (lastComma > 0) {
            builder.deleteCharAt(lastComma);
        }
        builder.append("]");
        return builder.toString();
    }


    @Override
    public void cleanUp() {
        flightPoints.forEach(FlightPoint::cleanUp);
        super.cleanUp();
    }
}
