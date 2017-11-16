package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatLocation;
import network.Messages.Enums.BoatLocationDeviceEnum;
import shared.model.Azimuth;
import shared.model.Bearing;

import java.util.Arrays;

import static network.Utils.AC35UnitConverter.*;
import static network.Utils.ByteConverter.*;


/**
 * Decodes {@link BoatLocation} messages.
 */
public class BoatLocationDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private BoatLocation message;




    /**
     * Constructs a decoder to decode a given message.
     */
    public BoatLocationDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte[] messageVersionNumberBytes = Arrays.copyOfRange(encodedMessage, 0, 1);
            byte messageVersionNumber = messageVersionNumberBytes[0];

            byte[] timeBytes = Arrays.copyOfRange(encodedMessage, 1, 7);
            long time = bytesToLong(timeBytes);

            byte[] sourceIDBytes = Arrays.copyOfRange(encodedMessage, 7, 11);
            int sourceID = bytesToInt(sourceIDBytes);

            byte[] seqNumBytes = Arrays.copyOfRange(encodedMessage, 11, 15);
            int seqNum = bytesToInt(seqNumBytes);

            byte[] deviceTypeBytes = Arrays.copyOfRange(encodedMessage, 15, 16);
            BoatLocationDeviceEnum deviceType = BoatLocationDeviceEnum.fromByte(deviceTypeBytes[0]);

            byte[] latitudeBytes = Arrays.copyOfRange(encodedMessage, 16, 20);
            int numLatitude = bytesToInt(latitudeBytes);
            double latitude = unpackGPS(numLatitude);

            byte[] longitudeBytes = Arrays.copyOfRange(encodedMessage, 20, 24);
            int numLongitude = bytesToInt(longitudeBytes);
            double longitude = unpackGPS(numLongitude);

            byte[] altitudeBytes = Arrays.copyOfRange(encodedMessage, 24, 28);
            int numAltitude = bytesToInt(altitudeBytes);

            byte[] headingBytes = Arrays.copyOfRange(encodedMessage, 28, 30);
            int numHeading = bytesToInt(headingBytes);
            Bearing heading = Bearing.fromDegrees(unpackHeading(numHeading));

            byte[] pitchBytes = Arrays.copyOfRange(encodedMessage, 30, 32);
            short numPitch = bytesToShort(pitchBytes);

            byte[] rollBytes = Arrays.copyOfRange(encodedMessage, 32, 34);
            short numRoll = bytesToShort(rollBytes);

            byte[] boatSpeedBytes = Arrays.copyOfRange(encodedMessage, 34, 36);
            int numBoatSpeed = bytesToInt(boatSpeedBytes);
            double boatSpeedKnots = unpackMMperSecToKnots(numBoatSpeed);

            byte[] cogBytes = Arrays.copyOfRange(encodedMessage, 36, 38);
            int numCog = bytesToInt(cogBytes);
            Bearing cog = Bearing.fromDegrees(unpackHeading(numCog));

            byte[] sogBytes = Arrays.copyOfRange(encodedMessage, 38, 40);
            int numSog = bytesToInt(sogBytes);
            double sogKnots = unpackMMperSecToKnots(numSog);

            byte[] apparentWindSpeedBytes = Arrays.copyOfRange(encodedMessage, 40, 42);
            int numApparentWindSpeed = bytesToInt(apparentWindSpeedBytes);
            double apparentWindSpeedKnots = unpackMMperSecToKnots(numApparentWindSpeed);

            byte[] apparentWindAngleBytes = Arrays.copyOfRange(encodedMessage, 42, 44);
            short numApparentWindAngle = bytesToShort(apparentWindAngleBytes);
            Azimuth apparentWindAngle = Azimuth.fromDegrees(unpackTrueWindAngle(numApparentWindAngle));

            byte[] trueWindSpeedBytes = Arrays.copyOfRange(encodedMessage, 44, 46);
            int numTrueWindSpeed = bytesToInt(trueWindSpeedBytes);
            double trueWindSpeedKnots = unpackMMperSecToKnots(numTrueWindSpeed);

            byte[] trueWindDirectionBytes = Arrays.copyOfRange(encodedMessage, 46, 48);
            short numTrueWindDirection = bytesToShort(trueWindDirectionBytes);
            Bearing trueWindDirection = Bearing.fromDegrees(unpackHeading(numTrueWindDirection));

            byte[] trueWindAngleBytes = Arrays.copyOfRange(encodedMessage, 48, 50);
            short numTrueWindAngle = bytesToShort(trueWindAngleBytes);
            Azimuth trueWindAngle = Azimuth.fromDegrees(unpackTrueWindAngle(numTrueWindAngle));

            byte[] currentDriftBytes = Arrays.copyOfRange(encodedMessage, 50, 52);
            int numCurrentDrift = bytesToInt(currentDriftBytes);
            double currentDriftKnots = unpackMMperSecToKnots(numCurrentDrift);

            byte[] currentSetBytes = Arrays.copyOfRange(encodedMessage, 52, 54);
            int numCurrentSet = bytesToShort(currentSetBytes);
            Bearing currentSet = Bearing.fromDegrees(unpackHeading(numCurrentSet));

            byte[] rudderAngleBytes = Arrays.copyOfRange(encodedMessage, 54, 56);
            short numRudderAngle = bytesToShort(rudderAngleBytes);
            Azimuth rudderAngle = Azimuth.fromDegrees(unpackTrueWindAngle(numRudderAngle));


            message = new BoatLocation(
                    messageVersionNumber,
                    time,
                    sourceID,
                    seqNum,
                    deviceType,
                    latitude,
                    longitude,
                    numAltitude,
                    heading,
                    numPitch,
                    numRoll,
                    boatSpeedKnots,
                    cog,
                    sogKnots,
                    apparentWindSpeedKnots,
                    apparentWindAngle,
                    trueWindSpeedKnots,
                    trueWindDirection,
                    trueWindAngle,
                    currentDriftKnots,
                    currentSet,
                    rudderAngle);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode BoatLocation message.", e);
        }

    }


    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public BoatLocation getMessage() {
        return message;
    }
}
