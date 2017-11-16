package visualiser.Controllers;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.AmbientLight;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import mock.app.Event;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.Enums.RequestToJoinEnum;
import org.apache.commons.codec.language.bm.Rule;
import shared.model.Boat;
import visualiser.app.App;
import visualiser.gameController.ControllerClient;
import visualiser.layout.*;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;
import visualiser.network.HttpMatchBrowserHost;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Hosting a game.
 */
public class InGameLobbyController extends Controller {
    @FXML
    private ImageView imageView;

    @FXML
    GridPane playerContainer;


    @FXML
    private Label playerLabel;

    @FXML
    private Label playerLabel2;

    @FXML
    private Label playerLabel3;

    @FXML
    private Label playerLabel4;

    @FXML
    private Label playerLabel5;

    @FXML
    private Label playerLabel6;

    @FXML
    private Button startButton;

    @FXML
    private GridPane animatedPane;

    @FXML
    private Button quitButton;

    private Event game;

    private View3D playerBoat;

    private VisualiserRaceEvent visualiserRaceEvent;

    private boolean isHost;

    private ControllerClient controllerClient;

    private ArrayList<Label> allPlayerLabels;

    private ObservableList<Subject3D> subjects = FXCollections.observableArrayList();

    private StlMeshImporter importer;

    private PopulatePlayers lobbyUpdateListener;


    public void initialize() {
        allPlayerLabels = new ArrayList(Arrays.asList(playerLabel, playerLabel2,
                playerLabel3,
                playerLabel4,
                playerLabel5,
                playerLabel6));

        URL asset = HostGameController.class.getClassLoader().getResource("assets/V1.2 Complete Boat.stl");
        importer = new StlMeshImporter();
        importer.read(asset);
        lobbyUpdateListener = new PopulatePlayers();

        AmbientLight ambientLight = new AmbientLight(Color.web("#888888"));
        ambientLight.setTranslateX(250);
        ambientLight.setTranslateZ(210);
        ambientLight.setLightOn(true);

        PointLight pointLight = new PointLight(Color.web("#888888"));
        pointLight.setTranslateX(250);
        pointLight.setTranslateZ(210);
        pointLight.setLightOn(true);

        ObservableList<Subject3D> subjects = FXCollections.observableArrayList();
        View3D view3D = new View3D();
        view3D.addAmbientLight(ambientLight);
        view3D.addPointLight(pointLight);
        view3D.setDistance(10);
        view3D.setPitch(5);
        view3D.setItems(subjects);

        SkyBox skyBox = new SkyBox(750,200,250,0,250);
        subjects.addAll(skyBox.getSkyBoxPlanes());

        SeaSurface seaSurface = new SeaSurface(750, 200);
        seaSurface.setX(250);
        seaSurface.setZ(250);
        subjects.add(seaSurface);

        asset = RaceViewController.class.getClassLoader().getResource("assets/V1.2 Complete Boat.stl");
        StlMeshImporter importer = new StlMeshImporter();
        importer.read(asset);
        Subject3D boat = new Subject3D(new MeshView(importer.getImport()), 0);

        double radius = 100;
        boat.setX(0);
        boat.setZ(radius);
        boat.setScale(0.1);

        subjects.add(boat);
        view3D.trackSubject(boat, -45);

        animatedPane.add(view3D, 0, 0);

        //add sail
        Material whiteSail = new PhongMaterial(Color.WHITE);
        Sails3D sails3D = new Sails3D();
        Subject3D sailsSubject = new Subject3D(sails3D, 0);
        sails3D.setMouseTransparent(true);
        sails3D.setMaterial(whiteSail);
        sailsSubject.setXRot(0d);
        sailsSubject.setX(0);
        sailsSubject.setZ(radius);
        sailsSubject.setScale(0.1);
        subjects.add(sailsSubject);

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

    }

    private void resetLobby(){
        for (Label label: allPlayerLabels){
            label.setText("No Player");
        }
        List<Node> nodeCopy = new ArrayList(playerContainer.getChildren());
        for (Node node: nodeCopy){
            if (node instanceof View3D){
                playerContainer.getChildren().remove(node);
            }
        }
    }

    public class PopulatePlayers implements ListChangeListener {

