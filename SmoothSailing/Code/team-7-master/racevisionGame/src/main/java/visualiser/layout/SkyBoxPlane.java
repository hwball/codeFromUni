package visualiser.layout;

import javafx.scene.shape.Shape3D;

public class SkyBoxPlane extends Subject3D {

    public SkyBoxPlane(Shape3D mesh, int sourceID) {
        super(mesh,sourceID);
    }

    @Override
    public void setScale(double scale) {
    }
}
