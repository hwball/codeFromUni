package network.Messages;


import java.util.ArrayList;
import java.util.List;


/**
 * Represents a snapshot of the race's state.
 * Contains a list of {@link AC35Data} messages.
 * Send a copy of each message to a connected client.
 */
public class RaceSnapshot {

    /**
     * The contents of the snapshot.
     */
    private List<AC35Data> snapshot;


    /**
     * Constructs a snapshot using a given list of messages.
     * @param snapshot Messages to use as snapshot.
     */
    public RaceSnapshot(List<AC35Data> snapshot) {
        this.snapshot = snapshot;
    }


    /**
     * Gets the contents of the snapshot.
     * This is a shallow copy.
     * @return Contents of the snapshot.
     */
    public List<AC35Data> getSnapshot() {

        List<AC35Data> copy = new ArrayList<>(snapshot);

        return copy;
    }
}
