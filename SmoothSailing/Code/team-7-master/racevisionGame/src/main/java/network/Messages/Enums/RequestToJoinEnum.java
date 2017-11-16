package network.Messages.Enums;


import java.util.HashMap;
import java.util.Map;

/**
 * This enum encapsulates the different ways in which a client may wish to connect to a server.
 */
public enum RequestToJoinEnum {


    /**
     * Client wants to spectate.
     */
    SPECTATOR(0),

    /**
     * Client wants to participate.
     */
    PARTICIPANT(1),

    /**
     * Client wants to play the tutorial.
     */
    CONTROL_TUTORIAL(2),

    /**
     * Client wants to particpate as a ghost.
     */
    GHOST(3),


    /**
     * Used to indicate that a given byte value is invalid.
     */
    NOT_A_REQUEST_TYPE(-1);


    /**
     * Primitive value of the enum.
     */
    private int value;


    /**
     * Ctor. Creates a RequestToJoinEnum from a given int value.
     * @param value Integer to construct from.
     */
    private RequestToJoinEnum(int value) {
        this.value = value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public int getValue() {
        return value;
    }




    /**
     * Stores a mapping between Integer values and RequestToJoinEnum values.
     */
    private static final Map<Integer, RequestToJoinEnum> intToRequestMap = new HashMap<>();


    /*
      Static initialization block. Initializes the intToRequestMap.
     */
    static {
        for (RequestToJoinEnum type : RequestToJoinEnum.values()) {
            RequestToJoinEnum.intToRequestMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given int value.
     * @param requestToJoinEnum int value to convert to a RequestToJoinEnum value.
     * @return The RequestToJoinEnum value which corresponds to the given int value.
     */
    public static RequestToJoinEnum fromInt(int requestToJoinEnum) {
        //Gets the corresponding MessageType from the map.
        RequestToJoinEnum type = RequestToJoinEnum.intToRequestMap.get(requestToJoinEnum);

        if (type == null) {
            //If the int value wasn't found, return the NOT_A_REQUEST_TYPE RequestToJoinEnum.
            return RequestToJoinEnum.NOT_A_REQUEST_TYPE;
        } else {
            //Otherwise, return the RequestToJoinEnum.
            return type;
        }

    }



}
