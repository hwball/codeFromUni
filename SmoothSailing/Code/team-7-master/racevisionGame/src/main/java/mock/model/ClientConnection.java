package mock.model;


import mock.app.MockOutput;
import mock.enums.ConnectionStateEnum;
import shared.exceptions.HandshakeException;
import mock.exceptions.SourceIDAllocationException;
import mock.model.commandFactory.CompositeCommand;
import network.Messages.*;
import network.Messages.Enums.JoinAcceptanceEnum;
import network.Messages.Enums.MessageType;
import network.Messages.Enums.RequestToJoinEnum;
import network.StreamRelated.MessageDeserialiser;
import network.StreamRelated.MessageSerialiser;
import visualiser.gameController.ControllerServer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the client connection handshake, and creation of MockOutput and ControllerServer.
 */
public class ClientConnection implements Runnable {

    /**
     * The socket for the client's connection.
     */
    private Socket socket;

    /**
     * Periodically sends HeartBeat messages to client.
     */
    private HeartBeatService heartBeatService;

    /**
     * The thread the {@link HeartBeatService} runs on.
     */
    private Thread heartBeatThread;


    /**
     * Used to allocate source ID to client, if they request to participate.
     */
    private SourceIdAllocator sourceIdAllocator;

    /**
     * Latest snapshot of the race, to send to client. Currently only used for XML messages.
     */
    private LatestMessages latestMessages;


    /**
     * Collection of commands from client for race to execute.
     */
    private CompositeCommand compositeCommand;

    /**
     * The race the client is connected to.
     */
    private RaceLogic raceLogic;

    /**
     * Used to send the race snapshot to client.
     */
    private MockOutput mockOutput;

    /**
     * The thread the {@link MockOutput} runs on.
     */
    private Thread mockOutputThread;

    /**
     * Used to receive client input, and turn it into commands.
     */
    private ControllerServer controllerServer;

    /**
     * The thread the {@link ControllerServer} runs on.
     */
    private Thread controllerServerThread;


    /**
     * Used to write messages to socket.
     */
    private MessageSerialiser messageSerialiser;

    /**
     * Stores messages to write to socket.
     */
    private BlockingQueue<AC35Data> outputQueue;

    /**
     * Used to read messages from socket.
     */
    private MessageDeserialiser messageDeserialiser;

    /**
     * Stores messages read from socket.
     */
    private BlockingQueue<AC35Data> inputQueue;

    /**
     * The state of the connection to the client.
     */
    private ConnectionStateEnum connectionState = ConnectionStateEnum.UNKNOWN;

    /**
     * The source ID that has been allocated to the client.
     * 0 means not allocated.
     */
    private int allocatedSourceID = 0;





    /**
     * Creates a client connection, using a given socket.
     * @param socket The socket which connects to the client.
     * @param sourceIdAllocator Used to allocate a source ID for the client.
     * @param latestMessages Latest race snapshot to send to client.
     * @param compositeCommand Collection of commands for race to execute.
     * @param raceLogic The race the client is connected to.
     * @throws IOException Thrown if there is a problem with the client socket.
     */
    public ClientConnection(Socket socket, SourceIdAllocator sourceIdAllocator, LatestMessages latestMessages, CompositeCommand compositeCommand, RaceLogic raceLogic) throws IOException {
        this.socket = socket;
        this.sourceIdAllocator = sourceIdAllocator;
        this.latestMessages = latestMessages;
        this.compositeCommand = compositeCommand;
        this.raceLogic = raceLogic;

        this.outputQueue = new LinkedBlockingQueue<>();
        this.inputQueue = new LinkedBlockingQueue<>();


        this.messageSerialiser = new MessageSerialiser(socket.getOutputStream(), outputQueue);
        this.messageDeserialiser = new MessageDeserialiser(socket.getInputStream(), inputQueue);

        new Thread(messageSerialiser, "ClientConnection()->MessageSerialiser thread " + messageSerialiser).start();
        new Thread(messageDeserialiser, "ClientConnection()->MessageDeserialiser thread " + messageDeserialiser).start();


        this.heartBeatService = new HeartBeatService(outputQueue);
        this.heartBeatThread = new Thread(heartBeatService, "ClientConnection()->HeartBeatService thread " + heartBeatService);
        this.heartBeatThread.start();

    }



