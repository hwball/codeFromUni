package network.MessageDecoders;

import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.CourseWind;
import network.Messages.CourseWinds;
import org.junit.Test;
import shared.model.Bearing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import static org.junit.Assert.*;


/**
 * Tests for CourseWinds encoder and decoder.
 */
public class CourseWindsDecoderTest {

    /**
     * Tests if a CourseWinds message can be encoded and decoded correctly.
     * @throws Exception Thrown if an error occurs.
     */
    @Test
    public void courseWindsEncodeDecodeTest() throws Exception {

        long time1 = System.currentTimeMillis();
        CourseWind testCourseWind1 = new CourseWind(
                1,
                time1,
                2,
                Bearing.fromDegrees(45),
                4,
                Bearing.fromDegrees(70),
                Bearing.fromDegrees(160),
                (byte) 0xCE   );

        long time2 = System.currentTimeMillis();
        CourseWind testCourseWind2 = new CourseWind(
                2,
                time2,
                2,
                Bearing.fromDegrees(55),
                4,
                Bearing.fromDegrees(80),
                Bearing.fromDegrees(180),
                (byte) 0x0D   );

        List<CourseWind> testCourseWinds = new ArrayList<>();
        testCourseWinds.add(testCourseWind1);
        testCourseWinds.add(testCourseWind2);

        CourseWinds courseWinds = new CourseWinds(CourseWinds.currentMessageVersionNumber, (byte) 2, testCourseWinds);


        byte[] testEncodedCourseWind = RaceVisionByteEncoder.encode(courseWinds);

        CourseWindsDecoder courseWindsDecoder = new CourseWindsDecoder();
        courseWindsDecoder.decode(testEncodedCourseWind);
        CourseWinds courseWindsDecoded = courseWindsDecoder.getMessage();

        compareCourseWindsMessages(courseWinds, courseWindsDecoded);


    }


    /**
     * Compares two course winds messages to ensure they are the same.
     * @param original The original message.
     * @param decoded The decoded message.
     */
    public static void compareCourseWindsMessages(CourseWinds original, CourseWinds decoded) {

        //Compare header.
        assertEquals(original.getMessageVersionNumber(), decoded.getMessageVersionNumber());
        assertEquals(original.getSelectedWindID(), decoded.getSelectedWindID());
        assertEquals(original.getCourseWinds().size(), decoded.getCourseWinds().size());

        //Compare each CourseWind.
        List<CourseWind> originalWinds = original.getCourseWinds();
        List<CourseWind> decodedWinds = decoded.getCourseWinds();

        Iterator<CourseWind> originalIterator = originalWinds.iterator();
        Iterator<CourseWind> decodedIterator = decodedWinds.iterator();

        while (originalIterator.hasNext() && decodedIterator.hasNext()) {

            CourseWindDecoderTest.compareCourseWindMessages(originalIterator.next(), decodedIterator.next());

        }

    }
}
