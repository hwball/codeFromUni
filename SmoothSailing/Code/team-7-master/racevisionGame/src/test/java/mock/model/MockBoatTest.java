package mock.model;

import org.junit.Before;
import org.junit.Test;
import shared.model.Bearing;
import shared.model.Boat;
import shared.model.GPSCoordinate;
import shared.model.Mark;

import static org.junit.Assert.*;

public class MockBoatTest {
    private MockBoat boat;
    private Mark near;
    private Mark far;


    public static MockBoat createMockBoat() {
        Boat boat = new Boat(121, "Test boat", "TS");
        MockBoat mockBoat = new MockBoat(boat, null);
        return mockBoat;
    }

    @Before
    public void setUp() {
        boat = new MockBoat(0, "Bob", "NZ", null);
        boat.setPosition(new GPSCoordinate(0,0));
        boat.setBearing(Bearing.fromDegrees(180));

        near = new Mark(0, "Near", new GPSCoordinate(-.0001, 0));
        far = new Mark(0, "Far", new GPSCoordinate(.001, 0));
    }

    @Test
    public void nearMarkWithin100m() {
        assertTrue(near.rayCast(boat, 100));
    }

    @Test
    public void farMarkBeyond100m() {
        assertFalse(far.rayCast(boat, 100));
    }
}
