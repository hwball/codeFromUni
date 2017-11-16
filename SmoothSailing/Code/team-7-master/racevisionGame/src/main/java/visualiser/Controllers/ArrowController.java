package visualiser.Controllers;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import shared.model.Bearing;
import shared.model.Wind;

/**
 * Controller for the wind direction arrow on the race screen.
 */
public class ArrowController {
    private @FXML StackPane arrowStackPane;
    private @FXML ImageView arrowImage;
    private @FXML Label speedLabel;
    private final static Integer MIN_KNOTS = 2;     // knots for min_height
    private final static Integer MAX_KNOTS = 30;    // knots for max_height
    private final static Integer MIN_HEIGHT = 25;   // min arrow height
    private final static Integer MAX_HEIGHT = 75;   // max arrow height

    /**
     * Sets which wind property the arrow control should bind to.
     * @param wind The wind property to bind to.
     */
    public void setWindProperty(Property<Wind> wind) {
        wind.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> updateWind(newValue));
            }
        });
    }

    /**
     * Updates the control to use the new wind value.
     * This updates the arrow direction (due to bearing), arrow length (due to speed), and label (due to speed).
     * @param wind The wind value to use.
     */
    private void updateWind(Wind wind) {
        updateWindBearing(wind.getWindDirection());
        updateWindSpeed(wind.getWindSpeed());
    }

    /**
     * Updates the control to account for the new wind speed.
     * This changes the length (height) of the wind arrow, and updates the speed label.
     * @param speedKnots The new wind speed, in knots.
     */
    private void updateWindSpeed(double speedKnots) {
        updateWindArrowLength(speedKnots);
        updateWindSpeedLabel(speedKnots);
    }

    /**
     * Updates the length of the wind arrow according to the specified wind speed.
     * @param speedKnots Wind speed, in knots.
     */
    private void updateWindArrowLength(double speedKnots) {
        double deltaKnots = MAX_KNOTS - MIN_KNOTS;
        double deltaHeight = MAX_HEIGHT - MIN_HEIGHT;

        //Clamp speed.
        if (speedKnots > MAX_KNOTS) {
            speedKnots = MAX_KNOTS;
        } else if (speedKnots < MIN_KNOTS) {
            speedKnots = MIN_KNOTS;
        }

        //How far between the knots bounds is the current speed?
        double currentDeltaKnots = speedKnots - MIN_KNOTS;
        double currentKnotsScalar = currentDeltaKnots / deltaKnots;

        //Thus, how far between the pixel height bounds should the arrow height be?
        double newHeight = MIN_HEIGHT + (currentKnotsScalar * deltaHeight);

        arrowImage.setFitHeight(newHeight);
    }

    /**
     * Updates the wind speed label according to the specified wind speed.
     * @param speedKnots Wind speed, in knots.
     */
    private void updateWindSpeedLabel(double speedKnots) {
        speedLabel.setText(String.format("%.1fkn", speedKnots));
    }

    /**
     * Updates the control to account for a new wind bearing.
     * This rotates the arrow according to the bearing.
     * @param bearing The bearing to use to rotate arrow.
     */
    private void updateWindBearing(Bearing bearing) {
        //Rotate the wind arrow.
        arrowStackPane.setRotate(bearing.degrees());
    }

}