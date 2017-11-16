package network.Messages;


import network.Messages.Enums.MessageType;

/**
 * Represents a Heartbeat message.
 */
public class HeartBeat extends AC35Data {

    /**
     * Sequence number of the heartbeat.
     */
    private long sequenceNumber;

    /**
     * Ctor.
     * @param sequenceNumber Sequence number of the heartbeat.
     */
    public HeartBeat(long sequenceNumber) {
        super(MessageType.HEARTBEAT);
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns the sequence number of this heartbeat message.
     * @return Sequence number of this heartbeat message.
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }
}
