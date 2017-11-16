package mock.model.collider;

import mock.model.MockBoat;
import shared.model.Boat;
import shared.model.GPSCoordinate;

import java.util.*;

/**
 * Registry for all Collider objects in a MockRace. Wraps the Collider interface as part of a Composite Pattern.
 */
public class ColliderRegistry extends Collider implements Observer {
    /**
     * List of all registered Colliders
     */
    private List<Collider> colliders;

    /**
     * Default constructor for ColliderRegistry
     */
    public ColliderRegistry() {
        this.colliders = new ArrayList<>();
    }

    public void addCollider(Collider collider) {
        collider.addObserver(this);
        colliders.add(collider);
    }

    public void addAllColliders(Collection<? extends Collider> colliders) {
        for(Collider collider: colliders) addCollider(collider);
    }

    @Override
    public boolean rayCast(Boat boat) {
        for(Collider collider: colliders) {
            if(collider.rayCast(boat)) return true;
        }
        return false;
    }

    @Override
    public void onCollisionEnter(Collision e) {}

    @Override
    public GPSCoordinate getPosition() {
        return null;
    }

    @Override
    public void setPosition(GPSCoordinate position) {

    }

    /**
     * Fire onCollisionEnter when collision bubbles up from registered colliders.
     * @param o object collided with
     * @param arg parameters of the collision
     */
    @Override
    public void update(Observable o, Object arg) {
        Collision collision = (Collision)arg;

        this.setChanged();
        notifyObservers(collision);
    }
}
