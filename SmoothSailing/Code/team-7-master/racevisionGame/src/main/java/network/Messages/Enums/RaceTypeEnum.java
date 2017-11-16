package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various types of races. See AC35 streaming spec, 4.2.
 */
public enum RaceTypeEnum {


    /**
     * A race between two boats.
     */
    MATCH_RACE(1),

    /**
     * A race between a fleet of boats.
     */
    FLEET_RACE(2),

    /**
     * Used to indicate that a given byte value is invalid.
     */
    NOT_A_RACE_TYPE(-1);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a RaceTypeEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private RaceTypeEnum(int value) {
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
     * Attempts to convert a string into a RaceTypeEnum.
     * Ignores case.
     * Treats anything starting with "fleet" as {@link #FLEET_RACE}, and anything starting with "match" as {@link #MATCH_RACE}.
     * @param value The string to convert.
     * @return The RaceTypeEnum.
     */
    public static RaceTypeEnum fromString(String value) {

        //Convert to lower case.
        value = value.toLowerCase();

        if (value.startsWith("fleet")) {
            return FLEET_RACE;
        } else if (value.startsWith("match")) {
            return MATCH_RACE;
        } else {
            return NOT_A_RACE_TYPE;
        }

    }




    /**
     * Stores a mapping between Byte values and RaceStatusEnum values.
     */
    private static final Map<Byte, RaceTypeEnum> byteToStatusMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStatusMap.
     */
    static {
        for (RaceTypeEnum type : RaceTypeEnum.values()) {
            RaceTypeEnum.byteToStatusMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param raceTypeEnum Byte value to convert to a RaceTypeEnum value.
     * @return The RaceTypeEnum value which corresponds to the given byte value.
     */
    public static RaceTypeEnum fromByte(byte raceTypeEnum) {
        //Gets the corresponding MessageType from the map.
        RaceTypeEnum type = RaceTypeEnum.byteToStatusMap.get(raceTypeEnum);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_RACE_TYPE RaceTypeEnum.
            return RaceTypeEnum.NOT_A_RACE_TYPE;
        } else {
            //Otherwise, return the RaceTypeEnum.
            return type;
        }

    }


    @Override
    public String toString() {
        if (fromByte(value) == FLEET_RACE) {
            return "fleet";
        } else if (fromByte(value) == MATCH_RACE) {
            return "match";
        } else {
            return super.toString();
        }
    }
}
