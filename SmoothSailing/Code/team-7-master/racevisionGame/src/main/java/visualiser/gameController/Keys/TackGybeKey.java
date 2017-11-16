package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * key to toggle between tacking and gybing
 */
public class TackGybeKey extends ControlKey {

    /**
     * Constructor for Control
     */
    public TackGybeKey() {
        super("Tack/Gybe", BoatActionEnum.TACK_GYBE);
    }

    @Override
    public void onAction() {

    }

    @Override
    public void onHold() {

    }

    @Override
    public void onRelease() {

    }
}
