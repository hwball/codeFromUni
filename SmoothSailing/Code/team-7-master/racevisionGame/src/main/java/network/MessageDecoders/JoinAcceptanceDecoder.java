package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.JoinAcceptanceEnum;
import network.Messages.JoinAcceptance;
import network.Utils.ByteConverter;

import java.util.Arrays;

/**
 * Decoder for {@link JoinAcceptance} messages.
 */
public class JoinAcceptanceDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private JoinAcceptance message;


    /**
     * Constructs a decoder to decode a given message.
     */
    public JoinAcceptanceDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            //SourceID is first four bytes.
            byte[] sourceIdBytes = Arrays.copyOfRange(encodedMessage, 0, 4);

            //Next byte is acceptance type.
            byte[] acceptanceBytes = Arrays.copyOfRange(encodedMessage, 4, 5);


            //SourceID is an int.
            int sourceID = ByteConverter.bytesToInt(sourceIdBytes);

            //Acceptance enum is a byte.
            JoinAcceptanceEnum acceptanceType = JoinAcceptanceEnum.fromByte(acceptanceBytes[0]);


            message = new JoinAcceptance(acceptanceType, sourceID);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode JoinAcceptance message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public JoinAcceptance getMessage() {
        return message;
    }


}
