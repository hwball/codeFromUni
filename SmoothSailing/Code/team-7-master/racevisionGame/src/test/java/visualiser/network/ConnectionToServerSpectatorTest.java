package visualiser.network;

import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import network.Messages.Enums.JoinAcceptanceEnum;
import network.Messages.Enums.RequestToJoinEnum;
import network.Messages.JoinAcceptance;
import org.junit.Before;
import org.junit.Test;
import visualiser.Commands.ConnectionToServerCommands.JoinSuccessParticipantCommand;
import visualiser.Commands.ConnectionToServerCommands.ServerFullCommand;
import visualiser.enums.ConnectionToServerState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

/**
 * Tests the {@link ConnectionToServer} class with a Spectator request, and how it reacts to various commands.
 */
public class ConnectionToServerSpectatorTest {

    private ConnectionToServer connectionToServer;
    private Thread connectionToServerThread;

    private BlockingQueue<AC35Data> outgoingMessages;
    private BlockingQueue<Command> incomingCommands;


    @Before
    public void setUp() throws Exception {

        incomingCommands = new LinkedBlockingQueue<>();
        outgoingMessages = new LinkedBlockingQueue<>();

        connectionToServer = new ConnectionToServer(ConnectionToServerState.UNKNOWN, RequestToJoinEnum.SPECTATOR, incomingCommands, outgoingMessages);
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
     * Sends a join successful command. Expects that the connection becomes Connected.
     * @throws Exception On error.
     */
    @Test
    public void sendJoinSuccessCommand() throws Exception {
        int sourceID = 0;
        JoinAcceptance joinAcceptance = new JoinAcceptance(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, sourceID);

        Command command = new JoinSuccessParticipantCommand(joinAcceptance, connectionToServer);

        incomingCommands.put(command);

        //Need to wait for connection thread to execute commands.
        Thread.sleep(250);

        assertEquals(ConnectionToServerState.CONNECTED, connectionToServer.getConnectionState());
        assertTrue(connectionToServer.getJoinAcceptance() != null);
        assertEquals(sourceID, connectionToServer.getJoinAcceptance().getSourceID());
        assertEquals(JoinAcceptanceEnum.JOIN_SUCCESSFUL_PARTICIPANT, connectionToServer.getJoinAcceptance().getAcceptanceType());


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


