package seng202.group6.importer;

import java.io.BufferedReader;

import seng202.group6.model.Airport;

import static seng202.group6.utils.Verify.validateAirportIATA;
import static seng202.group6.utils.Verify.validateAirportICAO;
import static seng202.group6.utils.Verify.validateDaylightSavingTime;
import static seng202.group6.utils.Verify.validateInteger;
import static seng202.group6.utils.Verify.validateLatitude;
import static seng202.group6.utils.Verify.validateLongitude;
import static seng202.group6.utils.Verify.validateName;
import static seng202.group6.utils.Verify.validateTimezone;
import static seng202.group6.utils.Verify.validateTimezoneDB;

/**
 * This class is for importing airport files and converting them into a ArrayList of Airports.
 * This class will also create a list of lines that are in the incorrect format.
 */
public class AirportImporter extends DataImporter<Airport> {

    /**
     * The number of fields expected in an input line.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int AIRPORT_FIELDS = 12;

    /**
     * The index of the name field in the input line
     */
    private final int NAME = 1;
    /**
     * The index of the city field in the input line
     */
    private final int CITY = 2;
    /**
     * The index of the country field in the input line
     */
    private final int COUNTRY = 3;
    /**
     * The index of the IATA field in the input line
     */
    private final int IATA = 4;
    /**
     * The index of the ICAO field in the input line
     */
    private final int ICAO = 5;
    /**
     * The index of the latitude field in the input line
     */
    private final int LATITUDE = 6;
    /**
     * The index of the longitude field in the input line
     */
    private final int LONGITUDE = 7;
    /**
     * The index of the altitude field in the input line
     */
    private final int ALTITUDE = 8;
    /**
     * The index of the time zone field in the input line
     */
    private final int TIME_ZONE = 9;
    /**
     * The index of the daylight saving time field in the input line
     */
    private final int DST = 10;
    /**
     * The index of the time zone database field in the input line
     */
    private final int TIME_ZONE_DATABASE = 11;


    /**
     * Constructs a new AirportImporter that readers from br.
     *
     * @param br the input file
     */
    public AirportImporter(BufferedReader br) {
        super(br);
    }


    /**
     * Reads an Airport from the given fields.
     *
     * @param fields the fields parsed from the input line
     * @return a airport defined by the fields
     */
    @Override
    protected Airport readLine(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equals("")) {
                continue;
            } else if (fields[i].charAt(0) == '"' && fields[i].charAt(fields[i].length() - 1) == '"') {
                fields[i] = fields[i].substring(1, fields[i].length() - 1);
            }
            fields[i] = fields[i].replaceAll("\\\\N", "");
            fields[i] = fields[i].replaceAll("\\\\'", "'");
        }
        return new Airport(fields[NAME], fields[CITY], fields[COUNTRY], fields[IATA], fields[ICAO],
                Double.parseDouble(fields[LATITUDE]), Double.parseDouble(fields[LONGITUDE]),
                Double.parseDouble(fields[ALTITUDE]), fields[TIME_ZONE], fields[DST], fields[TIME_ZONE_DATABASE]);
    }


    /**
     * Determines if a line is a valid representation of a Airport
     *
     * @return true if the line is a valid or not
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected Boolean validateLine(String[] airport, String line) {
        // error messages
        final String FIELDS_ERROR = "Expected 12 parameters";
        final String LONG_ERROR = "Expected 180 >= Longitude >= -180";
        final String LAT_ERROR = "Expected 90 >= Latitude >= -90";
        final String ALT_ERROR = "Expected a number (Altitude)";
        final String IATA_ERROR = "Expected three letter code or blank (IATA/FAA)";
        final String ICAO_ERROR = "Expected four letter code or blank (ICAO)";
        final String DST_ERROR = "Expected one of E, A, S, O, Z, N, or U (DST)";
        final String DUPLICATE_ERROR = "Duplicate line";
        final String COUNTRY_ERROR = "(Country)";
        final String NAME_ERROR = "(Name)";
        final String CITY_ERROR = "(City)";
        final String TIME_ZONE_DATABASE_ERROR = "Unrecognised tz database string (Time Zone Database)";
        final String TIMEZONE_ERROR = "Expected time zone between -14 and 14";

        if (airport.length == AIRPORT_FIELDS) {

            //verify name
            if (!validateName(airport[NAME])) {
                invalidRecords.add(new String[]{NAME_ERROR, line});
                return false;

                //verify name
            } else if (!validateName(airport[CITY])) {
                invalidRecords.add(new String[]{CITY_ERROR, line});
                return false;

                //verify IATA
            } else if (!validateAirportIATA(airport[IATA])) {
                invalidRecords.add(new String[]{IATA_ERROR, line});
                return false;

                //verify ICAO
            } else if (!validateAirportICAO(airport[ICAO])) {
                invalidRecords.add(new String[]{ICAO_ERROR, line});
                return false;

                //verify latitude
            } else if (!validateName(airport[COUNTRY])) {
                invalidRecords.add(new String[]{COUNTRY_ERROR, line});
                return false;

                //verify latitude
            } else if (!validateLatitude(airport[LATITUDE])) {
                invalidRecords.add(new String[]{LAT_ERROR, line});
                return false;

                //verify longitude
            } else if (!validateLongitude(airport[LONGITUDE])) {
                invalidRecords.add(new String[]{LONG_ERROR, line});
                return false;

                //verify altitude
            } else if (!validateInteger(airport[ALTITUDE])) {
                invalidRecords.add(new String[]{ALT_ERROR, line});
                return false;

                //verify DST
            } else if (!validateDaylightSavingTime(airport[DST])) {
                invalidRecords.add(new String[]{DST_ERROR, line});
                return false;

                //verify timezone
            } else if (!validateTimezone(airport[TIME_ZONE])) {
                invalidRecords.add(new String[]{TIMEZONE_ERROR, line});
                return false;

                //verify Timezone Database
            } else if (!validateTimezoneDB(airport[TIME_ZONE_DATABASE])) {
                invalidRecords.add(new String[]{TIME_ZONE_DATABASE_ERROR, line});
                return false;

                //check for duplicates
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
}
