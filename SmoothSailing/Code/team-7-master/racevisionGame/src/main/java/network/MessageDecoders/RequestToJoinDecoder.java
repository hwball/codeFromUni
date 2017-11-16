package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.Enums.RequestToJoinEnum;
import network.Messages.RequestToJoin;
import network.Utils.ByteConverter;

import java.util.Arrays;

/**
 * Decoder for {@link network.Messages.RequestToJoin} messages.
 */
public class RequestToJoinDecoder implements MessageDecoder{

    /**
     * The encoded message.
     */
    private byte[] encodedRequest;

    /**
     * The decoded message.
     */
    private RequestToJoin message;

    /**
     * Constructs a decoder to decode a given message.
     */
    public RequestToJoinDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedRequest) throws InvalidMessageException {
        this.encodedRequest = encodedRequest;

        try {

            //Request type is first four bytes.
            byte[] requestTypeBytes = Arrays.copyOfRange(encodedRequest, 0, 4);

            //Request type is an integral type.
            int requestTypeInt = ByteConverter.bytesToInt(requestTypeBytes);
            RequestToJoinEnum requestType = RequestToJoinEnum.fromInt(requestTypeInt);


            message = new RequestToJoin(requestType);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode RequestToJoin message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public RequestToJoin getMessage() {
        return message;
    }


}
