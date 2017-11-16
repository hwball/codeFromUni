package shared.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by esa46 on 29/03/17.
 */
public class CompoundMarkTest {

    private GPSCoordinate ORIGIN_COORD;


    @Before
    public void setUp() throws Exception {
        ORIGIN_COORD = new GPSCoordinate(0, 0);
    }

    @Test
    public void averageOfSingleMarkAtOriginIsSingleMark() {

        CompoundMark testMark = new CompoundMark(
                1,
                "test",
                new Mark(1, "test origin 1", ORIGIN_COORD)  );

        assertTrue(testMark.getAverageGPSCoordinate().equals(ORIGIN_COORD) );

    }

    @Test
    public void averageOfSingleMarkIsSingleMark() {

        GPSCoordinate testCoord = new GPSCoordinate(20, 25);
        CompoundMark testMark = new CompoundMark(
                1,
                "test",
                new Mark(1, "test origin 1", testCoord));

        assertTrue(testMark.getAverageGPSCoordinate().equals(testCoord));

    }

    @Test
    public void averageLatOfTwoMarksIsAccurate() {

        GPSCoordinate testCoord = new GPSCoordinate(0.001, 0);
        CompoundMark testMark = new CompoundMark(
                1,
                "test",
                new Mark(1, "test origin 1", ORIGIN_COORD),
                new Mark(2, "test origin 2", testCoord) );

        assertEquals(testMark.getAverageGPSCoordinate(), new GPSCoordinate(0.0005, 0));

    }

    @Test
    public void averageLongOfTwoMarksIsAccurate() {

        GPSCoordinate testCoord = new GPSCoordinate(0, 10);
        CompoundMark testMark = new CompoundMark(
                1,
                "test",
                new Mark(1, "test origin 1", ORIGIN_COORD),
                new Mark(2, "test origin 2", testCoord) );

        assertTrue(testMark.getAverageGPSCoordinate().equals(new GPSCoordinate(0, 5)));
    }


    @Test
    public void averageLatAndLongOfTwoMarksIsAccurate() {

        GPSCoordinate testCoord1 = new GPSCoordinate(0.0, 30);
        GPSCoordinate testCoord2 = new GPSCoordinate(0.001, 60);

        CompoundMark testMark = new CompoundMark(
                1,
                "test",
                new Mark(1, "test origin 1", testCoord1),
                new Mark(2, "test origin 2", testCoord2)    );

        assertEquals(testMark.getAverageGPSCoordinate().getLatitude(), 0.00051776, 1e-8);
        assertEquals(testMark.getAverageGPSCoordinate().getLongitude(), 45.000000, 1e-8);
    }

}
