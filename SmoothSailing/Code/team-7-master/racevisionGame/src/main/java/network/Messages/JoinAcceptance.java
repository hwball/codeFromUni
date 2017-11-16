package network.Messages;

import network.Messages.Enums.JoinAcceptanceEnum;
import network.Messages.Enums.MessageType;


/**
 * This is the message a server sends to a client to tell them their boat sourceID, and if they have actually managed to join the server.
 */
public class JoinAcceptance extends AC35Data {


    /**
     * The source ID of the boat assigned to the client.
     * 0 indicates they haven't been assigned a boat.
     */
    private int sourceID = 0;

    /**
     * The type of acceptance response this is.
     */
    private JoinAcceptanceEnum acceptanceType;




    /**
     * Constructs a JoinAcceptance message of a given acceptance type.
     * @param acceptanceType The type of join acceptance this is.
     * @param sourceID The sourceID to assign to the client. 0 indicates no sourceID.
     */
    public JoinAcceptance(JoinAcceptanceEnum acceptanceType, int sourceID){
        super(MessageType.JOIN_ACCEPTANCE);
        this.acceptanceType = acceptanceType;
        this.sourceID = sourceID;
    }


    /**
     * The type of acceptance response this is.
     * @return The type of acceptance response.
     */
    public JoinAcceptanceEnum getAcceptanceType() {
        return acceptanceType;
    }

    /**
     * Returns the source ID of the boat assigned to the client.
     * @return The source ID of the boat assigned to the client.
     */
    public int getSourceID() {
        return sourceID;
    }
}
