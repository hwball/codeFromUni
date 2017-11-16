package visualiser.Commands.VisualiserRaceCommands;

import mock.model.commandFactory.Command;
import network.Messages.BoatLocation;
import network.Messages.Enums.BoatStatusEnum;
import shared.exceptions.BoatNotFoundException;
import shared.exceptions.MarkNotFoundException;
import shared.model.GPSCoordinate;
import shared.model.Mark;
import visualiser.model.ThisBoat;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceState;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Command created when a {@link BoatLocation} message is received.
 */
public class BoatLocationCommand implements Command {

    /**
     * The message to operate on.
     */
    private BoatLocation boatLocation;

    /**
     * The context to operate on.
     */
    private VisualiserRaceState visualiserRace;


    /**
     * Creates a new {@link BoatLocationCommand}, which operates on a given {@link VisualiserRaceState}.
     * @param boatLocation The message to operate on.
     * @param visualiserRace The context to operate on.
     */
    public BoatLocationCommand(BoatLocation boatLocation, VisualiserRaceState visualiserRace) {
        this.boatLocation = boatLocation;
        this.visualiserRace = visualiserRace;
    }



    @Override
    public void execute() {

        if (visualiserRace.isVisualiserBoat(boatLocation.getSourceID())) {
            updateBoatLocation();
        } else if (visualiserRace.isMark(boatLocation.getSourceID())) {
            updateMarkLocation();
        }

    }


    /**
     * Updates the boat specified in the message.
     */
    private void updateBoatLocation() {

        try {
            VisualiserBoat boat = visualiserRace.getBoat(boatLocation.getSourceID());

            //Get the new position.
            GPSCoordinate gpsCoordinate = new GPSCoordinate(
                    boatLocation.getLatitude(),
                    boatLocation.getLongitude());

            boat.setPosition(gpsCoordinate);

            //Bearing.
            boat.setBearing(boatLocation.getHeading());


            //Speed.
            boat.setCurrentSpeed(boatLocation.getBoatSpeedKnots());


            //Attempt to add a track point.
            attemptAddTrackPoint(boat);


        } catch (BoatNotFoundException e) {
            Logger.getGlobal().log(Level.WARNING, "BoatLocationCommand: " + this + " could not execute. Boat with sourceID: " + boatLocation.getSourceID() + " not found.", e);
            return;
        }

    }


    /**
     * Attempts to add a track point to the boat. Only works if the boat is currently racing.
     * @param boat The boat to add a track point to.
     */
    private void attemptAddTrackPoint(VisualiserBoat boat) {
        if (boat.getStatus() == BoatStatusEnum.RACING) {
            boat.addTrackPoint(boat.getPosition(), visualiserRace.getRaceClock().getCurrentTime());
        }
    }


    /**
     * Updates the marker boat specified in message.
     */
    private void updateMarkLocation() {

        try {
            Mark mark = visualiserRace.getMark(boatLocation.getSourceID());

            GPSCoordinate gpsCoordinate = new GPSCoordinate(
                    boatLocation.getLatitude(),
                    boatLocation.getLongitude());

            mark.setPosition(gpsCoordinate);
        } catch (MarkNotFoundException e) {
            Logger.getGlobal().log(Level.WARNING, "BoatLocationCommand: " + this + " could not execute. Mark with sourceID: " + boatLocation.getSourceID() + " not found.", e);
            return;

        }



    }

}
