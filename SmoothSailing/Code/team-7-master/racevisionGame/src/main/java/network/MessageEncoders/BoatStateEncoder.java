package network.MessageEncoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatState;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;

/**
 * Encoder for {@link BoatState} message
 */
public class BoatStateEncoder implements MessageEncoder {
    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {
        // Downcast message
        BoatState boatState = (BoatState)message;

        //Serialise message
        byte[] sourceID = intToBytes(boatState.getSourceID());
        byte boatHealth = (byte)boatState.getBoatHealth();

        // Pack bytes into string
        ByteBuffer boatStateMessage = ByteBuffer.allocate(5);
        boatStateMessage.put(sourceID);
        boatStateMessage.put(boatHealth);

        // Return byte string
        return boatStateMessage.array();
    }
}
