package visualiser.Controllers;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.PointLight;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.stage.Modality;
import mock.exceptions.EventConstructionException;
import visualiser.app.App;
import visualiser.layout.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the opening title window.
 * Has two initial buttons for a user to decide how to play their game. Has a
 * burger-boat and comic sans styling to allure and entice users into playing
 * the game.
 */
public class TitleController extends Controller {
    private @FXML AnchorPane titleWrapper;
    private @FXML RadioButton dayModeRD;
    private @FXML RadioButton nightModeRD;
    private @FXML Button tutorialButton;
    private @FXML ImageView imgSun;
    private @FXML GridPane view3DContainer;
    private ToggleGroup toggleGroup = new ToggleGroup();
    private SkyBox skyBox;
    private SeaSurface seaSurface;
    private Subject3D boat;
    private AmbientLight ambientLight;
    private Subject3D sailsSubject;
    private ObservableList<Subject3D> subjects = FXCollections.observableArrayList();

    public void initialize() {
        dayModeRD.setToggleGroup(toggleGroup);
        nightModeRD.setToggleGroup(toggleGroup);

        if (App.dayMode) dayModeRD.setSelected(true);
        else nightModeRD.setSelected(true);

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

        view3DContainer.add(view3D, 0, 0);

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

        titleWrapper.getStylesheets().add("/css/dayMode.css");
    }

    /**
     * Method called when the 'host a game' button is pressed.
     * Opens the next window allowing a user to host their own game.
     * Currently used to run the RaceVision mock.
     * @throws IOException if main has problems
     */
    public void hostAGame() throws IOException {
        loadScene("hostGame.fxml");
    }

    /**
     * To be implemented at a later date- will open the next scene displaying
     * games a player can join. Place holder method for now!
     * @throws IOException socket error
     */
    public void joinAGame() throws IOException {
        loadScene("lobby.fxml");
    }

    /**
     * Switches the css of the program to day mode theme
     */
    public void setDayMode(){
        dayModeRD.getScene().getStylesheets().clear();
        dayModeRD.getScene().getStylesheets().add("/css/dayMode.css");
        nightModeRD.setSelected(false);
        App.dayMode = true;
        newSkyBox();
    }

    /**
     * Switches the css of the program to night mode theme
     */
    public void setNightMode(){
        nightModeRD.getScene().getStylesheets().clear();
        nightModeRD.getScene().getStylesheets().add("/css/nightMode.css");
        dayModeRD.setSelected(false);
        App.dayMode = false;
        newSkyBox();
    }

    /**
     * Called when control button is pressed. New pop up window displaying controls
     */
    public void showControls(){
        try {
            loadPopupScene("keyBindings.fxml",
                    "Game Controls", Modality.WINDOW_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tutorialStartPressed() throws IOException,
            EventConstructionException {
        App.gameType = 4;
        HostGameController hgc = new HostGameController();
        hgc.setCurrentMapIndex(4);
        hgc.hostGamePressed();
    }

    private void newSkyBox() {
        if (skyBox != null) {
            SkyBox newSkyBox = new SkyBox(750,200,250,0,250);
            subjects.removeAll(skyBox.getSkyBoxPlanes());
            subjects.addAll(newSkyBox.getSkyBoxPlanes());
            skyBox = newSkyBox;
            seaSurface.getMesh().toFront();
            boat.getMesh().toFront();
            sailsSubject.getMesh().toFront();

            if (App.dayMode) {
                ambientLight.setColor(Color.web("#CCCCFF"));
            } else {
                ambientLight.setColor(Color.web("#9999AA"));
            }
        }
    }

}