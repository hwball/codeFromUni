package mock.model;

import network.Messages.AC35Data;
import network.Messages.HeartBeat;
import shared.model.RunnableWithFramePeriod;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is responsible for sending {@link HeartBeat} messages to queue.
 */
public class HeartBeatService implements RunnableWithFramePeriod {

    /**
     * Timestamp of the last sent heartbeat message.
     */
    private long lastHeartbeatTime;

    /**
     * Period for the heartbeat - that is, how often we send it. Milliseconds.
     */
    private long heartbeatPeriod = 2500;


    /**
     * The messages we're writing to the stream.
     */
    private BlockingQueue<AC35Data> messagesToSend;



    /**
     * Sequence number for heartbeat messages.
     */
    private int heartbeatSequenceNum = 1;


    /**
     * Constructs a new HeartBeatService to send heartBeat messages to a given outputStream.
     * @param messagesToSend The queue to send heartBeat messages to.
     */
    public HeartBeatService(BlockingQueue<AC35Data> messagesToSend) {
        this.messagesToSend = messagesToSend;
        this.lastHeartbeatTime = System.currentTimeMillis();
    }




    /**
     * Increments the {@link #heartbeatSequenceNum} value, and returns it.
     * @return Incremented heat beat number.
     */
    private int getNextHeartBeatNumber(){
        this.heartbeatSequenceNum++;

        return this.heartbeatSequenceNum;
    }



    /**
     * Generates the next heartbeat message and returns it. Increments the heartbeat sequence number.
     * @return The next heartbeat message.
     */
    private HeartBeat createHeartbeatMessage() {

        HeartBeat heartBeat = new HeartBeat(getNextHeartBeatNumber());

        return heartBeat;
    }


    /**
     * Puts a HeartBeat message on the message queue.
     * @throws InterruptedException Thrown if the thread is interrupted.
     */
    private void sendHeartBeat() throws Exception {

        HeartBeat heartBeat = createHeartbeatMessage();

        messagesToSend.put(heartBeat);
    }



    @Override
    public void run() {

        while (!Thread.interrupted()) {
            long currentFrameTime = System.currentTimeMillis();
            waitForFramePeriod(lastHeartbeatTime, currentFrameTime, heartbeatPeriod);

            lastHeartbeatTime = currentFrameTime;

            try {
                sendHeartBeat();

            } catch (Exception e) {
                Logger.getGlobal().log(Level.WARNING, "HeartBeatService: " + this + " sendHeartBeat() was interrupted on thread: " + Thread.currentThread(), e);
                Thread.currentThread().interrupt();
                return;

            }
        }

    }
}
