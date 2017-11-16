package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;


/**
 * This is the interface that all message decoders must implement.
 * It allows for {@link #decode(byte[])}ing messages.
 */
public interface MessageDecoder {


    /**
     * Decodes a given message.
     * @param encodedMessage The message to decode.
     * @return The decoded message.
     * @throws InvalidMessageException Thrown if the encoded message is invalid in some way, or cannot be decoded.
     */
     AC35Data decode(byte[] encodedMessage) throws InvalidMessageException;
}
