package mock.dataInput;


import mock.exceptions.InvalidPolarFileException;
import mock.model.Polars;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the polar parser.
 */
public class PolarParserTest {



    public static Polars createPolars() {
        Polars polars = PolarParser.parse("mock/polars/acc_polars.csv");
        return polars;
    }


    /**
     * Tests if we can parse a valid polar data file (stored in a string), and create a polar table.
     */
    @Test
    public void testParseValidFile() {

        try {
            //Parse data file.
            Polars polars = PolarParser.parse("mock/polars/acc_polars.csv");

        } catch (InvalidPolarFileException e) {
            fail("Couldn't parse polar file.");
        }

    }

    /**
     * Tests if we can parse an invalid polar data file - it is expected that parsing will fail.
     */
    @Test
    public void testParseInvalidFile() {

        try {
            //Parse data file.
            Polars polars = PolarParser.parse("mock/polars/invalid_polars.csv");

            //As the file is invalid, we shouldn't reach here.
            fail("exception should have been thrown, but wasn't.");

        } catch (InvalidPolarFileException e) {
            assertTrue(true);
        }

    }

    /**
     * Tests if we can parse an non-existent polar data file - it is expected that parsing will fail.
     */
    @Test
    public void testParseNonExistentFile() {

        try {
            //Parse data file.
            Polars polars = PolarParser.parse("mock/polars/this_does_not_exist.csv");

            //As the file doesn't exist, we shouldn't reach here.
            fail("exception should have been thrown, but wasn't.");

        } catch (InvalidPolarFileException e) {
            assertTrue(true);
        }

    }




}
