package mock.model;

import mock.model.commandFactory.ActiveObserverCommand;
import mock.model.commandFactory.ObserverCommand;
import mock.model.wind.WindGenerator;
import mock.model.collider.ColliderRegistry;
import network.Messages.Enums.BoatStatusEnum;
import network.Messages.Enums.RaceStatusEnum;
import shared.dataInput.BoatDataSource;
import shared.dataInput.RaceDataSource;
import shared.dataInput.RegattaDataSource;
import shared.exceptions.BoatNotFoundException;
import shared.model.*;
import shared.model.Bearing;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Math.cos;


/**
 * Represents a yacht race.
 * Has a course, boats, boundaries, etc...
 * Is responsible for simulating the race, and sending messages to a MockOutput instance.
 */
public class MockRace extends RaceState {

    /**
     * An observable list of boats in the race.
     */
    private List<MockBoat> boats;

    /**
     * A copy of the boundary list, except "shrunk" inwards by 50m.
     */
    private List<GPSCoordinate> shrinkBoundary;


    /**
     * Registry for all collider object in this race
     */
    private ColliderRegistry colliderRegistry;


    /**
     * The scale factor of the race.
     * See {@link Constants#RaceTimeScale}.
     */
    private int scaleFactor;

    /**
     * Object used to generate changes in wind speed/direction.
     */
    private WindGenerator windGenerator;


    /**
     * The polars file to use for each boat.
     */
    private Polars polars;

    private Map<Integer, ActiveObserverCommand> activeObserverCommands;

    private long racePreStartTime = Constants.RacePreStartTime;

    private long racePreparatoryTime = Constants.RacePreparatoryTime;

    /**
     * True if the race has been manually started, false otherwise.
     */
    private boolean hasBeenStarted = false;

    /**
     * Constructs a race object with a given RaceDataSource, BoatDataSource, and RegattaDataSource and sends events to the given mockOutput.
     * @param boatDataSource Data source for boat related data (yachts and marker boats).
     * @param raceDataSource Data source for race related data (participating boats, legs, etc...).
     * @param regattaDataSource Data source for race related data (course name, location, timezone, etc...).
     * @param polars The polars table to be used for boat simulation.
     * @param timeScale The timeScale for the race. See {@link Constants#RaceTimeScale}.
     * @param windGenerator The wind generator used for the race.
     */
    public MockRace(BoatDataSource boatDataSource, RaceDataSource raceDataSource, RegattaDataSource regattaDataSource, Polars polars, int timeScale, WindGenerator windGenerator) {

        this.setBoatDataSource(boatDataSource);
        this.setRaceDataSource(raceDataSource);
        this.setRegattaDataSource(regattaDataSource);

        this.activeObserverCommands = new HashMap<>();
        this.polars = polars;
        this.scaleFactor = timeScale;

        this.boats = new ArrayList<>();

        this.shrinkBoundary = GPSCoordinate.getShrinkBoundary(this.getBoundary());


        this.windGenerator = windGenerator;

        //Wind.
        this.setWind(windGenerator.generateBaselineWind());


        // Set up colliders
        this.colliderRegistry = new ColliderRegistry();

        for(CompoundMark mark: this.getCompoundMarks()) {
            colliderRegistry.addCollider(mark.getMark1());
            if(mark.getMark2() != null) colliderRegistry.addCollider(mark.getMark2());
        }
    }


    /**
     * Generates a MockBoat from the BoatDataSource, given a source ID. Also adds it to the participant list.
     * @param sourceID The source ID to assign the boat.
     */
    public void generateMockBoat(Integer sourceID) {

        //Get the boat associated with the sourceID.
        Boat boat = getBoatDataSource().getBoats().get(sourceID);

        //Construct a MockBoat using the Boat and Polars.
        MockBoat mockBoat = new MockBoat(boat, polars);
        mockBoat.setCurrentLeg(this.getLegs().get(0));
        mockBoat.setEstimatedTimeAtNextMark(this.getRaceClock().getCurrentTime());

        //Update participant list.
        getRaceDataSource().getParticipants().add(sourceID);

        this.boats.add(mockBoat);
        this.activeObserverCommands.put(boat.getSourceID(), new ActiveObserverCommand());
        this.colliderRegistry.addCollider(mockBoat);

        getRaceDataSource().incrementSequenceNumber();
    }

