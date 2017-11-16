package visualiser.layout;

import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * Wrapper for controlling the position and heading of rendered 3D models.
 */
public class Subject3D {
    /**
     * Rendered mesh
     */
    private Shape3D mesh;
    /**
     * Source ID of subject in game model
     */
    private int sourceID;

    /**
     * Position translation updated by state listeners
     */
    private Translate position;

    /**
     * Heading rotation updated by state listeners
     */
    private Rotate heading;

    private Rotate xRot;

    private Rotate yRot;

    private Scale scale;


    /**
     * Constructor for view subject wrapper
     * @param mesh to be rendered
     * @param sourceID Source ID of the subject.
     */
    public Subject3D(Shape3D mesh, int sourceID) {
        this.mesh = mesh;
        this.sourceID = sourceID;
        this.scale = new Scale();
        this.position = new Translate();
        this.heading = new Rotate(0, Rotate.Y_AXIS);
        this.xRot = new Rotate(90, Rotate.X_AXIS);
        this.yRot = new Rotate(180, Rotate.Y_AXIS);
        this.mesh.getTransforms().addAll(position, scale, heading, xRot, yRot);
    }

    public Shape3D getMesh() {
        return mesh;
    }

    public int getSourceID() {
        return sourceID;
    }

    public Translate getPosition() {
        return this.position;
    }

    public Rotate getHeading() {
        return heading;
    }

    public void setScale(double scale) {
        this.scale.setX(scale);
        this.scale.setY(scale);
        this.scale.setZ(scale);
    }

    public void setX(double x) {
        position.setX(x);
    }

    public double getX(){
        return position.getX();
    }

    public void setY(double y) {
        position.setY(y);
    }

    public double getY(){
        return position.getY();
    }

    public void setZ(double z) {
        position.setZ(z);
    }

    public double getZ(){
        return position.getZ();
    }

    public void setHeading(double angle) {
        heading.setAngle(angle);
    }

    public void setXRot(double angle){
        xRot.setAngle(angle);
    }

    public void setYRot(double angle){
        yRot.setAngle(angle);
    }
}
