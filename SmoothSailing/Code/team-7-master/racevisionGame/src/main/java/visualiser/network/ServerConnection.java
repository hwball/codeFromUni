package visualiser.network;


import mock.model.commandFactory.Command;
import mock.model.commandFactory.CompositeCommand;
import network.MessageRouters.MessageRouter;
import network.Messages.AC35Data;
import network.Messages.Enums.MessageType;
import network.Messages.Enums.RequestToJoinEnum;
import network.StreamRelated.MessageDeserialiser;
import network.StreamRelated.MessageSerialiser;
import shared.model.RunnableWithFramePeriod;
import visualiser.enums.ConnectionToServerState;
import visualiser.gameController.ControllerClient;
import visualiser.model.VisualiserRaceController;
import visualiser.model.VisualiserRaceState;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the client-server connection handshake.
 */
public class ServerConnection implements RunnableWithFramePeriod {

    /**
     * The socket for the connection to server.
     */
    private Socket socket;





    /**
     * Used to send client input to server.
     */
    private ControllerClient controllerClient;



    /**
     * Used to write messages to socket.
     */
    private MessageSerialiser messageSerialiser;
    /**
     * The thread {@link #messageSerialiser} runs on.
     */
    private Thread messageSerialiserThread;

    /**
     * Used to read messages from socket.
     */
    private MessageDeserialiser messageDeserialiser;
    /**
     * The thread {@link #messageDeserialiser} runs on.
     */
    private Thread messageDeserialiserThread;




    /**
     * Router to route messages to correct queue.
     */
    private MessageRouter messageRouter;
    /**
     * The thread {@link #messageRouter} runs on.
     */
    private Thread messageRouterThread;



    /**
     * The state of the connection to the client.
     */
    private ConnectionToServer connectionToServer;
    /**
     * The thread {@link #connectionToServer} runs on.
     */
    private Thread connectionToServerThread;

    /**
     * The controller which handles JoinAcceptance messages.
     */
    private ConnectionToServerController connectionToServerController;
    /**
     * The thread {@link #connectionToServerController} runs on.
     */
    private Thread connectionToServerControllerThread;



    /**
     * Tracks the heartBeat status of the connection.
     */
    private IncomingHeartBeatService heartBeatService;
    /**
     * The thread {@link #heartBeatService} runs on.
     */
    private Thread heartBeatServiceThread;

    /**
     * Tracks the heartBeat status of the connection.
     */
    private IncomingHeartBeatController heartBeatController;
    /**
     * The thread {@link #heartBeatController} runs on.
     */
    private Thread heartBeatControllerThread;


    /**
     * This is the race we are modelling.
     */
    private VisualiserRaceState visualiserRaceState;

    /**
     * The CompositeCommand to place race commands in.
     */
    private CompositeCommand raceCommands;


    /**
     * Used to convert incoming messages into a race snapshot.
     */
    private VisualiserRaceController visualiserRaceController;
    /**
     * The thread {@link #visualiserRaceController} runs on.
     */
    private Thread visualiserRaceControllerThread;




    /**
     * Creates a server connection, using a given socket.
     * @param socket The socket which connects to the client.
     * @param visualiserRaceState The race for the {@link VisualiserRaceController} to send commands to.
     * @param raceCommands The CompositeCommand to place race commands in.
     * @param requestType The type of join request to make.
     * @throws IOException Thrown if there is a problem with the client socket.
     */
    public ServerConnection(Socket socket, VisualiserRaceState visualiserRaceState, CompositeCommand raceCommands, RequestToJoinEnum requestType) throws IOException {
        this.socket = socket;
        this.visualiserRaceState = visualiserRaceState;
        this.raceCommands = raceCommands;

        createMessageSerialiser(socket);
        createMessageDeserialiser(socket);

        createRouter(messageDeserialiser.getMessagesRead());

        createConnectionToServer(requestType);


        messageRouterThread.start();


        this.controllerClient = new ControllerClient(messageRouter.getIncomingMessageQueue());

    }



    /**
     * Creates this connection's {@link MessageRouter}, and gives it a queue to read from.
     * Does not start {@link #messageRouterThread}. Start it after setting up any initial routes.
     * @param inputQueue Queue for the MessageRouter to read from.
     */
    private void createRouter(BlockingQueue<AC35Data> inputQueue) {
        this.messageRouter = new MessageRouter(inputQueue);

        this.messageRouterThread = new Thread(messageRouter, "ServerConnection()->MessageRouter thread " + messageRouter);

        //Unrouted messages get sent back to the router. Kind of ugly, but we do this to ensure that no messages are lost while initializing (e.g., XML message being received before setting up the route for it).
        messageRouter.addDefaultRoute(messageRouter.getIncomingMessageQueue());
    }


