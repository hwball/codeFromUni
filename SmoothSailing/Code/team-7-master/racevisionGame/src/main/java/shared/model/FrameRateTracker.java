package shared.model;


import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * This class is used to track the framerate of something.
 * Use {@link #incrementFps(long)} to update it, and {@link #fpsProperty()} to observe it.
 */
public class FrameRateTracker {


    /**
     * The number of frames per second.
     * We essentially track the number of frames generated per second, over a one second period. When {@link #lastFpsResetTime} reaches 1 second, {@link #currentFps} is reset.
     */
    private int currentFps = 0;

    /**
     * The number of frames per second we generated over the last 1 second period.
     */
    private IntegerProperty lastFps = new SimpleIntegerProperty(0);

    /**
     * The time, in milliseconds, since we last reset our {@link #currentFps} counter.
     */
    private long lastFpsResetTime;


    /**
     * Creates a {@link FrameRateTracker}. Use {@link #incrementFps(long)} to update it, and {@link #fpsProperty()} to observe it.
     */
    public FrameRateTracker() {
        timer.start();
    }


    /**
     * Returns the number of frames generated per second.
     * @return Frames per second.
     */
    public int getFps() {
        return lastFps.getValue();
    }

    /**
     * Returns the fps property.
     * @return The fps property.
     */
    public IntegerProperty fpsProperty() {
        return  lastFps;
    }


    /**
     * Increments the FPS counter, and adds timePeriod milliseconds to our FPS reset timer.
     * @param timePeriod Time, in milliseconds, to add to {@link #lastFpsResetTime}.
     */
    private void incrementFps(long timePeriod) {
        //Increment.
        this.currentFps++;

        //Add period to timer.
        this.lastFpsResetTime += timePeriod;

        //If we have reached 1 second period, snapshot the framerate and reset.
        if (this.lastFpsResetTime > 1000) {
            this.lastFps.set(this.currentFps);

            this.currentFps = 0;
            this.lastFpsResetTime = 0;
        }
    }


    /**
     * Timer used to update the framerate.
     * This is used because we care about frames in the javaFX thread.
     */
    private AnimationTimer timer = new AnimationTimer() {

        long previousFrameTime = System.currentTimeMillis();

        @Override
        public void handle(long now) {

            long currentFrameTime = System.currentTimeMillis();
            long framePeriod = currentFrameTime - previousFrameTime;

            //Increment fps.
            incrementFps(framePeriod);

            previousFrameTime = currentFrameTime;
        }
    };


    /**
     * Stops the {@link FrameRateTracker}'s timer.
     */
    public void stop() {
        timer.stop();
    }


}
