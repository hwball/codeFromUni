package seng202.group6.importer;

import java.io.BufferedReader;
import java.util.ArrayList;

import seng202.group6.model.Flight;
import seng202.group6.model.FlightPoint;

import static seng202.group6.utils.Verify.validateInteger;
import static seng202.group6.utils.Verify.validateLatitude;
import static seng202.group6.utils.Verify.validateLongitude;

/**
 * This class is for importing flight csv files and converting them into a Flight. This class will
 * also create a list of lines (with errors) that are in the incorrect format.
 */
public class FlightImporter extends DataImporter<FlightPoint> {

    /**
     * The number of fields expected in an input line.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int FLIGHT_FIELDS = 5;

    /**
     * The index of the type field in the input line
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int TYPE = 0;
    /**
     * The index of the id field in the input line
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int ID = 1;
    /**
     * The index of the alt field in the input line
     */
    private final int ALT = 2;
    /**
     * The index of the latitude field in the input line
     */
    private final int LAT = 3;
    /**
     * The index of the longitude field in the input line
     */
    private final int LONG = 4;


    /**
     * Constructs a new FlightImporter that readers from br.
     *
     * @param br the file in the form of a BufferedReader
     */
    public FlightImporter(BufferedReader br) {
        super(br);
    }


    /**
     * converts a line into a FlightPoint
     *
     * @param split the comma separated line read from br
     * @return a new FlightPoint generated from the fields
     */
    @Override
    protected FlightPoint readLine(String[] split) {
        return new FlightPoint(split[TYPE], split[ID], Double.parseDouble(split[ALT]),
                Double.parseDouble(split[LAT]), Double.parseDouble(split[LONG]));
    }


    /**
     * Reads from the BufferedReader, br, given to the constructor and returns an
     * ArrayList&lt;FlightPoint&gt; which has been read from br
     *
     * @return an ArrayList of FlightPoint that has been translated from the BufferedReader br.
     */
    public Flight getFlight() {
        ArrayList<FlightPoint> flight = new ArrayList<>();
        while (sc.hasNext()) {
            String line = sc.next();
            String[] split = line.split(",");
            if (validateLine(split, line)) {
                flight.add(readLine(split));
            }
        }
        return new Flight(flight);
    }


    /**
     * This method is method is replaced by the getFlight method.
     *
     * @return new UnsupportedOperationException()
     */
    @Override
    public ArrayList<FlightPoint> getAll() {
        throw new UnsupportedOperationException();
    }


    /**
     * Determines if a line is a valid representation of a FlightPoint
     *
     * @param line the line read from the br.
     * @return if a line is a valid or not
     */
    @Override
    protected Boolean validateLine(String[] split, String line) {
        final String ALT_ERROR = "Expected a number (Altitude)";
        final String LAT_ERROR = "Expected a number (Latitude)";
        final String LONG_ERROR = "Expected a number (Longitude)";
        final String FIELDS_ERROR = "Expected 5 parameters";


        if (split.length == FLIGHT_FIELDS) {
            if (!validateInteger(split[ALT])) {
                invalidRecords.add(new String[]{ALT_ERROR, line});
                return false;
            } else if (!validateLatitude(split[LAT])) {
                invalidRecords.add(new String[]{LAT_ERROR, line});
                return false;
            } else if (!validateLongitude(split[LONG])) {
                invalidRecords.add(new String[]{LONG_ERROR, line});
                return false;
            }
            return true;
        }
        invalidRecords.add(new String[]{FIELDS_ERROR, line});
        return false;
    }
}
