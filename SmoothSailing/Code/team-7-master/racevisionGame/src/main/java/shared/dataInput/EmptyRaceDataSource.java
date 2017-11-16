package shared.dataInput;

import network.Messages.Enums.RaceTypeEnum;
import shared.model.CompoundMark;
import shared.model.Corner;
import shared.model.GPSCoordinate;
import shared.model.Leg;
import shared.xml.Race.XMLCorner;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An empty {@link RaceDataSource}. Can be used to initialise a race with no data.
 */
public class EmptyRaceDataSource implements RaceDataSource {


    /**
     * The GPS coordinate of the top left of the race boundary.
     */
    private GPSCoordinate mapTopLeft = new GPSCoordinate(0, 0);

    /**
     * The GPS coordinate of the bottom right of the race boundary.
     */
    private GPSCoordinate mapBottomRight = new GPSCoordinate(0, 0);


    /**
     * A list of GPS coordinates that make up the boundary of the race.
     */
    private final List<GPSCoordinate> boundary = new ArrayList<>();

    /**
     * A map between compoundMarkID and a CompoundMark for all CompoundMarks in a race.
     */
    private final Map<Integer, CompoundMark> compoundMarkMap = new HashMap<>();

    /**
     * A list of boat sourceIDs participating in the race.
     */
    private final List<Integer> participants = new ArrayList<>();

    /**
     * A list of legs in the race.
     */
    private final List<Leg> legs = new ArrayList<>();

    /**
     * Corners in race.
     */
    private final List<Corner> corners = new ArrayList<>();



    /**
     * The time that the race.xml file was created.
     */
    private ZonedDateTime creationTimeDate = ZonedDateTime.now();

    /**
     * The time that the race should start at, if it hasn't been postponed.
     */
    private ZonedDateTime raceStartTime = ZonedDateTime.now().plusMinutes(5);

    /**
     * Whether or not the race has been postponed.
     */
    private boolean postpone = false;


    /**
     * The ID number of the race.
     */
    private int raceID = 0;

    /**
     * The type of the race.
     */
    private RaceTypeEnum raceType = RaceTypeEnum.NOT_A_RACE_TYPE;


    private int sequenceNumber = -1;


    public EmptyRaceDataSource() {
    }



    public List<GPSCoordinate> getBoundary() {
        return boundary;
    }

    public GPSCoordinate getMapTopLeft() {
        return mapTopLeft;
    }

    public GPSCoordinate getMapBottomRight() {
        return mapBottomRight;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    @Override
    public List<Corner> getCorners() {
        return corners;
    }

    public List<CompoundMark> getCompoundMarks() {
        return new ArrayList<>(compoundMarkMap.values());
    }


    public ZonedDateTime getCreationDateTime() {
        return creationTimeDate;
    }

    public ZonedDateTime getStartDateTime() {
        return raceStartTime;
    }

    public int getRaceId() {
        return raceID;
    }

    public RaceTypeEnum getRaceType() {
        return raceType;
    }

    public boolean getPostponed() {
        return postpone;
    }

    public List<Integer> getParticipants() {
        return participants;
    }

    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void incrementSequenceNumber() {
        sequenceNumber++;
    }

    @Override
    public void setStartDateTime(ZonedDateTime time) {
        raceStartTime = time;
    }
}
