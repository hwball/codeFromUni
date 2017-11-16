package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.BoatStatus;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link BoatStatus} message.
 */
public class BoatStatusEncoder {


    /**
     * Constructor.
     */
    public BoatStatusEncoder() {
    }


    /**
     * Encodes a given BoatStatus message.
     * @param message The message to encode.
     * @return The encoded message.
     * @throws InvalidMessageException Thrown if the message is invalid in some way, or cannot be encoded.
     */
    public byte[] encode(BoatStatus message) throws InvalidMessageException {

        try {


            //Downcast.
            BoatStatus boatStatus = (BoatStatus) message;

            //BoatStatus is 20 bytes.
            ByteBuffer boatStatusBuffer = ByteBuffer.allocate(20);

            byte[] sourceID = intToBytes(boatStatus.getSourceID());
            byte[] boatStatusBytes = intToBytes(boatStatus.getBoatStatus().getValue(), 1);
            byte[] legNum = intToBytes(boatStatus.getLegNumber(), 1);
            byte[] numPenalties = intToBytes(boatStatus.getNumPenaltiesAwarded(), 1);
            byte[] numPenaltiesServed = intToBytes(boatStatus.getNumPenaltiesServed(), 1);
            byte[] estNextMarkTime = longToBytes(boatStatus.getEstTimeAtNextMark(), 6);
            byte[] estFinishTime = longToBytes(boatStatus.getEstTimeAtFinish(), 6);

            boatStatusBuffer.put(sourceID);
            boatStatusBuffer.put(boatStatusBytes);
            boatStatusBuffer.put(legNum);
            boatStatusBuffer.put(numPenalties);
            boatStatusBuffer.put(numPenaltiesServed);
            boatStatusBuffer.put(estNextMarkTime);
            boatStatusBuffer.put(estFinishTime);

            return boatStatusBuffer.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode BoatStatus message.", e);
        }

    }
}
