package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Control for rendering 3D objects visible through a PerspectiveCamera. Implements Adapter Pattern to
 * interface with camera, and allows clients to add shapes to the scene. All scenes contain sea plane and
 * sky box, whose textures are set with special methods.
 */
public class View3D extends Pane {
    /**
     * Container for group and camera
     */
    private SubScene scene;
    /**
     * Observable list of renderable items
     */
    private ObservableList<Subject3D> items;
    /**
     * Map for selecting Subject3D from Shape3D
     */
    private Map<Shape3D, Subject3D> shapeMap;
    /**
     * Map for selecting Subject3D from source ID
     */
    private Map<Integer, Subject3D> sourceMap;
    /**
     * Subject tracked by camera
     */
    private ObjectProperty<Subject3D> target;
    /**
     * Rendering container for shapes
     */
    private Group world;
    /**
     * Near limit of view frustum
     */
    private double nearClip;
    /**
     * Far limit of view frustum
     */
    private double farClip;
    /**
     * Camera origin
     */
    private Translate pivot;
    /**
     * Distance of camera from pivot point
     */
    private Translate distance;
    /**
     * Angle along ground between z-axis and camera
     */
    private Rotate yaw;
    /**
     * Angle between ground plane and camera direction
     */
    private Rotate pitch;
    /**
     * Animation loop for camera tracking
     */
    private AnimationTimer trackBoat;
    /**
     * Distance to switch from third person to bird's eye
     */
    private final double THIRD_PERSON_LIMIT = 500;
    /**
     * Distance to stop zoom
     */
    private final double ZOOM_IN_LIMIT = 15;
    private final double ZOOM_OUT_LIMIT = 700;
    private final double MAX_ZOOM_LIMIT = 1150;
    private final double MAX_PITCH = 60;    // birds eye view
    private final double MIN_PITCH = 5;    // third person view
    private final double ZOOM_PER_KEYPRESS = 5; // distance changed per zoom
    private double itemScale = 1;

    /**
     * Default constructor for View3D. Sets up Scene and PerspectiveCamera.
     * @param fill whether or not to fill the background of the view.
     */
    public View3D(boolean fill) {
        this.world = new Group();
        this.shapeMap = new HashMap<>();
        this.sourceMap = new HashMap<>();
        this.target = new SimpleObjectProperty<>(null);
        this.scene = new SubScene(world, 300, 300);

        scene.widthProperty().bind(this.widthProperty());
        scene.heightProperty().bind(this.heightProperty());
        if (fill) {
            scene.setFill(new Color(0.2, 0.6, 1, 1));
        }
        scene.setCamera(buildCamera());

        this.getChildren().add(scene);
    }

    public View3D(){
        this(true);
    }

