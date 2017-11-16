package mock.model;

import mock.dataInput.PolarParser;
import mock.exceptions.InvalidPolarFileException;
import org.junit.Before;
import org.junit.Test;
import shared.model.Bearing;

import static org.junit.Assert.*;


/**
 * Tests the polars class, to see if various VMGs can be calculated.
 */
public class PolarsTest {

    /**
     * The polars table used in each test.
     */
    private Polars polars = null;

    /**
     * The epsilon, in degrees, for computed bearing angles.
     */
    private double angleEpsilon = 2;

    /**
     * The epsilon, in knots, for computed speeds.
     */
    private double speedEpsilon = 0.5;


    /**
     * Creates the Polars object for the tests.
     */
    @Before
    public void setUp() {
        //Read data.
        try {
            //Parse data file.
            polars = PolarParser.parse("mock/polars/acc_polars.csv");
        }
        catch (InvalidPolarFileException e) {
            fail("Couldn't parse polar file.");
        }
    }




    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG1() {

        //Test 1.
        //This test has a wind speed that is between two values from the table (12kn, 16kn, this is 15.9kn).
        Bearing windAngle1 = Bearing.fromDegrees(31.5);
        Bearing destAngle1 = Bearing.fromDegrees(65.32);
        double windSpeed1 = 15.9;//knots
        Bearing vmgAngle1 = Bearing.fromDegrees(72.4);
        double vmgSpeed1 = 30.4;

        VMG calcVMG1 = polars.calculateVMG(windAngle1, windSpeed1, destAngle1, Bearing.fromDegrees(0), Bearing.fromDegrees(359.9));
        Bearing calcVMGAngle1 = calcVMG1.getBearing();
        double calcVMGSpeed1 = calcVMG1.getSpeed();


        assertEquals(calcVMGAngle1.degrees(), vmgAngle1.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed1, vmgSpeed1, speedEpsilon);

    }



    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG2() {
        //Test 2.
        //This test has a wind speed much larger than any in the table (max from table is 30kn, this is 40kn).
        Bearing windAngle2 = Bearing.fromDegrees(200);
        Bearing destAngle2 = Bearing.fromDegrees(35);
        double windSpeed2 = 40;//knots
        Bearing vmgAngle2 = Bearing.fromDegrees(69);
        double vmgSpeed2 = 32.8;

        VMG calcVMG2 = polars.calculateVMG(windAngle2, windSpeed2, destAngle2, Bearing.fromDegrees(0), Bearing.fromDegrees(359.9));
        Bearing calcVMGAngle2 = calcVMG2.getBearing();
        double calcVMGSpeed2 = calcVMG2.getSpeed();


        assertEquals(calcVMGAngle2.degrees(), vmgAngle2.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed2, vmgSpeed2, speedEpsilon);

    }


    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG3() {

        //Test 3.
        //This test has a wind speed lower than any non-zero values from the table (table has 0kn, 4kn, this is 2kn).
        Bearing windAngle3 = Bearing.fromDegrees(345);
        Bearing destAngle3 = Bearing.fromDegrees(199);
        double windSpeed3 = 2;//knots
        Bearing vmgAngle3 = Bearing.fromDegrees(222);
        double vmgSpeed3 = 4.4;

        VMG calcVMG3 = polars.calculateVMG(windAngle3, windSpeed3, destAngle3, Bearing.fromDegrees(0), Bearing.fromDegrees(359.9));
        Bearing calcVMGAngle3 = calcVMG3.getBearing();
        double calcVMGSpeed3 = calcVMG3.getSpeed();


        assertEquals(calcVMGAngle3.degrees(), vmgAngle3.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed3, vmgSpeed3, speedEpsilon);

    }


    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG4() {
        //Test 4.
        //This test has a wind speed of 0.
        Bearing windAngle4 = Bearing.fromDegrees(5);
        Bearing destAngle4 = Bearing.fromDegrees(100);
        double windSpeed4 = 0;//knots
        Bearing vmgAngle4 = Bearing.fromDegrees(100);
        double vmgSpeed4 = 0;

        VMG calcVMG4 = polars.calculateVMG(windAngle4, windSpeed4, destAngle4, Bearing.fromDegrees(0), Bearing.fromDegrees(359.9));
        Bearing calcVMGAngle4 = calcVMG4.getBearing();
        double calcVMGSpeed4 = calcVMG4.getSpeed();


        assertEquals(calcVMGAngle4.degrees(), vmgAngle4.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed4, vmgSpeed4, speedEpsilon);

    }


    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG5() {

        //Test 5.
        //This test has a bearing bound of [55, 70), which only contains a suboptimal VMG.
        Bearing windAngle5 = Bearing.fromDegrees(5);
        Bearing destAngle5 = Bearing.fromDegrees(100);
        double windSpeed5 = 9;//knots
        Bearing vmgAngle5 = Bearing.fromDegrees(70);
        double vmgSpeed5 = 15;
        Bearing bearingUpperBound5 = Bearing.fromDegrees(70);
        Bearing bearingLowerBound5 = Bearing.fromDegrees(55);

        VMG calcVMG5 = polars.calculateVMG(windAngle5, windSpeed5, destAngle5, bearingLowerBound5, bearingUpperBound5);
        Bearing calcVMGAngle5 = calcVMG5.getBearing();
        double calcVMGSpeed5 = calcVMG5.getSpeed();


        assertEquals(calcVMGAngle5.degrees(), vmgAngle5.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed5, vmgSpeed5, speedEpsilon);
        assertTrue(calcVMGAngle5.degrees() >= bearingLowerBound5.degrees());
        assertTrue(calcVMGAngle5.degrees() <= bearingUpperBound5.degrees());

    }



    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG6() {

        //Test 6.
        //This test has a bearing bound of [70, 55), which has a lower bound > upper bound, which is complementary to [55, 70).
        Bearing windAngle6 = Bearing.fromDegrees(5);
        Bearing destAngle6 = Bearing.fromDegrees(100);
        double windSpeed6 = 11;//knots
        Bearing vmgAngle6 = Bearing.fromDegrees(92.85);
        double vmgSpeed6 = 20.086;
        Bearing bearingUpperBound6 = Bearing.fromDegrees(55);
        Bearing bearingLowerBound6 = Bearing.fromDegrees(70);

        VMG calcVMG6 = polars.calculateVMG(windAngle6, windSpeed6, destAngle6, bearingLowerBound6, bearingUpperBound6);
        Bearing calcVMGAngle6 = calcVMG6.getBearing();
        double calcVMGSpeed6 = calcVMG6.getSpeed();


        assertEquals(calcVMGAngle6.degrees(), vmgAngle6.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed6, vmgSpeed6, speedEpsilon);
        if (bearingLowerBound6.degrees() > bearingUpperBound6.degrees()) {
            assertTrue((calcVMGAngle6.degrees() >= bearingLowerBound6.degrees()) || (calcVMGAngle6.degrees() <= bearingUpperBound6.degrees()));
        } else {
            assertTrue(calcVMGAngle6.degrees() >= bearingLowerBound6.degrees());
            assertTrue(calcVMGAngle6.degrees() <= bearingUpperBound6.degrees());
        }

    }


    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG7() {


        //Test 7.
        //This test has a bearing bound of [340, 5), which has a lower bound > upper bound, which is complementary to [5, 340).
        Bearing windAngle7 = Bearing.fromDegrees(340);
        Bearing destAngle7 = Bearing.fromDegrees(30);
        double windSpeed7 = 7;//knots
        Bearing vmgAngle7 = Bearing.fromDegrees(5);
        double vmgSpeed7 = 11;
        Bearing bearingUpperBound7 = Bearing.fromDegrees(5);
        Bearing bearingLowerBound7 = Bearing.fromDegrees(340);

        VMG calcVMG7 = polars.calculateVMG(windAngle7, windSpeed7, destAngle7, bearingLowerBound7, bearingUpperBound7);
        Bearing calcVMGAngle7 = calcVMG7.getBearing();
        double calcVMGSpeed7 = calcVMG7.getSpeed();


        assertEquals(calcVMGAngle7.degrees(), vmgAngle7.degrees(), angleEpsilon);
        assertEquals(calcVMGSpeed7, vmgSpeed7, speedEpsilon);
        if (bearingLowerBound7.degrees() > bearingUpperBound7.degrees()) {
            assertTrue((calcVMGAngle7.degrees() >= bearingLowerBound7.degrees()) || (calcVMGAngle7.degrees() <= bearingUpperBound7.degrees()));

        } else {
            assertTrue(calcVMGAngle7.degrees() >= bearingLowerBound7.degrees());
            assertTrue(calcVMGAngle7.degrees() <= bearingUpperBound7.degrees());

        }

    }


