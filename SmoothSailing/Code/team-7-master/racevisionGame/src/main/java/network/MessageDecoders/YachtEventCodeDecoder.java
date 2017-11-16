package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.YachtEventEnum;
import network.Messages.YachtEvent;

import java.util.Arrays;

import static network.Utils.ByteConverter.bytesToInt;
import static network.Utils.ByteConverter.bytesToLong;
import static network.Utils.ByteConverter.bytesToShort;

/**
 * Decodes {@link YachtEvent} messages.
 */
public class YachtEventCodeDecoder implements MessageDecoder {
    private YachtEvent message;

    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        // Deserialise message
        byte[] timestamp = Arrays.copyOfRange(encodedMessage, 1, 7);
        byte[] ackNum = Arrays.copyOfRange(encodedMessage, 7, 9);
        byte[] raceID = Arrays.copyOfRange(encodedMessage, 9, 13);
        byte[] sourceID = Arrays.copyOfRange(encodedMessage, 13, 17);
        byte[] incidentID = Arrays.copyOfRange(encodedMessage, 17, 21);
        byte eventID = encodedMessage[21];

        // Unpack bytes into YachtEvent
        this.message = new YachtEvent(
                bytesToLong(timestamp),
                bytesToShort(ackNum),
                bytesToInt(raceID),
                bytesToInt(sourceID),
                bytesToInt(incidentID),
                YachtEventEnum.fromByte(eventID)
        );

        // Return YachtEvent
        return message;
    }

    public YachtEvent getMessage() {
        return message;
    }
}
