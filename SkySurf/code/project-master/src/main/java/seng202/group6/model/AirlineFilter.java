package seng202.group6.model;

import java.util.HashSet;

/**
 * Holds a set of conditions that can be compared against Airlines, and the methods to do so.
 */
public class AirlineFilter extends Filter<Airline> {

    /**
     * Filter condition on airline activity. If null, accepts both true and false.
     */
    private Boolean active = null;
    /**
     * Collection of the accepted aliases.
     */
    private final HashSet<String> alias = new HashSet<>();
    /**
     * Collection of the accepted call signs.
     */
    private final HashSet<String> callSign = new HashSet<>();
    /**
     * Collection of accepted countries.
     */
    private final HashSet<String> country = new HashSet<>();
    /**
     * Collection of accepted IATA codes.
     */
    private final HashSet<String> iata = new HashSet<>();
    /**
     * Collection of accepted ICAO codes.
     */
    private final HashSet<String> icao = new HashSet<>();
    /**
     * Collection of accepted names.
     */
    private final HashSet<String> name = new HashSet<>();


    /**
     * Takes an airline object as a parameter and returns true if it meets the filter's criteria and
     * false if it does not.
     *
     * @param airline The airline object to be checked
     * @return returns true if the airline meets the filter criteria and false if it does not
     */
    public boolean matches(Airline airline) {
        return stringMatches(icao, airline.getIcao()) &&
                booleanMatches(active, airline.getActive()) &&
                stringMatches(country, airline.getCountry()) &&
                stringMatches(alias, airline.getAlias()) &&
                stringMatches(iata, airline.getIata()) &&
                stringMatches(name, airline.getName()) &&
                stringMatches(callSign, airline.getCallSign());
    }


    // Methods to add criteria

    /**
     * Adds an ICAO criterion to the filter.
     *
     * @param icao the ICAO string to add to the filter
     */
    public void addIcao(String icao) {
        this.icao.add(icao.toLowerCase());
    }


    /**
     * Adds a Country criterion to the filter.
     *
     * @param country the Country string to add to the filter
     */
    public void addCountry(String country) {
        this.country.add(country.toLowerCase());
    }


    /**
     * Adds an Alias criterion to the filter.
     *
     * @param alias the Alias string to add to the filter
     */
    public void addAlias(String alias) {
        this.alias.add(alias.toLowerCase());
    }


    /**
     * Adds an IATA criterion to the filter.
     *
     * @param iata the IATA string to add to the filter
     */
    public void addIata(String iata) {
        this.iata.add(iata.toLowerCase());
    }


    /**
     * Adds a Name criterion to the filter.
     *
     * @param name the Name string to add to the filter
     */
    public void addName(String name) {
        this.name.add(name.toLowerCase());
    }


    /**
     * Adds a CallSign criterion to the filter.
     *
     * @param callSign the CallSign string to add to the filter
     */
    public void addCallSign(String callSign) {
        this.callSign.add(callSign.toLowerCase());
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
     * Adds an Active criterion to the filter.
     *
     * @param active the Active boolean to add to the filter
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    // Getters

    /**
     * Returns the Active boolean criteria.
     *
     * @return the Active boolean criteria
     */
    public Boolean getActive() {
        return active;
    }


    /**
     * Returns the HashSet of Country string criteria.
     *
     * @return the HashSet of Country string criteria
     */
    public HashSet<String> getCountry() {
        return country;
    }


    /**
     * Returns the HashSet of Alias string criteria.
     *
     * @return the HashSet of Alias string criteria
     */
    public HashSet<String> getAlias() {
        return alias;
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
     * Returns the HashSet of Name string criteria.
     *
     * @return the HashSet of Name string criteria
     */
    public HashSet<String> getName() {
        return name;
    }


    /**
     * Returns the HashSet of CallSign string criteria.
     *
     * @return the HashSet of CallSign string criteria
     */
    public HashSet<String> getCallSign() {
        return callSign;
    }
}
