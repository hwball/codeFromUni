package mock.model;

import network.AckSequencer;
import network.Messages.*;
import network.Messages.Enums.BoatLocationDeviceEnum;
import network.Messages.Enums.YachtEventEnum;
import network.Messages.Enums.XMLMessageType;
import shared.model.Bearing;
import shared.model.CompoundMark;
import shared.model.Mark;
import shared.xml.Race.RaceDataSourceToXML;
import shared.xml.boats.BoatDataSourceToXML;
import shared.xml.regatta.RegattaDataSourceToXML;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by connortaylorbrown on 2/08/17.
 */
public class RaceServer {
    private MockRace race;
    private LatestMessages latestMessages;
    private List<YachtEvent> collisionEvents = new ArrayList<>();


    /**
     * The sequence number of the latest BoatLocation message sent or received.
     */
    private int boatLocationSequenceNumber = 1;

    /**
     * The sequence number of race XML messages.
     */
    private int raceXMLSeqNumber = -1;

    /**
     * The sequence number of boat XML messages.
     */
    private int boatXMLSeqNumber = -1;

    /**
     * The sequence number of regatta XML messages.
     */
    private int regattaXMLSeqNumber = -1;


    public RaceServer(MockRace race, LatestMessages latestMessages) {
        this.race = race;
        this.latestMessages = latestMessages;
    }


    /**
     * Parses the race to create a snapshot, and places it in latestMessages.
     */
    public void parseSnapshot() {

        List<AC35Data> snapshotMessages = new ArrayList<>();

        //Parse the boat locations.
        snapshotMessages.addAll(parseBoatLocations());

        //Parse the boat states
        snapshotMessages.addAll(parseBoatStates());

        //Parse the marks.
        snapshotMessages.addAll(parseMarks());

        //Parse the race status.
        snapshotMessages.add(parseRaceStatus());

        //Parse collisions
        if(collisionEvents.size()>0){
            snapshotMessages.addAll(collisionEvents);
        }

        latestMessages.setSnapshot(snapshotMessages);

        updateXMLFiles();

        //Reset collision list
        collisionEvents.clear();
        //System.out.println(collisionEvents.size());

    }

    /**
     * Checks if the race/boat/regatta data sources have changed, and if they have, update their xml representations.
     */
    private void updateXMLFiles() {
        updateRaceXMLFile();
        updateBoatXMLFile();
        updateRegattaXMLFile();
    }

    /**
     * Checks if the race data source has changed, and if it has, updates LatestMessages' race xml message.
     */
    private void updateRaceXMLFile() {
        if (raceXMLSeqNumber != race.getRaceDataSource().getSequenceNumber()) {

            raceXMLSeqNumber = race.getRaceDataSource().getSequenceNumber();
            try {
                String raceXMLString = RaceDataSourceToXML.toString(race.getRaceDataSource());
                XMLMessage message = createXMLMessage(raceXMLString, XMLMessageType.RACE);
                latestMessages.setXMLMessage(message);

            } catch (JAXBException e) {
                Logger.getGlobal().log(Level.WARNING, "Could not serialise: " + race.getRaceDataSource(), e);
            }

        }
    }

    /**
     * Checks if the boat data source has changed, and if it has, updates LatestMessages' boat xml message.
     */
    private void updateBoatXMLFile() {
        if (boatXMLSeqNumber != race.getBoatDataSource().getSequenceNumber()) {

            boatXMLSeqNumber = race.getBoatDataSource().getSequenceNumber();
            try {
                String boatXMLString = BoatDataSourceToXML.toString(race.getBoatDataSource());
                XMLMessage message = createXMLMessage(boatXMLString, XMLMessageType.BOAT);
                latestMessages.setXMLMessage(message);

            } catch (JAXBException e) {
                Logger.getGlobal().log(Level.WARNING, "Could not serialise: " + race.getBoatDataSource(), e);
            }

        }
    }

    /**
     * Checks if the regatta data source has changed, and if it has, updates LatestMessages' regatta xml message.
     */
    private void updateRegattaXMLFile() {
        if (regattaXMLSeqNumber != race.getRegattaDataSource().getSequenceNumber()) {

            regattaXMLSeqNumber = race.getRegattaDataSource().getSequenceNumber();

            try {
                String regattaXMLString = RegattaDataSourceToXML.toString(race.getRegattaDataSource());
                XMLMessage message = createXMLMessage(regattaXMLString, XMLMessageType.REGATTA);
                latestMessages.setXMLMessage(message);

            } catch (JAXBException e) {
                Logger.getGlobal().log(Level.WARNING, "Could not serialise: " + race.getRegattaDataSource(), e);
            }

        }
    }

    /**
     * Parses an individual marker boat, and returns it.
     * @param mark The marker boat to parse.
     * @return The BoatLocation message.
     */
    private BoatLocation parseIndividualMark(Mark mark) {
        //Create message.
        BoatLocation boatLocation = new BoatLocation(
                mark.getSourceID(),
                mark.getPosition().getLatitude(),
                mark.getPosition().getLongitude(),
                this.boatLocationSequenceNumber,
                BoatLocationDeviceEnum.Mark,
                Bearing.fromDegrees(0),
                0,
                race.getRaceClock().getCurrentTimeMilli());

        //Iterates the sequence number.
        this.boatLocationSequenceNumber++;

        return boatLocation;
    }

