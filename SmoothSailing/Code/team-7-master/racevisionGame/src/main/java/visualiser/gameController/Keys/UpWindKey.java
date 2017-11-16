package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * Key to go upwind
 */
public class UpWindKey extends ControlKey {

    /**
     * Constructor for Control
     */
    public UpWindKey() {
        super("Upwind", BoatActionEnum.UPWIND);
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