    /**
     * Removes a MockBoat from the race, by sourceID. Also removes it from the participant list.
     * @param sourceID Source ID of boat to remove.
     */
    public void removeMockBoat(Integer sourceID) {
        this.boats.removeIf(mockBoat -> mockBoat.getSourceID() == sourceID);
        getRaceDataSource().getParticipants().remove(sourceID);
        getRaceDataSource().incrementSequenceNumber();
    }


    public ColliderRegistry getColliderRegistry() {
        return colliderRegistry;
    }


    /**
     * Updates the race time to a specified value, in milliseconds since the unix epoch.
     * @param currentTime Milliseconds since unix epoch.
     */
    public void updateRaceTime(long currentTime) {
        this.getRaceClock().setUTCTime(currentTime);
    }


    /**
     * Delays the start of the race, if needed, to ensure that the race doesn't start until host wants it to.
     * If the time until start is less that 5 minutes, it is extended to 10 minutes.
     */
    public void delayRaceStart() {
        long timeToStart = getRaceDataSource().getStartDateTime().toInstant().toEpochMilli() - System.currentTimeMillis();

        long fiveMinutesMilli = 5 * 60 * 1000;
        long tenMinutesMilli = 10 * 60 * 1000;

        if ((timeToStart < fiveMinutesMilli) && (timeToStart > 0) && !hasBeenStarted) {
            startRace(tenMinutesMilli, false);
        }

    }


    /**
     * Updates the race status enumeration based on the current time.
     */
    public void updateRaceStatusEnum() {

        //The millisecond duration of the race. Negative means it hasn't started, so we flip sign.
        long timeToStart = - this.getRaceClock().getDurationMilli();

        if (timeToStart > racePreStartTime) {
            //Time > 3 minutes is the prestart period.
            this.setRaceStatusEnum(RaceStatusEnum.PRESTART);

        } else if ((timeToStart <= racePreStartTime) && (timeToStart >= racePreparatoryTime)) {
            //Time between [1, 3] minutes is the warning period.
            this.setRaceStatusEnum(RaceStatusEnum.WARNING);

        } else if ((timeToStart <= racePreparatoryTime) && (timeToStart > 0)) {
            //Time between (0, 1] minutes is the preparatory period.
            this.setRaceStatusEnum(RaceStatusEnum.PREPARATORY);

        } else {
            //Otherwise, the race has started!
            this.setRaceStatusEnum(RaceStatusEnum.STARTED);

        }


    }

    public void setRacePreStartTime(long racePreStartTime) {
        this.racePreStartTime = racePreStartTime;
    }

    public void setRacePreparatoryTime(long racePreparatoryTime) {
        this.racePreparatoryTime = racePreparatoryTime;
    }

    public long getRacePreparatoryTime() {
        return racePreparatoryTime;
    }


    /**
     * Starts the race in #timeToStartMilli milliseconds.
     * @param timeToStartMilli Millseconds before starting the race.
     * @param manualStart True if the race has been manually started, false otherwise.
     */
    public void startRace(long timeToStartMilli, boolean manualStart) {
        this.hasBeenStarted = manualStart;
        ZonedDateTime startTime = ZonedDateTime.now().plus(timeToStartMilli, ChronoUnit.MILLIS);

        getRaceDataSource().setStartDateTime(startTime);
        getRaceDataSource().incrementSequenceNumber();

        this.getRaceClock().setStartingTime(getRaceDataSource().getStartDateTime());
    }

    /**
     * Sets the status of all boats in the race to RACING.
     */
    public void setBoatsStatusToRacing() {

        for (MockBoat boat : this.boats) {
            boat.setStatus(BoatStatusEnum.RACING);
        }
    }


    /**
     * Sets the estimated time at next mark for each boat to a specified time. This is used during the countdown timer to provide this value to boat before the race starts.
     * @param time The time to provide to each boat.
     */
    public void setBoatsTimeNextMark(ZonedDateTime time) {

        for (MockBoat boat : this.boats) {
            boat.setEstimatedTimeAtNextMark(time);
        }
    }