    @Override
    public void run() {
        try {
            handshake();

        } catch (HandshakeException | SourceIDAllocationException e) {
            Logger.getGlobal().log(Level.WARNING, "Client handshake failed.", e);
            Thread.currentThread().interrupt();
            return;
        }

    }


    /**
     * Initiates the handshake with the client.
     * @throws HandshakeException Thrown if something goes wrong with the handshake.
     * @throws SourceIDAllocationException Thrown if we cannot allocate a sourceID.
     */
    private void handshake() throws SourceIDAllocationException, HandshakeException {

        //This function is a bit messy, and could probably be refactored a bit.

        connectionState = ConnectionStateEnum.WAITING_FOR_HANDSHAKE;



        RequestToJoin requestToJoin = waitForRequestToJoin();

        allocatedSourceID = 0;

        //If they want to participate, give them a source ID number.
        if (requestToJoin.getRequestType() == RequestToJoinEnum.PARTICIPANT) {

            allocatedSourceID = sourceIdAllocator.allocateSourceID();

            this.controllerServer = new ControllerServer(compositeCommand, inputQueue, allocatedSourceID, raceLogic.getRace());
            this.controllerServerThread = new Thread(controllerServer, "ClientConnection.run()->ControllerServer thread" + controllerServer);
            this.controllerServerThread.start();

        }


        sendJoinAcceptanceMessage(allocatedSourceID);

        this.mockOutput = new MockOutput(latestMessages, outputQueue);
        this.mockOutputThread = new Thread(mockOutput, "ClientConnection.run()->MockOutput thread" + mockOutput);
        this.mockOutputThread.start();


        connectionState = ConnectionStateEnum.CONNECTED;

    }


    /**
     * Waits until the client sends a {@link RequestToJoin} message, and returns it.
     * @return The {@link RequestToJoin} message.
     * @throws HandshakeException Thrown if we get interrupted while waiting.
     */
    private RequestToJoin waitForRequestToJoin() throws HandshakeException {

        try {


            while (connectionState == ConnectionStateEnum.WAITING_FOR_HANDSHAKE) {

                AC35Data message = inputQueue.take();

                //We need to wait until they actually send a join request.
                if (message.getType() == MessageType.REQUEST_TO_JOIN) {
                    return (RequestToJoin) message;
                }

            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HandshakeException("Handshake failed. Thread: " + Thread.currentThread() + " was interrupted while waiting on the incoming message queue.", e);

        }


        throw new HandshakeException("Handshake was cancelled. Connection state is now: " + connectionState);

    }


    /**
     * Sends the client a {@link JoinAcceptance} message, containing their assigned sourceID.
     * @param sourceID The sourceID to assign to client.
     * @throws HandshakeException Thrown if the thread is interrupted while placing message on the outgoing message queue.
     */
    private void sendJoinAcceptanceMessage(int sourceID) throws HandshakeException {

        //Send them the source ID.
        JoinAcceptance joinAcceptance = new JoinAcceptance(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, sourceID);

        try {
            outputQueue.put(joinAcceptance);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HandshakeException("Handshake failed. Thread: " + Thread.currentThread() + " interrupted while placing JoinAcceptance message on outgoing message queue.", e);
        }

    }


    /**
     * Determines whether or not this connection is still alive.
     * This is based off whether the {@link MessageSerialiser} is still alive.
     * @return True if it is alive, false otherwise.
     */
    public boolean isAlive() {
        return messageSerialiser.isRunning();
    }


    /**
     * Terminates this connection.
     */
    public void terminate() {

        if (this.heartBeatThread != null) {
            this.heartBeatThread.interrupt();
        }

        if (this.mockOutputThread != null) {
            this.mockOutputThread.interrupt();
        }

        if (this.controllerServerThread != null) {
            this.controllerServerThread.interrupt();
        }

        if (allocatedSourceID != 0) {
            sourceIdAllocator.returnSourceID(allocatedSourceID);
        }
    }

}
