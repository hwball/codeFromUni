package visualiser.Commands.ConnectionToServerCommands;


import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import network.Messages.JoinAcceptance;
import visualiser.network.ConnectionToServer;

/**
 * Factory to create ConnectionToServer commands.
 */
public class ConnectionToServerCommandFactory {

    /**
     * Generates a command to execute on server connection based on the type of {@link network.Messages.Enums.JoinAcceptanceEnum}.
     * @param message The message to turn into a command.
     * @param connectionToServer The connection for the command to operate on.
     * @return The command to execute the given action.
     * @throws CommandConstructionException Thrown if the command cannot be constructed.
     */
    public static Command create(AC35Data message, ConnectionToServer connectionToServer) throws CommandConstructionException {

        if (!(message instanceof JoinAcceptance)) {
            throw new CommandConstructionException("Message: " + message + " is not a JoinAcceptance message.");
        }

        JoinAcceptance joinAcceptance = (JoinAcceptance) message;


        switch(joinAcceptance.getAcceptanceType()) {

            case JOIN_SUCCESSFUL_PARTICIPANT: return new JoinSuccessParticipantCommand(joinAcceptance, connectionToServer);

            case JOIN_SUCCESSFUL_SPECTATOR: return new JoinSuccessSpectatorCommand(joinAcceptance, connectionToServer);

            case JOIN_FAILURE: return new JoinFailureCommand(joinAcceptance, connectionToServer);

            case SERVER_FULL: return new ServerFullCommand(joinAcceptance, connectionToServer);

            default: throw new CommandConstructionException("Could not create command for JoinAcceptance: " + joinAcceptance + ". Unknown JoinAcceptanceEnum.");
        }
    }

}
