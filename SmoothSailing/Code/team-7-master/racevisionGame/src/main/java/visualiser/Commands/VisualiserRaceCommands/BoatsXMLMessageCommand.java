package visualiser.Commands.VisualiserRaceCommands;

import mock.model.commandFactory.Command;
import network.Messages.XMLMessage;
import shared.dataInput.BoatDataSource;
import shared.dataInput.BoatXMLReader;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.XMLReaderException;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;


/**
 * Command created when a {@link network.Messages.Enums.XMLMessageType#BOAT} {@link XMLMessage} message is received.
 */
public class BoatsXMLMessageCommand implements Command {

    /**
     * The data source to operate on.
     */
    private BoatDataSource boatDataSource;

    /**
     * The context to operate on.
     */
    private VisualiserRaceState visualiserRace;


    /**
     * Creates a new {@link BoatsXMLMessageCommand}, which operates on a given {@link VisualiserRaceEvent}.
     * @param boatDataSource The data source to operate on.
     * @param visualiserRace The context to operate on.
     */
    public BoatsXMLMessageCommand(BoatDataSource boatDataSource, VisualiserRaceState visualiserRace) {
        this.boatDataSource = boatDataSource;
        this.visualiserRace = visualiserRace;
    }



    @Override
    public void execute() {
        if (boatDataSource.getSequenceNumber() > visualiserRace.getBoatDataSource().getSequenceNumber()) {
            visualiserRace.setBoatDataSource(boatDataSource);
        }
    }
}
