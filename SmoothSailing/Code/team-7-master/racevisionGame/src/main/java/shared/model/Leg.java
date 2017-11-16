package shared.model;


/**
 * Leg of the race, this is what each part of the race is divided into, from mark to mark.
 */
public class Leg {

    /**
     * The name of the leg.
     */
    private String name;

    /**
     * The distance of the leg, in nautical miles.
     */
    private double distanceNauticalMiles;

    /**
     * The starting marker of the leg.
     */
    private CompoundMark startCompoundMark;

    /**
     * The ending marking of the leg.
     */
    private CompoundMark endCompoundMark;

    /**
     * The leg number within a race.
     */
    private int legNumber;



    /**
     * Constructs a leg from a name, start marker, end marker, and leg number.
     *
     * @param name Name of the Leg.
     * @param start Starting marker of the leg.
     * @param end Ending marker of the leg.
     * @param number Leg's position within the race.
     */
    public Leg(String name, CompoundMark start, CompoundMark end, int number) {
        this.name = name;
        this.startCompoundMark = start;
        this.endCompoundMark = end;
        this.legNumber = number;
        this.calculateLegDistance();
    }


    /**
     * Constructs a leg from a name and leg number.
     * This is currently used for constructing "dummy" DNF and Finish legs.
     *
     * @param name Name of the leg.
     * @param number Leg's position within the race.
     */
    public Leg(String name, int number) {
        this.name = name;
        this.legNumber = number;
    }


    /**
     * Returns the name of the Leg.
     * @return The name of the Leg.
     */
    public String getName() {
        return name;
    }


    /**
     * Get the distance in nautical miles.
     * @return The total distance of the leg.
     */
    public double getDistanceNauticalMiles() {
        return distanceNauticalMiles;
    }


    /**
     * Returns the leg number of the leg within a race.
     * @return The leg number of the leg within a race
     */
    public int getLegNumber() {
        return legNumber;
    }


    /**
     * Returns the starting marker of the leg.
     * @return The starting marker of the leg.
     */
    public CompoundMark getStartCompoundMark() {
        return startCompoundMark;
    }


    /**
     * Returns the ending marker of the leg.
     * @return The ending marker of the leg.
     */
    public CompoundMark getEndCompoundMark() {
        return endCompoundMark;
    }



    /**
     * Calculates the distance of the leg, in nautical miles.
     */
    public void calculateLegDistance() {

        //Gets the start and end coordinates.
        GPSCoordinate startMarker = this.startCompoundMark.getAverageGPSCoordinate();
        GPSCoordinate endMarker = this.endCompoundMark.getAverageGPSCoordinate();

        //Calculates the distance between markers.
        double distanceNauticalMiles = GPSCoordinate.calculateDistanceNauticalMiles(startMarker, endMarker);

        this.distanceNauticalMiles = distanceNauticalMiles;
    }


}
