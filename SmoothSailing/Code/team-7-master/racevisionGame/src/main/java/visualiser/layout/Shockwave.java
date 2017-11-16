package visualiser.layout;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 * Created by cbt24 on 14/09/17.
 */
public class Shockwave extends Subject3D {
    public Shockwave(double radius) {
        super(new Cylinder(radius,0),0);
        getMesh().getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        getMesh().setMaterial(new PhongMaterial(new Color(0,0,0,0)));
        getMesh().setMouseTransparent(true);
    }
}
