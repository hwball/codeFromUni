package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.BoatStatus;
import network.Messages.Enums.BoatStatusEnum;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.Enums.RaceTypeEnum;
import network.Messages.RaceStatus;
import org.junit.Assert;
import org.junit.Test;
import shared.model.Bearing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test for the RaceStatus encoder and decoder
 */
public class RaceStatusDecoderTest {


    /**
     * Creates a RaceStatus message, encodes it, decodes it, and checks that the result matches the starting message.
     * @throws Exception if test fails.
     */
    @Test
    public void raceStatusEncodeDecodeTest() throws Exception {

        long time = System.currentTimeMillis();

        //Create data to serialize.
        int boat1SourceID = 5;
        int boat2SourceID = 8;
        BoatStatusEnum boat1Status = BoatStatusEnum.RACING;
        BoatStatusEnum boat2Status = BoatStatusEnum.RACING;
        byte boat1LegNumber = 5;
        byte boat2LegNumber = 3;
        byte boat1PenaltiesAwarded = 4;
        byte boat2PenaltiesAwarded = 0;
        byte boat1PenaltiesServed = 2;
        byte boat2PenaltiesServed = 0;
        long boat1TimeAtNextMark = time + (1000 * 3);
        long boat2TimeAtNextMark = time + (1000 * 2);
        long boat1TimeAtFinish = boat1TimeAtNextMark + (1000 * 15);
        long boat2TimeAtFinish = boat2TimeAtNextMark + (1000 * 7);

        BoatStatus boatStatus1 = new BoatStatus(
                boat1SourceID,
                boat1Status,
                boat1LegNumber,
                boat1PenaltiesAwarded,
                boat1PenaltiesServed,
                boat1TimeAtNextMark,
                boat1TimeAtFinish   );

        BoatStatus boatStatus2 = new BoatStatus(
                boat2SourceID,
                boat2Status,
                boat2LegNumber,
                boat2PenaltiesAwarded,
                boat2PenaltiesServed,
                boat2TimeAtNextMark,
                boat2TimeAtFinish   );


        int raceID = 585;
        RaceStatusEnum raceStatus = RaceStatusEnum.STARTED;
        long raceStartTime = time - (1000 * 31);
        Bearing windDirection = Bearing.fromDegrees(185.34);
        double windSpeedKnots = 14.52;
        RaceTypeEnum raceType = RaceTypeEnum.MATCH_RACE;
        List<BoatStatus> boatStatuses = new ArrayList<>(2);
        boatStatuses.add(boatStatus1);
        boatStatuses.add(boatStatus2);

        RaceStatus raceStatusOriginal = new RaceStatus(
                RaceStatus.currentMessageVersionNumber,
                time,
                raceID,
                raceStatus,
                raceStartTime,
                windDirection,
                windSpeedKnots,
                raceType,
                boatStatuses    );


        byte[] encodedRaceStatus = RaceVisionByteEncoder.encode(raceStatusOriginal);

        RaceStatusDecoder decoderTest = new RaceStatusDecoder();
        decoderTest.decode(encodedRaceStatus);

        RaceStatus decodedMessage = decoderTest.getMessage();

        compareRaceStatusMessages(raceStatusOriginal, decodedMessage);

    }


    /**
     * Compares two RaceStatus messages to check that they are equal.
     * @param original The original RaceStatus message.
     * @param decoded The decoded RaceStatus message.
     */
    public static void compareRaceStatusMessages(RaceStatus original, RaceStatus decoded) {

        //Compare RaceStatus body.
        Assert.assertEquals(original.getMessageVersionNumber(), decoded.getMessageVersionNumber());
        Assert.assertEquals(original.getCurrentTime(), decoded.getCurrentTime());
        Assert.assertEquals(original.getRaceID(), decoded.getRaceID());
        Assert.assertEquals(original.getRaceStatus(), decoded.getRaceStatus());
        Assert.assertEquals(original.getExpectedStartTime(), decoded.getExpectedStartTime());
        Assert.assertEquals(original.getWindDirection().degrees(), decoded.getWindDirection().degrees(), 0.01);
        Assert.assertEquals(original.getWindSpeed(), decoded.getWindSpeed(), 0.01);

        //Compare all BoatStatuses
        Iterator<BoatStatus> originalIterator = original.getBoatStatuses().iterator();
        Iterator<BoatStatus> decodedIterator = decoded.getBoatStatuses().iterator();

        while (originalIterator.hasNext() && decodedIterator.hasNext()) {

            BoatStatusDecoderTest.compareBoatStatusMessages(originalIterator.next(), decodedIterator.next());

        }

    }


}
