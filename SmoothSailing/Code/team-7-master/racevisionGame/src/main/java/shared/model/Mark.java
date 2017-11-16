package shared.model;

import shared.xml.Race.XMLMark;

import java.math.BigDecimal;
import java.math.BigInteger;

import mock.model.collider.Collider;
import mock.model.collider.Collision;

/**
 * Represents an individual mark.
 * Has a source ID, name, and position.
 */
public class Mark extends Collider{

    /**
     * The source ID of the mark.
     */
    private int sourceID;

    /**
     * The name of the mark.
     */
    private String name;

    /**
     * The position of the mark.
     */
    private GPSCoordinate position;

    /**
     * Constructs a mark with a given source ID, name, and position.
     * @param sourceID The source ID of the mark.
     * @param name The name of the mark.
     * @param position The position of the mark.
     */
    public Mark(int sourceID, String name, GPSCoordinate position) {
        super();
        /* TODO need to talk to connor about this as this class should be extending XMLMark
        targetLat = position.getLatitude();
        targetLng = position.getLongitude();
        setSourceID(sourceID);
        setName(name);*/

        this.sourceID = sourceID;
        this.name = name;
        this.position = position;
    }

    /**
     * Used to create marks that are not visible in the race
     * @param position position of the mark
     * @return the new mark
     */
    public static Mark tempMark(GPSCoordinate position){
         return new Mark(-1, "Hidden Mark", position);
    }


    /**
     * Returns the name of the mark.
     * @return The name of the mark.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the source ID of the mark
     * @return the source ID of the mark
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * Returns the position of the mark.
     * @return The position of the mark.
     */
    public GPSCoordinate getPosition() {
        return position;
    }

    /**
     * Sets the position of the mark to a specified GPSCoordinate.
     * @param position The new GPSCoordinate to use.
     */
    public void setPosition(GPSCoordinate position) {
        this.position = position;
    }

    @Override
    public boolean rayCast(Boat boat) {
        return rayCast(boat, 15);
    }

    @Override
    public void onCollisionEnter(Collision e) {
        e.getBoat().updateHealth(-10);
        this.setChanged();
        notifyObservers(e);
    }
}
