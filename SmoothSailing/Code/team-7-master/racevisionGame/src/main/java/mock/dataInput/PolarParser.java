package mock.dataInput;



import mock.exceptions.InvalidPolarFileException;
import mock.model.NewPolars;
import mock.model.Polars;
import shared.model.Bearing;

import java.io.*;
import java.util.ArrayList;


/**
 * Responsible for parsing a polar data file, and creating a Polar data object.
 */
public class PolarParser {


    /**
     * Given a filename, this function parses it and generates a Polar object, which can be queried for polar information.
     * @param filename The filename to load and read data from (loaded as a resource).
     * @return A Polar table containing data from the given file.
     */
    public static Polars parse(String filename) throws InvalidPolarFileException {
        //Temporary table to return later.
        Polars polarTable = new Polars();


        //Open the file for reading.
        InputStream fileStream = PolarParser.class.getClassLoader().getResourceAsStream(filename);
        if (fileStream == null) {
            throw new InvalidPolarFileException("Could not open polar data file: " + filename);
        }
        //Wrap it with buffered input stream to set encoding and buffer.
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(fileStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InvalidPolarFileException("Unsupported encoding: UTF-8", e);
        }
        BufferedReader inputStream = new BufferedReader(in);


        //We expect the polar data file to have the column headings:
        // Tws, Twa0, Bsp0, Twa1, Bsp1, UpTwa, UpBsp, Twa2, Bsp2, Twa3, Bsp3, Twa4, Bsp4, Twa5, Bsp5, Twa6, Bsp6, DnTwa, DnBsp, Twa7, Bsp7
        //and to have 7 rows of data.
        //Angles are expected to be in degrees, and velocities in knots.


        //We read data rows, and split them into arrays of elements.
        ArrayList<String[]> dataRows = new ArrayList<>(7);
        try {
            //Heading row.
            //We skip the heading row by reading it.
            String headingRow = inputStream.readLine();

            //Data rows.
            while (inputStream.ready()) {
                //Read line.
                String dataRow = inputStream.readLine();

                //Split line.
                String[] dataElements = dataRow.split(",");

                //Add to collection.
                dataRows.add(dataElements);

            }

        } catch (IOException e) {
            throw new InvalidPolarFileException("Could not read from polar data file: " + filename, e);
        }

        //Finished reading in data, now we need to construct polar rows and table from it.
        //For each row...
        int rowNumber = 0;
        for (String[] row : dataRows) {

            //For each pair of columns (the pair is angle, speed).
            //We start at column 1 since column 0 is the wind speed column.
            for (int i = 1; i < row.length; i += 2) {

                //Add angle+speed=velocity estimate to polar table.
                try {

                    //Add the polar value to the polar table
                    double windSpeedKnots = Double.parseDouble(row[0]);
                    double angleDegrees = Double.parseDouble(row[i]);
                    Bearing angle = Bearing.fromDegrees(angleDegrees);
                    double boatSpeedKnots = Double.parseDouble(row[i + 1]);
                    polarTable.addEstimate(windSpeedKnots, angle, boatSpeedKnots);

                } catch (NumberFormatException e) {
                    throw new InvalidPolarFileException("Could not convert (Row,Col): (" + rowNumber + "," + i +") = " + row[i] + " to a double.", e);

                }
            }

            //Increment row number.
            rowNumber++;

        }

        return polarTable;
    }

    /**
     * Given a filename, this function parses it and generates a Polar object, which can be queried for polar information.
     * @param filename The filename to load and read data from (loaded as a resource).
     */
    public static void parseNewPolars(String filename) throws InvalidPolarFileException {
        NewPolars newPolars = new NewPolars();


        //Open the file for reading.
        InputStream fileStream = PolarParser.class.getClassLoader().getResourceAsStream(filename);
        if (fileStream == null) {
            throw new InvalidPolarFileException("Could not open polar data file: " + filename);
        }
        //Wrap it with buffered input stream to set encoding and buffer.
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(fileStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InvalidPolarFileException("Unsupported encoding: UTF-8", e);
        }
        BufferedReader inputStream = new BufferedReader(in);


        //We expect the polar data file to have the column headings:
        // Tws, Twa0, Bsp0, Twa1, Bsp1, UpTwa, UpBsp, Twa2, Bsp2, Twa3, Bsp3, Twa4, Bsp4, Twa5, Bsp5, Twa6, Bsp6, DnTwa, DnBsp, Twa7, Bsp7
        //and to have 7 rows of data.
        //Angles are expected to be in degrees, and velocities in knots.


        //We read data rows, and split them into arrays of elements.
        ArrayList<String[]> dataRows = new ArrayList<>(7);
        try {
            //Heading row.
            //We skip the heading row by reading it.
            String headingRow = inputStream.readLine();

            //Data rows.
            while (inputStream.ready()) {
                //Read line.
                String dataRow = inputStream.readLine();

                //Split line.
                String[] dataElements = dataRow.split(",");

                //Add to collection.
                dataRows.add(dataElements);

            }

        } catch (IOException e) {
            throw new InvalidPolarFileException("Could not read from polar data file: " + filename, e);
        }

        //Finished reading in data, now we need to construct polar rows and table from it.
        //For each row...
        int rowNumber = 0;
        for (String[] row : dataRows) {

            //For each pair of columns (the pair is angle, speed).
            //We start at column 1 since column 0 is the wind speed column.
            for (int i = 1; i < row.length; i += 2) {

                //Add angle+speed=velocity estimate to polar table.
                try {

                    //Add the polar value to the polar table
                    double windSpeedKnots = Double.parseDouble(row[0]);
                    double angleDegrees = Double.parseDouble(row[i]);
                    Bearing angle = Bearing.fromDegrees(angleDegrees);
                    double boatSpeedKnots = Double.parseDouble(row[i + 1]);
                    newPolars.addPolars(windSpeedKnots, angle, boatSpeedKnots);

                } catch (NumberFormatException e) {
                    throw new InvalidPolarFileException("Could not convert (Row,Col): (" + rowNumber + "," + i +") = " + row[i] + " to a double.", e);

                }
            }

            //Increment row number.
            rowNumber++;

        }
        newPolars.linearInterpolatePolars();

    }

}
