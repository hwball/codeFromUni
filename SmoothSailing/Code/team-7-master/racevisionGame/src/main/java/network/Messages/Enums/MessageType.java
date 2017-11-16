package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that encapsulates the various types of messages that can be sent.
 */
public enum MessageType {
    HEARTBEAT(1),
    RACESTATUS(12),
    DISPLAYTEXTMESSAGE(20),
    XMLMESSAGE(26),
    RACESTARTSTATUS(27),
    YACHTEVENTCODE(29),
    YACHTACTIONCODE(31),
    CHATTERTEXT(36),
    BOATLOCATION(37),
    MARKROUNDING(38),
    COURSEWIND(44),
    AVGWIND(47),


    BOATACTION(100),

    /**
     * This is used for {@link network.Messages.RequestToJoin} messages.
     */
    REQUEST_TO_JOIN(101),

    /**
     * This is used for {@link network.Messages.JoinAcceptance} messages.
     */
    JOIN_ACCEPTANCE(102),


    /**
     * This is used for {@link network.Messages.AssignPlayerBoat} messages.
     */
    ASSIGN_PLAYER_BOAT(121),

    HOST_GAME(108),

    HOSTED_GAMES_REQUEST(109),

    BOATSTATE(103),

    NOTAMESSAGE(0);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Creates a MessageType enum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    MessageType(int value) {
        this.value = (byte)value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    ///Stores a mapping between Byte values and MessageType values.
    private static final Map<Byte, MessageType> byteToTypeMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToTypeMap.
     */
    static {
        for (MessageType type : MessageType.values()) {
            byteToTypeMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param messageTypeByte Byte value to convert to a MessageType value.
     * @return The MessageType value which corresponds to the given byte value.
     */
    public static MessageType fromByte(byte messageTypeByte) {
        //Gets the corresponding MessageType from the map.
        MessageType type = byteToTypeMap.get(messageTypeByte);

        if (type == null) {
            //If the byte value wasn't found, return the NOTAMESSAGE MessageType.
            return MessageType.NOTAMESSAGE;
        }
        else {
            //Otherwise, return the MessageType.
            return type;
        }

    }


}
