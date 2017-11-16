package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;
import visualiser.model.ThisBoat;

/**
 * Key to toggle the sails
 */
public class SailsToggleKey extends ControlKey {

    /**
     * Constructor for Control
     */
    public SailsToggleKey() {
        super("Toggle Sails", BoatActionEnum.TOGGLE_SAILS);
    }

    /**
     * Toggle command associated with sails key
     */
    @Override
    public void onAction() {
        if(ThisBoat.getInstance().isSailsOut()) {
            protocolCode = BoatActionEnum.SAILS_IN;
            ThisBoat.getInstance().setSailsOut(false);
        } else {
            protocolCode = BoatActionEnum.SAILS_OUT;
            ThisBoat.getInstance().setSailsOut(true);
        }
    }

    @Override
    public void onHold() {

    }

    @Override
    public void onRelease() {

    }
}
