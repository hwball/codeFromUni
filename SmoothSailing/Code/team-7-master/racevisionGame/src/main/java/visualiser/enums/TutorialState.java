package visualiser.enums;

import network.Messages.Enums.BoatActionEnum;

/**
 * State of which stage the tutorial is currently in
 */
public enum TutorialState {

    /**
     * State for upwind in tutorial
     */
    UPWIND(BoatActionEnum.UPWIND),

    /**
     * State for downwind in tutorial
     */
    DOWNWIND(BoatActionEnum.DOWNWIND),

    /**
     * State for tacking/gybing in tutorial
     */
    TACKGYBE(BoatActionEnum.TACK_GYBE),

    /**
     * State for vmg in tutorial
     */
    VMG(BoatActionEnum.VMG),

    /**
     * State for sails-in in tutorial
     */
    SAILSIN(BoatActionEnum.SAILS_IN),

    /**
     * State for sails-out in tutorial
     */
    SAILSOUT(BoatActionEnum.SAILS_OUT),

    /**
     * State for zoom-in in tutorial
     */
    ZOOMIN(null),

    /**
     * State for zoom-out in tutorial
     */
    ZOOMOUT(null);

    private BoatActionEnum action;

    TutorialState(BoatActionEnum action){
        this.action = action;
    }

    public BoatActionEnum getAction(){
        return action;
    }

}
