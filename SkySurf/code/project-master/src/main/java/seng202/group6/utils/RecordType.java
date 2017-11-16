package seng202.group6.utils;


import java.util.Arrays;

/**
 * Enum for storing type and field of a record to be filtered on
 */
public enum RecordType {
    AIRPORT("Airport"),
    AIRLINE("Airline"),
    ROUTE("Route"),
    FLIGHT("Flight");

    private static final String[] AIRPORT_SEARCH_TYPES =
            {"Name", "City", "Country", "IATA/FAA", "ICAO", "Latitude", "Longitude", "Altitude",
                    "Time zone", "DST (Daylight Savings Time)", "Tz database", "Number of Routes"};
    private static final String[] AIRLINE_SEARCH_TYPES =
            {"Name", "Alias", "IATA", "ICAO", "Call sign", "Country", "Status"};
    private static final String[] ROUTE_SEARCH_TYPES =
            {"Airline", "Source Airport", "Destination Airport", "Codeshare", "Number of stops", "Equipment"};
    private static final String[] FLIGHT_SEARCH_TYPES =
            {"Source Airport", "Destination Airport"};
    private static final String[] RANGE_SEARCH_TYPES =
            {"Latitude", "Longitude", "Altitude", "Number of stops", "Number of Routes"};

    private final String name;

    RecordType(String name) {
        this.name = name;
    }


    /**
     * Returns the value of enum that the parameter (string) represents.
     *
     * @param string a string
     * @return the enum type or null for no match
     */
    public static RecordType parseName(String string) {
        for (RecordType type : RecordType.values()) {
            if (type.toString().equals(string)) {
                return type;
            }
        }
        return null;
    }


    /**
     * Checks whether the string represents one of the search types which is defined by a range.
     *
     * @param searchType the string
     * @return whether the string represents a range search type
     */
    public static boolean isRangeSearchType(String searchType) {
        return Arrays.asList(RANGE_SEARCH_TYPES).contains(searchType);
    }


    /**
     * Returns the name of the RecordType.
     *
     * @return the string representation of the RecordType
     */
    @Override
    public String toString() {
        return this.name;
    }


    /**
     * Gets the string representations of the filterable fields of the record type this
     * enum represents.
     *
     * @return the filterable fields for the record type
     */
    public String[] getSearchTypes() {
        String[] searchTypes = null;
        switch (this) {
            case AIRPORT:
                searchTypes = AIRPORT_SEARCH_TYPES;
                break;
            case AIRLINE:
                searchTypes = AIRLINE_SEARCH_TYPES;
                break;
            case ROUTE:
                searchTypes = ROUTE_SEARCH_TYPES;
                break;
            case FLIGHT:
                searchTypes = FLIGHT_SEARCH_TYPES;
                break;
        }
        return searchTypes;
    }
}
