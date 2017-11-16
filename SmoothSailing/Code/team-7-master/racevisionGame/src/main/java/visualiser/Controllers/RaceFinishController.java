package visualiser.Controllers;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.PointLight;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import visualiser.app.App;
import visualiser.layout.*;
import visualiser.model.VisualiserBoat;

import java.io.IOException;
import java.net.URL;

/**
 * Finish Screen for when the race finishes.
 */
public class RaceFinishController extends Controller {
    private @FXML TableView<VisualiserBoat> boatInfoTable;
    private @FXML TableColumn<VisualiserBoat, String> boatRankColumn;
    private @FXML TableColumn<VisualiserBoat, String> boatNameColumn;
    private @FXML Label raceWinnerLabel;
    private @FXML GridPane animatedPane;
    private SkyBox skyBox;
    private SeaSurface seaSurface;
    private Subject3D boat;
    private AmbientLight ambientLight;
    private Subject3D sailsSubject;
    private ObservableList<Subject3D> subjects = FXCollections.observableArrayList();

    /**
     * Display the table
     * @param boats boats to display on the table.
     */
    public void loadFinish(ObservableList<VisualiserBoat> boats) {

        ambientLight = new AmbientLight(Color.web("#CCCCFF"));
        ambientLight.setTranslateX(250);
        ambientLight.setTranslateZ(210);
        ambientLight.setLightOn(true);

        if (!App.dayMode) ambientLight.setColor(Color.web("#9999AA"));

        PointLight pointLight = new PointLight(Color.web("#AAAAFF"));
        pointLight.setTranslateX(250);
        pointLight.setTranslateZ(210);
        pointLight.setLightOn(true);

        View3D view3D = new View3D();
        view3D.addAmbientLight(ambientLight);
        view3D.addPointLight(pointLight);
        view3D.setDistance(10);
        view3D.setPitch(5);
        view3D.setItems(subjects);

//        URL asset = RaceViewController.class.getClassLoader().getResource("assets/V1.2 Complete Boat.stl");
//        StlMeshImporter importer = new StlMeshImporter();
//        importer.read(asset);
//        Subject3D boat = new Subject3D(new MeshView(importer.getImport()), 0);

        Shape3D mesh = Assets3D.getBoat();
        boat = new Subject3D(mesh, 0);

        skyBox = new SkyBox(750,200,250,0,250);
        subjects.addAll(skyBox.getSkyBoxPlanes());

        seaSurface = new SeaSurface(750, 200);
        seaSurface.setX(250);
        seaSurface.setZ(250);
        subjects.add(seaSurface);

        double radius = 100;
        boat.setX(0);
        boat.setZ(radius);
        boat.setScale(0.1);

        subjects.add(boat);
        view3D.trackSubject(boat, -45);

        //add sail
        Material whiteSail = new PhongMaterial(Color.WHITE);
        Sails3D sails3D = new Sails3D();
        sailsSubject = new Subject3D(sails3D, 0);
        sails3D.setMouseTransparent(true);
        sails3D.setMaterial(whiteSail);
        sailsSubject.setXRot(0d);
        sailsSubject.setX(0);
        sailsSubject.setZ(radius);
        sailsSubject.setScale(0.1);
        subjects.add(sailsSubject);

        animatedPane.add(view3D, 0, 0);

        AnimationTimer loop = new AnimationTimer() {
            double angle = -90;
            double offset = 0.05;
            @Override
            public void handle(long now) {
                boat.setX(radius * Math.cos(angle * Math.PI/180));
                boat.setZ(radius * Math.sin(angle * Math.PI/180));
                boat.setHeading(-angle);
                sailsSubject.setX(boat.getX());
                sailsSubject.setZ(boat.getZ());
                sailsSubject.setHeading(boat.getHeading().getAngle());
                angle += offset;
            }
        };
        loop.start();


        // set table contents
        boatInfoTable.setItems(boats);
        //Name.
        boatNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        //Rank/position.
        boatRankColumn.setCellValueFactory(cellData -> cellData.getValue().placingProperty());

        //Winner label.
        if (boats.size() > 0) {
            raceWinnerLabel.setText("Winner: " +
                    boatNameColumn.getCellObservableValue(0).getValue());
            raceWinnerLabel.setWrapText(true);
        }
    }

    public void mainMenuPressed() throws IOException {
        AudioClip sound = new AudioClip(this.getClass().getResource("/visualiser/sounds/buttonpress.wav").toExternalForm());
        sound.play();
        try {
            App.game.endEvent();
        } catch (Exception e){};
        loadTitleScreen();
    }


}
