package shared.model;


import javafx.beans.property.*;
import mock.model.collider.Collider;
import mock.model.collider.Collision;
import network.Messages.Enums.BoatStatusEnum;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

/**
 * Boat Model that is used to store information on the boats that are running in the race.
 */
public class Boat extends Collider {
    /**
     * The name of the boat/team.
     */
    private StringProperty name = new SimpleStringProperty();

    /**
     * The current speed of the boat, in knots.
     * TODO knots
     */
    private DoubleProperty currentSpeed = new SimpleDoubleProperty(0);

    /**
     * The current bearing/heading of the boat.
     */
    private Bearing bearing;

    /**
     * The current position of the boat.
     */
    private GPSCoordinate position;

    /**
     * The country or team abbreviation of the boat.
     */
    private StringProperty country = new SimpleStringProperty();

    /**
     * The source ID of the boat.
     * This uniquely identifies an entity during a race.
     */
    private int sourceID;

    /**
     * The leg of the race that the boat is currently on.
     */
    private Property<Leg> currentLeg = new SimpleObjectProperty<>();

    /**
     * The distance, in meters, that the boat has travelled in the current leg.
     * TODO meters
     */
    private double distanceTravelledInLeg;

    /**
     * The boat's position within the race (e.g., 5th).
     */
    private StringProperty placing = new SimpleStringProperty();


    /**
     * The time, in milliseconds, that has elapsed during the current leg.
     * TODO milliseconds
     */
    private long timeElapsedInCurrentLeg;

    /**
     * The timestamp, in milliseconds, of when the boat finished the race.
     * Is -1 if it hasn't finished.
     * TODO milliseconds
     */
    private long timeFinished = -1;

    /**
     * The current status of the boat.
     */
    private BoatStatusEnum status;


    /**
     * The time at which the boat is estimated to reach the next mark, in milliseconds since unix epoch.
     */
    @Nullable
    private ZonedDateTime estimatedTimeAtNextMark;

    /**
     * The time at which the boat reached the previous mark.
     */
    @Nullable
    private ZonedDateTime timeAtLastMark;

    /**
     * The state of the boats sails. True if sails are out.
     */
    private boolean sailsOut = true;

    /**
     * Indicates whether boat is currently involved in a collision
     */
    private boolean isColliding = false;

    /**
     * Amount of health boat currently has, between 0 and 100
     */
    private double health;

    /**
     * Constructs a boat object with a given sourceID, name, country/team abbreviation, and polars table.
     *
     * @param sourceID The id of the boat
     * @param name The name of the Boat.
     * @param country The abbreviation or country code for the boat.
     */
    public Boat(int sourceID, String name, String country) {

        this.sourceID = sourceID;
        this.health = 100;
        this.setName(name);
        this.setCountry(country);

        this.bearing = Bearing.fromDegrees(0d);

        setPosition(new GPSCoordinate(0, 0));

        this.status = BoatStatusEnum.UNDEFINED;
    }




    /**
     * Returns the name of the boat/team.
     * @return Name of the boat/team.
     */
    public String getName() {
        return name.getValue();
    }

    /**
     * Sets the name of the boat/team.
     * @param name Name of the boat/team.
     */
    public void setName(String name) {
        this.name.setValue(name);
    }

    /**
     * Returns the name property of the boat.
     * @return The name of the boat, in a StringProperty.
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Returns the current speed of the boat, in knots.
     * @return The current speed of the boat, in knots.
     */
    public double getCurrentSpeed() {
        return currentSpeed.get();
    }