    /**
     * Sets up camera view frustum and binds transformations
     * @return perspective camera
     */
    private PerspectiveCamera buildCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);

        // Set up view frustum
        nearClip = 0.1;
        farClip = 3000.0;
        camera.setNearClip(nearClip);
        camera.setFarClip(farClip);

        // Set up transformations
        pivot = new Translate();
        distance = new Translate();
        yaw = new Rotate(0, Rotate.Y_AXIS);
        pitch = new Rotate(0, Rotate.X_AXIS);
        camera.getTransforms().addAll(pivot, yaw, pitch, distance);

        return camera;
    }

    /**
     * Provide the list of subjects to be automatically added or removed from the view as the list
     * changes.
     * @param items list managed by client
     */
    public void setItems(ObservableList<Subject3D> items) {
        this.items = items;
        this.items.addListener((ListChangeListener<? super Subject3D>) c -> {
            while(c.next()) {
                if (c.wasRemoved() || c.wasAdded()) {
                    for (Subject3D shape : c.getRemoved()) {
                        world.getChildren().remove(shape.getMesh());
                        shapeMap.remove(shape.getMesh());
                        sourceMap.remove(shape.getSourceID());
                    }
                    for (Subject3D shape : c.getAddedSubList()) {
                        world.getChildren().add(shape.getMesh());
                        shapeMap.put(shape.getMesh(), shape);
                        sourceMap.put(shape.getSourceID(), shape);
                    }
                }
            }
        });
    }

    public Subject3D getShape(int sourceID) {
        return sourceMap.get(sourceID);
    }

    /**
     * Intercept mouse clicks on subjects in view. The applied listener cannot be removed.
     */
    public void enableTracking() {
        scene.setOnMousePressed(e -> {
            PickResult result = e.getPickResult();
            if(result != null && result.getIntersectedNode() != null && result.getIntersectedNode() instanceof Shape3D) {
                untrackSubject();
                trackSubject(shapeMap.get(result.getIntersectedNode()), 0);
                setThirdPerson();
            }
        });
    }

    public ObjectProperty<Subject3D> targetProperty() {
        return target;
    }

    /**
     * Configures camera to third person view
     */
    public void setThirdPerson() {
        this.setDistance(ZOOM_IN_LIMIT * 5);
        adjustPitchForZoom();
        adjustScaleForZoom();
    }

    /**
     * Configures camera to bird's eye view
     */
    public void setBirdsEye() {
        this.setYaw(0);
        this.setPitch(MAX_PITCH);
        adjustScaleForZoom();
    }

    /**
     * Stop camera from following the last selected subject
     */
    public void untrackSubject() {
        if(target.get() != null) {
            trackBoat.stop();
            target.setValue(null);
        }
    }

    /**
     * Set camera to follow the selected subject
     * @param subject to track
     * @param yaw to add to boat heading
     */
    public void trackSubject(Subject3D subject, double yaw) {
        target.set(subject);

        this.trackBoat = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePivot(target.get().getPosition());
                setYaw(target.get().getHeading().getAngle() + yaw);
            }
        };
        trackBoat.start();
    }

    public void setNearClip(double nearClip) {
        this.nearClip = nearClip;
    }

    public void setFarClip(double farClip) {
        this.farClip = farClip;
    }

    public Translate getPivot() {
        return pivot;
    }

    /**
     * Sets the coordinates of the camera pivot once.
     * @param pivot source of coordinates
     */
    public void updatePivot(Translate pivot) {
        this.pivot.setX(pivot.getX());
        this.pivot.setY(pivot.getY());
        this.pivot.setZ(pivot.getZ());
    }

    /**
     * Set distance of camera from pivot
     * @param distance in units
     */
    public void setDistance(double distance) {
        this.distance.setZ(-distance);
    }

    /**
     * Adds delta to current distance and changes camera mode if applicable.
     * Third person limit specifies the distance at which a third person camera
     * switches to bird's-eye, remaining focused on the same position.
     * @param delta amount to change distance by
     */
    public void updateDistance(double delta) {
        double newDistance = -this.distance.getZ() + delta;
        if (target.get() == null){
            if (newDistance > MAX_ZOOM_LIMIT) {
                setDistance(MAX_ZOOM_LIMIT);
            } else if (newDistance <= ZOOM_IN_LIMIT) {
                setDistance(ZOOM_IN_LIMIT);
            } else {
                setDistance(newDistance);
            }
        } else if(newDistance <= ZOOM_IN_LIMIT) {
            setDistance(ZOOM_IN_LIMIT);
        } else if (newDistance > ZOOM_OUT_LIMIT){
            untrackSubject();
            setDistance(1050);
            updatePivot(new Translate(250, 0, 210));
            setYaw(0);
        } else {
            setDistance(newDistance);
        }
        adjustPitchForZoom();
        adjustScaleForZoom();
    }

    /**
     * Adjusts the scale size of boats and markers as a user zooms in or out,
     * to smooth the change between third person to birds eye view.
     */
    private void adjustScaleForZoom(){
        double itemScale = (((-distance.getZ() - (ZOOM_IN_LIMIT * 2)) /
                ((THIRD_PERSON_LIMIT - (ZOOM_IN_LIMIT * 2)) /
                        (1 - 0.1))) + 0.1);
        // if zoomed right in
        if (itemScale < 0.1){
            itemScale = 0.1;
            // if zoomed right out
        } else if (itemScale > 1) {
            itemScale = 1;
        }

        // update scale
        for (Subject3D item : items) {
            item.setScale(itemScale);
        }
    }

    /**
     * Adjusts the pitch as a user zooms in or out, to smooth the change
     * between third person to birds eye view.
     */
    private void adjustPitchForZoom(){
        double pitch = (((-distance.getZ() - (ZOOM_IN_LIMIT*2)) /
                ((THIRD_PERSON_LIMIT - (ZOOM_IN_LIMIT*2)) / (MAX_PITCH -
                        MIN_PITCH))) + MIN_PITCH);

        // if pitch is out of bounds, set it to the min/max
        if (pitch < MIN_PITCH) {
            pitch = MIN_PITCH;
        } else if (pitch > MAX_PITCH){
            pitch = MAX_PITCH;
        }
        setPitch(pitch);
    }

    /**
     * Method to be called when the zoom in key is pressed.
     */
    public void zoomIn(){
        updateDistance(-ZOOM_PER_KEYPRESS);
    }

    /**
     * Method to be called when the zoom out key is pressed.
     */
    public void zoomOut(){
        updateDistance(ZOOM_PER_KEYPRESS);
    }

    /**
     * Set angle of camera from z-axis along ground
     * @param yaw in degrees
     */
    public void setYaw(double yaw) {
        this.yaw.setAngle(yaw);
    }

    public double getYaw(){
        return this.yaw.getAngle();
    }

    /**
     * Set elevation of camera
     * @param pitch in degrees
     */
    public void setPitch(double pitch) {
        this.pitch.setAngle(-pitch);
    }

    public double getPitch(){
        return this.pitch.getAngle();
    }

    public void addAmbientLight(AmbientLight ambientLight) {
        this.world.getChildren().add(ambientLight);
    }

    public void addPointLight(PointLight pointLight) {
        this.world.getChildren().add(pointLight);
    }
}
