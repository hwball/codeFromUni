package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;

import java.util.Observer;

/**
 * Command that can observe the race
 */
public abstract class ObserverCommand implements Command, Observer {
    MockRace race;
    MockBoat boat;

    public ObserverCommand(MockRace race, MockBoat boat) {
        this.race = race;
        this.boat = boat;
        boat.setAutoVMG(false);
    }
}
