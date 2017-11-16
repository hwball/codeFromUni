package mock.model.wind;

import shared.model.Wind;


/**
 * Interface for wind generators. It allows for generating a baseline wind, and subsequent winds.
 */
public interface WindGenerator {



    /**
     * Generates a wind object using the baseline wind speed and bearing.
     * @return Baseline wind object.
     */
    Wind generateBaselineWind();



    /**
     * Generates the next Wind object, according to the implementation of the wind generator.
     * @param currentWind The current wind to change. This is not modified.
     * @return Generated wind object.
     */
    Wind generateNextWind(Wind currentWind);


}