    /**
     * Initialise the boats in the race.
     * This sets their starting positions and current legs.
     */
    @Override
    public void initialiseBoats() {

        //Gets the starting positions of the boats.
        List<GPSCoordinate> startingPositions = getSpreadStartingPositions();

        //Get iterators for our boat and position lists.
        Iterator<MockBoat> boatIt = this.boats.iterator();
        Iterator<GPSCoordinate> startPositionIt = startingPositions.iterator();

        //Iterate over the pair of lists.
        while (boatIt.hasNext() && startPositionIt.hasNext()) {

            //Get the next boat and position.
            MockBoat boat = boatIt.next();
            GPSCoordinate startPosition = startPositionIt.next();


            //The boat starts on the first leg of the race.
            boat.setCurrentLeg(this.getLegs().get(0));

            //Boats start with 0 knots speed.
            boat.setCurrentSpeed(0d);

            //Place the boat at its starting position.
            boat.setPosition(startPosition);

            //Boats start facing their next marker.
            boat.setBearing(boat.calculateBearingToNextMarker());

            //Sets the boats status to prestart - it changes to racing when the race starts.
            boat.setStatus(BoatStatusEnum.PRESTART);

            //We set a large time since tack change so that it calculates a new VMG when the simulation starts.
            //boat.setTimeSinceTackChange(Long.MAX_VALUE);
            boat.setTimeSinceTackChange(0);

        }

    }


    /**
     * Creates a list of starting positions for the different boats, so they do not appear cramped at the start line.
     *
     * @return A list of starting positions.
     */
    private List<GPSCoordinate> getSpreadStartingPositions() {

        //The first compound marker of the race - the starting gate.
        CompoundMark compoundMark = this.getLegs().get(0).getStartCompoundMark();

        //The position of the two markers from the compound marker.
        GPSCoordinate mark1Position = compoundMark.getMark1Position();
        GPSCoordinate mark2Position = compoundMark.getMark2Position();


        //Calculates the azimuth between the two points.
        Azimuth azimuth = GPSCoordinate.calculateAzimuth(mark1Position, mark2Position);

        //Calculates the distance between the two points.
        double distanceMeters = GPSCoordinate.calculateDistanceMeters(mark1Position, mark2Position);

        //The number of boats in the race.
        int numberOfBoats = this.boats.size();

        //Calculates the distance between each boat. We divide by numberOfBoats + 1 to ensure that no boat is placed on one of the starting gate's marks.
        double distanceBetweenBoatsMeters = distanceMeters / (numberOfBoats + 1);


        //List to store coordinates in.
        List<GPSCoordinate> positions = new ArrayList<>();

        //We start spacing boats out from mark 1.
        GPSCoordinate position = mark1Position;

        //For each boat, displace position, and store it.
        for (int i = 0; i < numberOfBoats; i++) {

            position = GPSCoordinate.calculateNewPosition(position, distanceBetweenBoatsMeters, azimuth);

            positions.add(position);

        }

        return positions;
    }


    /**
     * Determines whether or not a given VMG improves the velocity of a boat, if it were currently using currentVMG.
     * @param currentVMG The current VMG of the boat.
     * @param potentialVMG The new VMG to test.
     * @param bearingToDestination The bearing between the boat and its destination.
     * @return True if the new VMG is improves velocity, false otherwise.
     */
    public boolean improvesVelocity(VMG currentVMG, VMG potentialVMG, Bearing bearingToDestination) {

        //Calculates the angle between the boat and its destination.
        Angle angleBetweenDestAndHeading = Angle.fromDegrees(currentVMG.getBearing().degrees() - bearingToDestination.degrees());

        //Calculates the angle between the new VMG and the boat's destination.
        Angle angleBetweenDestAndNewVMG = Angle.fromDegrees(potentialVMG.getBearing().degrees() - bearingToDestination.degrees());


        //Calculate the boat's current velocity.
        double currentVelocity = Math.cos(angleBetweenDestAndHeading.radians()) * currentVMG.getSpeed();

        //Calculate the potential velocity with the new VMG.
        double vmgVelocity =  Math.cos(angleBetweenDestAndNewVMG.radians()) * potentialVMG.getSpeed();

        //Return whether or not the new VMG gives better velocity.
        return vmgVelocity > currentVelocity;

    }

    /**
     * Determines whether or not a given VMG improves the velocity of a boat.
     * @param boat The boat to test.
     * @param vmg The new VMG to test.
     * @return True if the new VMG is improves velocity, false otherwise.
     */
    private boolean improvesVelocity(MockBoat boat, VMG vmg) {

        //Get the boats "current" VMG.
        VMG boatVMG = new VMG(boat.getCurrentSpeed(), boat.getBearing());

        //Check if the new VMG is better than the boat's current VMG.
        return this.improvesVelocity(boatVMG, vmg, boat.calculateBearingToNextMarker());

    }

