package mock.model;

import mock.model.wind.RandomWindGenerator;
import org.junit.Before;
import org.junit.Test;
import shared.model.Bearing;
import shared.model.Wind;

import static org.junit.Assert.*;


public class RandomWindGeneratorTest {


    private RandomWindGenerator randomWindGenerator;

    private Bearing windBaselineBearing;
    private Bearing windBearingLowerBound;
    private Bearing windBearingUpperBound;
    private double windBaselineSpeed;
    private double windSpeedLowerBound;
    private double windSpeedUpperBound;

    private double speedKnotsEpsilon;
    private double bearingDegreeEpsilon;


    @Before
    public void setUp() throws Exception {


        //Bounds.
        this.windBaselineBearing = Bearing.fromDegrees(88.3);
        this.windBearingLowerBound = Bearing.fromDegrees(66.5);
        this.windBearingUpperBound = Bearing.fromDegrees(248.6);
        this.windBaselineSpeed = 13;
        this.windSpeedLowerBound = 7;
        this.windSpeedUpperBound = 20;

        this.randomWindGenerator = new RandomWindGenerator(
                windBaselineBearing,
                windBearingLowerBound,
                windBearingUpperBound,
                windBaselineSpeed,
                windSpeedLowerBound,
                windSpeedUpperBound );

        this.bearingDegreeEpsilon = 0.001;
        this.speedKnotsEpsilon = 0.001;
    }


    /**
     * Tests if the baseline wind generated it accurate.
     */
    @Test
    public void generateBaselineWindTest() {

        Wind wind = randomWindGenerator.generateBaselineWind();

        assertEquals(windBaselineSpeed, wind.getWindSpeed(), speedKnotsEpsilon);
        assertEquals(windBaselineBearing.degrees(), wind.getWindDirection().degrees(), bearingDegreeEpsilon);

    }

    /**
     * Tests if the random wind generated is inside the bounds.
     */
    @Test
    public void generateRandomWindTest() {

        int randomWindCount = 1000;

        for (int i = 0; i < randomWindCount; i++) {

            Wind wind = randomWindGenerator.generateRandomWind();

            assertTrue(wind.getWindSpeed() >= windSpeedLowerBound);
            assertTrue(wind.getWindSpeed() <= windSpeedUpperBound);

            assertTrue(wind.getWindDirection().degrees() >= windBearingLowerBound.degrees());
            assertTrue(wind.getWindDirection().degrees() <= windBearingUpperBound.degrees());

        }

    }


    /**
     * Tests if the next wind generated is inside the bounds.
     */
    @Test
    public void generateNextWindTest() {

        Wind wind = randomWindGenerator.generateBaselineWind();

        int randomWindCount = 1000;

        for (int i = 0; i < randomWindCount; i++) {

            wind = randomWindGenerator.generateNextWind(wind);

            assertTrue(wind.getWindSpeed() >= windSpeedLowerBound);
            assertTrue(wind.getWindSpeed() <= windSpeedUpperBound);

            assertTrue(wind.getWindDirection().degrees() >= windBearingLowerBound.degrees());
            assertTrue(wind.getWindDirection().degrees() <= windBearingUpperBound.degrees());

        }

    }
}
