package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatStatus;
import network.Messages.RaceStatus;
import network.Utils.AC35UnitConverter;
import shared.model.Bearing;

import java.nio.ByteBuffer;
import java.util.List;

import static network.Utils.ByteConverter.bytesToInt;
import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link RaceStatus} message.
 */
public class RaceStatusEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public RaceStatusEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            RaceStatus raceStatus = (RaceStatus) message;


            List<BoatStatus> boatStatuses = raceStatus.getBoatStatuses();

            //24 byte header, plus 20 bytes per boat status.
            ByteBuffer raceStatusMessage = ByteBuffer.allocate(24 + 20 * boatStatuses.size());

            //Version Number 1 bytes. this changes with the pdf. (2)
            byte versionNum = 0b10;

            //time (6 bytes)
            byte[] timeBytes = longToBytes(raceStatus.getCurrentTime(), 6);

            //race identifier in case multiple races are going at once.
            byte[] raceID = intToBytes(raceStatus.getRaceID());

            //race status 0 - 10
            byte[] raceStatusByte = intToBytes(raceStatus.getRaceStatus().getValue(), 1);

            //number of milliseconds from Jan 1, 1970 for when the data is valid
            byte[] expectedStart = longToBytes(raceStatus.getExpectedStartTime(), 6);

            //North = 0x0000 East = 0x4000 South = 0x8000.
            int windDirectionInt = AC35UnitConverter.packHeading(raceStatus.getWindDirection().degrees());
            byte[] raceWind = intToBytes(windDirectionInt, 2);

            //mm/sec
            int windSpeedInt = AC35UnitConverter.packKnotsToMMperSec(raceStatus.getWindSpeed());
            byte[] windSpeed = intToBytes(windSpeedInt, 2);


            byte[] numBoats = intToBytes(boatStatuses.size(), 1);

            //1 match race, 2 fleet race
            byte[] bytesRaceType = intToBytes(raceStatus.getRaceType().getValue(), 1);


            raceStatusMessage.put(versionNum);
            raceStatusMessage.put(timeBytes);
            raceStatusMessage.put(raceID);
            raceStatusMessage.put(raceStatusByte);
            raceStatusMessage.put(expectedStart);
            raceStatusMessage.put(raceWind);
            raceStatusMessage.put(windSpeed);
            raceStatusMessage.put(numBoats);
            raceStatusMessage.put(bytesRaceType);

            //Encode each BoatStatus.
            for (BoatStatus boatStatus : boatStatuses) {

                BoatStatusEncoder boatStatusEncoder = new BoatStatusEncoder();

                byte[] boatStatusEncoded = boatStatusEncoder.encode(boatStatus);

                raceStatusMessage.put(boatStatusEncoded);
            }

            return raceStatusMessage.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode RaceStatus message.", e);
        }

    }
}