    /**
     * Sets the speed of the boat, in knots.
     * @param currentSpeed The new speed of the boat, in knots.
     */
    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed.set(currentSpeed);
    }

    /**
     * Returns the current speed property of the boat.
     * @return The current speed of the boat, in a DoubleProperty.
     */
    public DoubleProperty currentSpeedProperty() {
        return currentSpeed;
    }


    /**
     * Gets the country/team abbreviation of the boat.
     * @return The country/team abbreviation of the boat.
     */
    public String getCountry() {
        return country.getValue();
    }

    /**
     * Sets the country/team abbreviation of the boat.
     * @param country  The new country/team abbreviation for the boat.
     */
    public void setCountry(String country) {
        this.country.setValue(country);
    }

    /**
     * Returns the country/abbreviation property of the boat.
     * @return The country/abbreviation of the boat, in a StringProperty.
     */
    public StringProperty countryProperty() {
        return country;
    }

    /**
     * Returns the source ID of the boat.
     * @return The source ID of the boat.
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * Sets the source ID of the boat.
     * @param sourceID The new source ID for the boat.
     */
    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }

    /**
     * Returns the current leg of the race the boat is in.
     * @return The current leg of the race the boat is in.
     */
    public Leg getCurrentLeg() {
        return currentLeg.getValue();
    }

    /**
     * Returns the current leg, wrapped in a property.
     * @return Current leg, wrapped in a property.
     */
    public Property<Leg> legProperty() {
        return currentLeg;
    }

    /**
     * Sets the current leg of the race the boat is in.
     * Clears time elapsed in current leg and distance travelled in current leg.
     * @param currentLeg The new leg of the race the boat is in.
     */
    public void setCurrentLeg(Leg currentLeg) {

        this.currentLeg.setValue(currentLeg);

        this.setTimeElapsedInCurrentLeg(0);
        this.setDistanceTravelledInLeg(0);

    }


    /**
     * Returns the distance, in meters, the boat has travelled in the current leg.
     * @return The distance, in meters, the boat has travelled in the current leg.
     */
    public double getDistanceTravelledInLeg() {
        return distanceTravelledInLeg;
    }

    /**
     * Sets the distance, in meters, the boat has travelled in the current leg.
     * @param distanceTravelledInLeg The distance, in meters, the boat has travelled in the current leg.
     */
    public void setDistanceTravelledInLeg(double distanceTravelledInLeg) {
        this.distanceTravelledInLeg = distanceTravelledInLeg;
    }


    /**
     * Returns the position within the race the boat has (e.g., 5th).
     * @return The boat's position in race.
     */
    public StringProperty placingProperty() {
        return placing;
    }

    /**
     * Sets the position within the race the boat has (e.g., 5th).
     * @param position The boat's position in race.
     */
    public void setPlacing(String position) {
        this.placing.set(position);
    }

    /**
     * Returns the current position of the boat.
     * @return The current position of the boat.
     */
    public GPSCoordinate getPosition() {
        return position;
    }

    /**
     * Sets the current position of the boat.
     * @param position The new position for the boat.
     */
    public void setPosition(GPSCoordinate position) {
        this.position = position;
    }


    /**
     * Gets the timestamp, in milliseconds, at which the boat finished the race.
     * @return The timestamp, in milliseconds, at which the boat finished the race.
     */
    public long getTimeFinished() {
        return timeFinished;
    }

    /**
     * Sets the timestamp, in milliseconds, at which the boat finished the race.
     * @param timeFinished The timestamp, in milliseconds, at which the boat finished the race.
     */
    public void setTimeFinished(long timeFinished) {
        this.timeFinished = timeFinished;
    }




    /**
     * Returns the current bearing of the boat.
     * @return The current bearing of the boat.
     */
    public Bearing getBearing() {
        return bearing;
    }

    /**
     * Sets the current bearing of the boat.
     * @param bearing The new bearing of the boat.
     */
    public void setBearing(Bearing bearing) {
        this.bearing = bearing;
    }


    /**
     * Returns the time, in milliseconds, that has elapsed since the boat started the current leg.
     * @return The time, in milliseconds, that has elapsed since the boat started the current leg.
     */
    public long getTimeElapsedInCurrentLeg() {
        return timeElapsedInCurrentLeg;
    }

    /**
     * Sets the time, in milliseconds, that has elapsed since the boat started the current leg.
     * @param timeElapsedInCurrentLeg The new time, in milliseconds, that has elapsed since the boat started the current leg.
     */
    public void setTimeElapsedInCurrentLeg(long timeElapsedInCurrentLeg) {
        this.timeElapsedInCurrentLeg = timeElapsedInCurrentLeg;
    }


    /**
     * Returns the status of the boat.
     * @return The sttus of the boat.
     */
    public BoatStatusEnum getStatus() {
        return status;
    }

    /**
     * Sets the status of the boat.
     * @param status The new status of the boat.
     */
    public void setStatus(BoatStatusEnum status) {
        this.status = status;
    }


    /**
     * Returns the time at which the boat should reach the next mark.
     * @return Time at which the boat should reach next mark.
     */
    @Nullable
    public ZonedDateTime getEstimatedTimeAtNextMark() {
        return estimatedTimeAtNextMark;
    }

    /**
     * Sets the time at which the boat should reach the next mark.
     * @param estimatedTimeAtNextMark Time at which the boat should reach next mark.
     */
    public void setEstimatedTimeAtNextMark(ZonedDateTime estimatedTimeAtNextMark) {
        this.estimatedTimeAtNextMark = estimatedTimeAtNextMark;
    }


    /**
     * Returns the time at which the boat reached the previous mark.
     * @return The time at which the boat reached the previous mark. May be null.
     */
    @Nullable
    public ZonedDateTime getTimeAtLastMark() {
        return timeAtLastMark;
    }

    /**
     * Sets the time at which the boat reached the previous mark to a specified time.
     * @param timeAtLastMark Time at which boat passed previous mark.
     */
    public void setTimeAtLastMark(ZonedDateTime timeAtLastMark) {
        this.timeAtLastMark = timeAtLastMark;
    }

    public void setSailsOut(boolean sailsOut) {
        this.sailsOut = sailsOut;
    }

    public boolean isSailsOut() {
        return sailsOut;
    }

    @Override
    public boolean rayCast(Boat boat) {
        if(boat != this) {
            return rayCast(boat, 15);
        } else return false;
    }

    @Override
    public void onCollisionEnter(Collision e) {
        if(e.getBearing().degrees() > 270 || e.getBearing().degrees() < 90) {
            // Deplete health
            e.getBoat().updateHealth(-5);
            // Notify observers of collision
            this.setChanged();
            notifyObservers(e);
        }
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean colliding) {
        isColliding = colliding;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Add a given amount of HP to boat health
     * @param delta amount of HP to add
     */
    public void updateHealth(double delta) {
        health += delta;
        if(health < 0) health = 0;
        else if(health > 100) health = 100;
    }
}
