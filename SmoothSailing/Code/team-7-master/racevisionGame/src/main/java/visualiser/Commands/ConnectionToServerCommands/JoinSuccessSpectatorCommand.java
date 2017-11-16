package visualiser.Commands.ConnectionToServerCommands;

import mock.model.commandFactory.Command;
import network.Messages.JoinAcceptance;
import visualiser.enums.ConnectionToServerState;
import visualiser.network.ConnectionToServer;


/**
 * Command created when a {@link network.Messages.Enums.JoinAcceptanceEnum#JOIN_SUCCESSFUL_PARTICIPANT} {@link JoinAcceptance} message is received.
 */
public class JoinSuccessSpectatorCommand implements Command {

    /**
     * The message to operate on.
     */
    private JoinAcceptance joinAcceptance;

    /**
     * The context to operate on.
     */
    private ConnectionToServer connectionToServer;


    /**
     * Creates a new {@link JoinSuccessSpectatorCommand}, which operates on a given {@link ConnectionToServer}.
     * @param joinAcceptance The message to operate on.
     * @param connectionToServer The context to operate on.
     */
    public JoinSuccessSpectatorCommand(JoinAcceptance joinAcceptance, ConnectionToServer connectionToServer) {
        this.joinAcceptance = joinAcceptance;
        this.connectionToServer = connectionToServer;
    }



    @Override
    public void execute() {

        connectionToServer.setJoinAcceptance(joinAcceptance);

        connectionToServer.setConnectionState(ConnectionToServerState.CONNECTED);

    }
}
