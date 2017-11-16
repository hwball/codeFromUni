package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;
import mock.model.NewPolars;
import mock.model.VMG;
import shared.model.Bearing;

import java.util.Observable;

/**
 * Command class for autoVMG
 */
public class VMGCommand extends ObserverCommand {
    private double goalAngle;
    private double goalRotation;
    private double totalRotation = 0;
    private int direction;

    /**
     * Constructor for class
     * @param race mock race
     * @param boat mock boat to update
     */
    public VMGCommand(MockRace race, MockBoat boat) {
        super(race, boat);
        race.addAngularCommand(this, boat.getSourceID());
    }

    @Override
    public void execute() {
        if (boat.getAutoVMG()){
            boat.setAutoVMG(false);
        } else {
            boat.setAutoVMG(true);
        }
        newOptimalVMG(boat);

        goalRotation = goalAngle - boat.getBearing().degrees();
        if (goalRotation < 0) {
            goalRotation += 360;
        }
        if (goalRotation > 180) {
            goalRotation = 360 - goalRotation;
            direction = -1;
        } else {
            direction = 1;
        }
    }

    private void newOptimalVMG(MockBoat boat) {
        long tackPeriod = 1000;
        if (boat.getTimeSinceTackChange() > tackPeriod) {
            VMG newVMG = NewPolars.setBestVMG(race.getWindDirection(), race.getWindSpeed(), boat.getBearing());
            goalAngle = newVMG.getBearing().degrees();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        double offset = 3.0;
        if (totalRotation < goalRotation) {
            boat.setBearing(Bearing.fromDegrees(boat.getBearing().degrees() + offset * direction));
            totalRotation += offset;
        } else {
            boat.setBearing(Bearing.fromDegrees(goalAngle));
            race.deleteObserver(this);
        }
    }
}
