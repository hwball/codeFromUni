package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatLocation;

import java.nio.ByteBuffer;

import static network.Utils.AC35UnitConverter.*;
import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link BoatLocation} message.
 */
public class BoatLocationEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public BoatLocationEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            BoatLocation boatLocation = (BoatLocation) message;


            int messageVersionNumber = 0b1;
            byte[] messageVersionBytes = intToBytes(messageVersionNumber, 1);
            byte[] time = longToBytes(boatLocation.getTime(), 6);
            byte[] sourceID = intToBytes(boatLocation.getSourceID(), 4);
            byte[] seqNum = longToBytes(boatLocation.getSequenceNumber(), 4);
            byte[] deviceType = intToBytes(boatLocation.getDeviceType().getValue(), 1);
            byte[] latitude = intToBytes(packGPS(boatLocation.getLatitude()), 4);
            byte[] longitude = intToBytes(packGPS(boatLocation.getLongitude()), 4);
            byte[] altitude = intToBytes(boatLocation.getAltitude(), 4);
            byte[] heading = intToBytes(packHeading(boatLocation.getHeading().degrees()), 2);
            byte[] pitch = intToBytes(boatLocation.getPitch(), 2);
            byte[] roll = intToBytes(boatLocation.getRoll(), 2);
            byte[] boatSpeed = intToBytes(packKnotsToMMperSec(boatLocation.getBoatSpeedKnots()), 2);
            byte[] cog = intToBytes(packHeading(boatLocation.getBoatCOG().degrees()), 2);
            byte[] sog = intToBytes(packKnotsToMMperSec(boatLocation.getBoatSOGKnots()), 2);
            byte[] apparentWindSpeed = intToBytes(packKnotsToMMperSec(boatLocation.getApparentWindSpeedKnots()), 2);
            byte[] apparentWindAngle = intToBytes(packTrueWindAngle(boatLocation.getApparentWindAngle().degrees()), 2);
            byte[] trueWindSpeed = intToBytes(packKnotsToMMperSec(boatLocation.getTrueWindSpeedKnots()), 2);
            byte[] trueWindDirection = intToBytes(packHeading(boatLocation.getTrueWindDirection().degrees()), 2);
            byte[] trueWindAngle = intToBytes(packTrueWindAngle(boatLocation.getTrueWindAngle().degrees()), 2);
            byte[] currentDrift = intToBytes(packKnotsToMMperSec(boatLocation.getCurrentDriftKnots()), 2);
            byte[] currentSet = intToBytes(packHeading(boatLocation.getCurrentSet().degrees()), 2);
            byte[] rudderAngle = intToBytes(packTrueWindAngle(boatLocation.getRudderAngle().degrees()), 2);

            ByteBuffer result = ByteBuffer.allocate(56);
            result.put(messageVersionBytes);
            result.put(time);
            result.put(sourceID);
            result.put(seqNum);
            result.put(deviceType);
            result.put(latitude);
            result.put(longitude);
            result.put(altitude);
            result.put(heading);
            result.put(pitch);
            result.put(roll);
            result.put(boatSpeed);
            result.put(cog);
            result.put(sog);
            result.put(apparentWindSpeed);
            result.put(apparentWindAngle);
            result.put(trueWindSpeed);
            result.put(trueWindDirection);
            result.put(trueWindAngle);
            result.put(currentDrift);
            result.put(currentSet);
            result.put(rudderAngle);

            return result.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode BoatLocation message.", e);
        }

    }
}
