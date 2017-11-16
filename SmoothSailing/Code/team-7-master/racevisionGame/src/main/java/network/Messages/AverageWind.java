package network.Messages;


import network.Messages.Enums.MessageType;

/**
 * Represents an AverageWind message in the streaming API (see section 4.12).
 */
public class AverageWind extends AC35Data {

    /**
     * The current version number for this message type.
     */
    public static final byte currentMessageVersionNumber = 1;


    /**
     * The version number for this message.
     */
    private byte messageVersionNumber;

    /**
     * Timestamp for when the measurement was taken. Milliseconds since unix epoch.
     */
    private long time;

    /**
     * Raw sample rate period. Milliseconds.
     */
    private long rawPeriod;

    /**
     * Raw wind speed, in knots.
     */
    private double rawSpeedKnots;

    /**
     * Wind speed average period for second sample. Milliseconds.
     */
    private long sampleTwoPeriod;

    /**
     * Wind speed of second sample. Knots.
     */
    private double sampleTwoSpeedKnots;

    /**
     * Wind speed average period for third sample. Milliseconds.
     */
    private long sampleThreePeriod;

    /**
     * Wind speed of third sample. Knots.
     */
    private double sampleThreeSpeedKnots;

    /**
     * Wind speed average period for fourth sample. Milliseconds.
     */
    private long sampleFourPeriod;

    /**
     * Wind speed of fourth sample. Knots.
     */
    private double sampleFourSpeedKnots;


    /**
     * Creates an AverageWind message, with the given parameters.
     * @param messageVersionNumber The version number of message.
     * @param time The timestamp of the message.
     * @param rawPeriod The period of the raw measurement. Milliseconds.
     * @param rawSpeedKnots The speed of the raw measurement. Knots.
     * @param sampleTwoPeriod The period of the second measurement. Milliseconds.
     * @param sampleTwoSpeedKnots The speed of the second measurement. Knots.
     * @param sampleThreePeriod The period of the third measurement. Milliseconds.
     * @param sampleThreeSpeedKnots The speed of the third measurement. Knots.
     * @param sampleFourPeriod The period of the fourth measurement. Milliseconds.
     * @param sampleFourSpeedKnots The speed of the fourth measurement. Knots.
     */
    public AverageWind(
            byte messageVersionNumber,
            long time,
            long rawPeriod,
            double rawSpeedKnots,
            long sampleTwoPeriod,
            double sampleTwoSpeedKnots,
            long sampleThreePeriod,
            double sampleThreeSpeedKnots,
            long sampleFourPeriod,
            double sampleFourSpeedKnots    ) {

        super(MessageType.AVGWIND);

        this.messageVersionNumber = messageVersionNumber;
        this.time = time;
        this.rawPeriod = rawPeriod;
        this.rawSpeedKnots = rawSpeedKnots;
        this.sampleTwoPeriod = sampleTwoPeriod;
        this.sampleTwoSpeedKnots = sampleTwoSpeedKnots;
        this.sampleThreePeriod = sampleThreePeriod;
        this.sampleThreeSpeedKnots = sampleThreeSpeedKnots;
        this.sampleFourPeriod = sampleFourPeriod;
        this.sampleFourSpeedKnots = sampleFourSpeedKnots;
    }


    /**
     * Returns the version number of this message.
     * @return Message version number.
     */
    public byte getMessageVersionNumber() {
        return messageVersionNumber;
    }

    /**
     * Returns the timestamp of this message.
     * @return Timestamp of this message. Milliseconds since unix epoch.
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns the period of time over which the raw sample was done. Milliseconds.
     * @return Raw sample's period.
     */
    public long getRawPeriod() {
        return rawPeriod;
    }

    /**
     * Returns the wind speed of the raw sample. Knots.
     * @return Wind speed of raw sample. Knots
     */
    public double getRawSpeedKnots() {
        return rawSpeedKnots;
    }

    /**
     * Returns the period of time over which the second sample was done. Milliseconds.
     * @return Second sample's period.
     */
    public long getSampleTwoPeriod() {
        return sampleTwoPeriod;
    }

    /**
     * Returns the wind speed of the second sample. Knots.
     * @return Wind speed of second sample. Knots
     */
    public double getSampleTwoSpeedKnots() {
        return sampleTwoSpeedKnots;
    }

    /**
     * Returns the period of time over which the third sample was done. Milliseconds.
     * @return Third sample's period.
     */
    public long getSampleThreePeriod() {
        return sampleThreePeriod;
    }

    /**
     * Returns the wind speed of the third sample. Knots.
     * @return Wind speed of third sample. Knots
     */
    public double getSampleThreeSpeedKnots() {
        return sampleThreeSpeedKnots;
    }

    /**
     * Returns the period of time over which the fourth sample was done. Milliseconds.
     * @return Fourth sample's period.
     */
    public long getSampleFourPeriod() {
        return sampleFourPeriod;
    }

    /**
     * Returns the wind speed of the fourth sample. Knots.
     * @return Wind speed of fourth sample. Knots
     */
    public double getSampleFourSpeedKnots() {
        return sampleFourSpeedKnots;
    }



}
