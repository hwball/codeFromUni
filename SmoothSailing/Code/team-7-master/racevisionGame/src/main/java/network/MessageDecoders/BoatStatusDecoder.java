package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.BoatStatus;
import network.Messages.Enums.BoatStatusEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static network.Utils.ByteConverter.*;


/**
 * Decodes {@link BoatStatus} messages.
 */
public class BoatStatusDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private BoatStatus message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public BoatStatusDecoder() {
    }


    /**
     * Decodes the contained message.
     * @param encodedMessage The message to decode.
     * @return The decoded message.
     * @throws InvalidMessageException Thrown if the encoded message is invalid in some way, or cannot be decoded.
     */
    public BoatStatus decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

        byte[] sourceIDBytes = Arrays.copyOfRange(encodedMessage, 0, 4);
        int sourceID = bytesToInt(sourceIDBytes);

        byte[] boatStatusBytes = Arrays.copyOfRange(encodedMessage, 4, 5);
        BoatStatusEnum boatStatus = BoatStatusEnum.fromByte(boatStatusBytes[0]);

        byte[] legNumberBytes = Arrays.copyOfRange(encodedMessage, 5, 6);
        byte legNumber = legNumberBytes[0];

        byte[] numPenaltiesAwardedBytes = Arrays.copyOfRange(encodedMessage, 6, 7);
        byte numPenaltiesAwarded = numPenaltiesAwardedBytes[0];

        byte[] numPenaltiesServedBytes = Arrays.copyOfRange(encodedMessage, 7, 8);
        byte numPenaltiesServed = numPenaltiesServedBytes[0];

        byte[] estTimeAtNextMarkBytes = Arrays.copyOfRange(encodedMessage, 8, 14);
        long estTimeAtNextMark = bytesToLong(estTimeAtNextMarkBytes);

        byte[] estTimeAtFinishBytes = Arrays.copyOfRange(encodedMessage, 14, 20);
        long estTimeAtFinish = bytesToLong(estTimeAtFinishBytes);

        message = new BoatStatus(
                sourceID,
                boatStatus,
                legNumber,
                numPenaltiesAwarded,
                numPenaltiesServed,
                estTimeAtNextMark,
                estTimeAtFinish    );

        return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode BoatStatus message.", e);
        }


    }



    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public BoatStatus getMessage() {
        return message;
    }
}
