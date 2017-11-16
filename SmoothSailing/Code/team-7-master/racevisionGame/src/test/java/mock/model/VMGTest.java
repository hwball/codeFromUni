package mock.model;

import org.junit.Before;
import org.junit.Test;
import shared.model.Bearing;

import static org.junit.Assert.assertEquals;

public class VMGTest {

    /**
     * VMG object to test.
     */
    private VMG vmg;

    /**
     * Speed of the VMG, in knots.
     */
    private double speed;

    private double speedEpsilon = 0.001;

    /**
     * Bearing of the vmg.
     */
    private Bearing bearing;

    private double bearingDegreeEpsilon = 0.001;



    /**
     * Constructs a VMG object.
     */
    @Before
    public void setUp() {

        speed = 14.98;

        bearing = Bearing.fromDegrees(78.5);


        vmg = new VMG(speed, bearing);
    }


    @Test
    public void testVMGSpeed() {

        assertEquals(speed, vmg.getSpeed(), speedEpsilon);
    }

    @Test
    public void testVMGBearing() {

        assertEquals(bearing.degrees(), vmg.getBearing().degrees(), bearingDegreeEpsilon);
    }
}
