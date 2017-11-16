package shared.model;



/**
 * Contains data related to mark rounding for a specific leg.
 */
public class MarkRoundingData {

    /**
     * The leg this relates to.
     */
    private Leg leg;

    /**
     * The mark that should be rounded.
     */
    private Mark markToRound;

    /**
     * The bearing of the leg.
     */
    private Bearing legBearing;

    /**
     * The bearing of the next leg.
     */
    private Bearing nextLegBearing;

    /**
     * The location of the first rounding check point.
     */
    private GPSCoordinate roundCheck1;

    /**
     * The location of the second rounding check point.
     */
    private GPSCoordinate roundCheck2;

    /**
     * A halfway point between mark to round and roundCheck1.
     */
    private GPSCoordinate roundCheck1Halfway;

    /**
     * A halfway point between mark to round and roundCheck2.
     */
    private GPSCoordinate roundCheck2Halfway;


    public MarkRoundingData() {
    }


    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public Mark getMarkToRound() {
        return markToRound;
    }

    public void setMarkToRound(Mark markToRound) {
        this.markToRound = markToRound;
    }

    public Bearing getLegBearing() {
        return legBearing;
    }

    public void setLegBearing(Bearing legBearing) {
        this.legBearing = legBearing;
    }

    public Bearing getNextLegBearing() {
        return nextLegBearing;
    }

    public void setNextLegBearing(Bearing nextLegBearing) {
        this.nextLegBearing = nextLegBearing;
    }

    public GPSCoordinate getRoundCheck1() {
        return roundCheck1;
    }

    public void setRoundCheck1(GPSCoordinate roundCheck1) {
        this.roundCheck1 = roundCheck1;
    }

    public GPSCoordinate getRoundCheck2() {
        return roundCheck2;
    }

    public void setRoundCheck2(GPSCoordinate roundCheck2) {
        this.roundCheck2 = roundCheck2;
    }

    public GPSCoordinate getRoundCheck1Halfway() {
        return roundCheck1Halfway;
    }

    public void setRoundCheck1Halfway(GPSCoordinate roundCheck1Halfway) {
        this.roundCheck1Halfway = roundCheck1Halfway;
    }

    public GPSCoordinate getRoundCheck2Halfway() {
        return roundCheck2Halfway;
    }

    public void setRoundCheck2Halfway(GPSCoordinate roundCheck2Halfway) {
        this.roundCheck2Halfway = roundCheck2Halfway;
    }
}