    /**
     * Calculates the distance a boat has travelled and updates its current position according to this value.
     *
     * @param boat The boat to be updated.
     * @param updatePeriodMilliseconds The time, in milliseconds, since the last update.
     * @param totalElapsedMilliseconds The total number of milliseconds that have elapsed since the start of the race.
     */
    public void updatePosition(MockBoat boat, long updatePeriodMilliseconds, long totalElapsedMilliseconds) {

        //Checks if the current boat has finished the race or not.
        boolean finish = this.isLastLeg(boat.getCurrentLeg());

        if (!finish && totalElapsedMilliseconds >= updatePeriodMilliseconds && !boat.isColliding()) {
            if(boat.isVelocityDefault()) setBoatSpeed(boat);
            //Calculates the distance travelled, in meters, in the current timeslice.
            double distanceTravelledMeters = boat.calculateMetersTravelled(updatePeriodMilliseconds) * this.scaleFactor;

            checkPosition(boat, totalElapsedMilliseconds);
            //Move the boat forwards that many meters, and advances its time counters by enough milliseconds.
            boat.moveForwards(distanceTravelledMeters);
            boat.setTimeSinceTackChange(boat.getTimeSinceTackChange() + updatePeriodMilliseconds);
        }

        // Remove one unit of health for every frame spent outside boundary
        if(!finish && !GPSCoordinate.isInsideBoundary(boat.getPosition(), getBoundary())) {
            boat.updateHealth(-0.1);
        }

        this.updateEstimatedTime(boat);

    }

    private void setBoatSpeed(MockBoat boat) {
        VMG vmg = new VMG(NewPolars.calculateSpeed(
                this.getWindDirection(),
                this.getWindSpeed(),
                boat.getBearing()
        ), boat.getBearing()) ;
        if (vmg.getSpeed() > 0) {
            boat.setCurrentSpeed(vmg.getSpeed() * Math.pow(boat.getHealth() / 100, 0.3));
        }
    }

    /**
     * Calculates the upper and lower bounds that the boat may have in order to not go outside of the course.
     * @param boat The boat to check.
     * @return An array of bearings. The first is the lower bound, the second is the upper bound.
     */
    private Bearing[] calculateBearingBounds(MockBoat boat) {

        Bearing[] bearings = new Bearing[2];

        Bearing lowerBearing = Bearing.fromDegrees(0.001);
        Bearing upperBearing = Bearing.fromDegrees(359.999);



        double lastAngle = -1;
        boolean lastAngleWasGood = false;

        //Check all bearings between [0, 360)
        for (double angle = 0; angle < 360; angle += 1) {

            //Create bearing from angle.
            Bearing bearing = Bearing.fromDegrees(angle);

            //Check that if it is acceptable.
            boolean bearingIsGood = this.checkBearingInsideCourse(bearing, boat.getPosition());


            if (lastAngle != -1) {

                if (lastAngleWasGood && !bearingIsGood) {
                    //We have flipped over from good bearings to bad bearings. So the last good bearing is the upper bearing.
                    upperBearing = Bearing.fromDegrees(lastAngle);
                }

                if (!lastAngleWasGood && bearingIsGood) {
                    //We have flipped over from bad bearings to good bearings. So the current bearing is the lower bearing.
                    lowerBearing = Bearing.fromDegrees(angle);
                }

            }

            lastAngle = angle;
            lastAngleWasGood = bearingIsGood;

        }



        //TODO BUG if it can't find either upper or lower, it returns (0, 359.999). Should return (boatbearing, boatbearing+0.0001)
        bearings[0] = lowerBearing;
        bearings[1] = upperBearing;

        return bearings;
    }



