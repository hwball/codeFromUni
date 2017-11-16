package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatState;

import java.util.Arrays;

import static network.Utils.ByteConverter.bytesToInt;

/**
 * Decoder for {@link BoatState} messages
 */
public class BoatStateDecoder implements MessageDecoder {
    /**
     * Decoded BoatState message
     */
    private BoatState message;

    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        byte[] sourceID = Arrays.copyOfRange(encodedMessage, 0, 4);
        byte boatHealth = encodedMessage[4];

        // Unpack bytes into BoatState
        this.message = new BoatState(
                bytesToInt(sourceID),
                boatHealth
        );

        // Return BoatState
        return this.message;
    }

    public BoatState getMessage() {
        return message;
    }
}
