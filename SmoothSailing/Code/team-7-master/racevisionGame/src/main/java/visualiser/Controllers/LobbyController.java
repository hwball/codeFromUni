package visualiser.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import network.Messages.Enums.RequestToJoinEnum;
import javafx.scene.media.AudioClip;
import network.Messages.HostGame;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.utils.JsonReader;
import visualiser.app.MatchBrowserSingleton;
import visualiser.model.RaceConnection;
import visualiser.network.HttpMatchBrowserClient;
import visualiser.network.MatchBrowserLobbyInterface;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

/**
 * Controller for the Lobby for entering games
 */
public class LobbyController extends Controller {
    private @FXML TableView<RaceConnection> lobbyTable;
    private @FXML TableColumn<RaceConnection, String> gameNameColumn;
    private @FXML TableColumn<RaceConnection, String> hostNameColumn;
    private @FXML TableColumn<RaceConnection, String> statusColumn;
    private @FXML Button joinGameBtn;
    private @FXML Button spectateButton;
    private @FXML TextField addressFld;
    private @FXML TextField portFld;

    private ObservableList<RaceConnection> allConnections;
    private ObservableList<RaceConnection> customConnections;

    private AudioClip sound;

    //the socket for match browser
    private HttpMatchBrowserClient httpMatchBrowserClient;


    public void initialize() {
        httpMatchBrowserClient = new HttpMatchBrowserClient();
        httpMatchBrowserClient.connections.addListener(new ListChangeListener<RaceConnection>() {
            @Override
            public void onChanged(Change<? extends RaceConnection> c) {
                refreshTable();
            }
        });

        new Thread(httpMatchBrowserClient, "Match Client").start();
        // set up the connection table
        customConnections = FXCollections.observableArrayList();
        allConnections = FXCollections.observableArrayList();
        //connections.add(new RaceConnection("localhost", 4942, "Local Game"));

        lobbyTable.setItems(allConnections);
        gameNameColumn.setCellValueFactory(cellData -> cellData.getValue().gamenameProperty());
        hostNameColumn.setCellValueFactory(cellData -> cellData.getValue().hostnameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        lobbyTable.getSelectionModel().selectedItemProperty().addListener((obs, prev, curr) -> {
            if (curr != null && curr.statusProperty().getValue().equals("Ready")) {
                joinGameBtn.setDisable(false);
                spectateButton.setDisable(false);
            } else {
                joinGameBtn.setDisable(true);
                spectateButton.setDisable(true);
            }
        });
        joinGameBtn.setDisable(true);
        spectateButton.setDisable(true);

        receiveMatchData();
    }

    /**
     * Refreshes the connections in the lobby
     */
    public void refreshBtnPressed(){
        sound = new AudioClip(this.getClass().getResource("/visualiser/sounds/buttonpress.wav").toExternalForm());
        sound.play();
        refreshTable();
    }

    private void refreshTable() {
        allConnections.clear();
        addCustomGames();
        addServerGames();
        for(RaceConnection connection: allConnections) {
            connection.check();
        }
        try {
            if (lobbyTable.getSelectionModel().getSelectedItem().statusProperty().getValue().equals("Ready")) {
                joinGameBtn.setDisable(false);
                spectateButton.setDisable(false);
            } else {
                joinGameBtn.setDisable(true);
                spectateButton.setDisable(true);
            }
        } catch (Exception ignored){}
    }

    /**
     * Connect to a connection.
     * @param joinType How the client wishes to join (e.g., participant).
     * @throws IOException socket error
     */
    public void connectSocket(RequestToJoinEnum joinType) throws IOException {
        httpMatchBrowserClient.interrupt();
        RaceConnection connection = lobbyTable.getSelectionModel().getSelectedItem();
        Socket socket = new Socket(connection.getHostname(), connection.getPort());
        InGameLobbyController iglc = (InGameLobbyController)loadScene("gameLobby.fxml");
        iglc.enterGameLobby(socket, false, joinType);
    }

    /**
     * Requests to join the game as a participant.
     * @throws IOException socket error.
     */
    public void connectParticipate() throws IOException {
        connectSocket(RequestToJoinEnum.PARTICIPANT);
    }

    /**
     * Requests to join the game as a spectator.
     * @throws IOException socket error.
     */
    public void connectSpectate() throws IOException {
        connectSocket(RequestToJoinEnum.SPECTATOR);
    }

    public void menuBtnPressed() throws IOException {
        sound = new AudioClip(this.getClass().getResource("/visualiser/sounds/buttonpress.wav").toExternalForm());
        sound.play();
        httpMatchBrowserClient.interrupt();
        loadScene("title.fxml");
    }

    /**
     * adds a new connection
     */
    public void addConnectionPressed(){
        sound = new AudioClip(this.getClass().getResource("/visualiser/sounds/buttonpress.wav").toExternalForm());
        sound.play();
        String hostName = addressFld.getText();
        String portString = portFld.getText();
        try {
            int port = Integer.parseInt(portString);
            customConnections.add(new RaceConnection(hostName, port, "Boat Game"));
            addressFld.clear();
            portFld.clear();
            refreshTable();
        } catch (NumberFormatException e) {
            System.err.println("Port number entered is not a number");
        }
    }

    public void receiveMatchData(){
        /*
        matchBrowserLobbyInterface = new MatchBrowserLobbyInterface();
        try {
            matchBrowserLobbyInterface.startReceivingHostData(new DatagramSocket(4941));
            Observer o = new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    refreshBtnPressed();
                }
            };
            matchBrowserLobbyInterface.addObserver(o);
        } catch (SocketException e) {
            System.err.println("Socket 4941 in use");
        }*/
    }

    /**
     * Adds the games received from the server
     */
    private void addServerGames() {
        allConnections.addAll(httpMatchBrowserClient.connections);
        /*
        for (HostGame game : matchBrowserLobbyInterface.getGames()) {
            connections.add(new RaceConnection(game.getIp(), 4942, "Boat Game"));
        }*/
    }

    private void addCustomGames() {
        allConnections.addAll(customConnections);
    }
}
