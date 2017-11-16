package network.StreamRelated;


import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.AC35Data;
import shared.model.RunnableWithFramePeriod;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for writing a queue of {@link network.Messages.AC35Data} messages to an output stream.
 */
public class MessageSerialiser implements RunnableWithFramePeriod {


    /**
     * The stream we're writing to.
     */
    private DataOutputStream outputStream;

    /**
     * The messages we're writing to the stream.
     */
    private BlockingQueue<AC35Data> messagesToSend;


    /**
     * Ack numbers used in messages.
     */
    private int ackNumber = 1;

    /**
     * Determines whether or not this runnable is currently running.
     */
    private boolean isRunning;



    /**
     * Constructs a new MessageSerialiser to write a queue of messages to a given stream.
     * @param outputStream The stream to write to.
     * @param messagesToSend The messages to send.
     */
    public MessageSerialiser(OutputStream outputStream, BlockingQueue<AC35Data> messagesToSend) {
        this.outputStream = new DataOutputStream(outputStream);
        this.messagesToSend = messagesToSend;
    }

    /**
     * Returns the queue of messages to write to the socket.
     * @return Queue of messages to write to the socket.
     */
    public BlockingQueue<AC35Data> getMessagesToSend() {
        return messagesToSend;
    }


    /**
     * Increments the ackNumber value, and returns it.
     * @return Incremented ackNumber.
     */
    private int getNextAckNumber(){
        this.ackNumber++;

        return this.ackNumber;
    }

    /**
     * Determines whether or not this runnable is running.
     * @return True means that it is still running, false means that it has stopped.
     */
    public boolean isRunning() {
        return isRunning;
    }


    @Override
    public void run() {

        long previousFrameTime = System.currentTimeMillis();

        isRunning = true;

        while (!Thread.interrupted()) {


            long currentFrameTime = System.currentTimeMillis();
            waitForFramePeriod(previousFrameTime, currentFrameTime, 16);
            previousFrameTime = currentFrameTime;


            //Send the messages.
            List<AC35Data> messages = new ArrayList<>();
            messagesToSend.drainTo(messages);

            for (AC35Data message : messages) {
                try {
                    byte[] messageBytes = RaceVisionByteEncoder.encodeBinaryMessage(message, getNextAckNumber());

                    outputStream.write(messageBytes);


                } catch (InvalidMessageException e) {
                    Logger.getGlobal().log(Level.WARNING, "Could not encode message: " + message, e);

                } catch (IOException e) {
                    Logger.getGlobal().log(Level.SEVERE, "Could not write message to outputStream: " + outputStream + " on thread: " + Thread.currentThread(), e);
                    isRunning = false;
                    return;

                }
            }

        }

    }
}
