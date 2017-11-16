package visualiser.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mock.app.Event;
import mock.exceptions.EventConstructionException;
import network.Messages.Enums.RequestToJoinEnum;
import visualiser.app.App;
import visualiser.app.MatchBrowserSingleton;
import visualiser.network.HttpMatchBrowserHost;
import visualiser.network.MatchBrowserInterface;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Hosting a game.
 */
public class HostGameController extends Controller {
    private @FXML ImageView mapImage;
    private @FXML Slider sliderLength;
    private @FXML Label lblLength;
    private ArrayList<Image> listOfMaps;
    private int currentMapIndex = 0;
    private int selectedRaceLength; // in minutes
    private final int MAX_RACE_LENGTH = 30; // in minutes
    private DatagramSocket udpSocket;
    private MatchBrowserInterface matchBrowserInterface;

    public void initialize() {
        loadMaps();
        this.udpSocket = MatchBrowserSingleton.getInstance().getUdpSocket();
        this.matchBrowserInterface = MatchBrowserSingleton.getInstance().getMatchBrowserInterface();
        setRaceLengthSlider();
    }

    /**
     * Sets up the values and display for a slider object which allows a user
     * to select how many minutes long they would like their race to be.
     */
    private void setRaceLengthSlider(){
        // set the listener to update the label
        sliderLength.valueProperty().addListener((ov, old_val, new_val) -> {
            selectedRaceLength = new_val.intValue();
            if (selectedRaceLength == 1){
                lblLength.setText(selectedRaceLength + " minute.");
            } else {
                lblLength.setText(selectedRaceLength + " minutes.");
            }
        });

        // set values and marks to be displayed
        sliderLength.setMin(5);
        sliderLength.setMax(MAX_RACE_LENGTH);
        sliderLength.setShowTickLabels(true);
        sliderLength.setMajorTickUnit(MAX_RACE_LENGTH-1);
        sliderLength.setBlockIncrement(1);
    }


    /**
     * Loads in the list of playable maps to be selected from.
     */
    private void loadMaps(){
        // image preview of maps
        Image ac35Map = new Image(getClass().getClassLoader().getResourceAsStream("images/AC35_Racecourse_MAP.png"));
        Image oMap = new Image(getClass().getClassLoader().getResourceAsStream("images/oMapLayout.png"));
        Image iMap = new Image(getClass().getClassLoader().getResourceAsStream("images/iMapLayout.png"));
        Image mMap = new Image(getClass().getClassLoader().getResourceAsStream("images/mMapLayout.png"));

        listOfMaps = new ArrayList(Arrays.asList(ac35Map, oMap, iMap, mMap));
        mapImage.setImage(listOfMaps.get(currentMapIndex));
        Platform.runLater(() -> {
            mapImage.fitHeightProperty()
                    .bind(mapImage.getScene().getWindow().heightProperty().multiply(0.6));
        });
    }

    /**
     * Hosts a game
     */
    public void hostGamePressed() {
        try {
            App.game = new Event(false, currentMapIndex,
                    selectedRaceLength*60*1000);
            App.gameType = currentMapIndex;

            HttpMatchBrowserHost matchBrowserHost = new HttpMatchBrowserHost();
            new Thread(matchBrowserHost).start();
            connectSocket("localhost", 4942);
            //alertMatchBrowser();

        } catch (EventConstructionException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not create Event.", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends info to the match browser so clients can see it
     */
    public void alertMatchBrowser(){
        try{
            if (matchBrowserInterface == null){
                return;//private game
            }
            matchBrowserInterface.startSendingHostData(App.game.getHostedGameData(), udpSocket);
        }catch (IOException e){
            System.err.println("failed to send out hosted game info");
        }
    }

    /**
     * Connect to a socket
     * @param address address of the server
     * @param port port that the server is run off
     * @throws IOException socket error
     */
    public void connectSocket(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        InGameLobbyController iglc = (InGameLobbyController)loadScene("gameLobby.fxml");
        iglc.enterGameLobby(socket, true, RequestToJoinEnum.PARTICIPANT);//TODO may want to let the host spectate if they wish.
    }

    /**
     * Menu button pressed. Prompt alert then return to menu
     * @throws IOException socket error
     */
    public void menuBtnPressed() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitting race");
        alert.setContentText("Do you wish to quit the race?");
        alert.setHeaderText("You are about to quit the race");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            loadTitleScreen();
        }
    }

    /**
     * Method called when the 'next' arrow button is pressed. It is used to
     * change the currently displayed map preview to the next in the list.
     */
    public void nextImage(){
        // increase index
        currentMapIndex = (currentMapIndex + 1) % listOfMaps.size();
        // update map preview
        mapImage.setImage(listOfMaps.get(currentMapIndex));
    }

    /**
     * Method called when the 'previous' arrow button is pressed. It is used to
     * change the currently displayed map preview to the previous in the list.
     */
    public void previousImage(){
        // decrease index
        currentMapIndex = ((((currentMapIndex - 1) % listOfMaps.size()) +
                listOfMaps.size()) % listOfMaps.size());
        // update map preview
        mapImage.setImage(listOfMaps.get(currentMapIndex));
    }

    public void setCurrentMapIndex(Integer index){
        this.currentMapIndex = index;
    }

}
