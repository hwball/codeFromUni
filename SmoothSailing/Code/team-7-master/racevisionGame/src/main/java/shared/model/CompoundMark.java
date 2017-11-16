package shared.model;


import shared.xml.Race.XMLCompoundMark;

import shared.enums.RoundingType;

/**
 * Represents a compound mark -  that is, either one or two individual marks which form a single compound mark.
 */
public class CompoundMark extends XMLCompoundMark{

    /**
     * The ID of the compound mark.
     */
    private int id;

    /**
     * The name of the compound mark.
     */
    private String name;

    /**
     * The first mark in the compound mark.
     */
    private Mark mark1;

    /**
     * The second mark in the compound mark.
     */
    private Mark mark2;

    /**
     * The average coordinate of the compound mark.
     */
    private GPSCoordinate averageGPSCoordinate;

    /**
     * The side that the mark must be rounded on
     */
    private RoundingType roundingType;


    /**
     * Constructs a compound mark from a single mark.
     * @param id the id of the compound mark
     * @param name name of the compound mark
     * @param mark1 The individual mark that comprises this compound mark.
     */
    public CompoundMark(int id, String name, Mark mark1) {
        this(id, name, mark1, null);
    }


    /**
     * Constructs a compound mark from a pair of marks.
     * @param id the id of the compound mark
     * @param name name of the compound mark
     * @param mark1 The first individual mark that comprises this compound mark.
     * @param mark2 The second individual mark that comprises this compound mark.
     */
    public CompoundMark(int id, String name, Mark mark1, Mark mark2) {
        //parent set up
        super();
        setName(name);
        setCompoundMarkID(id);
        /*TODO need to talk to connor about this as Mark should be extending XMLMark
        getMark().add(mark1);
        if (mark2 != null) getMark().add(mark2);*/

        this.id = id;
        this.name = name;
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.averageGPSCoordinate = calculateAverage();

    }


    /**
     * Returns the ID of this compound mark.
     * @return The ID of this compound mark.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the first mark of the compound mark.
     * @return The first mark of the compound mark.
     */
    public Mark getMark1() {
        return mark1;
    }

    /**
     * Returns the second mark of the compound mark.
     * @return The second mark of the compound mark.
     */
    public Mark getMark2() {
        return mark2;
    }


    /**
     * Returns the position of the first mark in the compound mark.
     * @return The position of the first mark in the compound mark.
     */
    public GPSCoordinate getMark1Position() {
        return mark1.getPosition();
    }

    /**
     * Returns the position of the second mark in the compound mark.
     * @return The position of the second mark in the compound mark.
     */
    public GPSCoordinate getMark2Position() {
        return mark2 == null? mark1.getPosition() : mark2.getPosition();
    }


    /**
     * Returns the average coordinate of the compound mark.
     * @return The average coordinate of the compound mark.
     */
    public GPSCoordinate getAverageGPSCoordinate() {
        return averageGPSCoordinate;
    }


    /**
     * Calculates the average coordinate of the compound mark.
     * @return The average coordinate of the compound mark.
     */
    private GPSCoordinate calculateAverage() {
        //Otherwise, calculate the average of both marks.
        GPSCoordinate averageCoordinate = GPSCoordinate.calculateAverageCoordinate(this.getMark1Position(), this.getMark2Position());

        return averageCoordinate;

    }

    /**
     * Used to find how far apart the marks that make up this gate are
     * If this compound mark is only one point return base length of 400m
     * @return the acceptable distance to round a mark
     */
    public double getRoundingDistance(){
        if (mark2 != null){
            return GPSCoordinate.calculateDistanceMeters(mark1.getPosition(), mark2.getPosition());
        }else{
            return 400;
        }
    }

    /**
     * Used to get how this mark should be rounded
     * @return rounding type for mark
     */
    public RoundingType getRoundingType() {
        return roundingType;
    }

    /**
     * Used to set the type of rounding for this mark
     * @param roundingType rounding type to set
     */
    public void setRoundingType(RoundingType roundingType) {
        this.roundingType = roundingType;
    }

    /**
     * Used to find the mark that is to be rounded at a gate when approaching from the south
     * will also give the single mark if there is only one
     * @param bearing the bearing a boat will approach form
     * @return the mark to round
     */
    public Mark getMarkForRounding(Bearing bearing){
        Mark westMostMark;
        Mark eastMostMark;
        Mark northMostMark;
        Mark southMostMark;

        //check to see if there are two marks
        if (mark2 == null){
            return mark1;
        }

        //finds the mark furthest west and east
        if(this.getMark1Position().getLongitude() < this.getMark2Position().getLongitude()){
            westMostMark = this.mark1;
            eastMostMark = this.mark2;
        }else{
            westMostMark = this.mark2;
            eastMostMark = this.mark1;
        }

        //finds the mark furthest north and south
        if(this.getMark1Position().getLatitude() > this.getMark2Position().getLatitude()){
            northMostMark = this.mark1;
            southMostMark = this.mark2;
        }else{
            northMostMark = this.mark2;
            southMostMark = this.mark1;
        }

        if (bearing.degrees() > 315 || bearing.degrees() <= 45){
            //north
            switch (this.getRoundingType()){
                case SP:
                case Port:
                    return westMostMark;
                case PS:
                case Starboard:
                    return eastMostMark;
                default:return null;
            }
        }else if(bearing.degrees() > 45 && bearing.degrees() <= 135){
            //east
            switch (this.getRoundingType()){
                case SP:
                case Port:
                    return northMostMark;
                case PS:
                case Starboard:
                    return southMostMark;
                default:return null;
            }
        }else if(bearing.degrees() > 135 && bearing.degrees() <= 225){
            //south
            switch (this.getRoundingType()){
                case SP:
                case Port:
                    return eastMostMark;
                case PS:
                case Starboard:
                    return westMostMark;
                default:return null;
            }
        }else if(bearing.degrees() > 225 && bearing.degrees() <= 315){
            //west
            switch (this.getRoundingType()){
                case SP:
                case Port:
                    return southMostMark;
                case PS:
                case Starboard:
                    return northMostMark;
                default:return null;
            }
        }else{
            return null;
        }

    }


}
