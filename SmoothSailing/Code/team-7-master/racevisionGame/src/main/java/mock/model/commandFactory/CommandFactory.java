package mock.model.commandFactory;

import mock.exceptions.CommandConstructionException;
import mock.model.MockBoat;
import mock.model.MockRace;
import network.Messages.BoatAction;
import shared.exceptions.BoatNotFoundException;

/**
 * Factory class for Command objects
 */
public class CommandFactory {
    /**
     * Generates a command on a race and boat corresponding to the protocol action number.
     * @param race to receive command
     * @param action number to select command
     * @return The command to execute the given action.
     * @throws CommandConstructionException Thrown if the command cannot be constructed (e.g., unknown action type).
     */
    public static Command createCommand(MockRace race, BoatAction action) throws CommandConstructionException {

        MockBoat boat = null;
        try {
            boat = race.getBoat(action.getSourceID());

        } catch (BoatNotFoundException e) {
            throw new CommandConstructionException("Could not create command for BoatAction: " + action + ". Boat with sourceID: " + action.getSourceID() + " not found.", e);

        }

        switch(action.getBoatAction()) {
            case VMG: return new VMGCommand(race, boat);
            case TACK_GYBE: return new TackGybeCommand(race, boat);
            case UPWIND: return new WindCommand(race, boat, true);
            case DOWNWIND: return new WindCommand(race, boat, false);
            case SAILS_OUT: return new SailsCommand(race, boat, true);
            case SAILS_IN: return new SailsCommand(race, boat, false);
            case ZOOM_IN: return null;
            case ZOOM_OUT: return null;

            default: throw new CommandConstructionException("Could not create command for BoatAction: " + action + ". Unknown BoatAction.");
        }
    }
}
