package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.RequestToJoin;
import network.Utils.ByteConverter;

import java.nio.ByteBuffer;

/**
 * This encoder can encode a {@link network.Messages.RequestToJoin} message.
 */
public class RequestToJoinEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public RequestToJoinEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            RequestToJoin requestToJoin = (RequestToJoin) message;


            ByteBuffer requestToJoinBuffer = ByteBuffer.allocate(4);

            requestToJoinBuffer.put(ByteConverter.intToBytes(requestToJoin.getRequestType().getValue(), 4));

            byte[] result = requestToJoinBuffer.array();

            return result;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode RequestToJoin message.", e);
        }

    }
}
