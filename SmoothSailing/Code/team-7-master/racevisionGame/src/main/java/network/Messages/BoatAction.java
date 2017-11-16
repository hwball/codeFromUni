package network.Messages;

import network.Messages.Enums.BoatActionEnum;
import network.Messages.Enums.MessageType;

/**
 * Represents a BoatAction message.
 */
public class BoatAction extends AC35Data {

    /**
     * The action for this message.
     */
    private BoatActionEnum boatAction;


    /**
     * The source ID of the boat this action relates to.
     */
    private int sourceID = 0;

    /**
     * Constructs a BoatActon message with a given action.
     * @param boatAction Action to use.
     */
    public BoatAction(BoatActionEnum boatAction){
        super(MessageType.BOATACTION);
        this.boatAction = boatAction;
    }

    /**
     * Returns the action for this message.
     * @return The action for this message.
     */
    public BoatActionEnum getBoatAction() {
        return boatAction;
    }

    /**
     * Returns the boat source ID for this message.
     * @return The source ID for this message.
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * Sets the boat source ID for this message.
     * @param sourceID The source for this message.
     */
    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }
}
