package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various types of XML messages that can be sent.
 */
public enum XMLMessageType {

    /**
     * A regatta.xml message.
     */
    REGATTA(5),

    /**
     * A race.xml message.
     */
    RACE(6),

    /**
     * A boats.xml message.
     */
    BOAT(7),

    /**
     * Used for unrecognised byte values.
     */
    NOT_A_MESSAGE_TYPE(0);


    ///Primitive value of the enum.
    private byte value;

    /**
     * Ctor. Creates a XMLMessageType enum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private XMLMessageType(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and XMLMessageType values.
    private static final Map<Byte, XMLMessageType> byteToTypeMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToTypeMap.
     */
    static {
        for (XMLMessageType type : XMLMessageType.values()) {
            byteToTypeMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param messageTypeByte Byte value to convert to a XMLMessageType value.
     * @return The XMLMessageType value which corresponds to the given byte value.
     */
    public static XMLMessageType fromByte(byte messageTypeByte) {
        //Gets the corresponding XMLMessageType from the map.
        XMLMessageType type = byteToTypeMap.get(messageTypeByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOTAMESSAGE XMLMessageType.
            return XMLMessageType.NOT_A_MESSAGE_TYPE;
        }
        else {
            //Otherwise, return the XMLMessageType.
            return type;
        }

    }


}
