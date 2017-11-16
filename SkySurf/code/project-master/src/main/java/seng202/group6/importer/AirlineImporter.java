package seng202.group6.importer;

import java.io.BufferedReader;

import seng202.group6.model.Airline;

import static seng202.group6.utils.Verify.validateAirlineIATA;
import static seng202.group6.utils.Verify.validateAirlineICAO;
import static seng202.group6.utils.Verify.validateName;
import static seng202.group6.utils.Verify.validateYesNo;

/**
 * This class is for importing airline files and converting them into a ArrayList of Airlines.
 * This class will also create a list of lines that are in the incorrect format.
 */
public class AirlineImporter extends DataImporter<Airline> {

    /**
     * The number of fields expected in an input line.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int AIRLINE_FIELDS = 8;

    /**
     * The index of the name field in the input line
     */
    private final int NAME = 1;
    /**
     * The index of the alias field in the input line
     */
    private final int ALIAS = 2;
    /**
     * The index of the IATA field in the input line
     */
    private final int IATA = 3;
    /**
     * The index of the ICAO field in the input line
     */
    private final int ICAO = 4;
    /**
     * The index of the call sign field in the input line
     */
    private final int CALL_SIGN = 5;
    /**
     * The index of the country field in the input line
     */
    private final int COUNTRY = 6;
    /**
     * The index of the active field in the input line
     */
    private final int ACTIVE = 7;


    /**
     * Constructs a new AirlineImporter that reads from br.
     *
     * @param br the file in the form of a BufferedReader
     */
    public AirlineImporter(BufferedReader br) {
        super(br);
    }


    /**
     * Takes a line from br that has been split by commas and returns a Airline generated from the
     * line.
     *
     * @param fields a line from br that has been split by commas
     * @return the Airline generated from fields
     */
    @Override
    protected Airline readLine(String[] fields) { //this will only works on acceptable file formats
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equals("\\N")) {
                fields[i] = "";
            }
            fields[i] = fields[i].replaceAll("\\\\'", "'");
            fields[i] = fields[i].replaceAll("\\\\N", "");
        }
        return new Airline(fields[NAME], fields[ALIAS], fields[IATA], fields[ICAO], fields[CALL_SIGN],
                fields[COUNTRY], fields[ACTIVE].equals("Y"));
    }


    /**
     * Determines if a line is a valid representation of a Airline.
     *
     * @param airline Airline in a list form ready to be checked
     * @param line    the line as it appeared in the file
     * @return true if the line represents a valid Airline in the input format, else false
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected Boolean validateLine(String[] airline, String line) {
        // error messages
        final String FIELDS_ERROR = "Expected 9 parameters";
        final String ACTIVE_ERROR = "Expected either \"Y\" or \"N\" (Active)";
        final String IATA_ERROR = "Expected two letter code or blank (IATA/FAA)";
        final String ICAO_ERROR = "Expected three letter code or blank (ICAO)";
        final String DUPLICATE_ERROR = "Duplicate line";
        final String NAME_ERROR = "Expected String between 1 and 32 characters (Name)";
        final String ALIAS_ERROR = "Expected String between 0 and 32 characters (Alias)";
        final String COUNTRY_ERROR = "Expected String made up of Letters between 0 and 32 characters (Country)";
        final String CALL_SIGN_ERROR = "Expected String between 0 and 32 characters (Call sign)";

        //split based on comma only if there are 0 or an even number of quotes(") in front of the comma
        //ie ignore commas inside quotes
        if (airline.length == AIRLINE_FIELDS) {
            //Verify active
            if (!validateYesNo(airline[ACTIVE])) {
                invalidRecords.add(new String[]{ACTIVE_ERROR, line});
                return false;

                //Verify IATA
            } else if (!validateAirlineIATA(airline[IATA])) {
                invalidRecords.add(new String[]{IATA_ERROR, line});
                return false;

                //Verify ICAO
            } else if (!validateAirlineICAO(airline[ICAO])) {
                invalidRecords.add(new String[]{ICAO_ERROR, line});
                System.out.println("INVALID ICAO " + airline[ICAO]);
                return false;

                //Verify Call sign
            } else if (!validateName(airline[CALL_SIGN])) {
                invalidRecords.add(new String[]{CALL_SIGN_ERROR, line});
                return false;

                //Verify Name
            } else if (!validateName(airline[NAME])) {
                invalidRecords.add(new String[]{NAME_ERROR, line});
                return false;

                //Verify Alias
            } else if (!validateName(airline[ALIAS])) {
                invalidRecords.add(new String[]{ALIAS_ERROR, line});
                return false;

                //Verify Country
            } else if (!validateName(airline[COUNTRY])) {
                invalidRecords.add(new String[]{COUNTRY_ERROR, line});
                return false;

            } else if (duplicates.contains(line)) {
                invalidRecords.add(new String[]{DUPLICATE_ERROR, line});
                return false;
            }

            return true;
        }
        invalidRecords.add(new String[]{FIELDS_ERROR, line});
        return false;
    }
}
