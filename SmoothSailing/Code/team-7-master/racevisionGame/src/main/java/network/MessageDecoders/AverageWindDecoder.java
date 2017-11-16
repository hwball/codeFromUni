package network.MessageDecoders;


import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.AverageWind;
import network.Utils.ByteConverter;

import static network.Utils.AC35UnitConverter.*;

import java.util.Arrays;

/**
 * Decodes {@link AverageWind} messages.
 */
public class AverageWindDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private AverageWind message;



    public AverageWindDecoder() {
    }



    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try {

            byte messageVersionNumber = encodedMessage[0];


            byte[] byteTime = Arrays.copyOfRange(encodedMessage, 1, 7);
            long time = ByteConverter.bytesToLong(byteTime);

            byte[] byteRawPeriod = Arrays.copyOfRange(encodedMessage, 7, 9);
            int intRawPeriod = ByteConverter.bytesToInt(byteRawPeriod);
            long rawPeriod = unpackAverageWindPeriod(intRawPeriod);

            byte[] byteRawSpeed = Arrays.copyOfRange(encodedMessage, 9, 11);
            int intRawSpeed = ByteConverter.bytesToInt(byteRawSpeed);
            double rawSpeedKnots = unpackMMperSecToKnots(intRawSpeed);

            byte[] bytePeriod2 = Arrays.copyOfRange(encodedMessage, 11, 13);
            int intPeriod2 = ByteConverter.bytesToInt(bytePeriod2);
            long period2 = unpackAverageWindPeriod(intPeriod2);

            byte[] byteSpeed2 = Arrays.copyOfRange(encodedMessage, 13, 15);
            int intSpeed2 = ByteConverter.bytesToInt(byteSpeed2);
            double speed2Knots = unpackMMperSecToKnots(intSpeed2);

            byte[] bytePeriod3 = Arrays.copyOfRange(encodedMessage, 15, 17);
            int intPeriod3 = ByteConverter.bytesToInt(bytePeriod3);
            long period3 = unpackAverageWindPeriod(intPeriod3);

            byte[] byteSpeed3 = Arrays.copyOfRange(encodedMessage, 17, 19);
            int intSpeed3 = ByteConverter.bytesToInt(byteSpeed3);
            double speed3Knots = unpackMMperSecToKnots(intSpeed3);

            byte[] bytePeriod4 = Arrays.copyOfRange(encodedMessage, 19, 21);
            int intPeriod4 = ByteConverter.bytesToInt(bytePeriod4);
            long period4 = unpackAverageWindPeriod(intPeriod4);

            byte[] byteSpeed4 = Arrays.copyOfRange(encodedMessage, 21, 23);
            int intSpeed4 = ByteConverter.bytesToInt(byteSpeed4);
            double speed4Knots = unpackMMperSecToKnots(intSpeed4);


            message = new AverageWind(
                    messageVersionNumber,
                    time,
                    rawPeriod,
                    rawSpeedKnots,
                    period2,
                    speed2Knots,
                    period3,
                    speed3Knots,
                    period4,
                    speed4Knots);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode AverageWind message.", e);
        }

    }

    /**
     * Returns the decoded message.
     * @return The decoded message.
     */
    public AverageWind getMessage() {
        return message;
    }
}
