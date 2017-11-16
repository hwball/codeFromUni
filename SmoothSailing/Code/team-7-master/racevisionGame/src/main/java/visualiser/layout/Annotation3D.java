package visualiser.layout;

import javafx.scene.shape.Shape3D;

/**
 * Created by connortaylorbrown on 13/09/17.
 */
public class Annotation3D extends Subject3D {
    /**
     * Constructor for view subject wrapper
     *
     * @param mesh to be rendered
     */
    public Annotation3D(Shape3D mesh) {
        super(mesh, 0);
    }

    /**
     * Prevent rescaling of this subject
     * @param scale ignored
     */
    @Override
    public void setScale(double scale) {}
}
