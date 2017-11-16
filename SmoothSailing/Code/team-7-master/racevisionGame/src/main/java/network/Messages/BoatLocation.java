package network.Messages;


import network.Messages.Enums.BoatLocationDeviceEnum;
import network.Messages.Enums.MessageType;
import network.Utils.AC35UnitConverter;
import shared.model.Azimuth;
import shared.model.Bearing;

import static network.Utils.AC35UnitConverter.unpackGPS;

/**
 * Represents the information in a boat location message (AC streaming spec: 4.9).
 */
public class BoatLocation extends AC35Data {


    /**
     * The current messageVersionNumber according to the API spec.
     */
    public static final byte currentMessageVersionNumber = 1;


    /**
     * Version number of the message.
     */
    private byte messageVersionNumber;

    /**
     * Time of the event - milliseconds since jan 1 1970. Proper type is 6 byte int.
     */
    private long time;

    /**
     * Source ID of the boat.
     */
    private int sourceID;

    /**
     * Sequence number of the message.
     */
    private long sequenceNumber;

    /**
     * Device type of the message (physical source of the message).
     */
    private BoatLocationDeviceEnum deviceType;

    /**
     * Latitude of the boat.
     */
    private double latitude;

    /**
     * Longitude of the boat.
     */
    private double longitude;

    /**
     * Altitude of the boat.
     */
    private int altitude;

    /**
     * Heading of the boat. Clockwise, 0 = north. Proper type is unsigned 2 byte int.
     */
    private Bearing heading;

    /**
     * Pitch of the boat.
     */
    private short pitch;

    /**
     * Roll of the boat.
     */
    private short roll;

    /**
     * Speed of the boat, in knots.
     */
    private double boatSpeedKnots;

    /**
     * Course over ground (COG) of the boat.
     */
    private Bearing boatCOG;

    /**
     * Speed over ground (SOG) of the boat, in knots.
     */
    private double boatSOGKnots;

    /**
     * Apparent wind speed at time of event. Proper type is unsigned 2 byte int. millimeters per second.
     */
    private double apparentWindSpeedKnots;

    /**
     * Apparent wind angle at time of the event. Wind over starboard = positive.
     */
    private Azimuth apparentWindAngle;

    /**
     * True wind speed, in knots.
     */
    private double trueWindSpeedKnots;

    /**
     * True wind direction.
     */
    private Bearing trueWindDirection;

    /**
     * True wind angle. Clockwise compass direction, 0 = north.
     */
    private Azimuth trueWindAngle;

    /**
     * Current drift, in knots.
     */
    private double currentDriftKnots;

    /**
     * Current set.
     */
    private Bearing currentSet;

    /**
     * Rudder angle. Positive is rudder set to turn yacht to port.
     */
    private Azimuth rudderAngle;



    /**
     * Constructs a BoatLocation message with the given parameters.
     *
     * @param messageVersionNumber message number
     * @param time time of message
     * @param sourceID id of boat
     * @param sequenceNumber number of boat message
     * @param deviceType The source of the BoatLocation message.
     * @param latitude lat of boat
     * @param longitude lon of boat
     * @param altitude altitude of boat
     * @param heading heading of boat
     * @param pitch pitch of boat
     * @param roll roll of boat
     * @param boatSpeedKnots boats speed
     * @param boatCOG boat cog
     * @param boatSOGKnots boat sog
     * @param apparentWindSpeedKnots wind speed
     * @param apparentWindAngle wind angle
     * @param trueWindSpeedKnots true wind speed
     * @param trueWindDirection true wind direction
     * @param trueWindAngle true wind angle
     * @param currentDriftKnots current drift
     * @param currentSet current set
     * @param rudderAngle rudder angle
     */
    public BoatLocation(
            byte messageVersionNumber,
            long time,
            int sourceID,
            long sequenceNumber,
            BoatLocationDeviceEnum deviceType,
            double latitude,
            double longitude,
            int altitude,
            Bearing heading,
            short pitch,
            short roll,
            double boatSpeedKnots,
            Bearing boatCOG,
            double boatSOGKnots,
            double apparentWindSpeedKnots,
            Azimuth apparentWindAngle,
            double trueWindSpeedKnots,
            Bearing trueWindDirection,
            Azimuth trueWindAngle,
            double currentDriftKnots,
            Bearing currentSet,
            Azimuth rudderAngle   ) {

        super(MessageType.BOATLOCATION);

        this.messageVersionNumber = messageVersionNumber;
        this.time = time;
        this.sourceID = sourceID;
        this.sequenceNumber = sequenceNumber;
        this.deviceType = deviceType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
        this.boatSpeedKnots = boatSpeedKnots;
        this.boatCOG = boatCOG;
        this.boatSOGKnots = boatSOGKnots;
        this.apparentWindSpeedKnots = apparentWindSpeedKnots;
        this.apparentWindAngle = apparentWindAngle;
        this.trueWindSpeedKnots = trueWindSpeedKnots;
        this.trueWindDirection = trueWindDirection;
        this.trueWindAngle = trueWindAngle;
        this.currentDriftKnots = currentDriftKnots;
        this.currentSet = currentSet;
        this.rudderAngle = rudderAngle;
    }


