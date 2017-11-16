package network.Messages;


import network.Messages.Enums.MessageType;
import network.Messages.Enums.RequestToJoinEnum;

/**
 * This is the message a client sends to a server to request to join/view a race.
 */
public class RequestToJoin extends AC35Data {


    /**
     * The type of join request this is.
     */
    private RequestToJoinEnum requestType;


    /**
     * Constructs a RequestToJoin message of a given request type.
     * @param requestType The type of join request this is.
     */
    public RequestToJoin(RequestToJoinEnum requestType){
        super(MessageType.REQUEST_TO_JOIN);
        this.requestType = requestType;
    }


    /**
     * The type of join request this is.
     * @return The type of join request.
     */
    public RequestToJoinEnum getRequestType() {
        return requestType;
    }
}