    /**
     * Parse the compound marker boats, and returns a list of BoatLocation messages.
     * @return BoatLocation messages for each mark.
     */
    private List<BoatLocation> parseMarks() {

        List<BoatLocation> markLocations = new ArrayList<>(race.getCompoundMarks().size());

        for (CompoundMark compoundMark : race.getCompoundMarks()) {

            //Get the individual marks from the compound mark.
            Mark mark1 = compoundMark.getMark1();
            Mark mark2 = compoundMark.getMark2();

            //If they aren't null, parse them (some compound marks only have one mark).
            if (mark1 != null) {
                markLocations.add(this.parseIndividualMark(mark1));
            }

            if (mark2 != null) {
                markLocations.add(this.parseIndividualMark(mark2));
            }
        }

        return markLocations;
    }


    /**
     * Parse the boats in the race, and returns all of their BoatLocation messages.
     * @return List of BoatLocation messages, for each boat.
     */
    private List<BoatLocation> parseBoatLocations() {

        List<BoatLocation> boatLocations = new ArrayList<>(race.getBoats().size());

        //Parse each boat.
        for (MockBoat boat : race.getBoats()) {
            boatLocations.add(this.parseIndividualBoatLocation(boat));
        }

        return boatLocations;
    }

    /**
     * Parses an individual boat, and returns it.
     * @param boat The boat to parse.
     * @return The BoatLocation message.
     */
    private BoatLocation parseIndividualBoatLocation(MockBoat boat) {

        BoatLocation boatLocation = new BoatLocation(
                boat.getSourceID(),
                boat.getPosition().getLatitude(),
                boat.getPosition().getLongitude(),
                this.boatLocationSequenceNumber,
                BoatLocationDeviceEnum.RacingYacht,
                boat.getBearing(),
                boat.getCurrentSpeed(),
                race.getRaceClock().getCurrentTimeMilli());

        //Iterates the sequence number.
        this.boatLocationSequenceNumber++;

        return boatLocation;

    }

    /**
     * Generates BoatState messages for every boat in the race
     * @return list of BoatState messages
     */
    private List<BoatState> parseBoatStates() {
        List<BoatState> boatStates = new ArrayList<>();
        for(MockBoat boat: race.getBoats()) {
            boatStates.add(parseIndividualBoatState(boat));
        }
        return boatStates;
    }

    /**
     * Creates a BoatState message for the current state of the given boat
     * @param boat to generate message for
     * @return BoatState message
     */
    private BoatState parseIndividualBoatState(MockBoat boat) {
        return new BoatState(
                boat.getSourceID(),
                (int)boat.getHealth()
        );
    }

    /**
     * Parses the race status, and returns it.
     * @return The race status message.
     */
    private RaceStatus parseRaceStatus() {

        //A race status message contains a list of boat statuses.
        List<BoatStatus> boatStatuses = new ArrayList<>();

        //Add each boat status to the status list.
        for (MockBoat boat : race.getBoats()) {

            BoatStatus boatStatus = new BoatStatus(
                    boat.getSourceID(),
                    boat.getStatus(),
                    boat.getCurrentLeg().getLegNumber(),
                    boat.getEstimatedTimeAtNextMark().toInstant().toEpochMilli() );

            boatStatuses.add(boatStatus);
        }



        //Create race status object, and send it.
        RaceStatus raceStatus = new RaceStatus(
                RaceStatus.currentMessageVersionNumber,
                System.currentTimeMillis(),
                race.getRaceId(),
                race.getRaceStatusEnum(),
                race.getRaceClock().getStartingTimeMilli(),
                race.getWindDirection(),
                race.getWindSpeed(),
                race.getRaceType(),
                boatStatuses);

        return raceStatus;
    }




    /**
     * Creates an XMLMessage of a specified subtype using the xml contents string.
     * @param xmlString The contents of the xml file.
     * @param messageType The subtype of xml message (race, regatta, boat).
     * @return The created XMLMessage object.
     */
    private XMLMessage createXMLMessage(String xmlString, XMLMessageType messageType) {

        //Get the correct sequence number to use.
        int sequenceNumber = 0;
        if (messageType == XMLMessageType.RACE) {
            sequenceNumber = this.raceXMLSeqNumber;

        } else if (messageType == XMLMessageType.BOAT) {
            sequenceNumber = this.boatXMLSeqNumber;

        } else if (messageType == XMLMessageType.REGATTA) {
            sequenceNumber = this.regattaXMLSeqNumber;

        }

        //Create the message.
        XMLMessage message = new XMLMessage(
                XMLMessage.currentVersionNumber,
                AckSequencer.getNextAckNum(),
                System.currentTimeMillis(),
                messageType,
                sequenceNumber,
                xmlString);


        return message;
    }

    /**
     * Parse the yacht event and return it
     * @param boat yacht with event
     * @param event event that happened
     * @return yacht event
     */
    private YachtEvent parseYachtEvent(MockBoat boat, YachtEventEnum event){
        YachtEvent yachtEvent = new YachtEvent(
                System.currentTimeMillis(),
                this.boatLocationSequenceNumber,
                race.getRaceId(),
                boat.getSourceID(),
                1337,
                event);
        return yachtEvent;
    }

    public void parseBoatCollisions(ArrayList<MockBoat> boats){
        for (MockBoat boat : boats){
            collisionEvents.add(parseYachtEvent(boat, YachtEventEnum.COLLISION));
        }
    }

}
