package visualiser.Controllers;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import eu.hansolo.medusa.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.PointLight;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.util.Callback;
import network.Messages.Enums.BoatActionEnum;
import network.Messages.Enums.BoatStatusEnum;
import network.Messages.Enums.RaceStatusEnum;
import shared.dataInput.RaceDataSource;
import shared.enums.RoundingType;
import shared.exceptions.BoatNotFoundException;
import shared.model.*;
import visualiser.app.App;
import visualiser.enums.TutorialState;
import visualiser.gameController.ControllerClient;
import visualiser.gameController.Keys.ControlKey;
import visualiser.gameController.Keys.KeyFactory;
import visualiser.layout.*;
import visualiser.model.*;
import visualiser.utils.GPSConverter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller used to display a running race.
 */
public class RaceViewController extends Controller {
    private VisualiserRaceEvent visualiserRace;
    private VisualiserRaceState raceState;
    private ControllerClient controllerClient;
    private KeyFactory keyFactory = new KeyFactory();
    private boolean infoTableShow = true;  // shown or hidden
    private boolean isHost;
    private TutorialState currentState;
    private ArrayList<TutorialState> tutorialStates;
    private boolean isTutorial = false;
    private String keyToPress;
    private View3D view3D;
    private WindCompass windCompass;
    private ObservableList<Subject3D> viewSubjects;
    private Gauge gauge;
    private FGauge fGauge;
    private long positionDelay = 1000;
    private long positionTime = 0;
    private ResizableRaceCanvas raceCanvas;
    private boolean mapToggle = true;
    private GPSConverter gpsConverter;
    private ArrayList<HealthEffect> healthEffectList = new ArrayList<>();
    private Subject3D clientBoat;
    private List<BoatActionEnum> konamiCodes = new ArrayList<>();
    private int konamiIndex = 0;

    /**
     * Arrow pointing to next mark in third person
     */
    private Subject3D nextMarkArrow;
    /**
     * Animation loop for rotating mark arrow
     */
    private AnimationTimer pointToMark;

    //seagulls
    private ObservableList<Subject3D> seagulls = FXCollections.observableArrayList();
    //seagulls goto
    private Map<Subject3D, List<Double>> seagullsGoToX = new HashMap<>();
    private Map<Subject3D, List<Double>> seagullsGoToY = new HashMap<>();
    private double seagullSpeed = 0.01;

    // note: it says it's not used but it is! do not remove :)
    private @FXML ArrowController arrowController;
    private @FXML NextMarkController nextMarkController;
    private @FXML GridPane canvasBase;
    private @FXML GridPane canvasBase1;
    private @FXML GridPane canvasBase2;
    private @FXML SplitPane racePane;
    private @FXML StackPane arrowPane;
    private @FXML Pane nextMarkPane;
    private @FXML Label timer;
    private @FXML Label FPS;
    private @FXML Label timeZone;
    private @FXML CheckBox showFPS;
    private @FXML TableView<VisualiserBoat> boatInfoTable;
    private @FXML TableColumn<VisualiserBoat, String> boatPlacingColumn;
    private @FXML TableColumn<VisualiserBoat, String> boatTeamColumn;
    private @FXML TableColumn<VisualiserBoat, Leg> boatMarkColumn;
    private @FXML TableColumn<VisualiserBoat, Number> boatSpeedColumn;
    private @FXML TableColumn<VisualiserBoat, Number> boatHealthColumn;
    private @FXML LineChart<Number, Number> sparklineChart;
    private @FXML Label tutorialText;
    private @FXML ImageView tom;
    private @FXML AnchorPane infoWrapper;
    private @FXML AnchorPane lineChartWrapper;
    private @FXML StackPane speedPane;
    private @FXML AnchorPane raceAnchorPane;
    private @FXML GridPane playerHealthContainer;
    private @FXML ImageView imageView;
    private @FXML AnchorPane deathTransPane;
    private @FXML StackPane deathPane;

    /**
     * Displays a specified race.
     * Intended to be called on loading the scene.
     * @param visualiserRace   Object modelling the race.
     * @param controllerClient Socket Client that manipulates the controller.
     * @param isHost           is user a host
     */
    public void startRace(VisualiserRaceEvent visualiserRace, ControllerClient controllerClient, Boolean isHost) {
        this.visualiserRace = visualiserRace;
        this.raceState = visualiserRace.getVisualiserRaceState();
        this.controllerClient = controllerClient;
        this.isHost = isHost;
        keyFactory.load();
        deathPane.setDisable(false);
        deathPane.setVisible(false);
        tutorialCheck();
        initKeypressHandler();
        healthLoop();
        initialiseRaceVisuals();
        konamiCodes.add(BoatActionEnum.SAILS_IN);
        konamiCodes.add(BoatActionEnum.SAILS_OUT);
        konamiCodes.add(BoatActionEnum.SAILS_IN);
        konamiCodes.add(BoatActionEnum.SAILS_OUT);
        konamiCodes.add(BoatActionEnum.UPWIND);
        konamiCodes.add(BoatActionEnum.DOWNWIND);
        konamiCodes.add(BoatActionEnum.UPWIND);
        konamiCodes.add(BoatActionEnum.DOWNWIND);
        konamiCodes.add(BoatActionEnum.TACK_GYBE);
        konamiCodes.add(BoatActionEnum.VMG);
    }

    /**
     * Checks if the current game is a tutorial race and sets up initial
     * tutorial displays if it is.
     */
    private void tutorialCheck(){
        if (App.gameType == 4) {
            isTutorial = true;
            tutorialText.setVisible(true);
            tom.setVisible(true);
            tutorialStates = new ArrayList<>(Arrays.asList(TutorialState.values()));
            currentState = tutorialStates.get(0);
            tutorialStates.remove(0);
            searchMapForKey("Upwind");

            tutorialText.setText(
                    "Welcome to the tutorial! Exit at anytime with ESC. \nWe will first learn how to turn upwind. Press " +
                            keyToPress + " to turn upwind.");

        } else {
            isTutorial = false;
            tutorialText.setVisible(false);
            tom.setVisible(false);
        }
    }

