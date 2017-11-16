package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * Key to zoom out of the game.
 */
public class ZoomOutKey extends ControlKey{

    /**
     * Constructor for Control
     */
    public ZoomOutKey() {
        super("Zoom Out", BoatActionEnum.ZOOM_OUT);
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
