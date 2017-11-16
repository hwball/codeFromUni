package mock.model.wind;


import shared.model.Bearing;
import shared.model.Wind;

/**
 * This class generates Wind objects for use in a MockRace.
 * Initialised with a baseline wind speed and direction, and keeps it constant.
 */
public class ConstantWindGenerator implements WindGenerator {

    /**
     * The bearing the wind direction starts at.
     */
    private Bearing windBaselineBearing;


    /**
     * The speed the wind starts at, in knots.
     */
    private double windBaselineSpeed;



    /**
     * Creates a constant wind generator, with a baseline wind speed and direction.
     * @param windBaselineBearing Baseline wind direction.
     * @param windBaselineSpeed Baseline wind speed, in knots.
     */
    public ConstantWindGenerator(Bearing windBaselineBearing, double windBaselineSpeed) {

        this.windBaselineBearing = windBaselineBearing;
        this.windBaselineSpeed = windBaselineSpeed;

    }


    @Override
    public Wind generateBaselineWind() {
        return new Wind(windBaselineBearing, windBaselineSpeed);
    }


    @Override
    public Wind generateNextWind(Wind currentWind) {

        return generateBaselineWind();

    }

}
