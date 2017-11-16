package seng202.group6.importer;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import seng202.group6.model.Record;

/**
 * The abstract super class for the Airline, Airport, and Route importers.
 */
public abstract class DataImporter<T extends Record> {

    protected final Scanner sc;
    protected final HashSet<String> duplicates;
    protected final ArrayList<String[]> invalidRecords;


    /**
     * Constructor for DataImporter. Data is read from br.
     *
     * @param br the input file
     */
    public DataImporter(BufferedReader br) {
        duplicates = new HashSet<>();
        invalidRecords = new ArrayList<>();
        sc = new Scanner(br);
        sc.useDelimiter("\n");
    }


    /**
     * Reads a file and returns an array list of all valid records from the file in the order they
     * appear in the file.
     *
     * @return all the valid records from the file
     */
    public ArrayList<T> getAll() {
        ArrayList<T> records = new ArrayList<>();
        while (sc.hasNext()) {
            String line = sc.next();
            //split based on comma only if there are 0 or an even number of quotes(") in front of the comma
            //ie ignore commas inside quotes
            String[] split = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].replaceAll("^\"|\"$", "");
                split[i] = split[i].replaceAll("\\\\N", "");
            }
            if (validateLine(split, line)) {
                records.add(readLine(split));
                duplicates.add(line);
            }
        }
        return records;
    }


    /**
     * Returns true if the record (split) is valid. Returns false if it is invalid and stores the
     * invalid record
     *
     * @param split the line of a record file split by commas
     * @param line  the line from the file that has just been read
     * @return true if valid, false otherwise
     */
    abstract protected Boolean validateLine(String[] split, String line);


    /**
     * Takes a line split by commas and transforms it into a record class.
     *
     * @param split the line of a record file split by commas
     * @return a record that the split line represents
     */
    abstract protected T readLine(String[] split);


    /**
     * Gets the invalid lines.
     *
     * @return the lines which were determined to be invalid after calling getAllRoute
     */
    public ArrayList<String[]> getInvalidRecords() {
        return invalidRecords;
    }
}
