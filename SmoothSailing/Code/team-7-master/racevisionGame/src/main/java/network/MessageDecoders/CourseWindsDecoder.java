package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.CourseWind;
import network.Messages.CourseWinds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static network.Utils.ByteConverter.bytesToInt;
import static network.Utils.ByteConverter.bytesToLong;


/**
 * Decodes {@link CourseWinds} messages.
 */
public class CourseWindsDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private CourseWinds message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public CourseWindsDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            //The header is three bytes.
            byte messageVersionNumber = encodedMessage[0];
            byte byteWindID = encodedMessage[1];
            byte loopCount = encodedMessage[2];


            //A CourseWind object is 20 bytes.
            final int courseWindByteLength = 20;

            List<CourseWind> loopMessages = new ArrayList();

            //The header is 3 bytes, so we need the remaining bytes.
            byte[] loopMessagesBytes = Arrays.copyOfRange(encodedMessage, 3, courseWindByteLength * loopCount + 3);

            for (int messageLoopIndex = 0; messageLoopIndex < (loopCount * courseWindByteLength); messageLoopIndex += courseWindByteLength) {

                byte[] messageBytes = Arrays.copyOfRange(loopMessagesBytes, messageLoopIndex, messageLoopIndex + courseWindByteLength);

                CourseWindDecoder courseWindDecoder = new CourseWindDecoder();
                CourseWind courseWind = courseWindDecoder.decode(messageBytes);

                loopMessages.add(courseWind);
            }


            message = new CourseWinds(
                    messageVersionNumber,
                    byteWindID,
                    loopMessages);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode CourseWinds message.", e);
        }
    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public CourseWinds getMessage() {
        return message;
    }

}
