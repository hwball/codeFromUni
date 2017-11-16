package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatAction;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;

/**
 * This encoder can encode a {@link BoatAction} message.
 */
public class BoatActionEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public BoatActionEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            BoatAction boatAction = (BoatAction) message;

            //Message is 1 byte.
            ByteBuffer boatActionMessage = ByteBuffer.allocate(1);

            boatActionMessage.put(intToBytes(boatAction.getBoatAction().getValue(), 1));

            byte[] result = boatActionMessage.array();

            return result;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode BoatAction message.", e);
        }

    }
}
