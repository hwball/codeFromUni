package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various sides around which a boat may pass a mark.
 */
public enum MarkRoundingSideEnum {

    UNKNOWN(0),

    /**
     * Boat rounded around port side of mark.
     */
    PORT(1),

    /**
     * Boat rounded around starboard side of mark.
     */
    STARBOARD(2),

    NOT_A_SIDE(-1);

    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a {@link MarkRoundingSideEnum} from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private MarkRoundingSideEnum(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and MarkRoundingSideEnum values.
    private static final Map<Byte, MarkRoundingSideEnum> byteToSideMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToSideMap.
     */
    static {
        for (MarkRoundingSideEnum type : MarkRoundingSideEnum.values()) {
            byteToSideMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param sideByte Byte value to convert to a {@link MarkRoundingSideEnum} value.
     * @return The {@link MarkRoundingSideEnum} value which corresponds to the given byte value.
     */
    public static MarkRoundingSideEnum fromByte(byte sideByte) {
        //Gets the corresponding MarkRoundingSideEnum from the map.
        MarkRoundingSideEnum type = byteToSideMap.get(sideByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_SIDE MarkRoundingSideEnum.
            return MarkRoundingSideEnum.NOT_A_SIDE;
        }
        else {
            //Otherwise, return the MarkRoundingSideEnum.
            return type;
        }

    }

}