    /**
     * Checks if a given bearing, starting at a given position, would put a boat out of the course boundaries.
     * @param bearing The bearing to check.
     * @param position The position to start from.
     * @return True if the bearing would keep the boat in the course, false if it would take it out of the course.
     */
    private boolean checkBearingInsideCourse(Bearing bearing, GPSCoordinate position) {

        //Get azimuth from bearing.
        Azimuth azimuth = Azimuth.fromBearing(bearing);


        //Tests to see if a point in front of the boat is out of bounds.
        double epsilonMeters = 50d;
        GPSCoordinate testCoord = GPSCoordinate.calculateNewPosition(position, epsilonMeters, azimuth);

        //If it isn't inside the boundary, calculate new bearing.
        if (GPSCoordinate.isInsideBoundary(testCoord, this.shrinkBoundary)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks to be run on boats rounding marks on the port side
     * @param boat the boat that is rounding a mark
     * @param roundingData The data for the current leg's rounding.
     */
    private void boatRoundingCheckPort(MockBoat boat, MarkRoundingData roundingData) {
        //boats must pass all checks in order to round a mark

        //boolean for if boat has to/needs to pass through a gate
        boolean gateCheck = boat.getCurrentLeg().getEndCompoundMark().getMark2() == null || boat.isBetweenGate(boat.getCurrentLeg().getEndCompoundMark());



        switch (boat.getRoundingStatus()) {
            case 0://hasn't started rounding
                if (boat.isPortSide(roundingData.getMarkToRound()) &&
                        GPSCoordinate.passesLine(
                                roundingData.getMarkToRound().getPosition(),
                                roundingData.getRoundCheck1(),
                                boat.getPosition(),
                                roundingData.getLegBearing()) &&
                        gateCheck &&
                        boat.isBetweenGate(
                                roundingData.getMarkToRound(),
                                Mark.tempMark(roundingData.getRoundCheck1()))   ) {
                    boat.increaseRoundingStatus();
                    if (boat.getCurrentLeg().getLegNumber() + 2 >= getLegs().size()){
                        //boat has finished race
                        boat.increaseRoundingStatus();
                    }
                }
                break;
            case 1://has been parallel to the mark;
                if (boat.isPortSide(roundingData.getMarkToRound()) &&
                        GPSCoordinate.passesLine(
                                roundingData.getMarkToRound().getPosition(),
                                roundingData.getRoundCheck2(),
                                boat.getPosition(),
                                Bearing.fromDegrees(
                                        GPSCoordinate.calculateBearing(
                                                roundingData.getMarkToRound().getPosition(),
                                                roundingData.getRoundCheck2()).degrees() - 90)) &&//negative 90 from bearing because of port rounding
                        boat.isBetweenGate(
                                roundingData.getMarkToRound(),
                                Mark.tempMark(roundingData.getRoundCheck2()))) {
                    boat.increaseRoundingStatus();
                }
                break;
            case 2://has traveled 180 degrees around the mark
                //Move boat on to next leg.
                boat.resetRoundingStatus();
                Leg nextLeg = this.getLegs().get(boat.getCurrentLeg().getLegNumber() + 1);
                boat.setCurrentLeg(nextLeg);
                break;
        }
    }

    /**
     * Checks to be run on boats rounding marks on the starboard side
     * @param boat the boat that is rounding a mark
     * @param roundingData The data for the current leg's rounding.
     */
    private void boatRoundingCheckStarboard(MockBoat boat, MarkRoundingData roundingData){
        //boats must pass all checks in order to round a mark

        //boolean for if boat has to/needs to pass through a gate
        boolean gateCheck = boat.getCurrentLeg().getEndCompoundMark().getMark2() == null || boat.isBetweenGate(boat.getCurrentLeg().getEndCompoundMark());


        switch (boat.getRoundingStatus()) {
            case 0://hasn't started rounding
                if (boat.isStarboardSide(roundingData.getMarkToRound()) &&
                        GPSCoordinate.passesLine(
                                roundingData.getMarkToRound().getPosition(),
                                roundingData.getRoundCheck1(),
                                boat.getPosition(),
                                roundingData.getLegBearing()) &&
                        gateCheck &&
                        boat.isBetweenGate(
                                roundingData.getMarkToRound(),
                                Mark.tempMark(roundingData.getRoundCheck1()))) {
                    boat.increaseRoundingStatus();
                    if (boat.getCurrentLeg().getLegNumber() + 2 >= getLegs().size()){
                        //boat has finished race
                        boat.increaseRoundingStatus();
                    }
                }
                break;
            case 1://has been parallel to the mark
                if (boat.isStarboardSide(roundingData.getMarkToRound()) &&
                        GPSCoordinate.passesLine(
                                roundingData.getMarkToRound().getPosition(),
                                roundingData.getRoundCheck2(),
                                boat.getPosition(),
                                Bearing.fromDegrees(
                                        GPSCoordinate.calculateBearing(
                                                roundingData.getMarkToRound().getPosition(),
                                                roundingData.getRoundCheck2()  ).degrees() + 90)) && //positive 90 from bearing because of starboard rounding
                        boat.isBetweenGate(
                                roundingData.getMarkToRound(),
                                Mark.tempMark(roundingData.getRoundCheck2()))   ) {
                    boat.increaseRoundingStatus();
                }
                break;
            case 2://has traveled 180 degrees around the mark
                //Move boat on to next leg.
                boat.resetRoundingStatus();
                Leg nextLeg = this.getLegs().get(boat.getCurrentLeg().getLegNumber() + 1);
                boat.setCurrentLeg(nextLeg);
                break;
        }
    }

    /**
     * Checks if a boat has finished any legs, or has pulled out of race (DNF).
     * @param boat The boat to check.
     * @param timeElapsed The total time, in milliseconds, that has elapsed since the race started.
     */
    protected void checkPosition(MockBoat boat, long timeElapsed) {

        switch (boat.getCurrentLeg().getEndCompoundMark().getRoundingType()) {
            case SP://Not yet implemented so these gates will be rounded port side
            case Port:
                boatRoundingCheckPort(
                        boat,
                        getMarkRoundingSequence().getRoundingData(boat.getCurrentLeg())  );
                break;
            case PS://not yet implemented so these gates will be rounded starboard side
            case Starboard:
                boatRoundingCheckStarboard(
                        boat,
                        getMarkRoundingSequence().getRoundingData(boat.getCurrentLeg())  );
                break;
        }


        //Check if the boat has finished or stopped racing.
        if (this.isLastLeg(boat.getCurrentLeg())) {
            //Boat has finished.
            boat.setTimeFinished(timeElapsed);
            boat.setCurrentSpeed(0);
            boat.setStatus(BoatStatusEnum.FINISHED);

        }

    }




    /**
     * Returns the number of boats that are still active in the race.
     * They become inactive by either finishing or withdrawing.
     * @return The number of boats still active in the race.
     */
    protected int getNumberOfActiveBoats() {

        int numberOfActiveBoats = 0;
        for (MockBoat boat : this.boats) {

            //If the boat is currently racing, count it.
            if (boat.getStatus() == BoatStatusEnum.RACING && boat.getHealth()>=1) {
                numberOfActiveBoats++;
            }

        }

        return numberOfActiveBoats;
    }


    /**
     * Returns a list of boats in the race.
     * @return List of boats in the race.
     */
    public List<MockBoat> getBoats() {
        return boats;
    }

    /**
     * Returns a boat by sourceID.
     * @param sourceID The source ID the boat.
     * @return The boat.
     * @throws BoatNotFoundException Thrown if there is not boat with the specified sourceID.
     */
    public MockBoat getBoat(int sourceID) throws BoatNotFoundException {

        for (MockBoat boat : boats) {

            if (boat.getSourceID() == sourceID) {
                return boat;
            }

        }

        throw new BoatNotFoundException("Boat with sourceID: " + sourceID + " was not found.");
    }

    /**
     * Changes the wind direction randomly, while keeping it within [windLowerBound, windUpperBound].
     */
    public void changeWindDirection() {

        Wind nextWind = windGenerator.generateNextWind(windProperty().getValue());

        setWind(nextWind);
    }



    /**
     * Updates the boat's estimated time to next mark if positive
     * @param boat to estimate time given its velocity
     */
    private void updateEstimatedTime(MockBoat boat) {

        double velocityToMark = boat.getCurrentSpeed() * cos(boat.getBearing().radians() - boat.calculateBearingToNextMarker().radians()) / Constants.KnotsToMMPerSecond;

        if (velocityToMark > 0) {

            //Calculate milliseconds until boat reaches mark.
            long timeFromNow = (long) (1000 * boat.calculateDistanceToNextMarker() / velocityToMark);

            //Calculate time at which it will reach mark.
            ZonedDateTime timeAtMark = this.getRaceClock().getCurrentTime().plus(timeFromNow, ChronoUnit.MILLIS);
            boat.setEstimatedTimeAtNextMark(timeAtMark);
        }

    }

    /**
     * Made public, so race logic can control it
     */
    public void setChanged() {
        super.setChanged();
    }

    public void addVelocityCommand(ObserverCommand c, int boatId) {
        this.activeObserverCommands.get(boatId).changeVelocityCommand(this, c);
    }

    public void addAngularCommand(ObserverCommand c, int boatId) {
        this.activeObserverCommands.get(boatId).changeAngularCommand(this, c);
    }
}
