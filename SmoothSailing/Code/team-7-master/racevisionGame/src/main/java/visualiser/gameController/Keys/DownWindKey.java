package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * Key to send downwind packet to server
 */
public class DownWindKey extends ControlKey {

    /**
     * Constructor for Control
     */
    public DownWindKey() {
        super("Downwind", BoatActionEnum.DOWNWIND);
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
