package visualiser.Commands.IncomingHeartBeatCommands;


import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import network.Messages.HeartBeat;
import visualiser.network.IncomingHeartBeatService;

/**
 * Factory to create IncomingHeartBeatService commands.
 */
public class IncomingHeartBeatCommandFactory {

    /**
     * Generates a command on an IncomingHeartBeatService.
     * @param message The message to turn into a command.
     * @param incomingHeartBeatService The context for the command to operate on.
     * @return The command to execute the given action.
     * @throws CommandConstructionException Thrown if the command cannot be constructed.
     */
    public static Command create(AC35Data message, IncomingHeartBeatService incomingHeartBeatService) throws CommandConstructionException {

        if (!(message instanceof HeartBeat)) {
            throw new CommandConstructionException("Message: " + message + " is not a HeartBeat message.");
        }

        HeartBeat heartBeat = (HeartBeat) message;

        return new IncomingHeartBeatCommand(heartBeat, incomingHeartBeatService);
    }

}
