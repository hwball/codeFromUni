package seng202.group6.model;

/**
 * Airport class to represent an airport Implements comparable to allow for use in a tree set.
 */
public class Airport extends IdentifiedRecord implements Comparable<Airport> {

    /**
     * The altitude in feet of the airport.
     */
    private double altitude;
    /**
     * The latitude of the airport.
     */
    private double latitude;
    /**
     * The longitude of the airport.
     */
    private double longitude;
    /**
     * The number of routes that terminate at the airport.
     */
    private Integer numRoutes;
    /**
     * The main city served by the airport.
     */
    private String city;
    /**
     * The country or territory where the airport is located.
     */
    private String country;
    /**
     * The daylight saving time followed by the airport.<p>One of E (Europe), A (US/Canada), S
     * (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown)
     */
    private String dst;
    /**
     * The name of the airport.
     */
    private String name;
    /**
     * Hours offset from UTC.
     */
    private String timeZone;
    /**
     * The timezone of the airport in "tz" Olson format.
     */
    private String timeZoneDatabase;


    /**
     * Creates a new airport object, specifying the number of routes.
     *
     * @param name             name of airport
     * @param city             main city served by airport
     * @param country          country or territory where airport is located
     * @param iata             3-letter IATA/FAA (International Air Travel Association / Federal
     *                         Aviation Authority [for US airports]) code
     * @param icao             4-letter ICAO (International Civil Aviation Organization) code
     * @param latitude         decimal degrees, usually to six significant digits. Negative is
     *                         South, positive is North.
     * @param longitude        decimal degrees, usually to six significant digits. Negative is West,
     *                         positive is East.
     * @param altitude         height, in feet.
     * @param timeZone         hours offset from UTC
     * @param dst              daylight savings time
     * @param timeZoneDatabase timezone in "tz" (Olson) format
     * @param numRoutes        number of routes
     */
    public Airport(String name, String city, String country, String iata, String icao,
                   double latitude, double longitude, double altitude, String timeZone,
                   String dst, String timeZoneDatabase, int numRoutes) {
        super(iata, icao);
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numRoutes = numRoutes;
        this.city = city;
        this.country = country;
        this.dst = dst;
        this.name = name;
        this.timeZone = timeZone;
        this.timeZoneDatabase = timeZoneDatabase;
    }


    /**
     * Create an airport without specifying the number of routes it has.
     *
     * @param name             name of airport
     * @param city             main city served by airport
     * @param country          country or territory where airport is located
     * @param iata             3-letter IATA/FAA (International Air Travel Association / Federal
     *                         Aviation Authority [for US airports]) code
     * @param icao             4-letter ICAO (International Civil Aviation Organization) code
     * @param latitude         decimal degrees, usually to six significant digits. Negative is
     *                         South, positive is North.
     * @param longitude        decimal degrees, usually to six significant digits. Negative is West,
     *                         positive is East.
     * @param altitude         height, in feet.
     * @param timeZone         hours offset from UTC
     * @param dst              daylight savings time
     * @param timeZoneDatabase timezone in "tz" (Olson) format
     */
    public Airport(String name, String city, String country, String iata, String icao,
                   double latitude, double longitude, double altitude, String timeZone,
                   String dst, String timeZoneDatabase) {
        super(iata, icao);
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numRoutes = 0;
        //this.icao = icao;
        this.city = city;
        this.country = country;
        this.dst = dst;
        //this.iata = iata;
        this.name = name;
        this.timeZone = timeZone;
        this.timeZoneDatabase = timeZoneDatabase;
    }


    /**
     * Get the altitude of this airport is feet above sea level.
     *
     * @return the altitude of this airport
     */
    public Double getAltitude() {
        return altitude;
    }


    /**
     * Sets the altitude of this airport in feet above sea level.
     *
     * @param altitude the altitude of this airport
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets the latitude of this airport
     *
     * @return the latitude of this airport
     */
    public Double getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude of this airport.
     *
     * @param latitude the latitude of this airport
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets the longitude of this airport.
     *
     * @return the longitude of this airport
     */
    public Double getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude of this airport.
     *
     * @param longitude the longitude of this airport
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets the number of routes for which this airport is a terminal.
     *
     * @return the number of routes connected to this airport
     */
    public int getNumRoutes() {
        return numRoutes;
    }


