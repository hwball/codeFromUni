package mock.model;

import mock.model.wind.ConstantWindGenerator;
import mock.model.wind.WindGenerator;
import org.junit.Before;
import org.junit.Test;
import shared.model.Bearing;
import shared.model.Wind;

import static org.junit.Assert.*;


/**
 * Tests the {@link ConstantWindGenerator}.
 */
public class ConstantWindGeneratorTest {

    WindGenerator windGenerator;
    Bearing windBearing;
    double windSpeedKnots;

    @Before
    public void setUp() throws Exception {

        windBearing = Bearing.fromDegrees(78.5);
        windSpeedKnots = 18.54;

        windGenerator = new ConstantWindGenerator(windBearing, windSpeedKnots);

    }


    /**
     * Tests if the {@link WindGenerator#generateBaselineWind()} function works.
     */
    @Test
    public void generateBaselineTest() {

        int repetitions = 100;

        for (int i = 0; i < repetitions; i++) {

            Wind wind = windGenerator.generateBaselineWind();

            assertEquals(windBearing.degrees(), wind.getWindDirection().degrees(), 0.01);
            assertEquals(windSpeedKnots, wind.getWindSpeed(), 0.01);

        }

    }


    /**
     * Tests if the {@link WindGenerator#generateNextWind(Wind)} ()} function works.
     */
    @Test
    public void generateNextWindTest() {

        int repetitions = 100;

        Wind wind = windGenerator.generateBaselineWind();

        for (int i = 0; i < repetitions; i++) {

            wind = windGenerator.generateNextWind(wind);

            assertEquals(windBearing.degrees(), wind.getWindDirection().degrees(), 0.01);
            assertEquals(windSpeedKnots, wind.getWindSpeed(), 0.01);

        }

    }

}
