package shared.model;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This interface is a {@link Runnable} interface, with the ability to sleep until a given time period has elapsed.
 */
public interface RunnableWithFramePeriod extends Runnable {






    /**
     * Waits for enough time for the period of this frame to be greater than minimumFramePeriod.
     * @param previousFrameTime The timestamp of the previous frame.
     * @param currentFrameTime The timestamp of the current frame.
     * @param minimumFramePeriod The minimum period the frame must be.
     */
    default void waitForFramePeriod(long previousFrameTime, long currentFrameTime, long minimumFramePeriod) {

        //This is the time elapsed, in milliseconds, since the last server "frame".
        long framePeriod = currentFrameTime - previousFrameTime;

        //We only attempt to send packets every X milliseconds.
        if (framePeriod >= minimumFramePeriod) {
            return;

        } else {
            //Wait until the frame period will be large enough.
            long timeToWait = minimumFramePeriod - framePeriod;

            try {
                Thread.sleep(timeToWait);

            } catch (InterruptedException e) {
                //If we get interrupted, exit the function.
                //Logger.getGlobal().log(Level.WARNING, "RunnableWithFramePeriod.waitForFramePeriod().sleep(framePeriod) was interrupted on thread: " + Thread.currentThread(), e);
                //Re-set the interrupt flag.
                Thread.currentThread().interrupt();
                return;

            }

        }

    }


}
