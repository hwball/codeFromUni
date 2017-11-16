package visualiser.Commands.VisualiserRaceCommands;

import mock.model.commandFactory.Command;
import network.Messages.AssignPlayerBoat;
import network.Messages.BoatLocation;
import shared.exceptions.BoatNotFoundException;
import shared.exceptions.MarkNotFoundException;
import shared.model.GPSCoordinate;
import shared.model.Mark;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceState;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Command created when a {@link AssignPlayerBoat} message is received.
 */
public class AssignPlayerBoatCommand implements Command {

    /**
     * The message to operate on.
     */
    private AssignPlayerBoat assignPlayerBoat;

    /**
     * The context to operate on.
     */
    private VisualiserRaceState visualiserRace;


    /**
     * Creates a new {@link AssignPlayerBoatCommand}, which operates on a given {@link VisualiserRaceState}.
     * @param assignPlayerBoat The message to operate on.
     * @param visualiserRace The context to operate on.
     */
    public AssignPlayerBoatCommand(AssignPlayerBoat assignPlayerBoat, VisualiserRaceState visualiserRace) {
        this.assignPlayerBoat = assignPlayerBoat;
        this.visualiserRace = visualiserRace;
    }



    @Override
    public void execute() {

        visualiserRace.setPlayerBoatID(assignPlayerBoat.getSourceID());

    }


}
