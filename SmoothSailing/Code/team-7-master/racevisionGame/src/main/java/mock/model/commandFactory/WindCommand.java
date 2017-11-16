package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;
import shared.model.Bearing;

import java.util.Observable;

/**
 * Command class for upwind and downwind controls
 */
public class WindCommand extends ObserverCommand {
    private int direction;

    /**
     * Constructor for class
     * @param race race context
     * @param boat boat controlled by command
     * @param upwind if true, downwind if false
     */
    public WindCommand(MockRace race, MockBoat boat, boolean upwind) {
        super(race, boat);
        race.addAngularCommand(this, boat.getSourceID());
        this.direction = upwind? -1 : 1;
    }

    @Override
    public void execute() {

        boat.setAutoVMG(false);

        double wind = race.getWindDirection().degrees();
        double heading = boat.getBearing().degrees();

        double offset = 3.0;

        offset *= direction;
        double headWindDelta = wind - heading;
        if ((headWindDelta < 0) || (headWindDelta > 180)) offset *= -1;

        boat.setBearing(Bearing.fromDegrees(heading + offset));
    }

    @Override
    public void update(Observable o, Object arg) {
        race.deleteObserver(this);
    }
}
