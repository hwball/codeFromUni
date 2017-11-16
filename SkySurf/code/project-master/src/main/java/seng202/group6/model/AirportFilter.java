package seng202.group6.model;

import java.util.HashSet;

/**
 * Holds a set of conditions that can be compared against Airports, and the methods to do so.
 */
public class AirportFilter extends Filter<Airport> {

    /* These contain the active criteria. */

    /**
     * Max, min of altitude. Null represents unbounded.
     */
    private Double[] altitude = new Double[2];
    /**
     * Max, min of latitude query. Extreme values are +-90. Null represents unbounded.
     */
    private Double[] latitude = new Double[2];
    /**
     * Max, min of latitude query. Extreme values are +-180. Null represents unbounded.
     */
    private Double[] longitude = new Double[2];
    /**
     * Max, min of number of routes. Null represents unbounded.
     */
    private Integer[] routes = new Integer[2];
    /**
     * Collection of accepted ICAO codes.
     */
    private final HashSet<String> icao = new HashSet<>();
    /**
     * Collection of accepted cities
     */
    private final HashSet<String> city = new HashSet<>();
    /**
     * Collection of accepted countries
     */
    private final HashSet<String> country = new HashSet<>();
    /**
     * Collection of accepted daylight saving time codes.
     */
    private final HashSet<String> daylightSavingTime = new HashSet<>();
    /**
     * Collection of accepted IATA codes.
     */
    private final HashSet<String> iata = new HashSet<>();
    /**
     * Collection of accepted names.
     */
    private final HashSet<String> name = new HashSet<>();
    /**
     * Collection of accepted time zones.
     */
    private final HashSet<String> timeZone = new HashSet<>();
    /**
     * Collection of accepted time zone databases.
     */
    private final HashSet<String> timeZoneDatabase = new HashSet<>();


    /**
     * Takes and airport object as a parameter and returns true if it meets the filter's criteria
     * and false if it does not.
     *
     * @param airport The airport object to be checked.
     * @return Returns true if the airport meets the filter criteria and false if it does not.
     */
    public boolean matches(Airport airport) {
        return inRange(altitude, airport.getAltitude()) &&
                inRange(latitude, airport.getLatitude()) &&
                inRange(longitude, airport.getLongitude()) &&
                inRange(routes, airport.getNumRoutes()) &&
                stringMatches(icao, airport.getIcao()) &&
                stringMatches(city, airport.getCity()) &&
                stringMatches(country, airport.getCountry()) &&
                stringMatches(daylightSavingTime, airport.getDst()) &&
                stringMatches(iata, airport.getIata()) &&
                stringMatches(name, airport.getName()) &&
                stringMatches(timeZone, airport.getTimeZone()) &&
                stringMatches(timeZoneDatabase, airport.getTimeZoneDatabase());
    }


    /* Methods to add criteria */

    /**
     * Sets the altitude range criteria for the filter.
     *
     * @param min the minimum altitude
     * @param max the maximum altitude
     */
    public void setAltitude(Double min, Double max) {
        Double[] range = {min, max};
        assert rangeIsValid(range);
        this.altitude = range;
    }


    /**
     * Sets the latitude range criteria for the filter.
     *
     * @param min the minimum latitude
     * @param max the maximum latitude
     */
    public void setLatitude(Double min, Double max) {
        Double[] range = {min, max};
        assert rangeIsValid(range);
        this.latitude = range;
    }


    /**
     * Sets the longitude range criteria for the filter.
     *
     * @param min the minimum longitude
     * @param max the maximum longitude
     */
    public void setLongitude(Double min, Double max) {
        Double[] range = {min, max};
        assert rangeIsValid(range);
        this.longitude = range;
    }


    /**
     * Sets the routes range criteria for the filter.
     *
     * @param min the minimum number of routes
     * @param max the maximum number of routes
     */
    public void setRoutes(Integer min, Integer max) {
        Integer[] range = {min, max};
        assert rangeIsValid(range);
        this.routes = range;
    }


