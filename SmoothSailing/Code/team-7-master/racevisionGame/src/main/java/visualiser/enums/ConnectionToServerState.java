package visualiser.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The states in which a connection from a client to a server may have.
 */
public enum ConnectionToServerState {

    UNKNOWN(0),

    /**
     * We're waiting for the server to complete the joining handshake.
     * See {@link network.Messages.RequestToJoin} and {@link network.Messages.JoinAcceptance}.
     */
    REQUEST_SENT(1),

    /**
     * The client has receved a {@link network.Messages.JoinAcceptance} from the server.
     */
    RESPONSE_RECEIVED(2),

    /**
     * The server has completed the handshake, and is connected.
     * That is, the client sent a {@link network.Messages.RequestToJoin}, which was successful, and the server responded with a {@link network.Messages.JoinAcceptance}.
     */
    CONNECTED(3),

    /**
     * The server has timed out, or the connection has been interrupted.
     */
    TIMED_OUT(4),

    /**
     * The client's connection has been declined.
     */
    DECLINED(5);




    private byte value;

    /**
     * Ctor. Creates a ConnectionToServerState from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private ConnectionToServerState(int value) {
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
     * Stores a mapping between Byte values and ConnectionToServerState values.
     */
    private static final Map<Byte, ConnectionToServerState> byteToStateMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToStateMap.
     */
    static {
        for (ConnectionToServerState type : ConnectionToServerState.values()) {
            ConnectionToServerState.byteToStateMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param connectionState Byte value to convert to a ConnectionToServerState value.
     * @return The ConnectionToServerState value which corresponds to the given byte value.
     */
    public static ConnectionToServerState fromByte(byte connectionState) {
        //Gets the corresponding ConnectionToServerState from the map.
        ConnectionToServerState type = ConnectionToServerState.byteToStateMap.get(connectionState);

        if (type == null) {
            //If the byte value wasn't found, return the UNKNOWN ConnectionToServerState.
            return ConnectionToServerState.UNKNOWN;
        } else {
            //Otherwise, return the ConnectionToServerState.
            return type;
        }

    }
}
