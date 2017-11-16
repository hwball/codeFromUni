package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.JoinAcceptance;
import network.Utils.ByteConverter;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;

/**
 * This encoder can encode a {@link JoinAcceptance} message.
 */
public class JoinAcceptanceEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public JoinAcceptanceEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            JoinAcceptance joinAcceptance = (JoinAcceptance) message;

            //Message is 5 bytes.
            ByteBuffer joinAcceptanceBuffer = ByteBuffer.allocate(5);

            //Source ID is first four bytes.
            joinAcceptanceBuffer.put(intToBytes(joinAcceptance.getSourceID(), 4));
            //Acceptance type is next byte.
            joinAcceptanceBuffer.put(intToBytes(joinAcceptance.getAcceptanceType().getValue(), 1));

            byte[] result = joinAcceptanceBuffer.array();

            return result;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode JoinAcceptance message.", e);
        }

    }
}