        @Override
        public void onChanged(Change change) {
            Platform.runLater(() -> {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                        populateLobby();
                    }
                }
            });
        }

        public void populateLobby() {

            try{
                resetLobby();
                int count = 0;
                int row = 0;

                ArrayList<VisualiserBoat> copy = new ArrayList<>(visualiserRaceEvent.getVisualiserRaceState().getBoats());

                for (VisualiserBoat boat : copy) {
                    View3D playerBoatToSet = new View3D();

                    playerBoatToSet.setItems(subjects);

                    playerContainer.add(playerBoatToSet, (count % 3) , row);
                    playerContainer.setMargin(playerBoatToSet, new Insets(10, 10, 10, 10));

//                    SeaSurface sea = new SeaSurface(750, 200);
//                    sea.setX(250);
//                    sea.setZ(210);
//                    subjects.add(sea);
//
                    NewSeaSurface sea = new NewSeaSurface();
                    Subject3D seaSubject = new Annotation3D(sea);
                    seaSubject.setX(50);
                    seaSubject.setZ(50);
                    seaSubject.setXRot(0);
                    subjects.add(seaSubject);

//                    MeshView mesh = new MeshView(importer.getImport());
//                    Subject3D subject = new Subject3D(mesh,0);
//                    subjects.add(subject);
                    Shape3D mesh = Assets3D.getBoat();
                    PhongMaterial boatColorMat = new PhongMaterial(boat.getColor());
                    //mesh.setMaterial(boatColorMat);
                    Subject3D subject = new Subject3D(mesh, 0);
                    subjects.add(subject);

                    Sails3D sails3D = new Sails3D();
                    Subject3D sailsSubject = new Subject3D(sails3D, 0);
                    sails3D.setMaterial(boatColorMat);
                    subjects.add(sailsSubject);
                    sailsSubject.setXRot(0d);
                    //sails3D.startLuffing();

                    playerBoatToSet.setDistance(50);
                    playerBoatToSet.setYaw(45);
                    playerBoatToSet.setPitch(20);


                    if (boat.isClientBoat()) {
                        /*Shockwave boatHighlight = new Shockwave(10);
                        boatHighlight.getMesh().setMaterial(new PhongMaterial(new Color(1, 1, 0, 0.1)));*/

                        Assets3D.boatHighlight.setX(subject.getPosition().getX());
                        Assets3D.boatHighlight.setY(subject.getPosition().getY());
                        Assets3D.boatHighlight.setZ(subject.getPosition().getZ());

                        subjects.add(Assets3D.boatHighlight);
                        subject.getMesh().toFront();
                    }

                    AnimationTimer rotate = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            subject.setHeading(subject.getHeading().getAngle() + 0.1);
                            sailsSubject.setHeading(subject.getHeading().getAngle() + 0.1);
                            sailsSubject.setX(subject.getX());
                            sailsSubject.setZ(subject.getZ());
                        }
                    };
                    rotate.start();

                    allPlayerLabels.get(count).setText(boat.getName());
                    allPlayerLabels.get(count).toFront();
                    count += 1;
                    if (count > 2){
                        row = 1;
                    }
                }
            }
            catch(ConcurrentModificationException e){
                e.printStackTrace();
            }
        }

    }
    /*
    private void populatePlayers(ListChangeListener.Change change){
    }*/

    /**
     * Starts the race.
     */
    private void startRace() {

        //Starts the race countdown timer.
        countdownTimer();
    }



    /**
     * Countdown timer until race starts.
     */
    private void countdownTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                //Get the current race status.
                RaceStatusEnum raceStatus = visualiserRaceEvent.getVisualiserRaceState().getRaceStatusEnum();


                //If the race has reached the preparatory phase, or has started...
                if (raceStatus == RaceStatusEnum.PREPARATORY || raceStatus == RaceStatusEnum.STARTED) {
                    //Stop this timer.
                    stop();

                    //Hide this, and display the race controller.
                    try {
                        visualiserRaceEvent.getVisualiserRaceState().getBoats().removeListener(lobbyUpdateListener);

                        RaceViewController rvc = (RaceViewController)
                                loadScene("newRaceView.fxml");
                        rvc.startRace(visualiserRaceEvent, controllerClient,
                                isHost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    /**
     * Joins a game and enters the game lobby for it.
     * @param socket Socket to connect to.
     * @param isHost Whether this client is the host.
     * @param joinType How the client wishes to join (e.g., participant).
     */
    public void enterGameLobby(Socket socket, boolean isHost, RequestToJoinEnum joinType){
        try {

            this.visualiserRaceEvent = new VisualiserRaceEvent(socket, joinType);
            this.isHost = isHost;
            this.controllerClient = visualiserRaceEvent.getControllerClient();

            this.visualiserRaceEvent.getVisualiserRaceState().getBoats().addListener(this.lobbyUpdateListener);

            enableStartIfHost();

            startRace();
        } catch (IOException e) {
            //TODO should probably let this propagate, so that we only enter this scene if everything works
            Logger.getGlobal().log(Level.WARNING, "Could not connect to server.", e);
        }
    }


    /**
     * Enables the start button if the client is the host of the game.
     */
    private void enableStartIfHost() {
        if (isHost) {
            startButton.setVisible(true);
            startButton.setDisable(false);
        } else {
            startButton.setVisible(false);
            startButton.setDisable(true);
        }
    }



    /**
     * Menu button pressed. Prompt alert then return to menu
     * @throws IOException socket erro
     */
    public void menuBtnPressed() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitting race");
        alert.setContentText("Do you wish to quit the race?");
        alert.setHeaderText("You are about to quit the race");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            visualiserRaceEvent.terminate();

            try{
                if(isHost) {
                    App.game.endEvent();
                }
                loadScene("title.fxml");

            }catch (IOException ignore){};
        }
    }

    /**
     * Start button pressed. Currently only prints out start
     */
    public void startBtnPressed(){
        try {
            HttpMatchBrowserHost.httpMatchBrowserHost.sendStarted();
        } catch (IOException e) {
            e.printStackTrace();
        }
        App.game.getRaceLogic().getRace().startRace(App.game.getRaceLogic().getRace().getRacePreparatoryTime(), true);
    }

    public void joinSpecPressed(){
        //System.out.println("Spectator list pressed. Joining spectators");
    }

    public void joinRacePressed(){
        //System.out.println("Empty race user pane pressed. Joining racers");
    }


}
