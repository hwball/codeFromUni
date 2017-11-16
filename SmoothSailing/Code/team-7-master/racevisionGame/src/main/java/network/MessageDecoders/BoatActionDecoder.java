package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatAction;
import network.Messages.Enums.BoatActionEnum;

import java.util.Arrays;

/**
 * Decodes {@link BoatAction} messages.
 */
public class BoatActionDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private BoatAction message;


    /**
     * Constructs a decoder to decode a given message.
     */
    public BoatActionDecoder() {
    }

    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {
            BoatActionEnum boatActionEnum = BoatActionEnum.fromByte(encodedMessage[0]);

            message = new BoatAction(boatActionEnum);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode BoatAction message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public BoatAction getMessage() {
        return message;
    }
}
