package mock.model;

import shared.model.*;


/**
 * Represents a Boat on the mock side of a race.
 * This adds mock specific functionality to a boat.
 */
public class MockBoat extends Boat {


    /**
    * This stores a boat's polars table.
    * Can be used to calculate VMG.
    */
    private Polars polars;

    /**
    * This stores the milliseconds since the boat has changed its tack, to allow for only updating the tack every X milliseconds.
    */
    private long timeSinceTackChange = 0;

    /**
     * This stores the boats current status of rounding a mark
     * 0: not started rounding
     * 1: passed only first check
     * 2: passed first and second check
     */
    private int roundingStatus = 0;

    /**
     * Stores whether the boat is on autoVMG or not
     */
    private boolean autoVMG = false;

    /**
     * Indicates whether boat velocity is determined by wind
     */
    private boolean velocityDefault = true;

    /**
     * Constructs a boat object with a given sourceID, name, country/team abbreviation, and polars table.
     *
     * @param sourceID The id of the boat
     * @param name The name of the Boat.
     * @param country The abbreviation or country code for the boat.
     * @param polars The polars table to use for this boat.
     */
    public MockBoat(int sourceID, String name, String country, Polars polars) {
        super(sourceID, name, country);

        this.polars = polars;
    }


    /**
     * Constructs a mock boat object from a given boat and polars table.
     *
     * @param boat The boat to convert into a MockBoat.
     * @param polars The polars table to use for this boat.
     */
    public MockBoat(Boat boat, Polars polars) {
        super(boat.getSourceID(), boat.getName(), boat.getCountry());

        this.polars = polars;
    }




    /**
     * Calculate the bearing of the boat to its next marker.
     * @return The bearing to the next marker.
     */
    public Bearing calculateBearingToNextMarker() {

        //Get the start and end points.
        GPSCoordinate currentPosition = this.getPosition();
        GPSCoordinate nextMarkerPosition;

        // if boat is at the finish
        if (this.getCurrentLeg().getEndCompoundMark() == null) {
            nextMarkerPosition = currentPosition;
        }
        else {
            nextMarkerPosition = this.getCurrentLeg().getEndCompoundMark().getAverageGPSCoordinate();
        }

        //Calculate bearing.
        Bearing bearing = GPSCoordinate.calculateBearing(currentPosition, nextMarkerPosition);
        return bearing;
    }



    /**
     * Calculates the distance between the boat and its target marker in nautical miles.
     * @return The distance (in nautical miles) between the boat and its target marker.
     */
    public double calculateDistanceToNextMarker() {

        //Get start and end markers.
        GPSCoordinate startPosition = this.getPosition();

        //When boats finish, their "current leg" doesn't have an end marker.
        if (this.getCurrentLeg().getEndCompoundMark() == null) {
            return 0d;
        }

        GPSCoordinate endMarker = this.getCurrentLeg().getEndCompoundMark().getAverageGPSCoordinate();


        //Calculate distance.
        return GPSCoordinate.calculateDistanceNauticalMiles(startPosition, endMarker);
    }




    /**
     * Returns the polars table for this boat.
     * @return The polars table for this boat.
     */
    public Polars getPolars() {
        return polars;
    }

    /**
     * Sets the polars table for this boat.
     * @param polars The new polars table for this boat.
     */
    public void setPolars(Polars polars) {
        this.polars = polars;
    }


    /**
     * Returns the time since the boat changed its tack, in milliseconds.
     * @return Time since the boat changed its tack, in milliseconds.
     */
    public long getTimeSinceTackChange() {
        return timeSinceTackChange;
    }

    /**
     * Sets the time since the boat changed it's tack, in milliseconds.
     * @param timeSinceTackChange Time since the boat changed its tack, in milliseconds.
     */
    public void setTimeSinceTackChange(long timeSinceTackChange) {
        this.timeSinceTackChange = timeSinceTackChange;
    }


    /**
     * Moves the boat meters forward in the direction that it is facing
     * @param meters The number of meters to move forward.
     *
     */
    public void moveForwards(double meters) {
        //Updates the current position of the boat.
        GPSCoordinate newPosition = GPSCoordinate.calculateNewPosition(this.getPosition(), meters, Azimuth.fromBearing(this.getBearing()));
        this.setPosition(newPosition);

    }


    /**
     * Sets the boats speed and bearing to those in the given VMG.
     * @param newVMG The new VMG to use for the boat - contains speed and bearing.
     */
    public void setVMG(VMG newVMG) {
        this.setBearing(newVMG.getBearing());
        this.setCurrentSpeed(newVMG.getSpeed());
        this.setTimeSinceTackChange(0);
    }


