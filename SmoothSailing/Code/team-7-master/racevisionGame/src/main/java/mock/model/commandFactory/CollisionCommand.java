package mock.model.commandFactory;

import mock.model.MockBoat;
import mock.model.MockRace;
import shared.model.Azimuth;
import shared.model.GPSCoordinate;

import java.util.Observable;

/**
 * Command class for collisions
 */
public class CollisionCommand extends ObserverCommand {
    private GPSCoordinate startingPosition;
    private Azimuth azimuth;
    private double distance;

    /**
     * Constructor for class
     * @param race race context
     * @param boat boat controlled by command
     */
    public CollisionCommand(MockRace race, MockBoat boat) {
        super(race, boat);
        race.addObserver(this);
    }

    @Override
    public void execute() {
        this.azimuth = Azimuth.fromDegrees(boat.getBearing().degrees() - 180d);
        this.startingPosition = boat.getPosition();
        this.distance = 60;
        boat.setVelocityDefault(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(GPSCoordinate.calculateDistanceMeters(boat.getPosition(), startingPosition) < distance) {
            boat.setVelocityDefault(false);
            boat.setPosition(GPSCoordinate.calculateNewPosition(boat.getPosition(), 3, azimuth));
        } else {
            race.deleteObserver(this);
            boat.setVelocityDefault(true);
            boat.setColliding(false);
        }
    }
}
