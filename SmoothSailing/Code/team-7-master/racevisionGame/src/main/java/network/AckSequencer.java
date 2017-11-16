package network;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Common source of ack numbers for all messages
 */
public class AckSequencer {
    /**
     * Generator for ack numbers
     */
    private static AtomicInteger ackNum = new AtomicInteger(0);

    /**
     * Retrieve next ack number
     * @return next ack number
     */
    public static int getNextAckNum() {
        return ackNum.getAndIncrement();
    }
}
