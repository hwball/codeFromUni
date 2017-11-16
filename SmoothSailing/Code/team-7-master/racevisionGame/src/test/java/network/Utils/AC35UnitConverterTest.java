package network.Utils;

import org.junit.Test;

import static network.Utils.AC35UnitConverter.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the pack/unpack and conversion functions in {@link AC35UnitConverter}.
 */
public class AC35UnitConverterTest {


    /**
     * Tests if gps coordinates can be unpacked.
     */
    @Test
    public void testUnpackGPS(){
        assertTrue(unpackGPS(0) == 0);
        assertTrue(unpackGPS(Integer.MAX_VALUE) == (double)Integer.MAX_VALUE * 180.0 / Math.pow(2, 31));

    }


    /**
     * Tests if gps coodinates can be packed.
     */
    @Test
    public void testPackGPS(){
        assertTrue(packGPS(0) == 0);
        assertTrue(packGPS(180) == (int)2147483648.0);

    }


    /**
     * Tests if headings/bearings can be unpacked.
     */
    @Test
    public void testUnpackHeading(){
        assertTrue(unpackHeading(0) == 0);
        assertTrue(unpackHeading(65536) == 360.0);
    }

    /**
     * Tests if headings/bearings can be packed.
     */
    @Test
    public void testPackHeading(){
        assertTrue(packHeading(0) == 0);
        assertTrue(packHeading(360) == 65536);
    }


    /**
     * Tests if true wind angles (azimuths) can be unpacked.
     */
    @Test
    public void testUnpackTrueWindAngle(){
        assertEquals(unpackTrueWindAngle((short)0), 0, 0.001);
        assertEquals(unpackTrueWindAngle((short)32767), 180.0, 0.01);
    }

    /**
     * Tests if true wind angles (azimuths) can be packed.
     */
    @Test
    public void testPackTrueWindAngle(){
        assertTrue(packTrueWindAngle(0) == (short)0);
        assertTrue(packTrueWindAngle(180.0) == (short)32768);
    }


    /**
     * Tests if millimeters per second can be unpacked to knots.
     */
    @Test
    public void testUnpackMMperSecToKnots(){
        assertEquals(unpackMMperSecToKnots(0), 0d, 0.001);
        assertEquals(unpackMMperSecToKnots(7331), 14.25, 0.01);
    }

    /**
     * Tests if knots can be packed into millimeters per second.
     */
    @Test
    public void testPackKnotsToMMperSec(){
        assertEquals(packKnotsToMMperSec(0), 0, 1);
        assertEquals(packKnotsToMMperSec(7.44), 3828, 1);
    }

}
