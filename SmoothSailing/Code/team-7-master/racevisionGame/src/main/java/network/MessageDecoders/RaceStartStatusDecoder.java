package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.RaceStartTypeEnum;
import network.Messages.RaceStartStatus;

import java.util.Arrays;

import static network.Utils.ByteConverter.*;


/**
 * Decodes {@link RaceStartStatus} messages.
 */
public class RaceStartStatusDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private RaceStartStatus message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public RaceStartStatusDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte messageVersion = encodedMessage[0];

            byte[] timestamp = Arrays.copyOfRange(encodedMessage, 1, 7);
            long time = bytesToLong(timestamp);

            byte[] ackNumber = Arrays.copyOfRange(encodedMessage, 7, 9);
            short ack = bytesToShort(ackNumber);

            byte[] raceStartTime = Arrays.copyOfRange(encodedMessage, 9, 15);
            long startTime = bytesToLong(raceStartTime);

            byte[] raceIdentifier = Arrays.copyOfRange(encodedMessage, 15, 19);
            int raceID = bytesToInt(raceIdentifier);

            byte notificationType = encodedMessage[19];


            message = new RaceStartStatus(
                    messageVersion,
                    time,
                    ack,
                    startTime,
                    raceID,
                    RaceStartTypeEnum.fromByte(notificationType)
            );

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode RaceStartStatus message.", e);
        }

    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public RaceStartStatus getMessage() {
        return message;
    }
}
