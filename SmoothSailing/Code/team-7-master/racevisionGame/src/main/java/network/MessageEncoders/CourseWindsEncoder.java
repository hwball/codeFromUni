package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatAction;
import network.Messages.CourseWind;
import network.Messages.CourseWinds;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;

/**
 * This encoder can encode a {@link CourseWinds} message.
 */
public class CourseWindsEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public CourseWindsEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            CourseWinds courseWinds = (CourseWinds) message;


            byte messageVersionNumber = CourseWinds.currentMessageVersionNumber;

            byte byteWindID = courseWinds.getSelectedWindID();

            byte[] loopcount = intToBytes(courseWinds.getCourseWinds().size(), 1);

            ByteBuffer result = ByteBuffer.allocate(3 + 20 * courseWinds.getCourseWinds().size());

            result.put(messageVersionNumber);
            result.put(byteWindID);
            result.put(loopcount);

            //Encode each CourseWind.
            for (CourseWind wind : courseWinds.getCourseWinds()) {

                CourseWindEncoder courseWindEncoder = new CourseWindEncoder();
                byte[] encodedCourseWind = courseWindEncoder.encode(wind);

                result.put(encodedCourseWind);
            }
            return result.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode CourseWinds message.", e);
        }


    }
}
