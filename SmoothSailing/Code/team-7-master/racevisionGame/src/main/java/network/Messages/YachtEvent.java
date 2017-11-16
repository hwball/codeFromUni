package network.Messages;

import network.Messages.Enums.MessageType;
import network.Messages.Enums.YachtEventEnum;

/**
 * Represents a Yacht Event Code message defined in the AC35 spec, with Event IDs amended for the purposes of
 * a game.
 */
public class YachtEvent extends AC35Data {
    private long currentTime;
    private int ackNum;
    private int raceID;
    private int sourceID;
    private int incidentID;
    private YachtEventEnum yachtEvent;

    public YachtEvent(long currentTime, int ackNum, int raceID, int sourceID, int incidentID, YachtEventEnum yachtEvent) {
        super(MessageType.YACHTEVENTCODE);
        this.currentTime = currentTime;
        this.ackNum = ackNum;
        this.raceID = raceID;
        this.sourceID = sourceID;
        this.incidentID = incidentID;
        this.yachtEvent = yachtEvent;
    }

    public YachtEventEnum getYachtEvent() {
        return yachtEvent;
    }

    public int getSourceID() {
        return sourceID;
    }

    public int getIncidentID() {
        return incidentID;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public int getAckNum() {
        return ackNum;
    }

    public int getRaceID() {
        return raceID;
    }
}
