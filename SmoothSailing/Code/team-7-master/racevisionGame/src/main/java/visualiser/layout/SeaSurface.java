package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import visualiser.utils.PerlinNoiseGenerator;

/**
 * Creates a SeaSurface
 */
public class SeaSurface extends Annotation3D {
    private static Image image;

    /**
     * Sea Surface Constructor
     *
     * @param size size of the sea surface (has to be square for simplicity's sake)
     * @param freq frequency the perlin noise is to be generated at
     */
    public SeaSurface(int size, double freq) {
        super(createSurface(size, freq));
        image = new Image(getClass().getClassLoader().getResourceAsStream("images/skybox/ThickCloudsWaterDown2048.png"));
    }

    /**
     * Creates the sea surface
     * @param size size of sea noise array.
     * @param freq frequency of sea noise array.
     * @return The sea surface.
     */
    private static Shape3D createSurface(int size, double freq) {
        float[][] noiseArray = PerlinNoiseGenerator.createNoise(size, freq);
        Image diffuseMap = createImage(noiseArray.length, noiseArray); //image

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.web("#FFFFFF"));
        material.setSpecularColor(Color.web("#000000"));
        material.setDiffuseMap(diffuseMap);

        Plane3D seaPlane = new Plane3D(noiseArray.length, noiseArray.length, 10, 10);
        MeshView seaSurface = new MeshView(seaPlane);
        seaSurface.setMaterial(material);
        seaSurface.setMouseTransparent(true);
        seaSurface.toFront();

        return seaSurface;
    }

    /**
     * Create texture for uv mapping
     *
     * @param size  size of the image to make
     * @param noise array of noise
     * @return image that is created
     */
    private static Image createImage(double size, float[][] noise) {

        int width = (int) size;
        int height = (int) size;

        WritableImage wr = new WritableImage(width, height);
        PixelWriter pw = wr.getPixelWriter();
        //interpolate colours based on noise
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                float value = noise[x][y];

                double gray = normalizeValue(value, -.5, .5, 0., 1.);

                gray = clamp(gray, 0, 1);

                //values to interpolate on
                Color brightBlue = new Color(0.06, 0.5, .78, 1);
                Color lightBlue = new Color(0.15, 0.68, .88, 1);
                Color lighterBlue = new Color(0.28, 0.73, .91, 1);

                Color colour = Color.WHITE.interpolate(brightBlue, gray).interpolate(lighterBlue, gray).interpolate(lightBlue, gray);

                pw.setColor(x, y, colour);

            }
        }

        return wr;

    }

    /**
     * Nomalises the values so that the colours are correct
     *
     * @param value  value to normalise
     * @param min    current min
     * @param max    current max
     * @param newMin new min
     * @param newMax new max
     * @return returns normalised value
     */
    private static double normalizeValue(double value, double min, double max, double newMin, double newMax) {

        return (value - min) * (newMax - newMin) / (max - min) + newMin;

    }

    /**
     * clamps a value between a min and max
     *
     * @param value value to clamp
     * @param min   minimum value it can be
     * @param max   maximum value it can be
     * @return result after clamp
     */
    private static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }

    /**
     * Prevent rescaling of sea surface
     *
     * @param scale ignored
     */
    @Override
    public void setScale(double scale) {
    }
}
