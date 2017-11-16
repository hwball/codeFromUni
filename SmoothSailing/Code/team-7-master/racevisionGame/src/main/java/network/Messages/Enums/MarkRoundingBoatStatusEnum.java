package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various statuses a boat can have when rounding a mark.
 */
public enum MarkRoundingBoatStatusEnum {

    UNKNOWN(0),

    /**
     * The boat is actively racing.
     */
    RACING(1),

    /**
     * The boat has been disqualified.
     */
    DSQ(2),

    /**
     * The boat has withdrawn from the race.
     */
    WITHDRAWN(3),

    NOT_A_STATUS(-1);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a {@link MarkRoundingBoatStatusEnum} from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private MarkRoundingBoatStatusEnum(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and MarkRoundingBoatStatusEnum values.
    private static final Map<Byte, MarkRoundingBoatStatusEnum> byteToStatusMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStatusMap.
     */
    static {
        for (MarkRoundingBoatStatusEnum type : MarkRoundingBoatStatusEnum.values()) {
            byteToStatusMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param boatStatusByte Byte value to convert to a {@link MarkRoundingBoatStatusEnum} value.
     * @return The {@link MarkRoundingBoatStatusEnum} value which corresponds to the given byte value.
     */
    public static MarkRoundingBoatStatusEnum fromByte(byte boatStatusByte) {
        //Gets the corresponding MarkRoundingBoatStatusEnum from the map.
        MarkRoundingBoatStatusEnum type = byteToStatusMap.get(boatStatusByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_STATUS MarkRoundingBoatStatusEnum.
            return MarkRoundingBoatStatusEnum.NOT_A_STATUS;
        }
        else {
            //Otherwise, return the MarkRoundingBoatStatusEnum.
            return type;
        }

    }

}
