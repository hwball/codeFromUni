package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;


/**
 * This is the interface that all message encoders must implement.
 * It allows for {@link #encode(AC35Data)}ing messages.
 */
public interface MessageEncoder {


    /**
     * Encodes a given message.
     * @param message The message to encode.
     * @return Message in byte encoded form.
     * @throws InvalidMessageException Thrown if the message is invalid in some way, or cannot be encoded.
     */
    public byte[] encode(AC35Data message) throws InvalidMessageException;

}
