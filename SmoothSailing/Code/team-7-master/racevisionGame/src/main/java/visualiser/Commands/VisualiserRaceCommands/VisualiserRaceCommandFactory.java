package visualiser.Commands.VisualiserRaceCommands;


import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import network.Messages.*;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;

/**
 * Factory to create VisualiserRace commands.
 */
public class VisualiserRaceCommandFactory {

    /**
     * Generates a command on an VisualiserRace.
     * @param message The message to turn into a command.
     * @param visualiserRace The context for the command to operate on.
     * @return The command to execute the given action.
     * @throws CommandConstructionException Thrown if the command cannot be constructed.
     */
    public static Command create(AC35Data message, VisualiserRaceState visualiserRace) throws CommandConstructionException {

        switch (message.getType()) {

            case BOATLOCATION:
                return new BoatLocationCommand((BoatLocation) message, visualiserRace);

            case RACESTATUS: return new RaceStatusCommand((RaceStatus) message, visualiserRace);

            case XMLMESSAGE: return XMLMessageCommandFactory.create((XMLMessage) message, visualiserRace);

            case ASSIGN_PLAYER_BOAT: return new AssignPlayerBoatCommand((AssignPlayerBoat) message, visualiserRace);

            case YACHTEVENTCODE:
                return new BoatCollisionCommand((YachtEvent) message, visualiserRace);

            case BOATSTATE:
                return new BoatStateCommand((BoatState) message, visualiserRace);

            default: throw new CommandConstructionException("Could not create VisualiserRaceCommand. Unrecognised or unsupported MessageType: " + message.getType());

        }

    }

}
