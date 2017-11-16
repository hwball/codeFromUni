package visualiser.Commands.ConnectionToServerCommands;

import mock.model.commandFactory.Command;
import network.Messages.AssignPlayerBoat;
import network.Messages.JoinAcceptance;
import visualiser.enums.ConnectionToServerState;
import visualiser.network.ConnectionToServer;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Command created when a {@link network.Messages.Enums.JoinAcceptanceEnum#JOIN_SUCCESSFUL_PARTICIPANT} {@link network.Messages.JoinAcceptance} message is received.
 */
public class JoinSuccessParticipantCommand implements Command {

    /**
     * The message to operate on.
     */
    private JoinAcceptance joinAcceptance;

    /**
     * The context to operate on.
     */
    private ConnectionToServer connectionToServer;


    /**
     * Creates a new {@link JoinSuccessParticipantCommand}, which operates on a given {@link ConnectionToServer}.
     * @param joinAcceptance The message to operate on.
     * @param connectionToServer The context to operate on.
     */
    public JoinSuccessParticipantCommand(JoinAcceptance joinAcceptance, ConnectionToServer connectionToServer) {
        this.joinAcceptance = joinAcceptance;
        this.connectionToServer = connectionToServer;
    }



    @Override
    public void execute() {

        connectionToServer.setJoinAcceptance(joinAcceptance);

        connectionToServer.setConnectionState(ConnectionToServerState.CONNECTED);


        AssignPlayerBoat assignPlayerBoat = new AssignPlayerBoat(joinAcceptance.getSourceID());
        try {
            connectionToServer.send(assignPlayerBoat);
        } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.WARNING, "JoinSuccessParticipantCommand: " + this + " was interrupted on thread: " + Thread.currentThread() + " while sending AssignPlayerBoat message.", e);
        }

    }
}
