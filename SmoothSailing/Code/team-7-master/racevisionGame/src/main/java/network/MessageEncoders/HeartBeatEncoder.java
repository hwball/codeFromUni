package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.HeartBeat;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link HeartBeat} message.
 */
public class HeartBeatEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public HeartBeatEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            HeartBeat heartbeat = (HeartBeat) message;

            //Message is 4 bytes.
            ByteBuffer heartBeat = ByteBuffer.allocate(4);
            heartBeat.put(longToBytes(heartbeat.getSequenceNumber(), 4));

            byte[] result = heartBeat.array();

            return result;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode HeartBeat message.", e);
        }

    }
}
