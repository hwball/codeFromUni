package visualiser.network;

import mock.model.commandFactory.Command;
import shared.model.RunnableWithFramePeriod;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Tracks the heart beat status of a connection.
 */
public class IncomingHeartBeatService implements RunnableWithFramePeriod {


    /**
     * Timestamp of the last sent heartbeat message.
     */
    private long lastHeartbeatTime;


    /**
     * Sequence number for heartbeat messages.
     */
    private long lastHeartBeatSeqNum;


    /**
     * The incoming commands to execute.
     */
    private BlockingQueue<Command> incomingCommands;



    /**
     * Creates an {@link IncomingHeartBeatService} which executes commands from a given queue.
     * @param incomingCommands Queue to read and execute commands from.
     */
    public IncomingHeartBeatService(BlockingQueue<Command> incomingCommands) {
        this.incomingCommands = incomingCommands;

        this.lastHeartbeatTime = System.currentTimeMillis();
        this.lastHeartBeatSeqNum = -1;
    }


    /**
     * Sets the last heart beat time to a given value.
     * @param lastHeartbeatTime Timestamp of heartbeat.
     */
    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    /**
     * Sets the last heart beat sequence number to a given value.
     * @param lastHeartBeatSeqNum Sequence number of heartbeat.
     */
    public void setLastHeartBeatSeqNum(long lastHeartBeatSeqNum) {
        this.lastHeartBeatSeqNum = lastHeartBeatSeqNum;
    }



    /**
     * Calculates the time since last heartbeat, in milliseconds.
     *
     * @return Time since last heartbeat, in milliseconds..
     */
    private long timeSinceHeartbeat() {
        long now = System.currentTimeMillis();
        return (now - lastHeartbeatTime);
    }


    /**
     * Returns whether or not the heartBeat service considers the connection "alive".
     * Going 10,000ms without receiving a heartBeat means that the connection is "dead".
     * @return True if alive, false if dead.
     */
    public boolean isAlive() {
        long heartBeatPeriod = 10000;

        return (timeSinceHeartbeat() < heartBeatPeriod);
    }



    @Override
    public void run() {


        while (!Thread.interrupted()) {

            try {
                Command command = incomingCommands.take();
                command.execute();

            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, "IncomingHeartBeatService: " + this + " was interrupted on thread: " + Thread.currentThread() + " while reading command.", e);
                Thread.currentThread().interrupt();

            }

        }

    }
}
