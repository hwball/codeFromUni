package visualiser.model;


import javafx.beans.property.IntegerProperty;
import mock.model.commandFactory.CompositeCommand;
import network.Messages.Enums.RequestToJoinEnum;
import shared.dataInput.EmptyBoatDataSource;
import shared.dataInput.EmptyRaceDataSource;
import shared.dataInput.EmptyRegattaDataSource;
import visualiser.gameController.ControllerClient;
import visualiser.network.HttpMatchBrowserHost;
import visualiser.network.ServerConnection;

import java.io.IOException;
import java.net.Socket;


/**
 * This class holds a race, and a client's connection to it
 */
public class VisualiserRaceEvent {

    /**
     * Our connection to the server.
     */
    private ServerConnection serverConnection;
    /**
     * The thread serverConnection is running on.
     */
    private Thread serverConnectionThread;



    /**
     * The race object which describes the currently occurring race.
     */
    private VisualiserRaceState visualiserRaceState;


    /**
     * The service for updating the {@link #visualiserRaceState}.
     */
    private VisualiserRaceService visualiserRaceService;
    /**
     * The thread {@link #visualiserRaceService} is running on.
     */
    private Thread visualiserRaceServiceThread;



    /**
     * Creates a visualiser race event, with a given socket and request type.
     * @param socket The socket to connect to.
     * @param requestType The type of {@link network.Messages.RequestToJoin} to make.
     * @throws IOException Thrown if there is a problem with the socket.
     */
    public VisualiserRaceEvent(Socket socket, RequestToJoinEnum requestType) throws IOException {

        this.visualiserRaceState = new VisualiserRaceState(new EmptyRaceDataSource(), new EmptyRegattaDataSource(), new EmptyBoatDataSource());


        CompositeCommand raceCommands = new CompositeCommand();
        this.visualiserRaceService = new VisualiserRaceService(raceCommands, visualiserRaceState);

        this.visualiserRaceServiceThread = new Thread(visualiserRaceService, "VisualiserRaceEvent()->VisualiserRaceService thread " + visualiserRaceService);
        this.visualiserRaceServiceThread.start();


        this.serverConnection = new ServerConnection(socket, visualiserRaceState, raceCommands, requestType);
        this.serverConnectionThread = new Thread(serverConnection, "RaceStartController.enterLobby()->serverConnection thread " + serverConnection);
        this.serverConnectionThread.start();


    }


    /**
     * Returns the state of the race.
     * @return The state of the race.
     */
    public VisualiserRaceState getVisualiserRaceState() {
        return visualiserRaceState;
    }


    /**
     * Returns the controller client, which writes BoatAction messages to the outgoing queue.
     * @return The ControllerClient.
     */
    public ControllerClient getControllerClient() {
        return serverConnection.getControllerClient();
    }

    /**
     * Returns the connection to server.
     * @return Connection to server.
     */
    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    /**
     * Returns the framerate property of the race.
     * @return Framerate property of race.
     */
    public IntegerProperty getFrameRateProperty() {
        return visualiserRaceService.getFrameRateProperty();
    }



    /**
     * Terminates the server connection and race service.
     */
    public void terminate() {
        this.visualiserRaceServiceThread.interrupt();
        this.serverConnectionThread.interrupt();
        serverConnection.terminate();
        if (HttpMatchBrowserHost.httpMatchBrowserHost != null) {
            HttpMatchBrowserHost.httpMatchBrowserHost.interrupt();
        }
    }
}
