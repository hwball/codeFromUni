package mock.model.wind;

import shared.model.Bearing;
import shared.model.Wind;

import java.util.Random;

public class ShiftingWindGenerator implements WindGenerator {
    private Bearing baselineBearing;
    private double baseLineSpeed;
    private double windSpeedVariance = 5;
    private double bearingVariance = 5;  // In degrees
    private double oscillationVariance = 0.25;
    private double oscillationPeriod = 1e3 * 60 * 1;  // In milliseconds
    private double shiftTime = 1e3 * 60;
    private double shiftedSoFar = 0;

    private double timeOfLastOscillationReset = 0;
    private double timeOfLastChange = 0;
    private double timeOfLastShift = 0; // Back / veer

    private boolean anticlockwise = false;
    private boolean shiftAnticlockwise = false;//true for Back, false for veer
    private boolean shiftThisRace = Math.random() > 0.5;

    /**
     * Constructor
     * @param baselineBearing baseline bearing for wind
     * @param baseLineSpeed base line speed for wind
     */
    public ShiftingWindGenerator(Bearing baselineBearing, double baseLineSpeed) {
        this.baselineBearing = baselineBearing;
        this.baseLineSpeed = baseLineSpeed;
        initialiseOscillationDirection();
    }

    @Override
    public Wind generateBaselineWind() {
        return new Wind(baselineBearing, baseLineSpeed);
    }

    @Override
    public Wind generateNextWind(Wind currentWind) {
        return changeWind(currentWind);
    }

    /**
     * @param wind the wind to change
     * @return the changed wind
     */
    private Wind changeWind(Wind wind) {
        Wind newWind = new Wind(wind.getWindDirection(), wind.getWindSpeed());
        oscillateWind(newWind);
        if (shiftThisRace){shiftWind(newWind);}
        changeWindSpeed(newWind);
        timeOfLastChange = System.currentTimeMillis();
        return newWind;
    }

    /**
     * moves the wind 5 degrees up and down
     * @param wind the wind to oscillate
     */
    private void oscillateWind(Wind wind) {
        double timeSinceLastOscillationReset = System.currentTimeMillis() - timeOfLastOscillationReset;
        double timeSinceLastChange = System.currentTimeMillis() - timeOfLastChange;
        double newBearing = wind.getWindDirection().degrees();
        double degreeChange = timeSinceLastChange * 2 * bearingVariance / oscillationPeriod;
        degreeChange = (1 - oscillationVariance) * degreeChange + (2 * oscillationVariance) * degreeChange * Math.random();

        if (timeSinceLastOscillationReset >= oscillationPeriod) {
            timeOfLastOscillationReset = System.currentTimeMillis();
            anticlockwise = !anticlockwise;
        }
        if (anticlockwise) {
            newBearing -= degreeChange;
            if (newBearing < baselineBearing.degrees() - bearingVariance) {
                anticlockwise = !anticlockwise;
                timeOfLastOscillationReset = System.currentTimeMillis();
            } else {
                wind.setWindDirection(Bearing.fromDegrees(newBearing % 360));
            }
        } else {
            newBearing += degreeChange;
            if (newBearing > baselineBearing.degrees() + bearingVariance) {
                anticlockwise = !anticlockwise;
                timeOfLastOscillationReset = System.currentTimeMillis();
            } else {
                wind.setWindDirection(Bearing.fromDegrees(newBearing % 360));
            }
        }
    }

    /**
     * Slowly shifts the wind up to 180 degrees from where it started
     * @param wind the wind to change
     */
    private void shiftWind(Wind wind) {
        double timeSinceLastShift = System.currentTimeMillis() - timeOfLastShift;
        double newBearing = wind.getWindDirection().degrees();
        double degreeChange = 7;

        if (timeSinceLastShift >= shiftTime){
            shiftedSoFar += degreeChange;
            if (shiftedSoFar >= 180){
                shiftAnticlockwise = Math.random() > 0.5;
                shiftedSoFar = 0;
            }

            timeOfLastShift = System.currentTimeMillis();
            if (shiftAnticlockwise){
                newBearing -= degreeChange;
                wind.setWindDirection(Bearing.fromDegrees(newBearing % 360));
            } else {
                newBearing += degreeChange;
                wind.setWindDirection(Bearing.fromDegrees(newBearing % 360));
            }
        }
    }

    /**
     * Change the wind speed
     * @param wind the wind to change
     */
    private void changeWindSpeed(Wind wind) {
        double offsetAngle = (wind.getWindDirection().radians() - baselineBearing.radians());
        double offset = Math.sin(offsetAngle) * windSpeedVariance;
        double newWindSpeed = baseLineSpeed + offset;
        wind.setWindSpeed(newWindSpeed);
    }

    /**
     * starts the wind oscillation direction
     */
    private void initialiseOscillationDirection() {
        anticlockwise = new Random().nextBoolean();
        timeOfLastOscillationReset = System.currentTimeMillis();
    }

    public void setBearingVariance(double maxBearingVariance) {
        this.bearingVariance = maxBearingVariance;
    }

    public void setWindSpeedVariance(double windSpeedVariance) {
        this.windSpeedVariance = windSpeedVariance;
    }

    public void setOscillationPeriod(double oscillationPeriod) {
        this.oscillationPeriod = oscillationPeriod;
    }
}
