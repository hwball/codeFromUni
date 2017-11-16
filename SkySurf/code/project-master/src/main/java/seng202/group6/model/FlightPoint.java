package seng202.group6.model;


/**
 * Class of a point that a flight flies through.
 */
public class FlightPoint extends Record {

    /**
     * The altitude of the FlightPoint.
     */
    private Double altitude;
    /**
     * The latitude of the FlightPoint.
     */
    private Double latitude;
    /**
     * The longitude of the FlightPoint.
     */
    private Double longitude;
    /**
     * Identification value of point.
     */
    private String id;
    /**
     * The type of point.
     */
    private String type;

    /**
     * Constructor for flight points.
     *
     * @param type      type of point
     * @param id        Identification value of point
     * @param altitude  height of point in feet
     * @param latitude  latitude of point
     * @param longitude longitude of point
     */
    public FlightPoint(String type, String id, Double altitude, Double latitude, Double longitude) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.type = type;
    }

    /**
     * Getter for altitude.
     *
     * @return altitude in feet
     */
    public Double getAltitude() {
        return altitude;
    }


    /**
     * Setter for altitude.
     *
     * @param altitude new altitude for point
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for latitude.
     *
     * @return latitude of point
     */
    public Double getLatitude() {
        return latitude;
    }


    /**
     * Setter for latitude.
     *
     * @param latitude new latitude of point
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for longitude.
     *
     * @return longitude of point
     */
    public Double getLongitude() {
        return longitude;
    }


    /**
     * Setter for longitude.
     *
     * @param longitude new longitude of point
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for id.
     *
     * @return id of point
     */
    public String getId() {
        return id;
    }


    /**
     * Setter for id.
     *
     * @param id new id of point
     */
    public void setId(String id) {
        this.id = id;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for type.
     *
     * @return type of point
     */
    public String getType() {
        return type;
    }


    /**
     * Setter for type.
     *
     * @param type new type of point
     */
    public void setType(String type) {
        this.type = type;
        setChanged();
        notifyObservers();
    }


    /**
     * Checks for equality.
     *
     * @param object object to check
     * @return true if object equals FlightPoint
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        Boolean result = false;
        if (object instanceof FlightPoint) {
            FlightPoint that = (FlightPoint) object;
            result = that.canEqual(this) &&
                    altitude.equals(that.getAltitude()) &&
                    latitude.equals(that.getLatitude()) &&
                    longitude.equals(that.getLongitude()) &&
                    id.equals(that.getId()) &&
                    type.equals(that.getType());
        }
        return result;
    }


    /**
     * Checks to see if param is the same type as FlightPoint.
     *
     * @param object object to check
     * @return true if object is a kind of FlightPoint
     */
    public boolean canEqual(Object object) {
        return object instanceof FlightPoint;
    }


    /**
     * Creates a new copy of the FlightPoint.
     *
     * @return the new copy of the FlightPoint
     */
    public FlightPoint copy() {
        return new FlightPoint(type, id, altitude, latitude, longitude);
    }


    /**
     * Gives the hashcode of the FlightPoint.
     *
     * @return hashcode of FlightPoint
     */
    @Override
    public int hashCode() {
        int result = altitude != null ? altitude.hashCode() : 0;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }


    /**
     * Returns the to string of the FlightPoint.
     *
     * @return to sting of FlightPoint
     */
    @Override
    public String toString() {
        return type + "-" + id;
    }


    /**
     * Gets the data required for mapping the flight point.
     *
     * @return a string formatted to contain the longitude and latitude.
     */
    public String getMappingJSON() {
        return String.format("{lat: %f, lng: %f}", getLatitude(), getLongitude());
    }
}
