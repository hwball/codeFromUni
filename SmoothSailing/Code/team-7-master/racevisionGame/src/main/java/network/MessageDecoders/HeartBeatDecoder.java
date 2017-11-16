package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.BoatActionEnum;
import network.Messages.HeartBeat;

import static network.Utils.ByteConverter.bytesToLong;

/**
 * Decodes {@link network.Messages.HeartBeat} messages.
 */
public class HeartBeatDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private HeartBeat message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public HeartBeatDecoder() {
    }

    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            message = new HeartBeat(bytesToLong(encodedMessage));

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode HeartBeat message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public HeartBeat getMessage() {
        return message;
    }

}
