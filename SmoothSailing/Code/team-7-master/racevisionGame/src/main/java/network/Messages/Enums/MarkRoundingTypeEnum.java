package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various types of marks that may be passed.
 */
public enum MarkRoundingTypeEnum {

    UNKNOWN(0),

    /**
     * The mark is a singular mark.
     */
    MARK(1),

    /**
     * The mark is a gate (windward, leeward, start, finish, etc...).
     */
    GATE(2),

    NOT_A_TYPE(-1);

    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a {@link MarkRoundingTypeEnum} from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private MarkRoundingTypeEnum(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and MarkRoundingTypeEnum values.
    private static final Map<Byte, MarkRoundingTypeEnum> byteToTypeMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToTypeMap.
     */
    static {
        for (MarkRoundingTypeEnum type : MarkRoundingTypeEnum.values()) {
            byteToTypeMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param sideByte Byte value to convert to a {@link MarkRoundingTypeEnum} value.
     * @return The {@link MarkRoundingTypeEnum} value which corresponds to the given byte value.
     */
    public static MarkRoundingTypeEnum fromByte(byte sideByte) {
        //Gets the corresponding MarkRoundingTypeEnum from the map.
        MarkRoundingTypeEnum type = byteToTypeMap.get(sideByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_TYPE MarkRoundingTypeEnum.
            return MarkRoundingTypeEnum.NOT_A_TYPE;
        }
        else {
            //Otherwise, return the MarkRoundingTypeEnum.
            return type;
        }

    }

}
