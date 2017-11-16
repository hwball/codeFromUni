package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * Key to trigger auto VMG
 */
public class VMGKey extends ControlKey{

    /**
     * Constructor for Control
     */
    public VMGKey() {
        super("VMG", BoatActionEnum.VMG);
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
