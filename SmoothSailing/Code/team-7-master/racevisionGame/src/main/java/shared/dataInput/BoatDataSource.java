package shared.dataInput;


import shared.model.Boat;
import shared.model.Mark;

import java.util.Map;

/**
 * Provides information about the boats and marker boats in a race.
 */
public interface BoatDataSource {

    /**
     * Returns a map between source ID and boat for all boats in the race.
     * @return Map between source ID and boat.
     */
    Map<Integer, Boat> getBoats();

    /**
     * Returns a map between source ID and mark for all marks in the race.
     * @return Map between source ID and mark.
     */
    Map<Integer, Mark> getMarkerBoats();


    /**
     * Returns the sequence number associated with this data source. Used to indicate when it has changed.
     * @return Sequence number.
     */
    int getSequenceNumber();

    /**
     * Increments the sequence number for this data source. Used to indicate that it has changed.
     */
    void incrementSequenceNumber();
}