    /**
     * Creates the {@link #connectionToServer} and {@link #connectionToServerController}, and starts their threads.
     * @param requestType The type of join request to make to server.
     */
    private void createConnectionToServer(RequestToJoinEnum requestType) {

        //ConnectionToServer executes these commands.
        BlockingQueue<Command> commands = new LinkedBlockingQueue<>();
        this.connectionToServer = new ConnectionToServer(ConnectionToServerState.UNKNOWN, requestType, commands, messageRouter.getIncomingMessageQueue());

        //ConnectionToServerController receives messages, and places commands on the above command queue.
        BlockingQueue<AC35Data> incomingJoinMessages = new LinkedBlockingQueue<>();
        this.connectionToServerController = new ConnectionToServerController(incomingJoinMessages, connectionToServer, commands);

        //Route JoinAcceptance messages to the controller, and RequestToJoin to the socket.
        this.messageRouter.addRoute(MessageType.JOIN_ACCEPTANCE, incomingJoinMessages);
        this.messageRouter.addRoute(MessageType.REQUEST_TO_JOIN, messageSerialiser.getMessagesToSend());


        //Start the above on new threads.
        this.connectionToServerThread = new Thread(connectionToServer, "ServerConnection()->ConnectionToServer thread " + connectionToServer);
        this.connectionToServerThread.start();

        this.connectionToServerControllerThread = new Thread(connectionToServerController,"ServerConnection()->ConnectionToServerController thread " + connectionToServerController);
        this.connectionToServerControllerThread.start();
    }

    /**
     * Removes connection message related routes from the router.
     * This is called after the client-server connection is properly established, so that any future (erroneous) connection messages get ignored.
     */
    private void removeConnectionRoutes() {
        this.messageRouter.removeRoute(MessageType.JOIN_ACCEPTANCE);
        this.messageRouter.removeRoute(MessageType.REQUEST_TO_JOIN);
    }


    /**
     * Creates the {@link #messageSerialiser} and starts its thread.
     * @param socket The socket to write to.
     * @throws IOException Thrown if we cannot get an outputStream from the socket
     */
    private void createMessageSerialiser(Socket socket) throws IOException {
        BlockingQueue<AC35Data> outputQueue = new LinkedBlockingQueue<>();
        this.messageSerialiser = new MessageSerialiser(socket.getOutputStream(), outputQueue);

        this.messageSerialiserThread = new Thread(messageSerialiser, "ServerConnection()->MessageSerialiser thread " + messageSerialiser);
        this.messageSerialiserThread.start();
    }

    /**
     * Creates the {@link #messageDeserialiser} and starts its thread.
     * @param socket The socket to read from.
     * @throws IOException Thrown if we cannot get an inputStream from the socket
     */
    private void createMessageDeserialiser(Socket socket) throws IOException {
        BlockingQueue<AC35Data> inputQueue = new LinkedBlockingQueue<>();
        this.messageDeserialiser = new MessageDeserialiser(socket.getInputStream(), inputQueue);

        this.messageDeserialiserThread = new Thread(messageDeserialiser, "ServerConnection()->MessageDeserialiser thread " + messageDeserialiser);
        this.messageDeserialiserThread.start();
    }


    /**
     * Creates the {@link #heartBeatService} and {@link #heartBeatController} and starts their threads.
     */
    private void createHeartBeatService() {

        //IncomingHeartBeatService executes these commands.
        BlockingQueue<Command> commands = new LinkedBlockingQueue<>();
        this.heartBeatService = new IncomingHeartBeatService(commands);

        //IncomingHeartBeatController receives messages, and places commands on the above command queue.
        BlockingQueue<AC35Data> incomingHeartBeatMessages = new LinkedBlockingQueue<>();
        this.heartBeatController = new IncomingHeartBeatController(incomingHeartBeatMessages, heartBeatService, commands);

        //Route HeartBeat messages to the controller.
        this.messageRouter.addRoute(MessageType.HEARTBEAT, incomingHeartBeatMessages);


        //Start the above on new threads.
        this.heartBeatServiceThread = new Thread(heartBeatService, "ServerConnection()->IncomingHeartBeatService thread " + connectionToServer);
        this.heartBeatServiceThread.start();

        this.heartBeatControllerThread = new Thread(heartBeatController,"ServerConnection()->IncomingHeartBeatController thread " + connectionToServerController);
        this.heartBeatControllerThread.start();

    }


