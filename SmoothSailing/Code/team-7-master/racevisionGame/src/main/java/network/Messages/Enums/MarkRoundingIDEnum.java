package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various mark identities.
 */
public enum MarkRoundingIDEnum {

    UNKNOWN(0),


    ENTRY_LIMIT_LINE(100),

    ENTRY_LINE(101),

    START_LINE(102),

    FINISH_LINE(103),

    SPEED_TEST_START(104),

    SPEED_TEST_FINISH(105),

    CLEAR_START(106),

    NOT_AN_ID(-1);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a {@link MarkRoundingIDEnum} from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private MarkRoundingIDEnum(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and MarkRoundingIDEnum values.
    private static final Map<Byte, MarkRoundingIDEnum> byteToIDMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToIDMap.
     */
    static {
        for (MarkRoundingIDEnum type : MarkRoundingIDEnum.values()) {
            byteToIDMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param sideByte Byte value to convert to a {@link MarkRoundingIDEnum} value.
     * @return The {@link MarkRoundingIDEnum} value which corresponds to the given byte value.
     */
    public static MarkRoundingIDEnum fromByte(byte sideByte) {
        //Gets the corresponding MarkRoundingIDEnum from the map.
        MarkRoundingIDEnum type = byteToIDMap.get(sideByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_AN_ID MarkRoundingIDEnum.
            return MarkRoundingIDEnum.NOT_AN_ID;
        }
        else {
            //Otherwise, return the MarkRoundingIDEnum.
            return type;
        }

    }

}
