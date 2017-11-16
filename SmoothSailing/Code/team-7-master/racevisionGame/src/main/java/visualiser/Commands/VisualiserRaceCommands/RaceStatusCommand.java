package visualiser.Commands.VisualiserRaceCommands;

import javafx.scene.media.AudioClip;
import mock.model.commandFactory.Command;
import network.Messages.BoatStatus;
import network.Messages.Enums.BoatStatusEnum;
import network.Messages.RaceStatus;
import shared.exceptions.BoatNotFoundException;
import shared.model.Leg;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Command created when a {@link RaceStatus} message is received.
 */
public class RaceStatusCommand implements Command {

    /**
     * The message to operate on.
     */
    private RaceStatus raceStatus;

    /**
     * The context to operate on.
     */
    private VisualiserRaceState visualiserRace;


    /**
     * Creates a new {@link RaceStatusCommand}, which operates on a given {@link VisualiserRaceState}.
     * @param raceStatus The message to operate on.
     * @param visualiserRace The context to operate on.
     */
    public RaceStatusCommand(RaceStatus raceStatus, VisualiserRaceState visualiserRace) {
        this.raceStatus = raceStatus;
        this.visualiserRace = visualiserRace;
    }



    @Override
    public void execute() {

        //Race status enum.
        visualiserRace.setRaceStatusEnum(raceStatus.getRaceStatus());

        //Wind.
        visualiserRace.setWind(
                raceStatus.getWindDirection(),
                raceStatus.getWindSpeed()  );

        //Current race time.
        visualiserRace.getRaceClock().setUTCTime(raceStatus.getCurrentTime());



        for (BoatStatus boatStatus : raceStatus.getBoatStatuses()) {
            updateBoatStatus(boatStatus);
        }


        visualiserRace.updateBoatPositions(visualiserRace.getBoats());



    }


    /**
     * Updates a single boat's status using the boatStatus message.
     * @param boatStatus BoatStatus message to get data from.
     */
    private void updateBoatStatus(BoatStatus boatStatus) {
        try {
            VisualiserBoat boat = visualiserRace.getBoat(boatStatus.getSourceID());

            //Time at next mark.
            updateEstimatedTimeAtNextMark(boatStatus, boat);


            BoatStatusEnum newBoatStatusEnum = boatStatus.getBoatStatus();

            //Time at last mark.
            initialiseTimeAtLastMark(boat, boat.getStatus(), newBoatStatusEnum);

            //Status.
            boat.setStatus(newBoatStatusEnum);


            List<Leg> legs = visualiserRace.getLegs();

            //Leg.
            updateLeg(boatStatus.getLegNumber(), boat, legs);


            //Set finish time if boat finished.
            attemptUpdateFinishTime(boatStatus, boat, legs);


        } catch (BoatNotFoundException e) {
            //Logger.getGlobal().log(Level.WARNING, "RaceStatusCommand.updateBoatStatus: " + this + " could not execute. Boat with sourceID: " + boatStatus.getSourceID() + " not found.", e);
            return;
        }
    }


    /**
     * Attempts to update the finish time of the boat. Only works if the boat has actually finished the race.
     * @param boatStatus BoatStatus to read data from.
     * @param boat Boat to update.
     * @param legs Legs of the race.
     */
    private void attemptUpdateFinishTime(BoatStatus boatStatus, VisualiserBoat boat, List<Leg> legs) {

        if (boat.getStatus() == BoatStatusEnum.FINISHED || boatStatus.getLegNumber() == legs.size()) {
            boat.setTimeFinished(visualiserRace.getRaceClock().getCurrentTimeMilli());
            boat.setStatus(BoatStatusEnum.FINISHED);
        }

    }



    /**
     * Updates a boat's leg.
     * @param legNumber The new leg number.
     * @param boat The boat to update.
     * @param legs The legs in the race.
     */
    private void updateLeg(int legNumber, VisualiserBoat boat, List<Leg> legs) {

        if (legNumber >= 1 && legNumber < legs.size()) {
            if (boat.getCurrentLeg() != legs.get(legNumber)) {
                boatFinishedLeg(boat, legs.get(legNumber));
            }
        }
    }

    /**
     * Initialises the time at last mark for a boat. Only changes if the boat's status is changing from non-racing to racing.
     * @param boat The boat to update.
     * @param currentBoatStatus The current status of the boat.
     * @param newBoatStatusEnum The new status of the boat, from the BoatStatus message.
     */
    private void initialiseTimeAtLastMark(VisualiserBoat boat, BoatStatusEnum currentBoatStatus, BoatStatusEnum newBoatStatusEnum) {
        //If we are changing from non-racing to racing, we need to initialise boat with their time at last mark.
        if ((currentBoatStatus != BoatStatusEnum.RACING) && (newBoatStatusEnum == BoatStatusEnum.RACING)) {
            boat.setTimeAtLastMark(visualiserRace.getRaceClock().getCurrentTime());
        }
    }


    /**
     * Updates the estimated time at next mark for a given boat.
     * @param boatStatus BoatStatus to read data from.
     * @param boat Boat to update.
     */
    private void updateEstimatedTimeAtNextMark(BoatStatus boatStatus, VisualiserBoat boat) {
        boat.setEstimatedTimeAtNextMark(visualiserRace.getRaceClock().getLocalTime(boatStatus.getEstTimeAtNextMark()));
    }



    /**
     * Updates a boat's leg to a specified leg. Also records the order in which the boat passed the leg.
     * @param boat The boat to update.
     * @param leg The leg to use.
     */
    private void boatFinishedLeg(VisualiserBoat boat, Leg leg) {

        //Record order in which boat finished leg.
        visualiserRace.getLegCompletionOrder().get(boat.getCurrentLeg()).add(boat);

        //play sound
        AudioClip sound = new AudioClip(getClass().getResource("/visualiser/sounds/passmark.wav").toExternalForm());
        sound.play();

        //Update boat.
        boat.setCurrentLeg(leg);
        boat.setTimeAtLastMark(visualiserRace.getRaceClock().getCurrentTime());

    }

}
