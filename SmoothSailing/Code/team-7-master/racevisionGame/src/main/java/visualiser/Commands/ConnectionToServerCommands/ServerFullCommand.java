package visualiser.Commands.ConnectionToServerCommands;

import mock.model.commandFactory.Command;
import network.Messages.JoinAcceptance;
import visualiser.enums.ConnectionToServerState;
import visualiser.network.ConnectionToServer;

import java.util.Optional;


/**
 * Command created when a {@link network.Messages.Enums.JoinAcceptanceEnum#SERVER_FULL} {@link JoinAcceptance} message is received.
 */
public class ServerFullCommand implements Command {

    /**
     * The message to operate on.
     */
    private JoinAcceptance joinAcceptance;

    /**
     * The context to operate on.
     */
    private ConnectionToServer connectionToServer;


    /**
     * Creates a new {@link ServerFullCommand}, which operates on a given {@link ConnectionToServer}.
     * @param joinAcceptance The message to operate on.
     * @param connectionToServer The context to operate on.
     */
    public ServerFullCommand(JoinAcceptance joinAcceptance, ConnectionToServer connectionToServer) {
        this.joinAcceptance = joinAcceptance;
        this.connectionToServer = connectionToServer;
    }



    @Override
    public void execute() {

        connectionToServer.setJoinAcceptance(joinAcceptance);

        connectionToServer.setConnectionState(ConnectionToServerState.DECLINED);

    }
}
