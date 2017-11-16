package seng202.group6.importer;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

import seng202.group6.model.Route;

import static seng202.group6.utils.Verify.validateAirlineIATA;
import static seng202.group6.utils.Verify.validateAirlineICAO;
import static seng202.group6.utils.Verify.validateAirportIATA;
import static seng202.group6.utils.Verify.validateAirportICAO;
import static seng202.group6.utils.Verify.validateInteger;
import static seng202.group6.utils.Verify.validateRouteEquipmentList;
import static seng202.group6.utils.Verify.validateYesNo;

/**
 * This class is for importing route files and converting them into a ArrayList of Routes. This
 * class will also create a list of lines (with errors) that are in the incorrect format.
 */
public class RouteImporter extends DataImporter<Route> {

    /**
     * The number of fields expected in an input line.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int ROUTE_FIELDS = 9;

    /**
     * The index of the airline field in the input line
     */
    private final int AIRLINE = 0;
    /**
     * The index of the source field in the input line
     */
    private final int SOURCE_AIRPORT = 2;
    /**
     * The index of the destination field in the input line
     */
    private final int DESTINATION_AIRPORT = 4;
    /**
     * The index of the code share field in the input line
     */
    private final int CODE_SHARE = 6;
    /**
     * The index of the stops field in the input line
     */
    private final int STOPS = 7;
    /**
     * The index of the equipment field in the input line
     */
    private final int EQUIPMENT = 8;


    /**
     * Constructs a new RouteImporter that readers from br.
     *
     * @param br the file in the form of a BufferedReader
     */
    public RouteImporter(BufferedReader br) {
        super(br);
    }


    /**
     * converts a line into a Route
     *
     * @return converted Route
     */
    @Override
    protected Route readLine(String[] fields) {
        if (fields.length == ROUTE_FIELDS) {
            return new Route(fields[AIRLINE], fields[SOURCE_AIRPORT], fields[DESTINATION_AIRPORT],
                    parseCodeShare(fields[CODE_SHARE]), Integer.parseInt(fields[STOPS]),
                    new ArrayList<>(Arrays.asList(fields[EQUIPMENT].trim().split(" +"))));
        } else { // equipment field is left out
            return new Route(fields[AIRLINE], fields[SOURCE_AIRPORT], fields[DESTINATION_AIRPORT],
                    parseCodeShare(fields[CODE_SHARE]), Integer.parseInt(fields[STOPS]), new ArrayList<>());
        }
    }


    /**
     * Determines if a line is a valid representation of a Route
     *
     * @return if a line is valid or not
     */
    @Override
    protected Boolean validateLine(String[] route, String line) {
        final String FIELDS_ERROR = "Expected 9 parameters";
        final String EQUIP_ERROR = "Expected space separated 3 letter codes (Equipment)";
        final String CODE_SHARE_ERROR = "Expected \"\" or \"Y\" (Code share)";
        final String STOP_ERROR = "Expected a integer (Stops)";
        final String SOURCE_ERROR = "Expected 3 letter IATA or 4 letter ICAO (Source Airport)";
        final String DEST_ERROR = "Expected 3 letter IATA or 4 letter ICAO (Destination Airport)";
        final String AIRLINE_ERROR = "Expected 2 letter IATA or 3-letter ICAO (Airline)";
        final String DUPLICATE_ERROR = "Duplicate line";

        if (route.length == ROUTE_FIELDS) {
            if (!validateRouteEquipmentList(route[EQUIPMENT].trim())) {
                invalidRecords.add(new String[]{EQUIP_ERROR, line});
                return false;
            }
        }

        // minus one is for when the equipment field is left blank
        if (route.length == ROUTE_FIELDS || route.length == ROUTE_FIELDS - 1) {
            if (!validateYesNo(route[CODE_SHARE])) {
                invalidRecords.add(new String[]{CODE_SHARE_ERROR, line});
                return false;
            } else if (!validateInteger(route[STOPS])) {
                invalidRecords.add(new String[]{STOP_ERROR, line});
                return false;
            } else if (!(validateAirportIATA(route[SOURCE_AIRPORT]) ||
                    validateAirportICAO(route[SOURCE_AIRPORT]))) {
                invalidRecords.add(new String[]{SOURCE_ERROR, line});
                return false;
            } else if (!(validateAirportIATA(route[DESTINATION_AIRPORT]) ||
                    validateAirportICAO(route[DESTINATION_AIRPORT]))) {
                invalidRecords.add(new String[]{DEST_ERROR, line});
                return false;
            } else if (!(validateAirlineIATA(route[AIRLINE]) ||
                    validateAirlineICAO(route[AIRLINE]))) {
                invalidRecords.add(new String[]{AIRLINE_ERROR, line});
                return false;
            } else if (duplicates.contains(line)) {
                invalidRecords.add(new String[]{DUPLICATE_ERROR, line});
                return false;
            }
            return true;
        } else {
            invalidRecords.add(new String[]{FIELDS_ERROR, line});
            return false;
        }
    }


    /**
     * Converts a code share string (either Y or nothing usually) into a boolean
     *
     * @param codeShare the string to parse
     * @return Boolean version of code share
     */
    private static boolean parseCodeShare(String codeShare) {
        return codeShare.toUpperCase().equals("Y");
    }
}
