package visualiser.model;

import javafx.beans.property.IntegerProperty;
import mock.model.commandFactory.CompositeCommand;
import shared.model.FrameRateTracker;
import shared.model.RunnableWithFramePeriod;


/**
 * Handles updating a {@link VisualiserRaceState} with incoming commands.
 */
public class VisualiserRaceService implements RunnableWithFramePeriod {


    /**
     * The race state to update.
     */
    private VisualiserRaceState visualiserRaceState;


    /**
     * A composite commands to execute to update the race.
     */
    private CompositeCommand raceCommands;


    /**
     * Used to track the framerate of the "simulation".
     */
    private FrameRateTracker frameRateTracker;






    /**
     * Constructs a visualiser race which models a yacht race, and is modified by CompositeCommand.
     * @param raceCommands A composite commands to execute to update the race.
     * @param visualiserRaceState The race state to update.
     */
    public VisualiserRaceService(CompositeCommand raceCommands, VisualiserRaceState visualiserRaceState) {
        this.raceCommands = raceCommands;
        this.visualiserRaceState = visualiserRaceState;

        this.frameRateTracker = new FrameRateTracker();
    }



    /**
     * Returns the CompositeCommand executed by the race.
     * @return CompositeCommand executed by race.
     */
    public CompositeCommand getRaceCommands() {
        return raceCommands;
    }





    @Override
    public void run() {

        long previousFrameTime = System.currentTimeMillis();

        while (!Thread.interrupted()) {

            long currentFrameTime = System.currentTimeMillis();
            waitForFramePeriod(previousFrameTime, currentFrameTime, 16);
            previousFrameTime = currentFrameTime;


            raceCommands.execute();

        }

        frameRateTracker.stop();

    }


    /**
     * Returns the framerate property of the race.
     * @return Framerate property of race.
     */
    public IntegerProperty getFrameRateProperty() {
        return frameRateTracker.fpsProperty();
    }
}