    /**
     * Sets the number of routes for which this airport is a terminal.
     *
     * @param nRoutes new number of routes connected to airport
     */
    void setNumRoutes(int nRoutes) {
        this.numRoutes = nRoutes;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets the city that this airport is located in.
     *
     * @return this airport's city
     */
    public String getCity() {
        return city;
    }


    /**
     * Sets the city that tis airport is located in.
     *
     * @param city the airport's city
     */
    public void setCity(String city) {
        this.city = city;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets the country or territory that this airport is located in.
     *
     * @return this airport's country
     */
    public String getCountry() {
        return country;
    }


    /**
     * Sets the country or territory this airport is located in.
     *
     * @param country this airport's country
     */
    public void setCountry(String country) {
        this.country = country;
        setChanged();
        notifyObservers();
    }


    /**
     * Get this airport's daylight savings time category.
     *
     * @return this airport's daylight savings time
     */
    public String getDst() {
        return dst;
    }


    /**
     * Set this airport's daylight savings time category.
     *
     * @param dst this daylight savings time
     */
    public void setDst(String dst) {
        this.dst = dst;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets this airport's name.
     *
     * @return the airport's name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets this airports name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }


    /**
     * Get this airport's time zone.
     *
     * @return this airport's time zone
     */
    public String getTimeZone() {
        return timeZone;
    }


    /**
     * Set's this airports time zone.
     *
     * @param timeZone the time zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        setChanged();
        notifyObservers();
    }


    /**
     * Gets this airport's time zone database.
     *
     * @return the airport's time zone database
     */
    public String getTimeZoneDatabase() {
        return timeZoneDatabase;
    }


    /**
     * Sets this airport's time zone database
     *
     * @param timeZoneDatabase the time zone database
     */
    public void setTimeZoneDatabase(String timeZoneDatabase) {
        this.timeZoneDatabase = timeZoneDatabase;
        setChanged();
        notifyObservers();
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        boolean result = false;
        if (object instanceof Airport) {
            Airport that = (Airport) object;
            result = that.canEqual(this) &&
                    ((Double) altitude).equals(that.getAltitude()) &&
                    ((Double) latitude).equals(that.getLatitude()) &&
                    ((Double) longitude).equals(that.getLongitude()) &&
                    /*rank == that.getRank() &&*/
                    numRoutes == that.getNumRoutes() &&
                    getIcao().equals(that.getIcao()) &&
                    city.equals(that.getCity()) &&
                    country.equals(that.getCountry()) &&
                    dst.equals(that.getDst()) &&
                    getIata().equals(that.getIata()) &&
                    name.equals(that.getName()) &&
                    timeZone.equals(that.getTimeZone()) &&
                    timeZoneDatabase.equals(that.getTimeZoneDatabase());
        }
        return result;
    }


    @Override
    public boolean canEqual(Object object) {
        return object instanceof Airport;
    }


    /**
     * Copies this Airport, creating a new Airport object
     *
     * @return a copy of this Airport
     */
    public Airport copy() {
        return new Airport(name, city, country, getIata(), getIcao(), latitude, longitude, altitude, timeZone, dst, timeZoneDatabase, numRoutes);
    }


    @Override
    public String toString() {
        return name;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getAltitude());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getNumRoutes();
        result = 31 * result + (getIcao() != null ? getIcao().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getDst() != null ? getDst().hashCode() : 0);
        result = 31 * result + (getIata() != null ? getIata().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getTimeZone() != null ? getTimeZone().hashCode() : 0);
        result = 31 * result + (getTimeZoneDatabase() != null ? getTimeZoneDatabase().hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(Airport o) {
        return this.toString().compareTo(o.toString());
    }


    /**
     * Returns a javascript array, of ["name", latitude, longitude].
     *
     * @return the javascript array
     */
    public String getMappingJSON() {
        return String.format("[\"%s\",%f,%f]", getName(), getLatitude(), getLongitude());
    }
}
