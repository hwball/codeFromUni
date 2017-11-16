package shared.dataInput;

import network.Messages.Enums.RaceTypeEnum;
import shared.model.CompoundMark;
import shared.model.Corner;
import shared.model.GPSCoordinate;
import shared.model.Leg;
import shared.xml.Race.XMLCorner;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * An object that holds relevant data for a race. <br>
 * Information includes: {@link shared.model.Boat Boat}s,
 * {@link shared.model.Leg Leg}s, {@link shared.model.CompoundMark CompoundMark}s and
 * the {@link shared.model.GPSCoordinate GPSCoordinate}s.
 */
public interface RaceDataSource {
    /**
     * Returns the list of sourceIDs for boats competing in the race.
     * @return SourceIDs for boats competing in the race.
     */
    List<Integer> getParticipants();

    /**
     * Returns the list of legs in the race.
     * @return The list of legs in the race.
     */
    List<Leg> getLegs();

    /**
     * Returns the list of corners in the race - two adjacent corners form a leg.
     * @return List of corners in race.
     */
    List<Corner> getCorners();

    /**
     * Returns a list of coordinates representing the boundary of the race.
     * @return The boundary of the race.
     */
    List<GPSCoordinate> getBoundary();

    /**
     * Returns a list of CompoundMarks in the race.
     * @return The sequence of compounds marks in the race.
     */
    List<CompoundMark> getCompoundMarks();


    /**
     * Returns the ID of the race.
     * @return The ID of the race.
     */
    int getRaceId();

    /**
     * Returns the type of race.
     * @return The type of race.
     */
    RaceTypeEnum getRaceType();


    /**
     * Returns the start time/date of the race.
     * @return The race's start time.
     */
    ZonedDateTime getStartDateTime();

    /**
     * Sets the start time/date of the race.
     * @param time Time to start at.
     */
    void setStartDateTime(ZonedDateTime time);

    /**
     * Returns the creation time/date of the race xml file.
     * @return The race xml file's creation time.
     */
    ZonedDateTime getCreationDateTime();

    /**
     * Returns whether or not the race has been postponed.
     * @return True if the race has been postponed, false otherwise.
     */
    boolean getPostponed();


    /**
     * Returns the GPS coordinate of the top left of the race map area.
     * @return Top left GPS coordinate.
     */
    GPSCoordinate getMapTopLeft();

    /**
     * Returns the GPS coordinate of the bottom right of the race map area.
     * @return Bottom right GPS coordinate.
     */
    GPSCoordinate getMapBottomRight();


    /**
     * Returns the sequence number associated with this data source. Used to indicate when it has changed.
     * @return Sequence number.
     */
    int getSequenceNumber();

    /**
     * Increments the sequence number for this data source. Used to indicate that it has changed.
     */
    void incrementSequenceNumber();
}
