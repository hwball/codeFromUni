package network.Messages;

import mock.model.RaceServer;
import network.Messages.Enums.XMLMessageType;

import java.util.*;

/**
 * This class contains a set of the latest messages received (e.g., the latest RaceStatus, the latest BoatLocation for each boat, etc...).
 * Currently, LatestMessage only notifies observers of change when a new XMLMessage is received.
 */
public class LatestMessages extends Observable {


    /**
     * A list of messages containing a snapshot of the race.
     */
    private List<AC35Data> snapshot = new ArrayList<>();


    /**
     * The latest race data XML message.
     */
    private XMLMessage raceXMLMessage;

    /**
     * The latest boat data XML message.
     */
    private XMLMessage boatXMLMessage;

    /**
     * The latest regatta data XML message.
     */
    private XMLMessage regattaXMLMessage;


    /**
     * Ctor.
     */
    public LatestMessages() {
    }


    /**
     * Returns a copy of the race snapshot.
     *
     * @return Copy of the race snapshot.
     */
    public List<AC35Data> getSnapshot() {
        return new ArrayList<>(snapshot);
    }


    /**
     * Sets the snapshot of the race.
     *
     * @param snapshot New snapshot of race.
     */
    public void setSnapshot(List<AC35Data> snapshot) {
        this.snapshot = snapshot;
    }


    /**
     * Returns the latest race xml message.
     *
     * @return The latest race xml message.
     */
    public XMLMessage getRaceXMLMessage() {
        return raceXMLMessage;
    }

    /**
     * Sets the latest race xml message to a specified race XML message.
     *
     * @param raceXMLMessage The new race XML message to use.
     */
    public void setRaceXMLMessage(XMLMessage raceXMLMessage) {
        this.raceXMLMessage = raceXMLMessage;

        this.setChanged();
        this.notifyObservers();
    }


    /**
     * Returns the latest boat xml message.
     *
     * @return The latest boat xml message.
     */
    public XMLMessage getBoatXMLMessage() {
        return boatXMLMessage;
    }

    /**
     * Sets the latest boat xml message to a specified boat XML message.
     *
     * @param boatXMLMessage The new boat XML message to use.
     */
    public void setBoatXMLMessage(XMLMessage boatXMLMessage) {
        this.boatXMLMessage = boatXMLMessage;

        this.setChanged();
        this.notifyObservers();
    }


    /**
     * Returns the latest regatta xml message.
     *
     * @return The latest regatta xml message.
     */
    public XMLMessage getRegattaXMLMessage() {
        return regattaXMLMessage;
    }

    /**
     * Sets the latest regatta xml message to a specified regatta XML message.
     *
     * @param regattaXMLMessage The new regatta XML message to use.
     */
    public void setRegattaXMLMessage(XMLMessage regattaXMLMessage) {
        this.regattaXMLMessage = regattaXMLMessage;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Checks the type of xml message, and places it in this LatestMessages object.
     *
     * @param xmlMessage The new xml message to use.
     */
    public void setXMLMessage(XMLMessage xmlMessage) {

        if (xmlMessage.getXmlMsgSubType() == XMLMessageType.RACE) {
            this.setRaceXMLMessage(xmlMessage);

        } else if (xmlMessage.getXmlMsgSubType() == XMLMessageType.REGATTA) {
            this.setRegattaXMLMessage(xmlMessage);

        } else if (xmlMessage.getXmlMsgSubType() == XMLMessageType.BOAT) {
            this.setBoatXMLMessage(xmlMessage);

        }

    }

    /**
     * Returns whether or not there is an xml message for each message type.
     *
     * @return True if race, boat, and regatta have an xml message, false otherwise.
     */
    public boolean hasAllXMLMessages() {
        if (this.regattaXMLMessage == null || this.boatXMLMessage == null ||
                this.raceXMLMessage == null) {
            return false;

        } else {
            return true;

        }

    }

}