    /**
     * Adds an ICAO criteria to the filter.
     *
     * @param icao the ICAO string to add to the filter
     */
    public void addIcao(String icao) {
        this.icao.add(icao.toLowerCase());
    }


    /**
     * Adds a city criteria to the filter.
     *
     * @param city the City string to add to the filter
     */
    public void addCity(String city) {
        this.city.add(city.toLowerCase());
    }


    /**
     * Adds a country criteria to the filter.
     *
     * @param country the Country string to add to the filter
     */
    public void addCountry(String country) {
        this.country.add(country.toLowerCase());
    }


    /**
     * Adds a daylight saving time criteria to the filter.
     *
     * @param daylightSavingTime the daylight saving time string to add to the filter
     */
    public void addDaylightSavingTime(String daylightSavingTime) {
        this.daylightSavingTime.add(daylightSavingTime.toLowerCase());
    }


    /**
     * Adds an IATA criteria to the filter.
     *
     * @param iata the IATA string to add to the filter
     */
    public void addIata(String iata) {
        this.iata.add(iata.toLowerCase());
    }


    /**
     * Adds a name criteria to the filter.
     *
     * @param name the Name string to add to the filter
     */
    public void addName(String name) {
        this.name.add(name.toLowerCase());
    }


    /**
     * Adds a time zone criteria to the filter.
     *
     * @param timeZone the time zone string to add to the filter
     */
    public void addTimeZone(String timeZone) {
        this.timeZone.add(timeZone.toLowerCase());
    }


    /**
     * Adds a time zone database criteria to the filter.
     *
     * @param timeZoneDatabase the time zone database string to add to the filter
     */
    public void addTimeZoneDatabase(String timeZoneDatabase) {
        this.timeZoneDatabase.add(timeZoneDatabase.toLowerCase());
    }


    /* Getters*/

    /**
     * Returns a Double array containing the minimum and maximum altitude.
     *
     * @return the minimum and maximum altitude
     */
    public Double[] getAltitude() {
        return altitude;
    }


    /**
     * Returns a Double array containing the minimum and maximum latitude.
     *
     * @return the minimum and maximum latitude
     */
    public Double[] getLatitude() {
        return latitude;
    }


    /**
     * Returns a Double array containing the minimum and maximum longitude.
     *
     * @return the minimum and maximum longitude
     */
    public Double[] getLongitude() {
        return longitude;
    }


    /**
     * Returns an Integer array containing the minimum and maximum number of routes.
     *
     * @return the minimum and maximum number of routes
     */
    public Integer[] getRoutes() {
        return routes;
    }


    /**
     * Returns the HashSet of ICAO string criteria.
     *
     * @return the HashSet of ICAO string criteria
     */
    public HashSet<String> getIcao() {
        return icao;
    }


    /**
     * Returns the HashSet of city string criteria.
     *
     * @return the HashSet of city string criteria
     */
    public HashSet<String> getCity() {
        return city;
    }


    /**
     * Returns the HashSet of country string criteria.
     *
     * @return the HashSet of country string criteria
     */
    public HashSet<String> getCountry() {
        return country;
    }


    /**
     * Returns the HashSet of daylight saving time string criteria.
     *
     * @return the HashSet of daylight saving time string criteria
     */
    public HashSet<String> getDaylightSavingTime() {
        return daylightSavingTime;
    }


    /**
     * Returns the HashSet of IATA string criteria.
     *
     * @return the HashSet of IATA string criteria
     */
    public HashSet<String> getIata() {
        return iata;
    }


    /**
     * Returns the HashSet of name string criteria.
     *
     * @return the HashSet of name string criteria
     */
    public HashSet<String> getName() {
        return name;
    }


    /**
     * Returns the HashSet of time zone string criteria.
     *
     * @return the HashSet of time zone string criteria
     */
    public HashSet<String> getTimeZone() {
        return timeZone;
    }


    /**
     * Returns the HashSet of time zone database string criteria.
     *
     * @return the HashSet of time zone database string criteria
     */
    public HashSet<String> getTimeZoneDatabase() {
        return timeZoneDatabase;
    }
}
