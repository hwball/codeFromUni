package network.Messages;

import network.Messages.Enums.MessageType;


/**
 * This is the message the client generates and sends to itself once the server has assigned a boat source ID with {@link JoinAcceptance}.
 */
public class AssignPlayerBoat extends AC35Data {


    /**
     * The source ID of the boat assigned to the client.
     * 0 indicates they haven't been assigned a boat.
     */
    private int sourceID = 0;




    /**
     * Constructs a AssignPlayerBoat message.
     * @param sourceID The sourceID to assign to the client. 0 indicates no sourceID.
     */
    public AssignPlayerBoat(int sourceID){
        super(MessageType.ASSIGN_PLAYER_BOAT);
        this.sourceID = sourceID;
    }


    /**
     * Returns the source ID of the boat assigned to the client.
     * @return The source ID of the boat assigned to the client.
     */
    public int getSourceID() {
        return sourceID;
    }
}
