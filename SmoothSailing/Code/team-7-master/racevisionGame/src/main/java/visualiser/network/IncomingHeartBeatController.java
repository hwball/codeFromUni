package visualiser.network;

import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import shared.model.RunnableWithFramePeriod;
import visualiser.Commands.IncomingHeartBeatCommands.IncomingHeartBeatCommandFactory;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The controller for heartbeat related messages, coming from the server to the client.
 */
public class IncomingHeartBeatController implements RunnableWithFramePeriod {


    /**
     * The incoming queue of messages to act on.
     */
    private BlockingQueue<AC35Data> incomingMessages;


    /**
     * The heart beat service we are acting on.
     */
    private IncomingHeartBeatService incomingHeartBeatService;


    /**
     * The queue to place commands on.
     */
    private BlockingQueue<Command> outgoingCommands;



    /**
     * Constructs a {@link IncomingHeartBeatService} controller with the given parameters.
     * This accepts connection related messages, converts them to commands, and passes them to an outgoing command queue.
     * @param incomingMessages The message queue to read from.
     * @param incomingHeartBeatService The IncomingHeartBeatService (context) to act on.
     * @param outgoingCommands The queue to place outgoing commands on.
     */
    public IncomingHeartBeatController(BlockingQueue<AC35Data> incomingMessages, IncomingHeartBeatService incomingHeartBeatService, BlockingQueue<Command> outgoingCommands) {
        this.incomingMessages = incomingMessages;
        this.incomingHeartBeatService = incomingHeartBeatService;
        this.outgoingCommands = outgoingCommands;
    }



    @Override
    public void run() {

        while (!Thread.interrupted()) {

            try {
                AC35Data message = incomingMessages.take();
                Command command = IncomingHeartBeatCommandFactory.create(message, incomingHeartBeatService);
                outgoingCommands.put(command);

            } catch (CommandConstructionException e) {
                Logger.getGlobal().log(Level.WARNING, "IncomingHeartBeatController: " + this + " could not create command from message.", e);

            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, "IncomingHeartBeatController: " + this + " was interrupted on thread: " + Thread.currentThread(), e);
                Thread.currentThread().interrupt();

            }


        }

    }

}
