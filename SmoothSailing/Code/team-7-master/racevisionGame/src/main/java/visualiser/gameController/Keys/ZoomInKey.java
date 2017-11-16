package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * key to zoom into the game
 */
public class ZoomInKey extends ControlKey {

    public ZoomInKey() {
        super("Zoom In", BoatActionEnum.ZOOM_IN);
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
