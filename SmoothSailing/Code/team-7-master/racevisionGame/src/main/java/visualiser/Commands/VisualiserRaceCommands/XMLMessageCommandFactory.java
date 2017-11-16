package visualiser.Commands.VisualiserRaceCommands;


import mock.exceptions.CommandConstructionException;
import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import network.Messages.BoatLocation;
import network.Messages.RaceStatus;
import network.Messages.XMLMessage;
import shared.dataInput.*;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.InvalidRegattaDataException;
import shared.exceptions.XMLReaderException;
import visualiser.model.VisualiserRaceState;

/**
 * Factory to create VisualiserRace commands, from XMLMessages.
 */
public class XMLMessageCommandFactory {

    /**
     * Generates a command on an VisualiserRace.
     * @param message The message to turn into a command.
     * @param visualiserRace The context for the command to operate on.
     * @return The command to execute the given action.
     * @throws CommandConstructionException Thrown if the command cannot be constructed.
     */
    public static Command create(XMLMessage message, VisualiserRaceState visualiserRace) throws CommandConstructionException {

        try {

            switch (message.getXmlMsgSubType()) {

                case BOAT:
                    BoatXMLReader boatDataSource = new BoatXMLReader(message.getXmlMessage(), XMLFileType.Contents);
                    boatDataSource.setSequenceNumber(message.getSequenceNumber());
                    return new BoatsXMLMessageCommand(boatDataSource, visualiserRace);


                case RACE:
                    RaceXMLReader raceDataSource = new RaceXMLReader(message.getXmlMessage(), XMLFileType.Contents);
                    raceDataSource.setSequenceNumber(message.getSequenceNumber());
                    return new RaceXMLMessageCommand(raceDataSource, visualiserRace);


                case REGATTA:
                    RegattaXMLReader regattaDataSource = new RegattaXMLReader(message.getXmlMessage(), XMLFileType.Contents);
                    regattaDataSource.setSequenceNumber(message.getSequenceNumber());
                    return new RegattaXMLMessageCommand(regattaDataSource, visualiserRace);


                default:
                    throw new CommandConstructionException("Could not create VisualiserRaceCommand/XMLCommand. Unrecognised or unsupported MessageType: " + message.getType());

            }

        } catch (XMLReaderException | InvalidBoatDataException | InvalidRegattaDataException | InvalidRaceDataException e) {
            throw new CommandConstructionException("Could not create VisualiserRaceCommand/XMLCommand. Could not parse XML message payload.", e);

        }

    }

}
