package visualiser.Commands.VisualiserRaceCommands;

import mock.model.commandFactory.Command;
import network.Messages.BoatState;
import shared.exceptions.BoatNotFoundException;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceState;

/**
 * Updates boats on visualiser when their health changes
 */
public class BoatStateCommand implements Command {
    private BoatState boatState;
    private VisualiserRaceState visualiserRace;

    public BoatStateCommand(BoatState boatState, VisualiserRaceState visualiserRace) {
        this.boatState = boatState;
        this.visualiserRace = visualiserRace;
    }

    @Override
    public void execute() {
        try {
            VisualiserBoat boat = visualiserRace.getBoat(boatState.getSourceID());
            boat.setHealth(boatState.getBoatHealth());
        } catch (BoatNotFoundException e) {
            // Fail silently
        }
    }
}
