package seng202.group6.model;

/**
 * A Airline class representing an airline.
 */
public class Airline extends IdentifiedRecord {

    /**
     * Whether this carrier is still active.
     */
    private boolean active;
    /**
     * The alias the carrier uses.
     */
    private String alias;
    /**
     * The call sign of the airline.
     */
    private String callSign;
    /**
     * The country of incorporation.
     */
    private String country;
    /**
     * The name of the airline.
     */
    private String name;


    /**
     * Creates a new airline.
     *
     * @param name     name of the airline
     * @param alias    alias the airline flies under
     * @param iata     2-letter IATA (International Air Transport Association) code
     * @param icao     3-letter ICAO (International Civil Aviation Organization) code
     * @param callSign airline's call sign
     * @param country  country or territory where airline is incorporated
     * @param active   true if the airline is or has until recently been operational, false if it is
     *                 defunct.
     */
    public Airline(String name, String alias, String iata, String icao,
                   String callSign, String country, boolean active) {
        super(iata, icao);
        this.active = active;
        this.alias = alias;
        this.callSign = callSign;
        this.country = country;
        this.name = name;
    }


    /**
     * Gets whether this airline is active.
     *
     * @return true if this airline is active, else false
     */
    public boolean getActive() {
        return active;
    }


    /**
     * Sets whether this airline is active.
     *
     * @param active active status
     */
    public void setActive(Boolean active) {
        this.active = active;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets this airline's alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }


    /**
     * Sets this airline's alias.
     *
     * @param alias the alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets this airline's call sign.
     *
     * @return the call sign
     */
    public String getCallSign() {
        return callSign;
    }


    /**
     * Sets this airline's call sign.
     *
     * @param callSign the call sign
     */
    public void setCallSign(String callSign) {
        this.callSign = callSign;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets the country or territory where this airline is incorporated.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }


    /**
     * Sets the country or territory where this airline is incorporated.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets this airline's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets this airline's name.
     *
     * @param name airline name
     */
    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }


    /**
     * Compares this airline for equality with the given object.
     *
     * @param object the object to compare with this airline
     * @return true if the object is an airline and equal to this airline, else false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        boolean result = false;
        if (object instanceof Airline) {
            Airline that = (Airline) object;
            result = that.canEqual(this) &&
                    active == that.getActive() &&
                    alias.equals(that.getAlias()) &&
                    callSign.equals(that.getCallSign()) &&
                    country.equals(that.getCountry()) &&
                    getIcao().equals(that.getIcao()) &&
                    getIata().equals(that.getIata()) &&
                    name.equals(that.getName());
        }
        return result;
    }


    /**
     * Checks whether an object is an instance of Airline.
     *
     * @param object object to check
     * @return true if the object is an Airline, else false
     */
    public boolean canEqual(Object object) {
        return object instanceof Airline;
    }


    /**
     * Creates a new Airline object equal to this one.
     *
     * @return a  copy of this airline
     */
    public Airline copy() {
        return new Airline(name, alias, getIata(), getIcao(), callSign, country, active);
    }


    /**
     * Returns the hash code of this airline.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = (active ? 1 : 0);
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (callSign != null ? callSign.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (getIata() != null ? getIata().hashCode() : 0);
        result = 31 * result + (getIcao() != null ? getIcao().hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


    /**
     * Returns a string representation of this airline.
     *
     * @return the string representation of this airline
     */
    @Override
    public String toString() {
        return "Airline{" +
                "active=" + active +
                ", alias='" + alias + '\'' +
                ", callSign='" + callSign + '\'' +
                ", country='" + country + '\'' +
                ", iata='" + getIata() + '\'' +
                ", icao='" + getIcao() + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
