package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.BoatStatusEncoder;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.BoatStatus;
import network.Messages.Enums.BoatStatusEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test for the BoatStatus encoder and decoder
 */
public class BoatStatusDecoderTest {


    /**
     * Creates a BoatStatus message, encodes it, decodes it, and checks that the result matches the starting message.
     * @throws Exception if test fails.
     */
    @Test
    public void boatStatusEncodeDecodeTest() throws Exception {

        long time = System.currentTimeMillis();

        //Create data to serialize.
        int boatSourceID = 5;
        BoatStatusEnum boatStatusEnum = BoatStatusEnum.RACING;
        byte boatLegNumber = 5;
        byte boatPenaltiesAwarded = 4;
        byte boatPenaltiesServed = 2;
        long boatTimeAtNextMark = time + (1000 * 3);
        long boatTimeAtFinish = boatTimeAtNextMark + (1000 * 15);

        BoatStatus boatStatus = new BoatStatus(
                boatSourceID,
                boatStatusEnum,
                boatLegNumber,
                boatPenaltiesAwarded,
                boatPenaltiesServed,
                boatTimeAtNextMark,
                boatTimeAtFinish   );


        BoatStatus boatStatusDecoded = encodeDecodeBoatStatus(boatStatus);

        compareBoatStatusMessages(boatStatus, boatStatusDecoded);

    }

    /**
     * Encodes and decodes a BoatStatus, and returns it.
     * @param boatStatus The BoatStatus to encode and decode.
     * @return The decoded BoatStatus.
     * @throws InvalidMessageException Thrown if message cannot be encoded or decoded.
     */
    private static BoatStatus encodeDecodeBoatStatus(BoatStatus boatStatus) throws InvalidMessageException {

        BoatStatusEncoder boatStatusEncoder = new BoatStatusEncoder();
        byte[] boatStatusEncoded = boatStatusEncoder.encode(boatStatus);

        BoatStatusDecoder boatStatusDecoder = new BoatStatusDecoder();
        BoatStatus boatStatusDecoded = boatStatusDecoder.decode(boatStatusEncoded);

        return boatStatusDecoded;
    }


    /**
     * Compares two BoatStatus messages to check that they are equal.
     * @param original The original BoatStatus message.
     * @param decoded The decoded BoatStatus message.
     */
    public static void compareBoatStatusMessages(BoatStatus original, BoatStatus decoded) {

        Assert.assertEquals(original.getSourceID(), decoded.getSourceID());
        Assert.assertEquals(original.getBoatStatus(), decoded.getBoatStatus());
        Assert.assertEquals(original.getLegNumber(), decoded.getLegNumber());
        Assert.assertEquals(original.getNumPenaltiesAwarded(), decoded.getNumPenaltiesAwarded());
        Assert.assertEquals(original.getNumPenaltiesServed(), decoded.getNumPenaltiesServed());
        Assert.assertEquals(original.getEstTimeAtNextMark(), decoded.getEstTimeAtNextMark());
        Assert.assertEquals(original.getEstTimeAtFinish(), decoded.getEstTimeAtFinish());

    }

}
