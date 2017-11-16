package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.BoatStatus;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.Enums.RaceTypeEnum;
import network.Messages.RaceStatus;
import network.Utils.AC35UnitConverter;
import shared.model.Bearing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static network.Utils.ByteConverter.*;


/**
 * Decodes {@link RaceStatus} messages.
 */
public class RaceStatusDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private RaceStatus message;



    /**
     * Constructs a decoder to decode a given message.
     */
    public RaceStatusDecoder() {
    }


    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte[] versionNumBytes = Arrays.copyOfRange(encodedMessage, 0, 1);
            byte versionNum = versionNumBytes[0];

            byte[] timeBytes = Arrays.copyOfRange(encodedMessage, 1, 7);
            long time = bytesToLong(timeBytes);

            byte[] raceIDBytes = Arrays.copyOfRange(encodedMessage, 7, 11);
            int raceID = bytesToInt(raceIDBytes);

            byte[] raceStatusBytes = Arrays.copyOfRange(encodedMessage, 11, 12);
            RaceStatusEnum raceStatus = RaceStatusEnum.fromByte(raceStatusBytes[0]);

            byte[] expectedStartBytes = Arrays.copyOfRange(encodedMessage, 12, 18);
            long expectedStart = bytesToLong(expectedStartBytes);

            byte[] windDirectionBytes = Arrays.copyOfRange(encodedMessage, 18, 20);
            int windDirectionInt = bytesToInt(windDirectionBytes);
            Bearing windDirection = Bearing.fromDegrees(AC35UnitConverter.unpackHeading(windDirectionInt));

            byte[] windSpeedBytes = Arrays.copyOfRange(encodedMessage, 20, 22);
            int windSpeedInt = bytesToInt(windSpeedBytes);
            double windSpeedKnots = AC35UnitConverter.unpackMMperSecToKnots(windSpeedInt);

            byte[] numberOfBoatsBytes = Arrays.copyOfRange(encodedMessage, 22, 23);
            int numberOfBoats = bytesToInt(numberOfBoatsBytes);

            byte[] raceTypeBytes = Arrays.copyOfRange(encodedMessage, 23, 24);
            RaceTypeEnum raceType = RaceTypeEnum.fromByte(raceTypeBytes[0]);

            byte[] boatStatusesBytes = Arrays.copyOfRange(encodedMessage, 24, 25 + 20 * numberOfBoats);
            List<BoatStatus> boatStatuses = new ArrayList<>();


            //BoatStatus is 20 bytes.
            int boatStatusByteLength = 20;


            //Decode each BoatStatus.
            for (int boatLoopIndex = 0; boatLoopIndex < (numberOfBoats * boatStatusByteLength); boatLoopIndex += boatStatusByteLength) {

                byte[] boatStatusBytes = Arrays.copyOfRange(boatStatusesBytes, boatLoopIndex, boatLoopIndex + boatStatusByteLength);

                BoatStatusDecoder boatStatusDecoder = new BoatStatusDecoder();

                boatStatuses.add(boatStatusDecoder.decode(boatStatusBytes));
            }


            message = new RaceStatus(
                    versionNum,
                    time,
                    raceID,
                    raceStatus,
                    expectedStart,
                    windDirection,
                    windSpeedKnots,
                    raceType,
                    boatStatuses);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode RaceStatus message.", e);
        }
    }



    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public RaceStatus getMessage() {
        return message;
    }
}
