package shared.model;

import java.util.*;

import static shared.enums.RoundingType.*;

/**
 * This class contains a sequence of points that describe the mark rounding order for a course.
 */
public class MarkRoundingSequence {


    /**
     * Legs in the race.
     */
    private List<Leg> legs;

    /**
     * For each leg, mark rounding information.
     * Maps between leg number and rounding data.
     */
    private Map<Integer, MarkRoundingData> roundingPoints;



    public MarkRoundingSequence(List<Leg> legs) {
        this.legs = legs;
        generateRoundingPoints();
    }


    /**
     * Returns the rounding points for a given leg.
     * @param leg Leg to check.
     * @return Rounding points for leg.
     */
    public MarkRoundingData getRoundingData(Leg leg) {
        return roundingPoints.get(leg.getLegNumber());
    }


    /**
     * Generates the rounding points for all legs in the race.
     */
    private void generateRoundingPoints() {
        this.roundingPoints = new HashMap<>(this.legs.size());

        for (int i = 0; i < this.legs.size(); i++) {
            Leg currentLeg = this.legs.get(i);

            Optional<Leg> nextLeg = Optional.empty();
            if (i < legs.size() - 1) {
                nextLeg = Optional.of(this.legs.get(i + 1));
            }

            generateRoundingPoint(currentLeg, nextLeg);
        }

    }


    /**
     * Generates the rounding points for a specific leg.
     * @param currentLeg The leg to generate rounding points for.
     * @param nextLeg The following leg, used to help generate rounding points. Final leg of race doesn't have a following leg.
     */
    private void generateRoundingPoint(Leg currentLeg, Optional<Leg> nextLeg) {

        Bearing bearingToAddFirstPoint = calculateBearingToAdd(currentLeg);

        GPSCoordinate startCoord = currentLeg.getStartCompoundMark().getAverageGPSCoordinate();
        GPSCoordinate endCoord = currentLeg.getEndCompoundMark().getAverageGPSCoordinate();
        Bearing legBearing = GPSCoordinate.calculateBearing(startCoord, endCoord);
        Bearing nextBearing = legBearing;

        Mark markToRound = currentLeg.getEndCompoundMark().getMarkForRounding(legBearing);

        GPSCoordinate roundCheck1;
        if (currentLeg.getEndCompoundMark().getMark2() == null) {
            //End is a single mark.
            roundCheck1 = calculateRoundingCheckPoint(
                    currentLeg,
                    markToRound,
                    legBearing,
                    bearingToAddFirstPoint);
        } else {
            //End is a gate.
            if (markToRound == currentLeg.getEndCompoundMark().getMark1()) {
                roundCheck1 = currentLeg.getEndCompoundMark().getMark2().getPosition();
            } else {
                roundCheck1 = currentLeg.getEndCompoundMark().getMark1().getPosition();
            }
        }

        //TODO the halfway points currently haven't been done properly.

        GPSCoordinate roundCheck1Halfway = calculateRoundingCheckPoint(
                currentLeg,
                markToRound,
                legBearing,
                bearingToAddFirstPoint);


        GPSCoordinate roundCheck2 = roundCheck1;
        GPSCoordinate roundCheck2Halfway = roundCheck1Halfway;
        if (nextLeg.isPresent()) {

            Bearing bearingToAddSecondPoint = bearingToAddFirstPoint;//calculateBearingToAdd(nextLeg.get());

            GPSCoordinate startCoord2 = nextLeg.get().getStartCompoundMark().getAverageGPSCoordinate();
            GPSCoordinate endCoord2 = nextLeg.get().getEndCompoundMark().getAverageGPSCoordinate();
            nextBearing = GPSCoordinate.calculateBearing(startCoord2, endCoord2);

            roundCheck2 = calculateRoundingCheckPoint(
                    currentLeg,
                    markToRound,
                    nextBearing,
                    bearingToAddSecondPoint);

            roundCheck2Halfway = calculateRoundingCheckPoint(
                    currentLeg,
                    markToRound,
                    nextBearing,
                    bearingToAddSecondPoint);
        }


        MarkRoundingData roundingData = new MarkRoundingData();
        roundingData.setLeg(currentLeg);

        roundingData.setLegBearing(legBearing);
        roundingData.setNextLegBearing(nextBearing);

        roundingData.setMarkToRound(markToRound);

        roundingData.setRoundCheck1(roundCheck1);
        roundingData.setRoundCheck1Halfway(roundCheck1Halfway);

        roundingData.setRoundCheck2(roundCheck2);
        roundingData.setRoundCheck2Halfway(roundCheck2Halfway);


        this.roundingPoints.put(currentLeg.getLegNumber(), roundingData);


        //Rounding points:

        //each mark/gate has a specific mark to round. Call this ROUNDINGMARK
        //  with a mark, it is the mark
        //  with a gate, it depends if it is a starboard or port gate.
        //  it is the mark that allows the boat to enter between both marks of the gate, whilst obeying the starboard/port requirement.

        //let the bearing between start of leg and end of leg be called LEGBEARING

        //the first rounding point is ROUNDINGDISTANCE units away from the ROUNDINGMARK, on an angle perpendicular to LEGBEARING.
        // the angle means that the rounding mark is at the center of a gate, for gates.

        //the second rounding point is the same as the first, except LEGBEARING is the bearing between end of current leg, and start of next leg.
    }



    /**
     * Calculates the location of the rounding check point, which together with the mark to round, forms a line that the boat must cross to round the mark.
     * @param leg Leg of race to check.
     * @param markToRound Mark at end of leg to round.
     * @param legBearing The bearing of the nearest leg. For the first rounding point this is the leg's bearing, for the second rounding point it is the next leg's bearing.
     * @param bearingToAdd The bearing to add to the leg bearing to get a perpendicular bearing.
     * @return The location of the rounding point, which together with the mark to round forms a line the boat must cross.
     */
    private GPSCoordinate calculateRoundingCheckPoint(Leg leg, Mark markToRound, Bearing legBearing, Bearing bearingToAdd) {


        double roundingDistanceMeters = leg.getEndCompoundMark().getRoundingDistance();


        //We project from rounding mark to get the second point which forms the line the boat must cross.
        /*
               c2
               |
               |
               r------c1
                  b
         */
        GPSCoordinate roundCheck = GPSCoordinate.calculateNewPosition(
                markToRound.getPosition(),
                roundingDistanceMeters,
                Azimuth.fromDegrees(legBearing.degrees() + bearingToAdd.degrees())    );

        return roundCheck;
    }


    /**
     * Calculates the bearing that must be added to a leg's bearing to calculate a perpendicular bearing, used for finding rounding points.
     * @param leg Leg to check.
     * @return Bearing to add. Will be either +90 or -90.
     */
    private Bearing calculateBearingToAdd(Leg leg) {

        if (leg.getEndCompoundMark().getRoundingType() == Port ||
                leg.getEndCompoundMark().getRoundingType() == SP) {
            return Bearing.fromDegrees(90);
        } else {
            return Bearing.fromDegrees(-90);
        }

    }


}