    public BoatLocation(
            int sourceID,
            double lat,
            double lon,
            long sequenceNumber,
            BoatLocationDeviceEnum deviceType,
            Bearing heading,
            double boatSpeedKnots,
            long time       ) {

        this(
            BoatLocation.currentMessageVersionNumber,
            time,
            sourceID,
            sequenceNumber,
            deviceType,
            lat,
            lon,
            0,
            heading,
            (short) 0,
            (short) 0,
            boatSpeedKnots,
            heading,
            boatSpeedKnots,
            0,
            Azimuth.fromDegrees(0),
            0,
            Bearing.fromDegrees(0),
            Azimuth.fromDegrees(0),
            0,
            Bearing.fromDegrees(0),
            Azimuth.fromDegrees(0)   );
    }


    /**
     * Returns the version number of the message.
     * @return The version number of the message.
     */
    public byte getMessageVersionNumber() {
        return messageVersionNumber;
    }


    /**
     * Returns the time that this message was generated at.
     * @return Time message was generated at, in milliseconds since unix epoch.
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns the sourceID of the boat this message relates to.
     * @return SourceID of the boat this message relates to.
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * Returns the sequence number of this message.
     * @return The sequence number of the message.
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }


    /**
     * Returns the device source of this message.
     * @return The device source of this message.
     */
    public BoatLocationDeviceEnum getDeviceType() {
        return deviceType;
    }


    /**
     * Returns the latitude, in degrees, that the boat is located at.
     * @return Latitude, in degrees, of boat.
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Returns the longitude, in degrees, that the boat is located at.
     * @return Longitude, in degrees, of boat.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the altitude of the boat.
     * @return The altitude of the boat.
     */
    public int getAltitude() {
        return altitude;
    }


    /**
     * Returns the current heading/bearing of the boat.
     * @return Heading of the boat.
     */
    public Bearing getHeading() {
        return heading;
    }


    /**
     * Returns the current pitch of the boat.
     * @return Pitch of the boat.
     */
    public short getPitch() {
        return pitch;
    }


    /**
     * Returns the current roll of the boat.
     * @return Roll of the boat.
     */
    public short getRoll() {
        return roll;
    }


    /**
     * Returns the current boat speed, in knots.
     * @return Current boat speed, in knots.
     */
    public double getBoatSpeedKnots() {
        return boatSpeedKnots;
    }


    /**
     * Returns the boat's Course Over Ground.
     * @return Boat's COG.
     */
    public Bearing getBoatCOG() {
        return boatCOG;
    }


    /**
     * Returns the boats Speed Over Ground, in knots.
     * @return Boat's SOG.
     */
    public double getBoatSOGKnots() {
        return boatSOGKnots;
    }


    /**
     * Returns the apparent wind speed, in knots, at the boat.
     * @return Wind speed, in knots, at the boat.
     */
    public double getApparentWindSpeedKnots() {
        return apparentWindSpeedKnots;
    }

    /**
     * Returns the apparent wind angle at the boat.
     * @return Wind angle at the boat.
     */
    public Azimuth getApparentWindAngle() {
        return apparentWindAngle;
    }


    /**
     * Returns the true wind speed, in knots.
     * @return True wind speed, in knots.
     */
    public double getTrueWindSpeedKnots() {
        return trueWindSpeedKnots;
    }


    /**
     * Returns the true wind direction.
     * @return True wind direction.
     */
    public Bearing getTrueWindDirection()
    {
        return trueWindDirection;
    }


    /**
     * Returns the true wind angle.
     * @return True wind angle.
     */
    public Azimuth getTrueWindAngle() {
        return trueWindAngle;
    }


    /**
     * Returns the current drift of the boat, in knots.
     * @return Current drift, in knots.
     */
    public double getCurrentDriftKnots() {
        return currentDriftKnots;
    }


    /**
     * Returns the current set of the boat.
     * @return Current set of the boat.
     */
    public Bearing getCurrentSet() {
        return currentSet;
    }


    /**
     * Returns the current rudder angle of the boat.
     * @return Current rudder angle of the boat.
     */
    public Azimuth getRudderAngle() {
        return rudderAngle;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Message version number: ");
        builder.append(this.getMessageVersionNumber());

        builder.append("\nTime: ");
        builder.append(this.getTime());

        builder.append("\nSource ID: ");
        builder.append(this.getSourceID());

        builder.append("\nSequence number: ");
        builder.append(this.getSequenceNumber());

        builder.append("\nDevice type: ");
        builder.append(this.getDeviceType());

        builder.append("\nLatitude: ");
        builder.append(this.getLatitude());

        builder.append("\nLongitude: ");
        builder.append(this.getLongitude());

        builder.append("\nAltitude: ");
        builder.append(this.getAltitude());

        builder.append("\nHeading: ");
        builder.append(this.getHeading());

        builder.append("\nPitch: ");
        builder.append(this.getPitch());

        builder.append("\nRoll: ");
        builder.append(this.getRoll());

        builder.append("\nBoat speed (mm/sec): ");
        builder.append(this.getBoatSpeedKnots());

        builder.append("\nBoat COG: ");
        builder.append(this.getBoatCOG());

        builder.append("\nBoat SOG: ");
        builder.append(this.getBoatSOGKnots());

        builder.append("\nApparent wind speed: ");
        builder.append(this.getApparentWindSpeedKnots());

        builder.append("\nApparent wind angle: ");
        builder.append(this.getApparentWindAngle());

        builder.append("\nTrue wind speed: ");
        builder.append(this.getTrueWindSpeedKnots());

        builder.append("\nTrue wind angle: ");
        builder.append(this.getTrueWindAngle());

        builder.append("\nCurrent drift: ");
        builder.append(this.getCurrentDriftKnots());

        builder.append("\nCurrent set: ");
        builder.append(this.getCurrentSet());

        builder.append("\nRudder angle: ");
        builder.append(this.getRudderAngle());

        return builder.toString();
    }
}
