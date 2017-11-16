package visualiser.network;

import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import network.Messages.Enums.JoinAcceptanceEnum;
import network.Messages.Enums.RequestToJoinEnum;
import network.Messages.JoinAcceptance;
import org.junit.Before;
import org.junit.Test;
import visualiser.Commands.ConnectionToServerCommands.JoinSuccessParticipantCommand;
import visualiser.Commands.ConnectionToServerCommands.JoinFailureCommand;
import visualiser.Commands.ConnectionToServerCommands.ServerFullCommand;
import visualiser.enums.ConnectionToServerState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Tests the {@link ConnectionToServer} class with a Participant request, and how it reacts to various commands.
 */
public class ConnectionToServerParticipantTest {

    private ConnectionToServer connectionToServer;
    private Thread connectionToServerThread;

    private BlockingQueue<AC35Data> outgoingMessages;
    private BlockingQueue<Command> incomingCommands;


    @Before
    public void setUp() throws Exception {

        incomingCommands = new LinkedBlockingQueue<>();
        outgoingMessages = new LinkedBlockingQueue<>();

        connectionToServer = new ConnectionToServer(ConnectionToServerState.UNKNOWN, RequestToJoinEnum.PARTICIPANT, incomingCommands, outgoingMessages);
        connectionToServerThread = new Thread(connectionToServer);
        connectionToServerThread.start();

    }


    /**
     * When a connection to server is created, is it expected that it will have sent a request and be in the Request_sent state.
     * @throws Exception On error.
     */
    @Test
    public void expectRequestSent() throws Exception {

        //Need to wait for connection thread to execute commands.
        Thread.sleep(250);

        assertEquals(ConnectionToServerState.REQUEST_SENT, connectionToServer.getConnectionState());
    }


    /**
     * When the connection to server thread is interrupted, it is expected the connection state will become TimedOut.
     * @throws Exception On error.
     */
    @Test
    public void interruptTimedOut() throws Exception {

        //Need to wait for connection thread to execute commands.
        Thread.sleep(250);


        //Disable logging as we know this will log but we don't care.
        Logger.getGlobal().setLevel(Level.OFF);
        connectionToServerThread.interrupt();
        Logger.getGlobal().setLevel(null);

        connectionToServerThread.join();

        assertEquals(ConnectionToServerState.TIMED_OUT, connectionToServer.getConnectionState());
    }


    /**
     * Sends a join successful command. Expects that the connection becomes Connected.
     * @throws Exception On error.
     */
    @Test
    public void sendJoinSuccessCommand() throws Exception {
        int sourceID = 123;
        JoinAcceptance joinAcceptance = new JoinAcceptance(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, sourceID);

        Command command = new JoinSuccessParticipantCommand(joinAcceptance, connectionToServer);

        incomingCommands.put(command);

        //Need to wait for connection thread to execute commands.
        Thread.sleep(500);

        assertEquals(ConnectionToServerState.CONNECTED, connectionToServer.getConnectionState());
        assertTrue(connectionToServer.getJoinAcceptance() != null);
        assertEquals(sourceID, connectionToServer.getJoinAcceptance().getSourceID());
        assertNotEquals(0, connectionToServer.getJoinAcceptance().getSourceID());
        assertEquals(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, connectionToServer.getJoinAcceptance().getAcceptanceType());


    }


    /**
     * Sends a participants full command. Expects that the connection becomes Declined.
     * @throws Exception On error.
     */
    @Test
    public void sendJoinFailureCommand() throws Exception {
        int sourceID = 0;
        JoinAcceptance joinAcceptance = new JoinAcceptance(JoinAcceptanceEnum.JOIN_FAILURE, sourceID);

        Command command = new JoinFailureCommand(joinAcceptance, connectionToServer);

        incomingCommands.put(command);

        //Need to wait for connection thread to execute commands.
        Thread.sleep(250);

        assertEquals(ConnectionToServerState.DECLINED, connectionToServer.getConnectionState());
        assertTrue(connectionToServer.getJoinAcceptance() != null);
        assertEquals(sourceID, connectionToServer.getJoinAcceptance().getSourceID());
        assertEquals(JoinAcceptanceEnum.JOIN_FAILURE, connectionToServer.getJoinAcceptance().getAcceptanceType());
    }


    /**
     * Sends a server full command. Expects that the connection becomes Declined.
     * @throws Exception On error.
     */
    @Test
    public void sendServerFullCommand() throws Exception {
        int sourceID = 0;
        JoinAcceptance joinAcceptance = new JoinAcceptance(JoinAcceptanceEnum.SERVER_FULL, sourceID);

        Command command = new ServerFullCommand(joinAcceptance, connectionToServer);

        incomingCommands.put(command);

        //Need to wait for connection thread to execute commands.
        Thread.sleep(250);

        assertEquals(ConnectionToServerState.DECLINED, connectionToServer.getConnectionState());
        assertTrue(connectionToServer.getJoinAcceptance() != null);
        assertEquals(sourceID, connectionToServer.getJoinAcceptance().getSourceID());
        assertEquals(JoinAcceptanceEnum.SERVER_FULL, connectionToServer.getJoinAcceptance().getAcceptanceType());
    }


}


