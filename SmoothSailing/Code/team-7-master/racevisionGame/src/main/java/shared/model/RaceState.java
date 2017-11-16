package shared.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.Enums.RaceTypeEnum;
import shared.dataInput.BoatDataSource;
import shared.dataInput.RaceDataSource;
import shared.dataInput.RegattaDataSource;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Represents a yacht race.
 * This is a base class inherited by {@link mock.model.MockRace} and {@link visualiser.model.VisualiserRaceState}.
 * Has a course, state, wind, boundaries, etc.... Boats are added by inheriting classes (see {@link Boat}, {@link mock.model.MockBoat}, {@link visualiser.model.VisualiserBoat}.
 */
public abstract class RaceState extends Observable{



    /**
     * Data source for race information.
     */
    private RaceDataSource raceDataSource;

    /**
     * Data source for boat information.
     */
    private BoatDataSource boatDataSource;

    /**
     * Data source for regatta information.
     */
    private RegattaDataSource regattaDataSource;

    /**
     * Legs in the race.
     * We have this in a separate list so that it can be observed.
     */
    private ObservableList<Leg> legs;


    /**
     * The sequence of rounding points for each leg/mark.
     */
    private MarkRoundingSequence markRoundingSequence;



    /**
     * The clock which tracks the race's start time, current time, and elapsed duration.
     */
    private RaceClock raceClock;


    /**
     * The current status of the race.
     */
    private RaceStatusEnum raceStatusEnum;


    /**
     * The race's wind.
     */
    private Property<Wind> raceWind = new SimpleObjectProperty<>();




    /**
     * Constructs an empty race object.
     * This is initialised into a "default" state, with no data.
     */
    public RaceState() {

        //Legs.
        this.legs = FXCollections.observableArrayList();

        //Race clock.
        this.raceClock = new RaceClock(ZonedDateTime.now());

        //Race status.
        this.setRaceStatusEnum(RaceStatusEnum.NOT_ACTIVE);

        //Wind.
        this.setWind(Bearing.fromDegrees(0), 0);

    }



    /**
     * Initialise the boats in the race.
     * This sets their starting positions and current legs.
     */
    protected abstract void initialiseBoats();


    /**
     * Updates the race to use a new list of legs, and adds a dummy "Finish" leg at the end.
     * @param legs The new list of legs to use.
     */
    protected void useLegsList(List<Leg> legs) {
        this.legs.setAll(legs);

        //We create this before adding the extra finish leg, as it doesn't contain compound marks.
        this.markRoundingSequence = new MarkRoundingSequence(getLegs());

        //We add a "dummy" leg at the end of the race.
        if (getLegs().size() > 0) {
            getLegs().add(new Leg("Finish", getLegs().size()));
        }

    }


    /**
     * Determines whether or not a specific leg is the last leg in the race.
     * @param leg The leg to check.
     * @return Returns true if it is the last, false otherwise.
     */
    protected boolean isLastLeg(Leg leg) {

        //Get the last leg.
        Leg lastLeg = getLegs().get(getLegs().size() - 1);

        //Check its ID.
        int lastLegID = lastLeg.getLegNumber();

        //Get the specified leg's ID.
        int legID = leg.getLegNumber();


        //Check if they are the same.
        return legID == lastLegID;
    }


    /**
     * Sets the race data source for the race.
     * @param raceDataSource New race data source.
     */
    public void setRaceDataSource(RaceDataSource raceDataSource) {
        if ((this.raceDataSource == null) || (raceDataSource.getSequenceNumber() > this.raceDataSource.getSequenceNumber())) {
            this.raceDataSource = raceDataSource;
            this.getRaceClock().setStartingTime(raceDataSource.getStartDateTime());
            useLegsList(raceDataSource.getLegs());
        }
    }

    /**
     * Sets the boat data source for the race.
     * @param boatDataSource New boat data source.
     */
    public void setBoatDataSource(BoatDataSource boatDataSource) {
        if ((this.boatDataSource == null) || (boatDataSource.getSequenceNumber() > this.boatDataSource.getSequenceNumber())) {
            this.boatDataSource = boatDataSource;
        }
    }

    /**
     * Sets the regatta data source for the race.
     * @param regattaDataSource New regatta data source.
     */
    public void setRegattaDataSource(RegattaDataSource regattaDataSource) {
        if ((this.regattaDataSource == null) || (regattaDataSource.getSequenceNumber() > this.regattaDataSource.getSequenceNumber())) {
            this.regattaDataSource = regattaDataSource;
        }
    }


    /**
     * Returns the race data source for the race.
     * @return Race data source.
     */
    public RaceDataSource getRaceDataSource() {
        return raceDataSource;
    }

    /**
     * Returns the race data source for the race.
     * @return Race data source.
     */
    public BoatDataSource getBoatDataSource() {
        return boatDataSource;
    }

    /**
     * Returns the race data source for the race.
     * @return Race data source.
     */
    public RegattaDataSource getRegattaDataSource() {
        return regattaDataSource;
    }


    /**
     * Returns a list of {@link Mark} boats.
     * @return List of mark boats.
     */
    public List<Mark> getMarks() {
        //BoatDataSource contains a collection of Marks, and RaceDataSource contains a collection of Compound marks (which contain marks). RaceDataSource is the "definitive" source of mark data.
        List<Mark> marks = new ArrayList<>(getCompoundMarks().size() * 2);

        for (CompoundMark compoundMark : getCompoundMarks()) {
            if (compoundMark.getMark1() != null) {
                marks.add(compoundMark.getMark1());
            }

            if (compoundMark.getMark2() != null) {
                marks.add(compoundMark.getMark2());
            }
        }

        return marks;
    }

    /**
     * Returns a list of sourceIDs participating in the race.
     * @return List of sourceIDs participating in the race.
     */
    public List<Integer> getParticipants() {
        return raceDataSource.getParticipants();
    }



    /**
     * Returns the current race status.
     * @return The current race status.
     */
    public RaceStatusEnum getRaceStatusEnum() {
        return raceStatusEnum;
    }

    /**
     * Sets the current race status.
     * @param raceStatusEnum The new status of the race.
     */
    public void setRaceStatusEnum(RaceStatusEnum raceStatusEnum) {
        this.raceStatusEnum = raceStatusEnum;
    }


    /**
     * Returns the type of race this is.
     * @return The type of race this is.
     */
    public RaceTypeEnum getRaceType() {
        return raceDataSource.getRaceType();
    }


    /**
     * Returns the name of the regatta.
     * @return The name of the regatta.
     */
    public String getRegattaName() {
        return regattaDataSource.getRegattaName();
    }


    /**
     * Updates the race to have a specified wind bearing and speed.
     * @param windBearing New wind bearing.
     * @param windSpeedKnots New wind speed, in knots.
     */
    public void setWind(Bearing windBearing, double windSpeedKnots) {
        Wind wind = new Wind(windBearing, windSpeedKnots);
        setWind(wind);
    }

    /**
     * Updates the race to have a specified wind (bearing and speed).
     * @param wind New wind.
     */
    public void setWind(Wind wind) {
        this.raceWind.setValue(wind);
    }


    /**
     * Returns the wind bearing.
     * @return The wind bearing.
     */
    public Bearing getWindDirection() {
        return raceWind.getValue().getWindDirection();
    }

    /**
     * Returns the wind speed.
     * Measured in knots.
     * @return The wind speed.
     */
    public double getWindSpeed() {
        return raceWind.getValue().getWindSpeed();
    }

    /**
     * Returns the race's wind.
     * @return The race's wind.
     */
    public Property<Wind> windProperty() {
        return raceWind;
    }

    /**
     * Returns the RaceClock for this race.
     * This is used to track the start time, current time, and elapsed duration of the race.
     * @return The RaceClock for the race.
     */
    public RaceClock getRaceClock() {
        return raceClock;
    }



    /**
     * Returns the number of legs in the race.
     * @return The number of legs in the race.
     */
    public int getLegCount() {
        //We minus one, as we have added an extra "dummy" leg.
        return getLegs().size() - 1;
    }


    /**
     * Returns the race boundary.
     * @return The race boundary.
     */
    public List<GPSCoordinate> getBoundary() {
        return raceDataSource.getBoundary();
    }


    /**
     * Returns the marks of the race.
     * @return Marks of the race.
     */
    public List<CompoundMark> getCompoundMarks() {
        return raceDataSource.getCompoundMarks();
    }

    /**
     * Returns the legs of the race.
     * @return Legs of the race.
     */
    public ObservableList<Leg> getLegs() {
        return legs;
    }


    /**
     * Returns the ID of the race.
     * @return ID of the race.
     */
    public int getRaceId() {
        return raceDataSource.getRaceId();
    }


    /**
     * Returns the ID of the regatta.
     * @return The ID of the regatta.
     */
    public int getRegattaID() {
        return regattaDataSource.getRegattaID();
    }


    /**
     * Returns the name of the course.
     * @return Name of the course.
     */
    public String getCourseName() {
        return regattaDataSource.getCourseName();
    }


    /**
     * Returns the rounding sequences for each leg.
     * @return Rounding sequence for each leg.
     */
    public MarkRoundingSequence getMarkRoundingSequence() {
        return markRoundingSequence;
    }
}
