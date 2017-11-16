package network.Messages.Enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various statuses a race can have. See AC35 streaming spec, 4.2.
 */
public enum RaceStatusEnum {

    NOT_ACTIVE(0),

    /**
     * Between 3:00 and 1:00 minutes before start.
     */
    WARNING(1),

    /**
     * Less than 1:00 minutes before start.
     */
    PREPARATORY(2),

    /**
     * Race has started.
     */
    STARTED(3),

    /**
     * Obsolete.
     */
    FINISHED(4),

    /**
     * Obsolete.
     */
    RETIRED(5),
    ABANDONED(6),
    POSTPONED(7),
    TERMINATED(8),
    RACE_START_TIME_NOT_SET(9),

    /**
     * More than 3:00 minutes until start.
     */
    PRESTART(10),

    /**
     * Used to indicate that a given byte value is invalid.
     */
    NOT_A_STATUS(-1);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a RaceStatusEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private RaceStatusEnum(int value) {
        this.value = (byte) value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    /**
     * Stores a mapping between Byte values and RaceStatusEnum values.
     */
    private static final Map<Byte, RaceStatusEnum> byteToStatusMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStatusMap.
     */
    static {
        for (RaceStatusEnum type : RaceStatusEnum.values()) {
            RaceStatusEnum.byteToStatusMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param raceStatusByte Byte value to convert to a RaceStatusEnum value.
     * @return The RaceStatusEnum value which corresponds to the given byte value.
     */
    public static RaceStatusEnum fromByte(byte raceStatusByte) {
        //Gets the corresponding MessageType from the map.
        RaceStatusEnum type = RaceStatusEnum.byteToStatusMap.get(raceStatusByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_STATUS RaceStatusEnum.
            return RaceStatusEnum.NOT_A_STATUS;
        } else {
            //Otherwise, return the RaceStatusEnum.
            return type;
        }

    }

}
