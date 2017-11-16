package shared.dataInput;

import shared.model.Boat;
import shared.model.Mark;

import java.util.HashMap;
import java.util.Map;

/**
 * An empty {@link BoatDataSource}. Can be used to initialise a race with no data.
 */
public class EmptyBoatDataSource implements BoatDataSource {

    /**
     * A map of source ID to boat for all boats in the race.
     */
    private final Map<Integer, Boat> boatMap = new HashMap<>();

    /**
     * A map of source ID to mark for all marks in the race.
     */
    private final Map<Integer, Mark> markerMap = new HashMap<>();


    private int sequenceNumber = -1;


    public EmptyBoatDataSource() {
    }


    /**
     * Get the boats that are going to participate in this race
     * @return Dictionary of boats that are to participate in this race indexed by SourceID
     */
    @Override
    public Map<Integer, Boat> getBoats() {
        return boatMap;
    }

    /**
     * Get the marker Boats that are participating in this race
     * @return Dictionary of the Markers Boats that are in this race indexed by their Source ID.
     */
    @Override
    public Map<Integer, Mark> getMarkerBoats() {
        return markerMap;
    }


    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void incrementSequenceNumber() {
        sequenceNumber++;
    }
}