    /**
     * Calculates the number of nautical miles the boat will travel in a given time slice.
     * E.g., in 53 milliseconds a boat may travel 0.0002 nautical miles.
     * @param timeSlice The timeslice to use.
     * @return The distance travelled, in nautical miles, over the given timeslice.
     */
    public double calculateNauticalMilesTravelled(long timeSlice) {

        //The proportion of one hour the current timeslice is.
        //This will be a low fractional number, so we need to go from long -> double.
        double hourProportion = ((double) timeSlice) / Constants.OneHourMilliseconds;

        //Calculates the distance travelled, in nautical miles, in the current timeslice.
        //distanceTravelledNM = speed (nm p hr) * time taken to update loop
        double distanceTravelledNM = this.getCurrentSpeed() * hourProportion;

        return distanceTravelledNM;
    }

    /**
     * Calculates the number of meters the boat will travel in a given time slice.
     * E.g., in 53 milliseconds a boat may travel 0.02 meters.
     * @param timeSlice The timeslice to use.
     * @return The distance travelled, in meters, over the given timeslice.
     */
    public double calculateMetersTravelled(long timeSlice) {

        //Calculate the distance travelled, in nautical miles.
        double distanceTravelledNM = this.calculateNauticalMilesTravelled(timeSlice);

        //Convert to meters.
        double distanceTravelledMeters = distanceTravelledNM * Constants.NMToMetersConversion;

        return distanceTravelledMeters;
    }

    /**
     * Check if a mark is on the port side of the boat
     * @param mark mark to be passed
     * @return true if mark is on port side
     */
    public boolean isPortSide(Mark mark){
        Bearing towardsMark = GPSCoordinate.calculateBearing(this.getPosition(), mark.getPosition());
        if (towardsMark.degrees() > 315 || towardsMark.degrees() <= 45){
            //south quadrant
            return this.getBearing().degrees() <= 180;
        } else if(towardsMark.degrees() > 45 && towardsMark.degrees() <= 135){
            //west quadrant
            return (this.getBearing().degrees() <= 270 && this.getBearing().degrees() >= 90);
        }else if(towardsMark.degrees() > 135 && towardsMark.degrees() <= 225){
            //north quadrant
            return this.getBearing().degrees() >= 180;
        }else if(towardsMark.degrees() > 225 && towardsMark.degrees() <= 315){
            //east quadrant
            return (this.getBearing().degrees() <= 90 || this.getBearing().degrees() >= 270);
        }else{
            //should not reach here
            return false;
        }
    }

    /**
     * Check if a mark is on the starboard side of the boat
     * @param mark mark to be passed
     * @return true if mark is on starboard side
     */
    public boolean isStarboardSide(Mark mark){
        //if this boat is lower than the mark check which way it is facing
        Bearing towardsMark = GPSCoordinate.calculateBearing(this.getPosition(), mark.getPosition());
        if (towardsMark.degrees() > 315 || towardsMark.degrees() <= 45){
            //south quadrant
            return !(this.getBearing().degrees() <= 180);
        } else if(towardsMark.degrees() > 45 && towardsMark.degrees() <= 135){
            //west quadrant
            return !(this.getBearing().degrees() <= 270 && this.getBearing().degrees() >= 90);
        }else if(towardsMark.degrees() > 135 && towardsMark.degrees() <= 225){
            //north quadrant
            return !(this.getBearing().degrees() >= 180);
        }else if(towardsMark.degrees() > 225 && towardsMark.degrees() <= 315){
            //east quadrant
            return !(this.getBearing().degrees() <= 90 || this.getBearing().degrees() >= 270);
        }else{
            //should not reach here
            return false;
        }
    }

    /**
     * Used to check if this boat is between a gate
     * @param gate the gate to be checked
     * @return true if the boat is between two marks that make up a gate
     */
    public boolean isBetweenGate(CompoundMark gate){
        return (this.isPortSide(gate.getMark1()) && this.isStarboardSide(gate.getMark2())) ||
                (this.isStarboardSide(gate.getMark1()) && this.isPortSide(gate.getMark2()));
    }

    /**
     * Used to check if this boat is between a two marks
     * @param mark1 the first mark
     * @param mark2 the second mark
     * @return true if the boat is between two marks
     */
    public boolean isBetweenGate(Mark mark1, Mark mark2){
        return (this.isPortSide(mark1) && this.isStarboardSide(mark2)) ||
                (this.isStarboardSide(mark1) && this.isPortSide(mark2));
    }

    public int getRoundingStatus() {
        return roundingStatus;
    }

    public void increaseRoundingStatus() {
        this.roundingStatus++;
    }

    public void resetRoundingStatus() {
        this.roundingStatus = 0;
    }

    public boolean getAutoVMG(){
        return autoVMG;
    }

    public void setAutoVMG(boolean autoVMG) {
        this.autoVMG = autoVMG;
    }

    public boolean isVelocityDefault() {
        return velocityDefault;
    }

    public void setVelocityDefault(boolean velocityDefault) {
        this.velocityDefault = velocityDefault;
    }
}
