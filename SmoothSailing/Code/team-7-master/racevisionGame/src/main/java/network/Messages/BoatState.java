package network.Messages;

import network.Messages.Enums.MessageType;

/**
 * Represents the information in a BoatState message according to protocol meeting
 */
public class BoatState extends AC35Data {
    /**
     * Source ID of boat described in message
     */
    private int sourceID;
    /**
     * Health between 0-100 of boat with above source ID
     */
    private int boatHealth;

    public BoatState(int sourceID, int boatHealth) {
        super(MessageType.BOATSTATE);
        this.sourceID = sourceID;
        this.boatHealth = boatHealth;
    }

    public int getSourceID() {
        return sourceID;
    }

    public int getBoatHealth() {
        return boatHealth;
    }
}
