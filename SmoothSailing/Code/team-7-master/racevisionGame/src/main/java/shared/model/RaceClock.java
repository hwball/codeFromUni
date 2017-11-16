package shared.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.Nullable;
import visualiser.Controllers.RaceStartController;
import visualiser.model.ResizableRaceCanvas;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * This class is used to implement a clock which keeps track of and
 * displays times relevant to a race. This is displayed on the
 * {@link ResizableRaceCanvas} via the
 * {@link visualiser.Controllers.RaceViewController} and the
 * {@link RaceStartController}.
 */
public class RaceClock {

    /**
     * The time zone of the race.
     */
    private final ZoneId zoneId;

    /**
     * The start time of the race.
     */
    private ZonedDateTime startingTime;

    /**
     * The current time of the race.
     */
    private final StringProperty startingTimeProperty = new SimpleStringProperty();


    /**
     * The current time of the race.
     */
    @Nullable
    private ZonedDateTime currentTime;

    /**
     * The current time of the race.
     */
    private final StringProperty currentTimeProperty = new SimpleStringProperty();

    /**
     * The time until the race starts, or elapsed time in the race after it has started.
     */
    private StringProperty durationProperty = new SimpleStringProperty();


    //Format strings.
    /**
     * Format string used for starting time.
     */
    private String startingTimeFormat = "'Starting time:' HH:mm dd/MM/YYYY";

    /**
     * Format string used for current time.
     */
    private String currentTimeFormat = "'Current time:' HH:mm dd/MM/YYYY";

    /**
     * Format string used for duration before it has started.
     */
    private String durationBeforeStartFormat = "%02d:%02d:%02d";
    /**
     * Format string used for duration once the race has started.
     */
    private String durationAfterStartFormat = "%02d:%02d:%02d";




    /**
     * Constructs a RaceClock using a specified starting ZonedDateTime.
     * @param startingTime The ZonedDateTime that the race starts at.
     */
    public RaceClock(ZonedDateTime startingTime) {
        this.zoneId = startingTime.getZone();

        //Set start time.
        setStartingTime(startingTime);

    }



    /**
     * Sets time to given UTC time in seconds from Unix epoch, preserving timezone.
     * @param time UTC time.
     */
    public void setUTCTime(long time) {
        Date utcTime = new Date(time);
        setCurrentTime(utcTime.toInstant().atZone(this.zoneId));
    }



    /**
     * Get ZonedDateTime corresponding to local time zone and given UTC time.
     * @param time time in mills
     * @return local date time
     */
    public ZonedDateTime getLocalTime(long time) {
        Date utcTime = new Date(time);
        return utcTime.toInstant().atZone(this.zoneId);
    }


    /**
     * Returns the starting time of the race.
     * @return The starting time of the race.
     */
    public ZonedDateTime getStartingTime() {
        return startingTime;
    }

    /**
     * Returns the race start time, expressed as the number of milliseconds since the unix epoch.
     * @return Start time expressed as milliseconds since unix epoch.
     */
    public long getStartingTimeMilli() {
        return startingTime.toInstant().toEpochMilli();
    }

    /**
     * Sets the starting time of the race.
     * @param startingTime The starting time of the race.
     */
    public void setStartingTime(ZonedDateTime startingTime) {
        this.startingTime = startingTime;

        //Convert time into string.
        String startingTimeString = DateTimeFormatter.ofPattern(this.startingTimeFormat).format(startingTime);

        //Use it.
        setStartingTimeString(startingTimeString);
    }

    /**
     * Returns the starting time of the race, as a string.
     * @return The starting time of the race, as a string.
     */
    public String getStartingTimeString() {
        return startingTimeProperty.get();
    }

    /**
     * Sets the starting time string of the race.
     * This should only be called by {@link #setStartingTime(ZonedDateTime)}.
     * @param startingTime The new value for the starting time string.
     */
    private void setStartingTimeString(String startingTime) {
        this.startingTimeProperty.setValue(startingTime);
    }

    /**
     * Returns the starting time property.
     * @return The starting time property.
     */
    public StringProperty startingTimeProperty() {
        return startingTimeProperty;
    }



    /**
     * Returns the race duration, in milliseconds.
     * A negative value means that the race has not started.
     * @return Race duration in milliseconds.
     */
    public long getDurationMilli() {
        return getCurrentTimeMilli() - getStartingTimeMilli();
    }


    /**
     * Returns the race duration, as a string.
     * @return Duration as a string.
     */
    public String getDurationString() {
        return durationProperty.get();
    }

    /**
     * Sets the duration time string of the race.
     * @param duration The new value for the duration time string.
     */
    private void setDurationString(String duration) {
        this.durationProperty.setValue(duration);
    }

    /**
     * Returns the duration property.
     * @return The duration property.
     */
    public StringProperty durationProperty() {
        return durationProperty;
    }




    /**
     * Returns the current time of the race.
     * @return The current time of the race.
     */
    @Nullable
    public ZonedDateTime getCurrentTime() {
        return currentTime;
    }

    /**
     * Returns the race current time, expressed as the number of milliseconds since the unix epoch.
     * @return Current time expressed as milliseconds since unix epoch.
     */
    public long getCurrentTimeMilli() {
        return currentTime.toInstant().toEpochMilli();
    }

    /**
     * Sets the current time of the race.
     * @param currentTime The current time of the race.
     */
    private void setCurrentTime(ZonedDateTime currentTime) {
        this.currentTime = currentTime;

        //Convert time into string.
        String currentTimeString = DateTimeFormatter.ofPattern(this.currentTimeFormat).format(currentTime);

        //Use it.
        setCurrentTimeString(currentTimeString);


        //Update the duration string.
        updateDurationString();

    }

    /**
     * Updates the duration string based on the start time and current time.
     * This requires {@link #currentTime} to be non-null.
     */
    private void updateDurationString() {
        //Calculates the duration in seconds.
        long seconds = Duration.between(startingTime.toLocalDateTime(), currentTime.toLocalDateTime()).getSeconds();

        //Check if the race has already started or not. This determines the format string used.
        String formatString;
        if (seconds < 0) {
            //Race hasn't started.
            formatString = this.durationBeforeStartFormat;
            //The seconds value is negative, so we make it positive.
            seconds = seconds * -1;
        } else {
            //Race has started.
            formatString = this.durationAfterStartFormat;
        }

        //Format the seconds value.
        //Hours : minutes : seconds.
        String formattedDuration = String.format(formatString, seconds / 3600, (seconds % 3600) / 60, seconds % 60);

        //Use it.
        setDurationString(formattedDuration);
    }

    /**
     * Returns the current time of the race, as a string.
     * @return The current time of the race, as a string.
     */
    public String getCurrentTimeString() {
        return currentTimeProperty.get();
    }

    /**
     * Sets the current time string of the race.
     * @param currentTime The new value for the current time string.
     */
    private void setCurrentTimeString(String currentTime) {
        this.currentTimeProperty.setValue(currentTime);
    }

    /**
     * Returns the current time property.
     * @return The current time property.
     */
    public StringProperty currentTimeProperty() {
        return currentTimeProperty;
    }


    /**
     * Returns the time zone of the race, as a string.
     * @return The race time zone.
     */
    public String getTimeZone() {
        return zoneId.toString();
    }
}
