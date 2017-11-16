package visualiser.Controllers;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.Enums.RequestToJoinEnum;
import visualiser.gameController.ControllerClient;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller to for waiting for the race to start.
 */
public class RaceStartController extends Controller {
    private @FXML Label raceTitleLabel;
    private @FXML Label raceStartLabel;
    private @FXML Label timeZoneTime;
    private @FXML Label timer;
    private @FXML TableView<VisualiserBoat> boatNameTable;
    private @FXML TableColumn<VisualiserBoat, String> boatNameColumn;
    private @FXML TableColumn<VisualiserBoat, String> boatCodeColumn;
    private @FXML Label raceStatusLabel;

    private VisualiserRaceEvent visualiserRaceEvent;
    private VisualiserRaceState raceState;
    private ControllerClient controllerClient;
    private boolean isHost;

    /**
     * Show starting information for a race given a socket.
     * Intended to be called on loading the scene.
     * @param socket network source of information
     * @param isHost is user a host
     */
    public void enterLobby(Socket socket, Boolean isHost) {
        try {
            this.isHost = isHost;
            this.visualiserRaceEvent = new VisualiserRaceEvent(socket, RequestToJoinEnum.PARTICIPANT);
            this.controllerClient = visualiserRaceEvent.getControllerClient();
            this.raceState = visualiserRaceEvent.getVisualiserRaceState();
            showRaceDetails();
        } catch (IOException e) {
            //TODO should probably let this propagate, so that we only enter this scene if everything works
            Logger.getGlobal()
                    .log(Level.WARNING, "Could not connect to server.", e);
        }
    }

    /**
     * Displays details and starts the timer for the race being started
     */
    private void showRaceDetails() {
        raceTitleLabel.setText(this.raceState.getRegattaName());
        initialiseBoatTable();
        initialiseRaceClock();
        countdownTimer();
    }

    /**
     * Initialises the boat table that is to be shown on the pane.
     */
    private void initialiseBoatTable() {
        //Get the boats.
        ObservableList<VisualiserBoat> boats =
                this.raceState.getBoats();

        //Populate table.
        boatNameTable.setItems(boats);
        boatNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        boatCodeColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
    }

    /**
     * Initialises the race clock/timer labels for the start time, current time, and remaining time.
     */
    private void initialiseRaceClock() {
        raceStartLabel.setText(
                this.raceState.getRaceClock().getStartingTimeString());

        // init clock start time
        this.raceState.getRaceClock().startingTimeProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                raceStartLabel.setText(newValue);
            });
        });

        // init clock current time
        this.raceState.getRaceClock().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                timeZoneTime.setText(newValue);
            });
        });

        // init clock remaining time
        this.raceState.getRaceClock().durationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                timer.setText(newValue);
            });
        });
    }

    /**
     * Countdown timer until race starts.
     */
    private void countdownTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                // display current race status
                RaceStatusEnum raceStatus = raceState.getRaceStatusEnum();
                raceStatusLabel.setText("Race Status: " + raceStatus.name());

                // if race is in PREPARATORY or STARTED status
                if (raceStatus == RaceStatusEnum.PREPARATORY || raceStatus == RaceStatusEnum.STARTED) {
                    stop(); // stop this timer
                    // load up the race scene
                    try {
                        RaceViewController rvc = (RaceViewController)
                                loadScene("raceView.fxml");
                        rvc.startRace(visualiserRaceEvent, controllerClient,
                                isHost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}