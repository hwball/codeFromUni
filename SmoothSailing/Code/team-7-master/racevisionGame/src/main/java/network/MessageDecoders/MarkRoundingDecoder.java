package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.MarkRoundingBoatStatusEnum;
import network.Messages.Enums.MarkRoundingSideEnum;
import network.Messages.Enums.MarkRoundingTypeEnum;
import network.Messages.MarkRounding;
import network.Utils.ByteConverter;

import java.util.Arrays;

/**
 * Decoder for {@link MarkRounding} messages.
 */
public class MarkRoundingDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private MarkRounding message;


    /**
     * Constructs a decoder to decode a given message.
     */
    public MarkRoundingDecoder() {
    }

    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte messageVersionNumber = encodedMessage[0];

            byte[] byteTime = Arrays.copyOfRange(encodedMessage, 1, 7);
            long time = ByteConverter.bytesToLong(byteTime);

            byte[] byteAck = Arrays.copyOfRange(encodedMessage, 7, 9);
            int ackNumber = ByteConverter.bytesToInt(byteAck);

            byte[] byteRaceID = Arrays.copyOfRange(encodedMessage, 9, 13);
            int raceID = ByteConverter.bytesToInt(byteRaceID);

            byte[] byteSourceID = Arrays.copyOfRange(encodedMessage, 13, 17);
            int sourceID = ByteConverter.bytesToInt(byteSourceID);

            byte byteBoatStatus = encodedMessage[17];
            MarkRoundingBoatStatusEnum boatStatus = MarkRoundingBoatStatusEnum.fromByte(byteBoatStatus);

            byte byteRoundingSide = encodedMessage[18];
            MarkRoundingSideEnum roundingSide = MarkRoundingSideEnum.fromByte(byteRoundingSide);

            byte byteMarkType = encodedMessage[19];
            MarkRoundingTypeEnum markType = MarkRoundingTypeEnum.fromByte(byteMarkType);

            byte byteMarkID = encodedMessage[20];


            message = new MarkRounding(
                    messageVersionNumber,
                    time,
                    ackNumber,
                    raceID,
                    sourceID,
                    boatStatus,
                    roundingSide,
                    markType,
                    byteMarkID);


            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode AverageWind message.", e);
        }
    }

    /**
     * Returns the decoded message.
     *
     * @return The decoded message.
     */
    public MarkRounding getMessage() {
        return message;
    }

}

