package visualiser.Commands.VisualiserRaceCommands;

import mock.model.commandFactory.Command;
import network.Messages.XMLMessage;
import shared.dataInput.RaceDataSource;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;


/**
 * Command created when a {@link network.Messages.Enums.XMLMessageType#BOAT} {@link XMLMessage} message is received.
 */
public class RaceXMLMessageCommand implements Command {

    /**
     * The data source to operate on.
     */
    private RaceDataSource raceDataSource;

    /**
     * The context to operate on.
     */
    private VisualiserRaceState visualiserRace;


    /**
     * Creates a new {@link RaceXMLMessageCommand}, which operates on a given {@link VisualiserRaceEvent}.
     * @param raceDataSource The data source to operate on.
     * @param visualiserRace The context to operate on.
     */
    public RaceXMLMessageCommand(RaceDataSource raceDataSource, VisualiserRaceState visualiserRace) {
        this.raceDataSource = raceDataSource;
        this.visualiserRace = visualiserRace;
    }



    @Override
    public void execute() {

        if (raceDataSource.getSequenceNumber() > visualiserRace.getRaceDataSource().getSequenceNumber()) {
            visualiserRace.setRaceDataSource(raceDataSource);
        }

    }
}
