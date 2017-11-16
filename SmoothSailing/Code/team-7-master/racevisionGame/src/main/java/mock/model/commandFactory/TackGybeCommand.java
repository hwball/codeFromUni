package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;
import shared.model.Bearing;

import java.util.Observable;

/**
 * Command class for tacking and gybing
 */
public class TackGybeCommand extends ObserverCommand {
    private double goalRotation;
    private double totalRotation = 0;
    private int direction;  // -1 for anticlockwise, 1 for clockwise
    private double goalAngle;

    /**
     * Constructor for class
     * @param race mock race
     * @param boat mock boat to update
     */
    public TackGybeCommand(MockRace race, MockBoat boat) {
        super(race, boat);
        race.addAngularCommand(this, boat.getSourceID());
    }

    @Override
    public void execute() {
        double boatAngle = boat.getBearing().degrees();
        double windAngle = race.getWindDirection().degrees();
        double differenceAngle = calcDistance(boatAngle, windAngle);
        double angleA = windAngle + differenceAngle;
        double angleB = windAngle - differenceAngle;
        if (angleA % 360 == boatAngle) {
            goalAngle = angleB % 360;
        } else {
            goalAngle = angleA % 360;
        }

        goalRotation = goalAngle - boatAngle;
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

    /**
     * Method to calculate smallest angle between 2 angles
     * @param degreeA first angle degree
     * @param degreeB second angle degree
     * @return the calculated smallest angle
     */
    public double calcDistance(double degreeA, double degreeB){
        double phi = Math.abs(degreeB - degreeA) % 360;
        return phi > 180 ? 360 - phi : phi;
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

