package network.Messages;


import network.Messages.Enums.MessageType;

/**
 * The base class for all message types.
 */
public abstract class AC35Data {

    /**
     * Message type from the header.
     */
    private MessageType type;


    /**
     * Ctor.
     * @param type The concrete type of this message.
     */
    public AC35Data (MessageType type){
        this.type = type;
    }


    /**
     * The concrete type of message this is.
     * @return The type of message this is.
     */
    public MessageType getType() {
        return type;
    }
}
