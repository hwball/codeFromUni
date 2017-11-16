package mock.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The states in which a connection to a client may have.
 */
public enum ConnectionStateEnum {

    UNKNOWN(0),

    /**
     * We're waiting for the client to complete the joining handshake (see {@link network.Messages.RequestToJoin}.
     */
    WAITING_FOR_HANDSHAKE(1),

    /**
     * The server has receved a {@link network.Messages.RequestToJoin} from the client.
     */
    REQUEST_RECEIVED(2),

    /**
     * The client has completed the handshake, and is connected.
     * That is, the client sent a {@link network.Messages.RequestToJoin}, which was successful, and the server responded with a {@link network.Messages.JoinAcceptance}.
     */
    CONNECTED(3),

    /**
     * The client has timed out.
     */
    TIMED_OUT(4),

    /**
     * The client's connection has been declined.
     */
    DECLINED(5);




    private byte value;

    /**
     * Ctor. Creates a ConnectionStateEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private ConnectionStateEnum(int value) {
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
     * Stores a mapping between Byte values and ConnectionStateEnum values.
     */
    private static final Map<Byte, ConnectionStateEnum> byteToStatusMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStatusMap.
     */
    static {
        for (ConnectionStateEnum type : ConnectionStateEnum.values()) {
            ConnectionStateEnum.byteToStatusMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param connectionState Byte value to convert to a ConnectionStateEnum value.
     * @return The ConnectionStateEnum value which corresponds to the given byte value.
     */
    public static ConnectionStateEnum fromByte(byte connectionState) {
        //Gets the corresponding MessageType from the map.
        ConnectionStateEnum type = ConnectionStateEnum.byteToStatusMap.get(connectionState);

        if (type == null) {
            //If the byte value wasn't found, return the UNKNOWN connectionState.
            return ConnectionStateEnum.UNKNOWN;
        } else {
            //Otherwise, return the connectionState.
            return type;
        }

    }
}