    /**
     * Creates the {@link #visualiserRaceController} and starts its thread.
     */
    private void createVisualiserRaceController() {


        //VisualiserRaceController receives messages, and places commands on the race's command queue.
        BlockingQueue<AC35Data> incomingMessages = new LinkedBlockingQueue<>();
        this.visualiserRaceController = new VisualiserRaceController(incomingMessages, visualiserRaceState, raceCommands);


        //Routes.
        this.messageRouter.addRoute(MessageType.BOATLOCATION, incomingMessages);
        this.messageRouter.addRoute(MessageType.RACESTATUS, incomingMessages);
        this.messageRouter.addRoute(MessageType.RACESTARTSTATUS, incomingMessages);
        this.messageRouter.addRoute(MessageType.AVGWIND, incomingMessages);
        this.messageRouter.addRoute(MessageType.COURSEWIND, incomingMessages);
        this.messageRouter.addRoute(MessageType.CHATTERTEXT, incomingMessages);
        this.messageRouter.addRoute(MessageType.DISPLAYTEXTMESSAGE, incomingMessages);
        this.messageRouter.addRoute(MessageType.YACHTACTIONCODE, incomingMessages);
        this.messageRouter.addRoute(MessageType.YACHTEVENTCODE, incomingMessages);
        this.messageRouter.addRoute(MessageType.MARKROUNDING, incomingMessages);
        this.messageRouter.addRoute(MessageType.XMLMESSAGE, incomingMessages);
        this.messageRouter.addRoute(MessageType.ASSIGN_PLAYER_BOAT, incomingMessages);
        this.messageRouter.addRoute(MessageType.BOATSTATE, incomingMessages);
        this.messageRouter.removeDefaultRoute(); //We no longer want to keep un-routed messages.


        //Start the above on a new thread.

        this.visualiserRaceControllerThread = new Thread(visualiserRaceController, "ServerConnection()->VisualiserRaceController thread " + visualiserRaceController);
        this.visualiserRaceControllerThread.start();

    }


    //TODO create input controller here. RaceViewController should query for it, if it exists.
    private void createPlayerInputController() {

        this.messageRouter.addRoute(MessageType.BOATACTION, messageSerialiser.getMessagesToSend());
        //TODO routes
    }




    @Override
    public void run() {

        //Monitor the connection state.

        long previousFrameTime = System.currentTimeMillis();

        while (!Thread.interrupted()) {

            long currentFrameTime = System.currentTimeMillis();
            waitForFramePeriod(previousFrameTime, currentFrameTime, 100);

            previousFrameTime = currentFrameTime;


            ConnectionToServerState state = connectionToServer.getConnectionState();

            switch (state) {

                case CONNECTED:
                    connected();
                    break;

                case DECLINED:
                    declined();
                    break;

                case TIMED_OUT:
                    timedOut();
                    break;

            }

        }

    }



    /**
     * Called when the {@link #connectionToServer} state changes to {@link ConnectionToServerState#CONNECTED}.
     */
    private void connected() {

        createHeartBeatService();

        createVisualiserRaceController();

        if (connectionToServer.getRequestType() == RequestToJoinEnum.PARTICIPANT) {
            createPlayerInputController();
        }


        //We no longer want connection messages to be accepted.
        removeConnectionRoutes();


        //We interrupt as this thread's run() isn't needed anymore.
        Thread.currentThread().interrupt();
    }


    /**
     * Called when the {@link #connectionToServer} state changes to {@link ConnectionToServerState#DECLINED}.
     */
    private void declined() {
        Logger.getGlobal().log(Level.WARNING, "Server handshake failed. Connection was declined.");

        terminate();

        Thread.currentThread().interrupt();
    }


    /**
     * Called when the {@link #connectionToServer} state changes to {@link ConnectionToServerState#TIMED_OUT}.
     */
    private void timedOut() {
        Logger.getGlobal().log(Level.WARNING, "Server handshake failed. Connection timed out.");

        terminate();

        Thread.currentThread().interrupt();
    }




    /**
     * Determines whether or not this connection is still alive.
     * This is based off whether the {@link #messageDeserialiser}, {@link #messageSerialiser}, and {@link #heartBeatService} are alive.
     * @return True if it is alive, false otherwise.
     */
    public boolean isAlive() {
        return messageDeserialiser.isRunning() && messageSerialiser.isRunning() && heartBeatService.isAlive();
    }


    /**
     * Returns the controller client, which writes BoatAction messages to the outgoing queue.
     * @return The ControllerClient.
     */
    public ControllerClient getControllerClient() {
        return controllerClient;
    }




    /**
     * Terminates the connection and any running threads.
     */
    public void terminate() {

        if (this.messageRouterThread != null) {
            this.messageRouterThread.interrupt();
        }


        if (this.messageSerialiserThread != null) {
            this.messageSerialiserThread.interrupt();
        }
        if (this.messageDeserialiserThread != null) {
            this.messageDeserialiserThread.interrupt();
        }



        if (this.connectionToServerThread != null) {
            this.connectionToServerThread.interrupt();
        }
        if (this.connectionToServerControllerThread != null) {
            this.connectionToServerControllerThread.interrupt();
        }


        if (this.heartBeatServiceThread != null) {
            this.heartBeatServiceThread.interrupt();
        }
        if (this.heartBeatControllerThread != null) {
            this.heartBeatControllerThread.interrupt();
        }


        if (this.visualiserRaceControllerThread != null) {
            this.visualiserRaceControllerThread.interrupt();
        }

        if (this.socket != null) {
            try {
                this.socket.getInputStream().close();
                this.socket.getOutputStream().close();
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //TODO input controller?

    }


}
