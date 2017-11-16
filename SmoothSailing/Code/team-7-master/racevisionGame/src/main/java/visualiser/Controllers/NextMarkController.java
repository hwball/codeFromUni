package visualiser.Controllers;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import shared.model.Bearing;
import shared.model.CompoundMark;
import shared.model.GPSCoordinate;
import visualiser.layout.Annotation3D;
import visualiser.layout.Assets3D;
import visualiser.layout.Subject3D;
import visualiser.layout.View3D;
import visualiser.model.VisualiserBoat;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class NextMarkController {
    private @FXML StackPane arrowStackPane2d;
    private @FXML StackPane arrowStackPane3d;
    private @FXML Pane pane2d;
    private @FXML Pane pane3d;

    private View3D view3D;
    private VisualiserBoat boat;
    private boolean zoomedOut = false;

    public void initialiseArrowView(View3D view3D, VisualiserBoat boat) {
        this.view3D = view3D;
        this.boat = boat;
        pane2d.setVisible(false);
        pane3d.setVisible(true);
        initialise2dArrowView();
        initialise3dArrowView();
    }

    private void initialise2dArrowView() {
        AnimationTimer arrow2d = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (boat.getCurrentLeg().getEndCompoundMark() != null) {
                    CompoundMark target = boat.getCurrentLeg().getEndCompoundMark();
                    Bearing headingToMark = GPSCoordinate.calculateBearing(boat.getPosition(), target.getAverageGPSCoordinate());
                    arrowStackPane2d.setRotate(headingToMark.degrees());
                } else {
                    stop();
                }
            }
        };
        arrow2d.start();
    }

    private void initialise3dArrowView() {
        ObservableList<Subject3D> viewSubjects = FXCollections.observableArrayList();
        String arrowPath = "assets/mark_arrow.x3d";

        Shape3D arrow = Assets3D.loadX3d(arrowPath);
        AmbientLight ambientLight = new AmbientLight(Color.web("#CCCCCC"));

        arrow.setScaleX(15);
        arrow.setScaleY(15);
        arrow.setScaleZ(200);
        arrow.setRotationAxis(new Point3D(1,0,0));

        arrowStackPane3d.getChildren().add(arrow);
        arrowStackPane3d.getChildren().add(ambientLight);

        AnimationTimer arrow3d = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (boat.getCurrentLeg().getEndCompoundMark() != null) {
                    arrow.getTransforms().clear();
                    double zRotation = calculateZRotate();
                    arrow.getTransforms().add(new Rotate(calculateXRotate(), new Point3D(1, 0, 0)));
                    arrow.getTransforms().add(new Rotate(zRotation, new Point3D(0, 0, 1)));
                } else {
                    stop();
                }
            }
        };
        arrow3d.start();
    }

    public void show2d() {
        zoomedOut = true;
    }

    public void show3d() {
        zoomedOut = false;
    }

    private double calculateZRotate() {
        CompoundMark target = boat.getCurrentLeg().getEndCompoundMark();
        Bearing headingToMark = GPSCoordinate.calculateBearing(boat.getPosition(), target.getAverageGPSCoordinate());
        if (zoomedOut) {
            return -headingToMark.degrees() + 180;
        }
        return -headingToMark.degrees() + boat.getBearing().degrees() + 180;
    }

    private double calculateXRotate() {
        return 100 - view3D.getPitch();
    }
}
