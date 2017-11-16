package shared.model;



/**
 * This class encapsulates the wind during a race.
 * It has speed and a bearing.
 * This is intended to be immutable.
 */
public class Wind {

    /**
     * The current wind direction bearing.
     */
    private Bearing windDirection;

    /**
     * Wind speed (knots).
     * Convert this to millimeters per second before passing to RaceStatus.
     */
    private double windSpeed;


    /**
     * Constructs a new wind object, with a given direction and speed, in knots.
     * @param windDirection The direction of the wind.
     * @param windSpeed The speed of the wind, in knots.
     */
    public Wind(Bearing windDirection, double windSpeed) {
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
    }

    /**
     * Returns the race wind's bearing.
     * @return The race wind's bearing.
     */
    public Bearing getWindDirection() {
        return windDirection;
    }


    /**
     * Returns the race wind's speed, in knots.
     * @return The race wind's speed, in knots.
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDirection(Bearing windDirection) {
        this.windDirection = windDirection;
    }
}
