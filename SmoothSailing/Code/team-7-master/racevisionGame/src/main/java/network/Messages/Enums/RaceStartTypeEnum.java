package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various types race start status notifications. See AC35 streaming spec, 4.5.
 */
public enum RaceStartTypeEnum {


    /**
     * The race start time is being set.
     */
    SET_RACE_START(1),

    /**
     * The race has been postponed.
     */
    RACE_POSTPONED(2),

    /**
     * The race has been abandoned.
     */
    RACE_ABANDONED(3),

    /**
     * The race has been terminated.
     */
    RACE_TERMINATED(4),

    /**
     * Used to indicate that a given byte value is invalid.
     */
    NOT_A_TYPE(-1);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a RaceStartTypeEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private RaceStartTypeEnum(int value) {
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
     * Stores a mapping between Byte values and RaceStartTypeEnum values.
     */
    private static final Map<Byte, RaceStartTypeEnum> byteToTypeMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToTypeMap.
     */
    static {
        for (RaceStartTypeEnum type : RaceStartTypeEnum.values()) {
            RaceStartTypeEnum.byteToTypeMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param startTypeEnum Byte value to convert to a RaceStartTypeEnum value.
     * @return The RaceStartTypeEnum value which corresponds to the given byte value.
     */
    public static RaceStartTypeEnum fromByte(byte startTypeEnum) {
        //Gets the corresponding MessageType from the map.
        RaceStartTypeEnum type = RaceStartTypeEnum.byteToTypeMap.get(startTypeEnum);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_TYPE RaceStartTypeEnum.
            return RaceStartTypeEnum.NOT_A_TYPE;
        } else {
            //Otherwise, return the RaceStartTypeEnum.
            return type;
        }

    }


}
