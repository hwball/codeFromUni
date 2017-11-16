package visualiser.Commands.IncomingHeartBeatCommands;

import mock.model.commandFactory.Command;
import network.Messages.HeartBeat;
import network.Messages.JoinAcceptance;
import visualiser.enums.ConnectionToServerState;
import visualiser.network.ConnectionToServer;
import visualiser.network.IncomingHeartBeatService;


/**
 * Command created when a {@link HeartBeat} message is received.
 */
public class IncomingHeartBeatCommand implements Command {

    /**
     * The message to operate on.
     */
    private HeartBeat heartBeat;

    /**
     * The context to operate on.
     */
    private IncomingHeartBeatService incomingHeartBeatService;


    /**
     * Creates a new {@link IncomingHeartBeatCommand}, which operates on a given {@link IncomingHeartBeatService}.
     * @param heartBeat The message to operate on.
     * @param incomingHeartBeatService The context to operate on.
     */
    public IncomingHeartBeatCommand(HeartBeat heartBeat, IncomingHeartBeatService incomingHeartBeatService) {
        this.heartBeat = heartBeat;
        this.incomingHeartBeatService = incomingHeartBeatService;
    }



    @Override
    public void execute() {

        incomingHeartBeatService.setLastHeartBeatSeqNum(heartBeat.getSequenceNumber());

        incomingHeartBeatService.setLastHeartbeatTime(System.currentTimeMillis());

    }
}