    private AnimationTimer arrowToNextMark;

    private void initKeypressHandler() {
        racePane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            String codeString = event.getCode().toString();

            // tab key
            if (codeString.equals("TAB")){toggleTable();}

            //map key
            if (codeString.equals("M")){bigMap();}

            // any key pressed
            ControlKey controlKey = keyFactory.getKey(codeString);
            if(controlKey != null) {
                try {
                    controlKey.onAction();  // Change key state if applicable

                    //Check if current race is a tutorial
                    if (isTutorial){
                        //Check if current tutorial state has the same boat protocol code as key press
                        if (controlKey.getProtocolCode().equals(currentState.getAction())){
                            //Update tutorial
                            checkTutorialState();
                        }
                    }
                    checkKonami(controlKey.getProtocolCode());
                    controllerClient.sendKey(controlKey);
                    event.consume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.getGlobal().log(Level.WARNING, "RaceViewController was interrupted on thread: " + Thread.currentThread() + "while sending: " + controlKey, e);
                    Logger.getGlobal().log(Level.WARNING, "RaceController was interrupted on thread: " + Thread.currentThread() + "while sending: " + controlKey, e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // escape key
            if(event.getCode() == KeyCode.ESCAPE) {
                try {
                    if (isHost) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Exit Race");
                        alert.setContentText("Do you wish to quit the race? You are the host");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            App.game.endEvent();
                            loadTitleScreen();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Exit Race");
                        alert.setContentText("Do you wish to quit the race?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            loadTitleScreen();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create speedometer
     */
    private void initialiseSpeedometer() {
        //Create the Medusa Gauge
        gauge = GaugeBuilder.create()
                .prefSize(200,200)
                .foregroundBaseColor(Color.WHITE)
                .title("Title")
                .subTitle("Speed")
                .unit("Knots")
                .decimals(2)
                .lcdVisible(true)
                .lcdDesign(LcdDesign.STANDARD)
                .lcdFont(LcdFont.DIGITAL_BOLD)
                .scaleDirection(Gauge.ScaleDirection.CLOCKWISE)
                .minValue(0)
                .maxValue(50)
                .startAngle(320)
                .angleRange(280)
                .tickLabelDecimals(0)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .tickLabelOrientation(TickLabelOrientation.ORTHOGONAL)
                .onlyFirstAndLastTickLabelVisible(false)
                .tickLabelSectionsVisible(false)
                .tickLabelColor(Color.BLACK)
                .tickMarkSectionsVisible(false)
                .majorTickMarksVisible(true)
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(false)
                .mediumTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .ledVisible(false)
                .ledType(Gauge.LedType.STANDARD)
                .ledColor(Color.rgb(255, 200, 0))
                .ledBlinking(false)
                .needleShape(Gauge.NeedleShape.ANGLED)
                .needleSize(Gauge.NeedleSize.STANDARD)
                .needleColor(Color.CRIMSON)
                .startFromZero(false)
                .returnToZero(false)
                .knobType(Gauge.KnobType.METAL)
                .knobColor(Color.LIGHTGRAY)
                .interactive(false)
                .thresholdVisible(true)
                .threshold(50)
                .thresholdColor(Color.RED)
                .checkThreshold(true)
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.BLUE),
                        new Stop(0.25, Color.CYAN),
                        new Stop(0.5, Color.LIME),
                        new Stop(0.75, Color.YELLOW),
                        new Stop(1.0, Color.RED))
                .markersVisible(true)
                .build();


        //Create a gauge with a frame and background that utilizes a Medusa gauge
        fGauge = FGaugeBuilder
                .create()
                .prefSize(190, 190)
                .gauge(gauge)
                .gaugeDesign(GaugeDesign.METAL)
                .gaugeBackground(GaugeDesign.GaugeBackground.CARBON)
                .foregroundVisible(true)
                .build();
        speedPane.getChildren().add(fGauge);

    }

    /**
     * Initialises the various UI components to listen to the {@link #visualiserRace}.
     */
    private void initialiseRaceVisuals() {
        // initialise displays
        initialiseFps();
        initialiseInfoTable();
        initialiseView3D(this.visualiserRace);
        initialiseHealthPane();
        initialiseRaceClock();
        initialiseSpeedometer();
        initialiseRaceCanvas();
        raceTimer();    // start the timer
        //nextMarkPane.toFront();
        speedometerLoop();
        new Sparkline(this.raceState, this.sparklineChart);
    }

    private void initialiseHealthPane(){
        InputStream tomato = this.getClass().getClassLoader().getResourceAsStream("visualiser/images/tomato.png");
        initialiseHealthPane(tomato);
    }

    private void initialiseHealthPane(InputStream picture) {
        while(playerHealthContainer.getChildren().size() > 0) {
            playerHealthContainer.getChildren().remove(0);
        }
        HealthSlider healthSlider = new HealthSlider(new Image(picture));
        playerHealthContainer.add(healthSlider, 0, 0);

        try {
            VisualiserBoat player = raceState.getBoat(raceState.getPlayerBoatID());
            player.healthProperty().addListener((o, prev, curr) -> {
                healthSlider.setCrop((double)curr/100.0);
            });
        } catch (BoatNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initialiseView3D(VisualiserRaceEvent race) {
        viewSubjects = FXCollections.observableArrayList();

        AmbientLight ambientLight = new AmbientLight(Color.web("#CCCCFF"));
        ambientLight.setTranslateX(250);
        ambientLight.setTranslateZ(210);
        ambientLight.setLightOn(true);

        PointLight pointLight = new PointLight(Color.web("#AAAAFF"));
        pointLight.setTranslateX(250);
        pointLight.setTranslateZ(210);
        pointLight.setLightOn(true);

        if (!App.dayMode) {
            ambientLight.setColor(Color.web("#9999AA"));
            pointLight.setColor(Color.web("#777799"));
        }

        // Import boat mesh
        URL asset = RaceViewController.class.getClassLoader().getResource("assets/V1.2 Complete Boat.stl");
        StlMeshImporter importer = new StlMeshImporter();
        importer.read(asset);

        // Configure camera angles and control
        URL markerAsset = RaceViewController.class.getClassLoader().getResource("assets/Bouy V1.1.stl");
        StlMeshImporter importerMark = new StlMeshImporter();
        importerMark.read(markerAsset);

        URL alternateBoatAsset = RaceViewController.class.getClassLoader().getResource("assets/V1.3 BurgerBoat.stl");
        StlMeshImporter importerBurgerBoat = new StlMeshImporter();
        importerBurgerBoat.read(alternateBoatAsset);

        view3D = new View3D(false);
        view3D.setItems(viewSubjects);
        view3D.setDistance(1050);
        view3D.setBirdsEye();
        view3D.enableTracking();
        view3D.addAmbientLight(ambientLight);
        view3D.addPointLight(pointLight);
        canvasBase.add(view3D, 0, 0);

        windCompass = new WindCompass(view3D, this.raceState.windProperty());
        arrowPane.getChildren().add(windCompass);

        try {
            nextMarkController.initialiseArrowView(view3D, race.getVisualiserRaceState().getBoat(race.getVisualiserRaceState().getPlayerBoatID()));
        } catch (BoatNotFoundException e) {
            e.printStackTrace();
        }

        // Set up projection from GPS to view
        RaceDataSource raceData = visualiserRace.getVisualiserRaceState().getRaceDataSource();
        gpsConverter = new GPSConverter(raceData, 450, 450);

        SkyBox skyBox = new SkyBox(750, 200, 250, 0, 210);
        viewSubjects.addAll(skyBox.getSkyBoxPlanes());

        // Set up sea surface
//        SeaSurface sea = new SeaSurface(750, 200);
//        sea.setX(250);
//        sea.setZ(210);
//        viewSubjects.add(sea);

        // Set up sea surface overlay
//        SeaSurface seaOverlay = new SeaSurface(4000, 200);
//        seaOverlay.setX(250);
//        seaOverlay.setZ(210);
//        viewSubjects.add(seaOverlay);
//        int seaX = 15;
//        int seaY = 15;
        int seaX = 15;
        int seaY = 15;
        for (int x = 0; x < seaX; x++) {
            for (int y = 0; y < seaY; y++) {
                NewSeaSurface seaSurface = new NewSeaSurface();
                Subject3D seaSubject = new Annotation3D(seaSurface);
                seaSubject.setXRot(0);
//                seaSubject.setX(-75 + x * 50);
//                seaSubject.setZ(-150 + y * 50);
//                seaSubject.setX(-150 + x * 250);
//                seaSubject.setZ(-80 + y * 250);
                seaSubject.setX(-75 + x * 100);
                seaSubject.setZ(-150 + y * 100);
                seaSubject.setY(3);
                viewSubjects.add(seaSubject);
            }
        }

        Boundary3D boundary3D = new Boundary3D(visualiserRace.getVisualiserRaceState().getRaceDataSource().getBoundary(), gpsConverter);
        for (Subject3D subject3D: boundary3D.getBoundaryNodes()){
            viewSubjects.add(subject3D);
        }
        // Position and add each mark to view
        for(Mark mark: race.getVisualiserRaceState().getMarks()) {
//            MeshView mesh = new MeshView(importerMark.getImport());
//            Subject3D markModel = new Subject3D(mesh, mark.getSourceID());
            Subject3D markModel = new Subject3D(Assets3D.getMark(), mark.getSourceID());

            markModel.setX(gpsConverter.convertGPS(mark.getPosition()).getX());
            markModel.setZ(gpsConverter.convertGPS(mark.getPosition()).getY());

            viewSubjects.add(markModel);
        }

        // Position and add each boat to view
        for(VisualiserBoat boat: race.getVisualiserRaceState().getBoats()) {
            Shape3D mesh = Assets3D.getBoat();

            PhongMaterial boatColorMat = new PhongMaterial(boat.getColor());
            //mesh.setMaterial(boatColorMat);
            Subject3D boatModel = new Subject3D(mesh, boat.getSourceID());

            // Configure visualiser for client's boat
            if (boat.isClientBoat()) {
                clientBoat = boatModel;
                // Add player boat highlight
//                Shockwave boatHighlight = new Shockwave(10);
//                boatHighlight.getMesh().setMaterial(new PhongMaterial(new Color(1, 1, 0, 0.1)));
//                viewSubjects.add(boatHighlight);

                viewSubjects.add(Assets3D.boatHighlight);

                // Track player boat with camera
                viewSubjects.add(boatModel);
                Platform.runLater(() -> {
                    view3D.trackSubject(boatModel, 0);
                    view3D.setThirdPerson();
                });

                // Track player boat with highlight
                AnimationTimer highlightTrack = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        double boatX = gpsConverter.convertGPS(boat.getPosition()).getX();
                        double boatZ = gpsConverter.convertGPS(boat.getPosition()).getY();
                        Assets3D.boatHighlight.setX(boatX);
                        Assets3D.boatHighlight.setZ(boatZ);
//                        pointLight.setTranslateX(boatX);
//                        pointLight.setTranslateZ(boatZ);
                    }
                };
                highlightTrack.start();

                // Highlight next mark only for player boat
//                Material markColor = new PhongMaterial(new Color(0.15,0.9,0.2,1));
//                CompoundMark nextMark = boat.getCurrentLeg().getEndCompoundMark();
//                view3D.getShape(nextMark.getMark1().getSourceID()).getMesh().setMaterial(markColor);
//                if(nextMark.getMark2() != null) {
//                    view3D.getShape(nextMark.getMark2().getSourceID()).getMesh().setMaterial(markColor);
//                }
//                boat.legProperty().addListener((o, prev, curr) -> Platform.runLater(() -> swapColours(curr)));

                //next mark indicator
                changeNextMark(boat.getCurrentLeg());
                viewSubjects.add(Assets3D.ccwNextArrow);
                viewSubjects.add(Assets3D.cwNextArrow);
                boat.legProperty().addListener((o, prev, curr) -> Platform.runLater(() -> changeNextMark(curr)));
            } else {
                viewSubjects.add(boatModel);
                boatModel.getMesh().toFront();
            }

            //Create health effect
            HealthEffect healthEffect = new HealthEffect(boat.getSourceID(), System.currentTimeMillis());
            viewSubjects.add(healthEffect);
            healthEffectList.add(healthEffect);

            //add sail
            Sails3D sails3D = new Sails3D();
            Subject3D sailsSubject = new Subject3D(sails3D, 0);
            sails3D.setMouseTransparent(true);
            sails3D.setMaterial(boatColorMat);
            sailsSubject.setXRot(0d);
            sailsSubject.setHeading(visualiserRace.getVisualiserRaceState().getWindDirection().degrees());
            viewSubjects.add(sailsSubject);


//            SeagullFlock seagullFlock = new SeagullFlock(67, 43, 0);
////        seagullFlock.addToFlock();
////        seagullFlock.addToFlock();
//            seagullFlock.setxBound(450);
//            seagullFlock.setyBound(450);
//            viewSubjects.addAll(seagullFlock.getSeagulls());
//
//            Subject3D textSubject = new Annotation3D(new SeagullMesh());
//            textSubject.setY(-10);
//            viewSubjects.add(textSubject);
            addToFlock();
            addToFlock();
            addToFlock();
            addToFlock();
            addToFlock();
            addToFlock();
            addToFlock();
            addToFlock();
            seagullGoTo.start();

            AnimationTimer rotateArrows = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    Assets3D.ccwNextArrow.setHeading(Assets3D.ccwNextArrow.getHeading().getAngle() - 3.0);
                    Assets3D.cwNextArrow.setHeading(Assets3D.cwNextArrow.getHeading().getAngle() + 3.0);
                }
            };
            rotateArrows.start();

            AnimationTimer sailsFollowBoat = new AnimationTimer() {
                double sailCurrent = visualiserRace.getVisualiserRaceState().getWindDirection().degrees();
                boolean canLuff = true;
                double turnRate = 5;

                @Override
                public void handle(long now) {
                    double sailDir;
                    //if sails are out etc
                    if (boat.isSailsOut()) {
                        double windDir = visualiserRace.getVisualiserRaceState().getWindDirection().degrees();
                        double windOffset = (360 - windDir + boat.getBearing().degrees()) % 360;
                        sailDir =  windOffset / 180 * 270 + windDir + 180;
                        boolean leftOfWind = windOffset >= 180;
                        if (leftOfWind){
                            sailDir = -sailDir;
                        } else {
                            sailDir = windDir - sailDir;
                        }
                    } else {
                        sailDir = visualiserRace.getVisualiserRaceState().getWindDirection().degrees();
                    }

                    //get new place to move towards
                    double compA = ((sailCurrent - sailDir) % 360 + 360) % 360;//degrees right
                    if (compA > 180) compA = 360 - compA;
                    double compB = ((sailDir - sailCurrent) % 360 + 360) % 360;//degrees left
                    if (compB > compA){
                        if (compA > turnRate){
                            sailCurrent = ((sailCurrent - turnRate) % 360 + 360) % 360;
                            canLuff = false;
                        } else {
                            sailCurrent = sailDir;
                            canLuff = true;
                        }
                    } else {
                        if (compB > turnRate){
                            sailCurrent = ((sailCurrent + turnRate) % 360 + 360) % 360;
                            canLuff = false;
                        } else {
                            sailCurrent = sailDir;
                            canLuff = true;
                        }
                    }
                    sailsSubject.setHeading(sailCurrent);
                    if (canLuff) {
                        if (boat.isSailsOut()) {
                            if (sails3D.isLuffing()) {
                                sails3D.stopLuffing();
                            }
                        } else {
                            if (!sails3D.isLuffing()) {
                                sails3D.startLuffing();
                            }
                        }
                    }
                    sailsSubject.setX(gpsConverter.convertGPS(boat.getPosition()).getX());
                    sailsSubject.setZ(gpsConverter.convertGPS(boat.getPosition()).getY());
                    sailsSubject.getMesh().toFront();
                }
            };
            sailsFollowBoat.start();

            // Track this boat's movement with the new subject
            AnimationTimer trackBoat = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    boatModel.setHeading(boat.getBearing().degrees());
                    boatModel.setX(gpsConverter.convertGPS(boat.getPosition()).getX());
                    boatModel.setZ(gpsConverter.convertGPS(boat.getPosition()).getY());
                    boatModel.getMesh().toFront();

                    //Fire follows boat
                    healthEffect.setHeading(boat.getBearing().degrees());
                    healthEffect.setX(gpsConverter.convertGPS(boat.getPosition()).getX());
                    healthEffect.setZ(gpsConverter.convertGPS(boat.getPosition()).getY());
                    healthEffect.setY(0);

                }
            };
            trackBoat.start();

            //next mark indicator
            //Material markColor = new PhongMaterial(new Color(0.15,0.9,0.2,1));
            /*CompoundMark nextMark = boat.getCurrentLeg().getEndCompoundMark();

            view3D.getShape(nextMark.getMark1().getSourceID()).getMesh().setMaterial(markColor);
            if(nextMark.getMark2() != null) {
                view3D.getShape(nextMark.getMark2().getSourceID()).getMesh().setMaterial(markColor);
            }*/

            Subject3D shockwave = new Shockwave(10);
            viewSubjects.add(shockwave);

            //boat.legProperty().addListener((o, prev, curr) -> Platform.runLater(() -> swapColours(curr)));
            boat.hasCollidedProperty().addListener((o, prev, curr) -> Platform.runLater(() -> showCollision(boat, shockwave)));
        }
        // Fix initial bird's-eye position
        view3D.updatePivot(new Translate(250, 0, 210));

        view3D.targetProperty().addListener((o, prev, curr)-> {
            if(curr != null && visualiserRace.getVisualiserRaceState().isVisualiserBoat(curr.getSourceID())) {
                addThirdPersonAnnotations(curr);
            } else {
                removeThirdPersonAnnotations();
            }
        });

        // Bind zooming to scrolling
        view3D.setOnScroll(e -> {
            //view3D.updateDistance(e.getDeltaY());
            if (e.getDeltaY() > 0) {
                view3D.zoomIn();
            } else {
                view3D.zoomOut();
            }
        });

        // Bind zooming to keypress (Z/X default)
        racePane.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            ControlKey key = keyFactory.getKey(e.getCode().toString());
            if(key != null) {
                switch (key.toString()) {
                    case "Zoom In":
                        //Check if race is a tutorial
                        if (isTutorial) {
                            //Check if the current tutorial state is zoom-in
                            if (currentState.equals(TutorialState.ZOOMIN)) {
                                try {
                                    //Update tutorial
                                    checkTutorialState();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        view3D.zoomIn();
                        break;
                    case "Zoom Out":
                        //Check if race is a tutorial
                        if(isTutorial) {
                            //Check if current tutorial state is zoom-out
                            if (currentState.equals(TutorialState.ZOOMOUT)) {
                                try {
                                    //Update tutorial
                                    checkTutorialState();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        view3D.zoomOut();
                        break;
                }
            }
        });
    }

    private void showCollision(VisualiserBoat boat, Subject3D shockwave) {
        Subject3D boatModel = view3D.getShape(boat.getSourceID());
        AnimationTimer shockFront = new AnimationTimer() {
            double opacity = 1;

            @Override
            public void handle(long now) {
                shockwave.setX(boatModel.getPosition().getX());
                shockwave.setY(boatModel.getPosition().getY());
                shockwave.setZ(boatModel.getPosition().getZ());

                if(opacity <= 0) {
                    shockwave.getMesh().setMaterial(new PhongMaterial(Color.TRANSPARENT));
                    boat.setHasCollided(false);
                    this.stop();
                }
                else {
                    shockwave.getMesh().setMaterial(new PhongMaterial(new Color(1,0,0,opacity)));
                    opacity -= 0.1;
                }
            }
        };
        shockFront.start();
    }

    private void addThirdPersonAnnotations(Subject3D subject3D) {
        nextMarkController.show3d();
//        viewSubjects.add(nextMarkArrow);
//        final VisualiserBoat boat;
//        try {
//            boat = visualiserRace.getVisualiserRaceState().getBoat(subject3D.getSourceID());
//        } catch (BoatNotFoundException e) {
//            e.printStackTrace();
//            return;
//        }
//        arrowToNextMark = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                CompoundMark target = boat.getCurrentLeg().getEndCompoundMark();
//                Bearing headingToMark = GPSCoordinate.calculateBearing(boat.getPosition(), target.getAverageGPSCoordinate());
//
//                nextMarkArrow.setX(view3D.getPivot().getX());
//                nextMarkArrow.setY(view3D.getPivot().getY());
//                nextMarkArrow.setZ(view3D.getPivot().getZ() + 15);
//                nextMarkArrow.setHeading(headingToMark.degrees());
//            }
//        };
//        arrowToNextMark.start();
    }

    private void removeThirdPersonAnnotations() {
        nextMarkController.show2d();
    }

    private void setNextMarkArrowDirection(Mark mark1, Mark mark2, RoundingType roundingType, int direction){
        boolean port = roundingType == RoundingType.Port;
        Mark chosenMark = null;

        if (mark2 == null){
            chosenMark = mark1;
        } else {
            boolean rounding = port;
            switch (direction) {
                case 2:
                    rounding = !port;
                case 0:
                    if (rounding){
                        if (mark1.getPosition().getLongitude() < mark2.getPosition().getLongitude()){
                            chosenMark = mark1;
                        } else {
                            chosenMark = mark2;
                        }
                    } else {
                        if (mark1.getPosition().getLongitude() > mark2.getPosition().getLongitude()){
                            chosenMark = mark1;
                        } else {
                            chosenMark = mark2;
                        }
                    }
                    break;
                case 3:
                    rounding = !port;
                case 1:
                    if (rounding){
                        if (mark1.getPosition().getLatitude() > mark2.getPosition().getLatitude()){
                            chosenMark = mark1;
                        } else {
                            chosenMark = mark2;
                        }
                    } else {
                        if (mark1.getPosition().getLatitude() < mark2.getPosition().getLatitude()){
                            chosenMark = mark1;
                        } else {
                            chosenMark = mark2;
                        }
                    }
                    break;
            }
        }

        if (chosenMark == null){
            System.err.println("Mark to pick is null.");
            return;
        }

        if (port){
            Assets3D.ccwNextArrow.setX(gpsConverter.convertGPS(chosenMark.getPosition()).getX());
            Assets3D.ccwNextArrow.setZ(gpsConverter.convertGPS(chosenMark.getPosition()).getY());
            Assets3D.ccwNextArrow.getMesh().setVisible(true);
            Assets3D.cwNextArrow.getMesh().setVisible(false);
        } else {
            Assets3D.cwNextArrow.setX(gpsConverter.convertGPS(chosenMark.getPosition()).getX());
            Assets3D.cwNextArrow.setZ(gpsConverter.convertGPS(chosenMark.getPosition()).getY());
            Assets3D.cwNextArrow.getMesh().setVisible(true);
            Assets3D.ccwNextArrow.getMesh().setVisible(false);
        }
    }

    private void changeNextMark(Leg leg){
        CompoundMark start = leg.getStartCompoundMark();
        CompoundMark end = leg.getEndCompoundMark();

        //The last leg "finish" doesn't have compound marks.
        if (start == null || end == null ) {
            return;
        }


        //find direction coming
        double angle = gpsConverter.getAngle(gpsConverter.convertGPS(start.getMark1().getPosition()),
                gpsConverter.convertGPS(end.getMark1Position()));
        angle = (Math.toDegrees(angle) % 360 + 360) % 360;
        int dir = 0; //0 = top 1 = right 2 = down 3 = down
        if (angle < 315){
            if (angle > 45){
                dir ++;
            }
            if (angle > 135){
                dir ++;
            }
            if (angle > 225){
                dir ++;
            }
        }

        Mark startMark1 = end.getMark1();
        Mark startMark2 = end.getMark2();

        setNextMarkArrowDirection(startMark1, startMark2, end.getRoundingType(), dir);
    }

    /**
     * Initialises the frame rate functionality. This allows for toggling the
     * frame rate, and connect the fps label to the race's fps property.
     */
    private void initialiseFps() {
        // fps toggle listener
        showFPS.selectedProperty().addListener((ov, old_val, new_val) -> {
            if (showFPS.isSelected()) {
                FPS.setVisible(true);
            } else {
                FPS.setVisible(false);
            }
        });

        // fps label display
        this.visualiserRace.getFrameRateProperty().addListener((observable,
                oldValue, newValue) -> {
            Platform.runLater(() ->
                    this.FPS.setText("FPS: " + newValue.toString()));
        });
    }

    /**
     * Initialises the information table view to listen to a given race.
     */
    private void initialiseInfoTable() {
        // list of boats to display data for
        ObservableList<VisualiserBoat> boats = FXCollections
                .observableArrayList(this.visualiserRace.getVisualiserRaceState().getBoats());
        SortedList<VisualiserBoat> sortedBoats = new SortedList<>(boats);
        sortedBoats.comparatorProperty().bind(boatInfoTable.comparatorProperty());

        // update list when boat information changes
        this.visualiserRace.getVisualiserRaceState().getBoats().addListener(
                (ListChangeListener.Change<? extends VisualiserBoat> c) -> Platform.runLater(() -> {
            boats.setAll(this.visualiserRace.getVisualiserRaceState().getBoats());
        }));

        // set table data
        boatInfoTable.setItems(sortedBoats);
        boatTeamColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty()
        );
        boatSpeedColumn.setCellValueFactory(
                cellData -> cellData.getValue().currentSpeedProperty()
        );
        boatMarkColumn.setCellValueFactory(
                cellData -> cellData.getValue().legProperty()
        );
        boatHealthColumn.setCellValueFactory(
                cellData -> cellData.getValue().healthProperty()
        );

        //Kind of ugly, but allows for formatting an observed speed.
        boatSpeedColumn.setCellFactory(
                new Callback<TableColumn<VisualiserBoat, Number>, TableCell<VisualiserBoat, Number>>() {
                    @Override
                    public TableCell<VisualiserBoat, Number> call(TableColumn<VisualiserBoat, Number> param) {
                        //We return a table cell that populates itself with a Number, and formats it.
                        return new TableCell<VisualiserBoat, Number>(){

                            //Function to update the cell text.
                            @Override
                            protected void updateItem(Number item, boolean empty) {
                                if (item != null) {
                                    super.updateItem(item, empty);
                                    setText(String.format("%.2fkn", item.doubleValue()));
                                }
                            }
                        };
                    }
                });

        //Kind of ugly, but allows for turning an observed Leg into a string.
        boatMarkColumn.setCellFactory(
                new Callback<TableColumn<VisualiserBoat, Leg>, TableCell<VisualiserBoat, Leg>>() {
                    @Override
                    public TableCell<VisualiserBoat, Leg> call(TableColumn<VisualiserBoat, Leg> param) {
                        //We return a table cell that populates itself with a Leg's name.
                        return new TableCell<VisualiserBoat, Leg>(){

                            //Function to update the cell text.
                            @Override
                            protected void updateItem(Leg item, boolean empty) {
                                if (item != null) {
                                    super.updateItem(item, empty);
                                    setText(item.getName());
                                }
                            }
                        };
                    }
                });
    }

    /**
     * Initialises the race clock to listen to the specified race.
     */
    private void initialiseRaceClock() {
        raceState.getRaceClock().durationProperty().addListener((observable,
                oldValue, newValue) -> {
            Platform.runLater(() -> {
                timer.setText(newValue);
            });
        });
    }

    /**
     * Transition from the race view to the finish view.
     * @throws IOException Thrown if the finish scene cannot be loaded.
     */
    private void finishRace() throws IOException {
        RaceFinishController fc =
                (RaceFinishController)loadScene("raceFinish.fxml");
        fc.loadFinish(raceState.getBoats());
    }

    /**
     * Timer which monitors the race.
     */
    private void raceTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                //If the race has finished, go to finish view.
                if (raceState.getRaceStatusEnum() == RaceStatusEnum.FINISHED) {
                    stop(); // stop the timer
                    try {
                        finishRace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    raceCanvas.drawRace();

                    boatInfoTable.sort();
                }

                //Return to main screen if we lose connection.
                if (!visualiserRace.getServerConnection().isAlive()) {
                    try {
                        loadTitleScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //TODO we should display an error to the user
                    //TODO also need to "reset" any state (race, connections, etc...).
                }
            }
        }.start();
    }

    /**
     * Animation timer loop for the speedometer
     */
    private void speedometerLoop(){
        new AnimationTimer(){
            @Override
            public void handle(long arg0){
                if (raceState.getRaceStatusEnum() == RaceStatusEnum.FINISHED) {
                    stop(); // stop the timer
                } else {
                    try {
                        //Set the current speed value of the boat
                        gauge.setValue(raceState.getBoat(raceState.getPlayerBoatID()).getCurrentSpeed());
                        fGauge.getGauge().setValue(raceState.getBoat(raceState.getPlayerBoatID()).getCurrentSpeed());

                        //Create list with sorted boat placements
                        List<VisualiserBoat> boatList = boatInfoTable.getItems();
                        for (VisualiserBoat boat : boatList){
                            if(raceState.getPlayerBoatID()==boat.getSourceID()){
                                //Set boat current placement value as title of speedometer
                                gauge.titleProperty().setValue("Position: " + (boatInfoTable.getItems().indexOf(boat)+1));
                                fGauge.getGauge().titleProperty().setValue("Position: " + (boatInfoTable.getItems().indexOf(boat)+1));
                            }
                        }
                    } catch (BoatNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * Animation timer for health
     */
    private void healthLoop(){
        new AnimationTimer(){
            @Override
            public void handle(long arg0){
                if (raceState.getRaceStatusEnum() == RaceStatusEnum.FINISHED) {
                    stop(); // stop the timer
                } else {
                    try {
                        //Check if boat is dead
                        if(raceState.getBoat(raceState.getPlayerBoatID()).getHealth()<=0){
                            if(!deathPane.isDisable()) {
                                deathPane.setVisible(true);
                            }
                        }
                    } catch (BoatNotFoundException e) {
                        e.printStackTrace();
                    }

                    for(VisualiserBoat boat : raceState.getBoats()){
                        for (HealthEffect fp : healthEffectList){

                            if(fp.getSourceID()==boat.getSourceID()){
                                if(boat.getHealth()<=0){
                                    //Boat is dead. Don't check it anymore for hp
                                    fp.displayDeath(fp.getSourceID()==raceState.getPlayerBoatID());
                                    Annotation3D sharks = new Annotation3D(Assets3D.getSharks());
                                    sharks.setX(gpsConverter.convertGPS(boat.getPosition()).getX());
                                    sharks.setZ(gpsConverter.convertGPS(boat.getPosition()).getY());
                                    viewSubjects.add(sharks);
                                    new AnimationTimer(){
                                        @Override
                                        public void handle(long now) {
                                            sharks.setHeading(sharks.getHeading().getAngle() - 0.5);
                                        }
                                    }.start();

                                    fp.setSourceID(0);
                                }
                                else
                                    //Speed up tick when <=10 hp
                                if(boat.getHealth()<=10){
                                    fp.flash(System.currentTimeMillis(), 300, boat.getSourceID()==raceState.getPlayerBoatID());
                                }
                                else
                                    //Visual indication of low hp
                                if(boat.getHealth()<=20) {
                                    //fp.setVisible(true);
                                    fp.flash(System.currentTimeMillis(), 500, boat.getSourceID()==raceState.getPlayerBoatID());
                                } else {
                                    fp.setVisible(false);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * toggles if the info table is shown
     */
    private void toggleTable() {
        infoWrapper.setVisible(infoTableShow);
        boatInfoTable.refresh();
        infoTableShow = !infoTableShow;
    }

    /**
     * Get the next tutorial state
     */
    private void updateTutorialState(){
        //Next tutorial state is popped from list
        currentState = tutorialStates.get(0);
        tutorialStates.remove(0);
    }

    /**
     * Search key map for key given string of command
     * @param command the command of the key
     */
    private void searchMapForKey(String command){
        //For loop through keyFactory
        for (Map.Entry<String, ControlKey> entry: keyFactory.getKeyState().entrySet()){
            if(entry.getValue().toString().equals(command)){

                //Found next key required to press
                keyToPress = entry.getKey();
            }
        }
    }

    /**
     * Updates tutorial state and gui display for tutorial text
     * @throws Exception Exception thrown
     */
    private void checkTutorialState() throws Exception {
        //Switch statement to check what the current tutorial state is
        switch (currentState){
            case UPWIND:
                //Set next key to press as the downwind key
                searchMapForKey("Downwind");
                //Update tutorial text
                tutorialText.setText("Nice! To turn downwind press " + keyToPress + ".");
                updateTutorialState();
                break;
            case DOWNWIND:
                //Set next key to press as the tack/gybe key
                searchMapForKey("Tack/Gybe");
                //Update tutorial text
                tutorialText.setText("Nice! To tack or gybe press " + keyToPress + ".");
                updateTutorialState();
                break;
            case TACKGYBE:
                //Set next key to press as the VMG key
                searchMapForKey("VMG");
                //Update tutorial text
                tutorialText.setText("Nice! To use VMG press " + keyToPress + ". This will turn the boat.");
                updateTutorialState();
                break;
            case VMG:
                //Set next key to press as the sails-in key
                searchMapForKey("Toggle Sails");
                //Update tutorial text
                tutorialText.setText("Nice! To sails in press " + keyToPress + ". This will stop the boat.");
                updateTutorialState();
                break;
            case SAILSIN:
                //Set next key to press as the sails-out key
                searchMapForKey("Toggle Sails");
                //Update tutorial text
                tutorialText.setText("Nice! To sails out press " + keyToPress + " again. The will start moving again.");
                updateTutorialState();
                break;
            case SAILSOUT:
                //Set next key to press as the zoom-in key
                searchMapForKey("Zoom In");
                //Update tutorial text
                tutorialText.setText("Nice! To zoom in press " + keyToPress + ".");
                updateTutorialState();
                break;
            case ZOOMIN:
                //Set next key to press as the zoom-out key
                searchMapForKey("Zoom Out");
                //Update tutorial text
                tutorialText.setText("Nice! You will also be able to zoom into boats and marks by clicking them. \nTo zoom out press " + keyToPress + ".");
                updateTutorialState();
                break;
            case ZOOMOUT:
                //Finished tutorial. Display pop-up for exiting the tutorial
                tutorialText.setText("Congratulations! You're done!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Finished Tutorial");
                alert.setHeaderText("You have finished the tutorial.");
                alert.setContentText("Now you know the controls you are ready to race!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    App.game.endEvent();
                    loadTitleScreen();
                }
                break;
            default:
                //State not found. Exit tutorial to title menu
                App.game.endEvent();
                loadTitleScreen();
                break;
        }
    }

    private void checkKonami(BoatActionEnum protocolCode){
        if (protocolCode == konamiCodes.get(konamiIndex)){
            if (konamiIndex < konamiCodes.size() - 1) {
                konamiIndex++;
                System.out.println("Next Konami Code is: " + konamiCodes.get(konamiIndex));
            } else {
                turnOnTheKonami();
            }
        } else {
            konamiIndex = 0;
        }
    }

    private void turnOnTheKonami(){
        for(Subject3D seagull: seagulls){
            ((SeagullMesh) seagull.getMesh()).konamiTriggered();
        }
        InputStream newHp = this.getClass().getClassLoader().getResourceAsStream("visualiser/images/fannypride.png");
        initialiseHealthPane(newHp);
    }

    /**
     * Initialises the map
     */
    private void initialiseRaceCanvas() {

        //Create canvas.
        raceCanvas = new ResizableRaceCanvas(raceState);

        //Set properties.
        raceCanvas.setMouseTransparent(true);
        raceCanvas.widthProperty().bind(canvasBase1.widthProperty());
        raceCanvas.heightProperty().bind(canvasBase1.heightProperty());

        //Draw it and show it.
        raceCanvas.draw();
        raceCanvas.setVisible(true);

        //Add to scene.
        canvasBase1.getChildren().add(0, raceCanvas);
    }

    private void bigMap(){
        if (mapToggle){
            raceCanvas.widthProperty().bind(canvasBase2.widthProperty());
            raceCanvas.heightProperty().bind(canvasBase2.heightProperty());

            raceCanvas.setFullScreen(true);
            raceCanvas.setOpacity(0.6);

            canvasBase1.getChildren().remove(raceCanvas);
            canvasBase2.getChildren().add(0, raceCanvas);

        }else{
            raceCanvas.widthProperty().bind(canvasBase1.widthProperty());
            raceCanvas.heightProperty().bind(canvasBase1.heightProperty());

            raceCanvas.setFullScreen(false);
            raceCanvas.setOpacity(1);

            canvasBase2.getChildren().remove(raceCanvas);
            canvasBase1.getChildren().add(0, raceCanvas);
        }
        mapToggle = !mapToggle;
    }

    /**
     * FXML method for death button
     */
    public void deathOKPressed(){
        deathPane.setDisable(true);
        deathPane.setVisible(false);
    }


    private AnimationTimer seagullGoTo = new AnimationTimer() {
        @Override
        public void handle(long now) {
            for (Subject3D seagull: seagulls) {
//                view3D.getPivot().getX(), view3D.getPivot().getZ();

//                if (Math.abs(seagull.getX() - view3D.getPivot().getX()) < 75 &&
//                        Math.abs(seagull.getZ() - view3D.getPivot().getZ()) < 75) {
                if (Math.abs(seagull.getX() - clientBoat.getX()) < 75 &&
                        Math.abs(seagull.getZ() - clientBoat.getZ()) < 75) {
                    ((SeagullMesh) seagull.getMesh()).playCry();
                }
                if (seagullsGoToX.get(seagull).size() > 0) {
                    //System.out.println(xPosition + " " + yPosition);
                    seagull.setHeading(GPSConverter.getAngle(new GraphCoordinate(seagull.getX(), seagull.getZ()),
                            new GraphCoordinate(seagullsGoToX.get(seagull).get(0), seagullsGoToY.get(seagull).get(0))));
                    double delx = seagullsGoToX.get(seagull).get(0) - seagull.getX();
                    double dely = seagullsGoToY.get(seagull).get(0) - seagull.getZ();
                    double scale = seagullSpeed / Math.sqrt(delx * delx + dely * dely);
                    if (scale < 1) {
                        seagull.setX(seagull.getX() + delx * scale);
                        seagull.setZ(seagull.getZ() + dely * scale);
                    } else {
                        seagullsGoToX.get(seagull).remove(0);
                        seagullsGoToY.get(seagull).remove(0);
                    }
                } else {
                    randSeagullNewAction(seagull);
                }
            }
        }
    };


    public void randSeagullNewAction(Subject3D seagull){
        Random rand = new Random();
        int nextAction = rand.nextInt(1);
        switch(nextAction){
            case 0:
                //do a straight line
                double nextX = rand.nextInt((int)gpsConverter.getLongitudeFactor());
                /*if (nextX > this.xBound/2){
                    nextX = - nextX/2;
                }*/
                double nextY = rand.nextInt((int)gpsConverter.getLatitudeFactor());
                /*if (nextY > this.yBound/2){
                    nextY = - nextY/2;
                }*/
                seagullsGoToX.get(seagull).add(nextX);
                seagullsGoToY.get(seagull).add(nextY);
                break;
            case 1:
                //do a octogan circle
                seagullsGoToX.get(seagull).add(seagull.getX() - 3);
                seagullsGoToX.get(seagull).add(seagull.getX() - 3);
                seagullsGoToX.get(seagull).add(seagull.getX());
                seagullsGoToX.get(seagull).add(seagull.getX() + 3);
                seagullsGoToX.get(seagull).add(seagull.getX() + 6);
                seagullsGoToX.get(seagull).add(seagull.getX() + 6);
                seagullsGoToX.get(seagull).add(seagull.getX() + 3);
                seagullsGoToX.get(seagull).add(seagull.getX());
                //y
                seagullsGoToY.get(seagull).add(seagull.getZ() - 3);
                seagullsGoToY.get(seagull).add(seagull.getZ() - 6);
                seagullsGoToY.get(seagull).add(seagull.getZ() - 9);
                seagullsGoToY.get(seagull).add(seagull.getZ() - 9);
                seagullsGoToY.get(seagull).add(seagull.getZ() - 6);
                seagullsGoToY.get(seagull).add(seagull.getZ() - 3);
                seagullsGoToY.get(seagull).add(seagull.getZ());
                seagullsGoToY.get(seagull).add(seagull.getZ());
                break;
        }
    }


    public void addToFlock(){
        Annotation3D newSeagull = new Annotation3D(new SeagullMesh());
        newSeagull.setY(-15);
        Random rand = new Random();
        newSeagull.setX(rand.nextInt((int)gpsConverter.getLongitudeFactor()));
        newSeagull.setZ(rand.nextInt((int)gpsConverter.getLatitudeFactor()));
        newSeagull.setXRot(0);
        seagulls.add(newSeagull);
        seagullsGoToX.put(newSeagull, new ArrayList<>());
        seagullsGoToY.put(newSeagull, new ArrayList<>());
        viewSubjects.add(newSeagull);
        randSeagullNewAction(newSeagull);
    }

}
