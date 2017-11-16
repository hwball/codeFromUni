package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;

import java.util.Observer;

/**
 * Allows RaceLogic to control MockRace state according to the Command pattern
 */
public interface Command {
    /**
     * Execute command - standard method name in pattern
     */
    void execute();
}
