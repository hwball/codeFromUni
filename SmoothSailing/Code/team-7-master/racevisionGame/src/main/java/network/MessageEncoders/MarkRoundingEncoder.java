package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.MarkRounding;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link MarkRounding} message.
 */
public class MarkRoundingEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public MarkRoundingEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            MarkRounding markRounding = (MarkRounding) message;

            byte messageVersionNumber = markRounding.getMessageVersionNumber();
            byte[] byteTime = longToBytes(markRounding.getTime(), 6);
            byte[] byteAck = intToBytes(markRounding.getAckNum(), 2);
            byte[] byteRaceID = intToBytes(markRounding.getRaceID(), 4);
            byte[] byteSourceID = intToBytes(markRounding.getSourceID(), 4);
            byte[] byteBoatStatus = intToBytes(markRounding.getBoatStatus().getValue(), 1);
            byte[] byteRoundingSide = intToBytes(markRounding.getRoundingSide().getValue(), 1);
            byte[] byteMarkType = intToBytes(markRounding.getMarkType().getValue(), 1);
            byte[] byteMarkID = intToBytes(markRounding.getMarkID(), 1);


            ByteBuffer result = ByteBuffer.allocate(21);

            result.put(messageVersionNumber);
            result.put(byteTime);
            result.put(byteAck);
            result.put(byteRaceID);
            result.put(byteSourceID);
            result.put(byteBoatStatus);
            result.put(byteRoundingSide);
            result.put(byteMarkType);
            result.put(byteMarkID);

            return result.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode MarkRounding message.", e);
        }

    }
}
