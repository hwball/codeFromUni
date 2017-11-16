package network.MessageEncoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.YachtEvent;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * Encodes a {@link YachtEvent} message.
 */
public class YachtEventCodeEncoder implements MessageEncoder {
    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {
        // Downcast message
        YachtEvent yachtEvent = (YachtEvent)message;

        // Serialise message
        byte messageVersion = 0b10;
        byte[] timestamp = longToBytes(yachtEvent.getCurrentTime(), 6);
        byte[] ackNum = intToBytes(yachtEvent.getAckNum(), 2);
        byte[] raceID = intToBytes(yachtEvent.getRaceID());
        byte[] sourceID = intToBytes(yachtEvent.getSourceID());
        byte[] incidentID = intToBytes(yachtEvent.getIncidentID());
        byte eventID = yachtEvent.getYachtEvent().getValue();

        // Pack bytes into string
        ByteBuffer yachtEventMessage = ByteBuffer.allocate(22);
        yachtEventMessage.put(messageVersion);
        yachtEventMessage.put(timestamp);
        yachtEventMessage.put(ackNum);
        yachtEventMessage.put(raceID);
        yachtEventMessage.put(sourceID);
        yachtEventMessage.put(incidentID);
        yachtEventMessage.put(eventID);

        // Return byte string
        return yachtEventMessage.array();
    }
}
