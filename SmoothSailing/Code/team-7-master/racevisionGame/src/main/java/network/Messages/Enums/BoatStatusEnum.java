package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various statuses a boat can have.
 */
public enum BoatStatusEnum {
    UNDEFINED(0),
    PRESTART(1),
    RACING(2),
    FINISHED(3),
    DNS(4),
    DNF(5),
    DSQ(6),
    OCS(7),
    NOT_A_STATUS(-1);

    ///Primitive value of the enum.
    private byte value;


    /**
     * Ctor. Creates a BoatStatusEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private BoatStatusEnum(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and BoatStatusEnum values.
    private static final Map<Byte, BoatStatusEnum> byteToStatusMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStatusMap.
     */
    static {
        for (BoatStatusEnum type : BoatStatusEnum.values()) {
            byteToStatusMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param boatStatusByte Byte value to convert to a BoatStatusEnum value.
     * @return The BoatStatusEnum value which corresponds to the given byte value.
     */
    public static BoatStatusEnum fromByte(byte boatStatusByte) {
        //Gets the corresponding MessageType from the map.
        BoatStatusEnum type = byteToStatusMap.get(boatStatusByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_STATUS BoatStatusEnum.
            return BoatStatusEnum.NOT_A_STATUS;
        }
        else {
            //Otherwise, return the BoatStatusEnum.
            return type;
        }

    }

}
