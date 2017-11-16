package visualiser.gameController;

import mock.exceptions.CommandConstructionException;
import mock.model.MockRace;
import mock.model.commandFactory.Command;
import mock.model.commandFactory.CommandFactory;
import mock.model.commandFactory.CompositeCommand;
import network.Messages.AC35Data;
import network.Messages.BoatAction;
import network.Messages.Enums.MessageType;
import shared.model.RunnableWithFramePeriod;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for dispatching key press data to race from client
 */
public class ControllerServer implements RunnableWithFramePeriod {


    /**
     * Queue of incoming messages from client.
     */
    private BlockingQueue<AC35Data> inputQueue;


    /**
     * Collection of commands from client for race to execute.
     */
    private CompositeCommand compositeCommand;
    
    /**
     * The context for each command.
     */
    private MockRace raceState;

    /**
     * This is the source ID associated with the client.
     */
    private int clientSourceID;



    /**
     * Initialise server-side controller with live client socket.
     * @param compositeCommand Commands for the race to execute.
     * @param inputQueue The queue of messages to read from.
     * @param clientSourceID The source ID of the client's boat.
     * @param raceState The context for each command.
     */
    public ControllerServer(CompositeCommand compositeCommand, BlockingQueue<AC35Data> inputQueue, int clientSourceID, MockRace raceState) {
        this.compositeCommand = compositeCommand;
        this.inputQueue = inputQueue;
        this.clientSourceID = clientSourceID;
        this.raceState = raceState;
    }




    /**
     * Wait for controller key input from client and loop.
     */
    @Override
    public void run() {
        while(!Thread.interrupted()) {

            try {

                AC35Data message = inputQueue.take();

                if (message.getType() == MessageType.BOATACTION) {

                    BoatAction boatAction = (BoatAction) message;


                    boatAction.setSourceID(clientSourceID);

                    try {
                        Command command = CommandFactory.createCommand(raceState, boatAction);
                        if(command != null) {
                            compositeCommand.addCommand(command);
                        }
                    } catch (CommandConstructionException e) {
                        Logger.getGlobal().log(Level.WARNING, "ControllerServer could not create a Command for BoatAction: " + boatAction + ".", e);

                    }
                }


            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.WARNING, "ControllerServer Interrupted while waiting for message on incoming message queue.", e);
                Thread.currentThread().interrupt();
                return;

            }

        }

    }
}
