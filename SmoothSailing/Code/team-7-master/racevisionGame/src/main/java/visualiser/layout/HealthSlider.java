package visualiser.layout;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Created by connortaylorbrown on 21/09/17.
 */
public class HealthSlider extends Pane {
    /**
     * Image used to fill health slider
     */
    private Image fillImage;
    /**
     * Size of background for image configuration
     */
    private BackgroundSize backgroundSize;
    /**
     * Percentage of image cropped out from top
     */
    private double crop;

    public HealthSlider(Image fillImage) {
        this.fillImage = fillImage;
        this.crop = 1;
        this.backgroundSize = new BackgroundSize(
                100,
                100,
                true,
                true,
                true,
                false);
        drawSlider();
    }

    public void setCrop(double crop) {
        this.crop = crop;
        drawSlider();
    }

    private void drawSlider() {
        int top = Math.max(0,(int)(fillImage.getHeight() - crop * fillImage.getHeight()));

        WritableImage croppedImage = new WritableImage(
                fillImage.getPixelReader(),
                0,
                top,
                (int)fillImage.getWidth(),
                (int)fillImage.getHeight() - top
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                croppedImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        this.setBackground(new Background(backgroundImage));
    }
}
