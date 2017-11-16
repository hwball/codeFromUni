package network.Messages.Enums;


import java.util.HashMap;
import java.util.Map;

/**
 * This enum encapsulates the different ways in which a server may respond to a client {@link network.Messages.RequestToJoin} message.
 */
public enum JoinAcceptanceEnum {


    /**
     * Client is allowed to join and spectate.
     */
    JOIN_SUCCESSFUL_SPECTATOR(0),

    /**
     * Client is allowed to join and participate.
     */
    JOIN_SUCCESSFUL_PARTICIPANT(1),

    /**
     * Client is allowed to join and play the tutorial.
     */
    JOIN_SUCCESSFUL_TUTORIAL(2),

    /**
     * Client is allowed to join and participate as a ghost player.
     */
    JOIN_SUCCESSFUL_GHOST(3),


    /**
     * Join Request was denied.
     */
    JOIN_FAILURE(0x10),

    /**
     * The server is completely full, cannot participate or spectate.
     */
    SERVER_FULL(0x11),


    /**
     * Used to indicate that a given byte value is invalid.
     */
    NOT_AN_ACCEPTANCE_TYPE(-1);


    /**
     * Primitive value of the enum.
     */
    private byte value;


    /**
     * Ctor. Creates a JoinAcceptanceEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private JoinAcceptanceEnum(int value) {
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
     * Stores a mapping between Byte values and JoinAcceptanceEnum values.
     */
    private static final Map<Byte, JoinAcceptanceEnum> byteToAcceptanceMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToAcceptanceMap.
     */
    static {
        for (JoinAcceptanceEnum type : JoinAcceptanceEnum.values()) {
            JoinAcceptanceEnum.byteToAcceptanceMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param joinAcceptanceEnum Byte value to convert to a JoinAcceptanceEnum value.
     * @return The RequestToJoinEnum value which corresponds to the given byte value.
     */
    public static JoinAcceptanceEnum fromByte(byte joinAcceptanceEnum) {
        //Gets the corresponding MessageType from the map.
        JoinAcceptanceEnum type = JoinAcceptanceEnum.byteToAcceptanceMap.get(joinAcceptanceEnum);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_AN_ACCEPTANCE_TYPE JoinAcceptanceEnum.
            return JoinAcceptanceEnum.NOT_AN_ACCEPTANCE_TYPE;
        } else {
            //Otherwise, return the JoinAcceptanceEnum.
            return type;
        }

    }



}
