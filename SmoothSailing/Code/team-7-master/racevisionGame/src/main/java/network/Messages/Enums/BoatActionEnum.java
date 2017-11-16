package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Boat actions
 */
public enum BoatActionEnum {
    NOT_A_STATUS(-1),

    /**
     * Autopilot = auto VMG.
     */
    VMG(1),
    SAILS_IN(2),
    SAILS_OUT(3),
    TACK_GYBE(4),
    UPWIND(5),
    DOWNWIND(6),
    ZOOM_IN(7),
    ZOOM_OUT(8),
    TOGGLE_SAILS(9);

    private byte value;

    /**
     * Ctor. Creates a BoatActionEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private BoatActionEnum(int value) {
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
     * Stores a mapping between Byte values and BoatActionEnum values.
     */
    private static final Map<Byte, BoatActionEnum> byteToStatusMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStatusMap.
     */
    static {
        for (BoatActionEnum type : BoatActionEnum.values()) {
            BoatActionEnum.byteToStatusMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param boatActionEnum Byte value to convert to a BoatActionEnum value.
     * @return The BoatActionEnum value which corresponds to the given byte value.
     */
    public static BoatActionEnum fromByte(byte boatActionEnum) {
        //Gets the corresponding MessageType from the map.
        BoatActionEnum type = BoatActionEnum.byteToStatusMap.get(boatActionEnum);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_STATUS boatActionEnum.
            return BoatActionEnum.NOT_A_STATUS;
        } else {
            //Otherwise, return the boatActionEnum.
            return type;
        }

    }
}
