package visualiser.network;


import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import shared.model.RunnableWithFramePeriod;
import visualiser.Commands.ConnectionToServerCommands.ConnectionToServerCommandFactory;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The controller for connection related messages, coming from the server to the client.
 */
public class ConnectionToServerController implements RunnableWithFramePeriod {


    /**
     * The incoming queue of messages to act on.
     */
    private BlockingQueue<AC35Data> incomingMessages;


    /**
     * The connection we are acting on.
     */
    private ConnectionToServer connectionToServer;


    /**
     * The queue to place commands on.
     */
    private BlockingQueue<Command> outgoingCommands;



    /**
     * Constructs a {@link ConnectionToServer} controller with the given parameters.
     * This accepts connection related messages, converts them to commands, and passes them to an outgoing command queue.
     * @param incomingMessages The message queue to read from.
     * @param connectionToServer The ConnectionToServer (context) to act on.
     * @param outgoingCommands The queue to place outgoing commands on.
     */
    public ConnectionToServerController(BlockingQueue<AC35Data> incomingMessages, ConnectionToServer connectionToServer, BlockingQueue<Command> outgoingCommands) {
        this.incomingMessages = incomingMessages;
        this.connectionToServer = connectionToServer;
        this.outgoingCommands = outgoingCommands;
    }


    @Override
    public void run() {

        while (!Thread.interrupted()) {

            try {
                AC35Data message = incomingMessages.take();
                Command command = ConnectionToServerCommandFactory.create(message, connectionToServer);
                outgoingCommands.put(command);

            } catch (CommandConstructionException e) {
                Logger.getGlobal().log(Level.WARNING, "ConnectionToServerController: " + this + " could not create command from message.", e);

            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, "ConnectionToServerController: " + this + " was interrupted on thread: " + Thread.currentThread(), e);
                Thread.currentThread().interrupt();

            }


        }

    }

}
