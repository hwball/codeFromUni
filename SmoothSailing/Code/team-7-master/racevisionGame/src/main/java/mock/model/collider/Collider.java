package mock.model.collider;

import shared.model.Bearing;
import shared.model.Boat;
import shared.model.GPSCoordinate;
import shared.model.Locatable;

import java.util.Observable;

/**
 * Interface for all objects sensitive to collision in a race.
 */
public abstract class Collider extends Observable implements Locatable {
    /**
     * Indicates whether a ray cast from a boat to a target collider is within the specified length.
     * @param boat potentially colliding with target
     * @param distance distance for valid collision
     * @return whether or not a collision has occurred
     */
    public boolean rayCast(Boat boat, double distance) {
        double actualDistance = GPSCoordinate.calculateDistanceMeters(boat.getPosition(), this.getPosition());
        // Compass direction of collider
        Bearing absolute = Bearing.fromAzimuth(GPSCoordinate.calculateAzimuth(boat.getPosition(), this.getPosition()));
        // Direction of collider from heading
        Bearing relative = Bearing.fromDegrees(absolute.degrees() - boat.getBearing().degrees());

        if(!boat.isColliding() && actualDistance <= distance) {
            boat.setColliding(true);
            Collision collision = new Collision(boat, relative, distance);
            // Notify object of collision
            onCollisionEnter(collision);
            return true;
        } else return false;
    }

    /**
     * Indicates whether a ray cast from a boat to a target collider triggers a collision. Distance is set by the object.
     * @param boat potentially colliding with target
     * @return whether or not a collision has occurred
     */
    public abstract boolean rayCast(Boat boat);

    /**
     * Handle a collision event
     * @param e details of collision
     */
    public abstract void onCollisionEnter(Collision e);
}
