package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.RaceStartStatus;
import network.Utils.ByteConverter;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link RaceStartStatus} message.
 */
public class RaceStartStatusEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public RaceStartStatusEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            RaceStartStatus raceStartStatus = (RaceStartStatus) message;


            byte messageVersion = raceStartStatus.getMessageVersionNumber();
            byte[] timestamp = longToBytes(raceStartStatus.getTimestamp(), 6);
            byte[] ackNumber = intToBytes(raceStartStatus.getAckNum(), 2);
            byte[] raceStartTime = longToBytes(raceStartStatus.getRaceStartTime(), 6);
            byte[] raceIdentifier = intToBytes(raceStartStatus.getRaceID());
            byte[] notificationType = intToBytes(raceStartStatus.getNotificationType().getValue(), 1);

            ByteBuffer result = ByteBuffer.allocate(20);
            result.put(messageVersion);
            result.put(timestamp);
            result.put(ackNumber);
            result.put(raceStartTime);
            result.put(raceIdentifier);
            result.put(notificationType);

            return result.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode RaceStartStatus message.", e);
        }

    }
}
