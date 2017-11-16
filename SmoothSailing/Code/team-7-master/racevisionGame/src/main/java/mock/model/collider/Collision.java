package mock.model.collider;

import shared.model.Bearing;
import shared.model.Boat;

/**
 * Data structure for holding collision details for ray casting and event handling.
 */
public class Collision {
    /**
     * Bearing from boat heading to target
     */
    private Bearing bearing;
    /**
     * Distance from boat centre to target centre
     */
    private double distance;
    /**
     * Boat involved in the collision
     */
    private Boat boat;

    /**
     * Constructor for Collision structure
     * @param boat involved in collision
     * @param bearing from boat heading to target
     * @param distance from boat centre to target centre
     */
    public Collision(Boat boat, Bearing bearing, double distance) {
        this.boat = boat;
        this.bearing = bearing;
        this.distance = distance;
    }

    public Bearing getBearing() {
        return bearing;
    }

    public double getDistance() {
        return distance;
    }

    public Boat getBoat() {
        return boat;
    }
}
