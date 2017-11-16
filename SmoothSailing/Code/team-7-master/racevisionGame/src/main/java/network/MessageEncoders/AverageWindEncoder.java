package network.MessageEncoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.AverageWind;

import java.nio.ByteBuffer;

import static network.Utils.AC35UnitConverter.*;
import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;

/**
 * This encoder can encode a {@link AverageWind} message.
 */
public class AverageWindEncoder implements MessageEncoder {


    /**
     * Constructor.
     */
    public AverageWindEncoder() {
    }


    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {

        try {

            //Downcast.
            AverageWind averageWind = (AverageWind) message;


            byte messageVersionNumber = averageWind.getMessageVersionNumber();

            long time = averageWind.getTime();
            byte[] byteTime = longToBytes(time, 6);

            long rawPeriod = averageWind.getRawPeriod();
            int rawPeriodInt = packAverageWindPeriod(rawPeriod);
            byte[] byteRawPeriod = intToBytes(rawPeriodInt, 2);

            double rawSampleSpeed = averageWind.getRawSpeedKnots();
            int rawSampleSpeedInt = packKnotsToMMperSec(rawSampleSpeed);
            byte[] byteRawSpeed = intToBytes(rawSampleSpeedInt, 2);

            long period2 = averageWind.getSampleTwoPeriod();
            int period2Int = packAverageWindPeriod(period2);
            byte[] bytePeriod2 = intToBytes(period2Int, 2);

            double speed2 = averageWind.getSampleTwoSpeedKnots();
            int speed2Int = packKnotsToMMperSec(speed2);
            byte[] byteSpeed2 = intToBytes(speed2Int, 2);

            long period3 = averageWind.getSampleThreePeriod();
            int period3Int = packAverageWindPeriod(period3);
            byte[] bytePeriod3 = intToBytes(period3Int, 2);

            double speed3 = averageWind.getSampleThreeSpeedKnots();
            int speed3Int = packKnotsToMMperSec(speed3);
            byte[] byteSpeed3 = intToBytes(speed3Int, 2);

            long period4 = averageWind.getSampleFourPeriod();
            int period4Int = packAverageWindPeriod(period4);
            byte[] bytePeriod4 = intToBytes(period4Int, 2);

            double speed4 = averageWind.getSampleFourSpeedKnots();
            int speed4Int = packKnotsToMMperSec(speed4);
            byte[] byteSpeed4 = intToBytes(speed4Int, 2);


            ByteBuffer result = ByteBuffer.allocate(23);
            result.put(messageVersionNumber);
            result.put(byteTime);
            result.put(byteRawPeriod);
            result.put(byteRawSpeed);
            result.put(bytePeriod2);
            result.put(byteSpeed2);
            result.put(bytePeriod3);
            result.put(byteSpeed3);
            result.put(bytePeriod4);
            result.put(byteSpeed4);
            return result.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode AverageWind message.", e);
        }

    }
}
