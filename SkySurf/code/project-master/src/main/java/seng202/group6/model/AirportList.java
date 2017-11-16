package seng202.group6.model;

import java.util.Collection;
import java.util.HashMap;

/**
 * Contains methods specific to lists of airports
 */
public class AirportList extends IdentifiedRecordList<Airport> {
    /**
     * Creates a AirportList with the specified name and no airports.
     *
     * @param name the name of the AirportList
     */
    public AirportList(String name) {
        super(name);
    }


    /**
     * Creates a AirportList with the specified name that holds the given airports
     *
     * @param name     the name of the AirportList
     * @param airports the airports to store in the internal list
     */
    public AirportList(String name, Collection<Airport> airports) {
        super(name, airports);
    }


    /**
     * For each airport whose IATA or ICAO code is a key in the mapping, set that airport to have
     * that key's mapped value. This can be used to set the number of routes for a subset of the
     * airports in the internal list without affecting the others.
     *
     * Where more than one key of the mapping resolves to the same airport, the number of routes
     * will be set from the last key visited.
     *
     * @param numRoutes a mapping from airport IATA/ICAO
     * @param reset     whether to zero the number of all airports not in the mapping
     */
    public void setNumberOfRoutes(HashMap<String, Integer> numRoutes, boolean reset) {
        if (reset) {
            // Reset each airport's number of routes
            getRecords().forEach(airport -> {
                airport.deleteObserver(this);
                airport.setNumRoutes(0);
                airport.addObserver(this);
            });
        }
        // Attempt to match each id in number of routes to an airport and set the number of routes for it
        numRoutes.keySet().forEach(id -> {
            Airport airport = getById(id);
            if (airport != null) {
                airport.deleteObserver(this);
                airport.setNumRoutes(numRoutes.get(id));
                airport.addObserver(this);
            }
        });

        setChanged();
        notifyObservers();
    }
}