    /**
     * Tests if we can calculate VMG for a variety of values.
     */
    @Test
    public void testVMG8() {
        //Test 8.
        //This test has a bearing bound of [340, 5), which has a lower bound > upper bound, which is complementary to [5, 340). Due to the wind, dest angles, and bearing bounds, it cannot actually find a VMG > 0 (valid VMGs will actually be in the angle interval [10, 190]), so it will return the VMG(angle=0, speed=0).
        Bearing windAngle8 = Bearing.fromDegrees(5);
        Bearing destAngle8 = Bearing.fromDegrees(100);
        double windSpeed8 = 7;//knots
        Bearing vmgAngle8 = Bearing.fromDegrees(0);
        double vmgSpeed8 = 0;
        Bearing bearingUpperBound8 = Bearing.fromDegrees(5);
        Bearing bearingLowerBound8 = Bearing.fromDegrees(340);

        VMG calcVMG8 = polars.calculateVMG(windAngle8, windSpeed8, destAngle8, bearingLowerBound8, bearingUpperBound8);
        Bearing calcVMGAngle8 = calcVMG8.getBearing();
        double calcVMGSpeed8 = calcVMG8.getSpeed();


        assertEquals(calcVMGAngle8.degrees(), vmgAngle8.degrees(), 0);
        assertEquals(calcVMGSpeed8, vmgSpeed8, 0);

    }

}
