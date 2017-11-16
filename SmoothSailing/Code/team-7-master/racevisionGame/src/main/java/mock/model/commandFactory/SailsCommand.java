package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;
import mock.model.NewPolars;
import mock.model.VMG;

import java.util.Observable;

public class SailsCommand extends ObserverCommand {
    private boolean sailsOut;
    private double goalVelocity;

    public SailsCommand(MockRace race, MockBoat boat, boolean sailsOut) {
        super(race, boat);
        race.addVelocityCommand(this, boat.getSourceID());
        this.sailsOut = sailsOut;
    }

    @Override
    public void execute() {
        this.boat.setSailsOut(this.sailsOut);
        boat.setVelocityDefault(false);

        if(sailsOut) {
            // Accelerate to VMG speed
            double polarSpeed = NewPolars.calculateSpeed(race.getWindDirection(), race.getWindSpeed(), boat.getBearing());
            VMG vmg = new VMG(polarSpeed, boat.getBearing());
            goalVelocity = vmg.getSpeed();
        } else {
            // Decelerate to 0
            goalVelocity = 0;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        double acceleration = 0.5;

        if (!boat.isColliding()) {
            boat.setVelocityDefault(false);
            if (sailsOut && boat.getCurrentSpeed() < goalVelocity) {
                boat.setCurrentSpeed(Math.min(goalVelocity, boat.getCurrentSpeed() + acceleration));
            } else if (!sailsOut && boat.getCurrentSpeed() > goalVelocity) {
                // Apply deceleration to strictly 0 speed
                boat.setCurrentSpeed(Math.max(0, boat.getCurrentSpeed() - acceleration));
            } else {
                // Release boat from SailsCommand control
                if (sailsOut) boat.setVelocityDefault(true);
                race.deleteObserver(this);
            }
        }
    }
}
