package visualiser.model;

import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import mock.model.commandFactory.CompositeCommand;
import network.Messages.*;
import shared.model.RunnableWithFramePeriod;
import visualiser.Commands.VisualiserRaceCommands.VisualiserRaceCommandFactory;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The controller for race related messages, coming from the server to the client.
 */
public class VisualiserRaceController implements RunnableWithFramePeriod {


    /**
     * Incoming messages from server.
     */
    private BlockingQueue<AC35Data> incomingMessages;


    /**
     * Commands are placed in here, and executed by visualiserRace.
     */
    private CompositeCommand compositeRaceCommand;


    /**
     * The context that created commands operate on.
     */
    private VisualiserRaceState visualiserRace;




    /**
     * Constructs a visualiserInput to convert an incoming stream of messages into commands.
     * @param incomingMessages The incoming queue of messages.
     * @param visualiserRace The context to for commands to operate on.
     * @param compositeRaceCommand The composite command to place command in.
     */
    public VisualiserRaceController(BlockingQueue<AC35Data> incomingMessages, VisualiserRaceState visualiserRace, CompositeCommand compositeRaceCommand) {
        this.incomingMessages = incomingMessages;
        this.compositeRaceCommand = compositeRaceCommand;
        this.visualiserRace = visualiserRace;
    }




    @Override
    public void run() {

        while (!Thread.interrupted()) {

            try {
                AC35Data message = incomingMessages.take();

                Command command = VisualiserRaceCommandFactory.create(message, visualiserRace);
                compositeRaceCommand.addCommand(command);

            } catch (CommandConstructionException e) {
                //Logger.getGlobal().log(Level.WARNING, "VisualiserRaceController could not create a command for incoming message.");

            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, "VisualiserRaceController was interrupted on thread: " + Thread.currentThread() + " while waiting for messages.");
                Thread.currentThread().interrupt();
                return;
            }

        }

    }
}
