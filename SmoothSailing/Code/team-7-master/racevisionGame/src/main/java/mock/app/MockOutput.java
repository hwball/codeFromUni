package mock.app;



import network.Messages.*;
import shared.model.RunnableWithFramePeriod;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP server to send race information to connected clients.
 */
public class MockOutput implements RunnableWithFramePeriod {

    /**
     * A queue to send messages to client.
     */
    private BlockingQueue<AC35Data> outgoingMessages;


    /**
     * An object containing the set of latest messages to send.
     * Every server frame, MockOutput reads messages from this, and send them.
     */
    private LatestMessages latestMessages;

    //These sequence number track the last race/boat/regatta xml message we've sent.
    private int lastSentRaceNumber = -1;

    private int lastSentBoatNumber = -1;

    private int lastSentRegattaNumber = -1;




    /**
     * Ctor.
     * @param latestMessages Latest Messages that the Mock is to send out
     * @param outgoingMessages A queue to place outgoing messages on.
     */
    public MockOutput(LatestMessages latestMessages, BlockingQueue<AC35Data> outgoingMessages) {
        this.outgoingMessages = outgoingMessages;
        this.latestMessages = latestMessages;
    }



    /**
     * Sending loop of the Server
     */
    public void run() {


        //Wait until all of the xml files have been set.
        while (!this.latestMessages.hasAllXMLMessages()) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //If we get interrupted, exit the function.
                Logger.getGlobal().log(Level.WARNING, "MockOutput.run().sleep(waitForXMLs) was interrupted on thread: " + Thread.currentThread(), e);

                //Re-set the interrupt flag.
                Thread.currentThread().interrupt();
                return;

            }
        }

        long previousFrameTime = System.currentTimeMillis();



        while (!Thread.interrupted()) {

            try {

                long currentFrameTime = System.currentTimeMillis();
                waitForFramePeriod(previousFrameTime, currentFrameTime, 16);
                previousFrameTime = currentFrameTime;



                //Send XML messages if needed.

                if (lastSentRaceNumber != latestMessages.getRaceXMLMessage().getSequenceNumber()) {
                    lastSentRaceNumber = latestMessages.getRaceXMLMessage().getSequenceNumber();
                    outgoingMessages.put(latestMessages.getRaceXMLMessage());
                }

                if (lastSentBoatNumber != latestMessages.getBoatXMLMessage().getSequenceNumber()) {
                    lastSentBoatNumber = latestMessages.getBoatXMLMessage().getSequenceNumber();
                    outgoingMessages.put(latestMessages.getBoatXMLMessage());
                }

                if (lastSentRegattaNumber != latestMessages.getRegattaXMLMessage().getSequenceNumber()) {
                    lastSentRegattaNumber = latestMessages.getRegattaXMLMessage().getSequenceNumber();
                    outgoingMessages.put(latestMessages.getRegattaXMLMessage());
                }



                List<AC35Data> snapshot = latestMessages.getSnapshot();
                for (AC35Data message : snapshot) {
                    outgoingMessages.put(message);
                }

            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.WARNING, "MockOutput.run() interrupted while putting message in queue.", e);
                Thread.currentThread().interrupt();
                return;
            }

        }

    }


}
