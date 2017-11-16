package mock.app;

import mock.model.RaceLogic;
import mock.model.ClientConnection;
import mock.model.SourceIdAllocator;
import mock.model.commandFactory.CompositeCommand;
import network.AckSequencer;
import network.Messages.Enums.XMLMessageType;
import network.Messages.LatestMessages;
import network.Messages.XMLMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection acceptor for multiple clients
 */
public class ConnectionAcceptor implements Runnable {


    /**
     * Port to expose server on.
     */
    private int serverPort = 4942;
    /**
     * Socket used to listen for clients on.
     */
    private ServerSocket serverSocket;

    private Socket mockSocket = null;


    /**
     * List of client connections.
     */
    private BlockingQueue<ClientConnection> clientConnections = new ArrayBlockingQueue<>(16, true);

    /**
     * Snapshot of the race.
     */
    private LatestMessages latestMessages;

    /**
     * Collection of commands from clients for race to execute.
     */
    private CompositeCommand compositeCommand;

    /**
     * Used to allocate source IDs to clients.
     */
    private SourceIdAllocator sourceIdAllocator;


    //
    private RaceLogic raceLogic = null;

    /**
     * Connection Acceptor Constructor
     * @param latestMessages Latest messages to be sent
     * @param compositeCommand Collection of commands for race to execute.
     * @param sourceIdAllocator Object used to allocate source IDs for clients.
     * @param raceLogic The race the client will connect to.
     * @throws IOException if a server socket cannot be instantiated.
     */
    public ConnectionAcceptor(LatestMessages latestMessages, CompositeCommand compositeCommand, SourceIdAllocator sourceIdAllocator, RaceLogic raceLogic) throws IOException {

        this.latestMessages = latestMessages;
        this.compositeCommand = compositeCommand;
        this.sourceIdAllocator = sourceIdAllocator;
        this.raceLogic = raceLogic;

        this.serverSocket = new ServerSocket(serverPort);
        CheckClientConnection checkClientConnection = new CheckClientConnection(clientConnections);
        new Thread(checkClientConnection, "ConnectionAcceptor()->CheckClientConnection thread").start();

    }

    public String getAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public int getServerPort() {
        return serverPort;
    }

    public void closeConnection() throws IOException {
        this.raceLogic.boolFalse();
        if(!this.serverSocket.isClosed()){
            this.serverSocket.close();
        }
    }



    /**
     * Run the Acceptor
     */
    @Override
    public void run(){

        while(clientConnections.remainingCapacity() > 0) {

            try {

                if(Thread.currentThread().isInterrupted()){
                    break;
                }

                try {
                    this.mockSocket = serverSocket.accept();
                } catch (Exception e){}

                Logger.getGlobal().log(Level.INFO, String.format("Client connected. client ip/port = %s. Local ip/port = %s.", mockSocket.getRemoteSocketAddress(), mockSocket.getLocalSocketAddress()));


                ClientConnection clientConnection = new ClientConnection(mockSocket, sourceIdAllocator, latestMessages, compositeCommand, raceLogic);

                clientConnections.add(clientConnection);

                new Thread(clientConnection, "ConnectionAcceptor.run()->ClientConnection thread " + clientConnection).start();



                Logger.getGlobal().log(Level.INFO, String.format("%d number of Visualisers Connected.", clientConnections.size()));

            } catch (IOException e) {
                Logger.getGlobal().log(Level.WARNING, "Got an IOException while a client was attempting to connect.", e);

            }

        }
    }

    /**
     * Nested class to remove disconnected clients
     */
    class CheckClientConnection implements Runnable{

        private BlockingQueue<ClientConnection> connections;

        /**
         * Constructor
         * @param connections Clients "connected"
         */
        public CheckClientConnection(BlockingQueue<ClientConnection> connections){
            this.connections = connections;
        }

        /**
         * Run the remover.
         */
        @Override
        public void run() {

            //We track the number of times each connection fails the !isAlive() test.
            //This is to give a bit of lee-way in case the connection checker checks a connection before its thread has actually started.
            Map<ClientConnection, Integer> connectionDeadCount = new HashMap<>();

            while(!Thread.interrupted()) {

                //Make copy of connections.
                List<ClientConnection> clientConnections = new ArrayList<>(connections);


                for (ClientConnection client : clientConnections) {

                    connectionDeadCount.put(client, connectionDeadCount.getOrDefault(client, 0));

                    if (!client.isAlive()) {
                        //Add one to fail count.
                        connectionDeadCount.put(client, connectionDeadCount.get(client) + 1);
                    }

                    //We only remove them if they fail 5 times.
                    if (connectionDeadCount.get(client) > 5) {
                        connections.remove(client);
                        connectionDeadCount.remove(client);
                        client.terminate();

                        Logger.getGlobal().log(Level.WARNING, "CheckClientConnection is removing the dead connection: " + client);
                    }
                }

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    Logger.getGlobal().log(Level.WARNING, "CheckClientConnection was interrupted while sleeping.", e);
                    Thread.currentThread().interrupt();
                    return;

                }
            }
        }
    }


}
