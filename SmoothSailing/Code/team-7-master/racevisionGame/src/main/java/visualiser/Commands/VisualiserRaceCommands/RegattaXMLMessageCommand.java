package visualiser.Commands.VisualiserRaceCommands;

import mock.model.commandFactory.Command;
import network.Messages.XMLMessage;
import shared.dataInput.RegattaDataSource;
import visualiser.model.VisualiserRaceEvent;
import visualiser.model.VisualiserRaceState;


/**
 * Command created when a {@link network.Messages.Enums.XMLMessageType#BOAT} {@link XMLMessage} message is received.
 */
public class RegattaXMLMessageCommand implements Command {

    /**
     * The data source to operate on.
     */
    private RegattaDataSource regattaDataSource;

    /**
     * The context to operate on.
     */
    private VisualiserRaceState visualiserRace;


    /**
     * Creates a new {@link RegattaXMLMessageCommand}, which operates on a given {@link VisualiserRaceEvent}.
     * @param regattaDataSource The data source to operate on.
     * @param visualiserRace The context to operate on.
     */
    public RegattaXMLMessageCommand(RegattaDataSource regattaDataSource, VisualiserRaceState visualiserRace) {
        this.regattaDataSource = regattaDataSource;
        this.visualiserRace = visualiserRace;
    }



    @Override
    public void execute() {
        if (regattaDataSource.getSequenceNumber() > visualiserRace.getRegattaDataSource().getSequenceNumber()) {
            visualiserRace.setRegattaDataSource(regattaDataSource);
        }

    }
}
