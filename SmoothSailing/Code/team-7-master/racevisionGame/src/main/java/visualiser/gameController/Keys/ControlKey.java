package visualiser.gameController.Keys;

import network.Messages.Enums.BoatActionEnum;

/**
 * Key for the controller, part of the abstract factory KeyFactory
 */
public abstract class ControlKey {

    private String name;
    protected BoatActionEnum protocolCode;

    /**
     * Constructor for key state with specified protocol code
     * @param name of action
     * @param protocolCode NOT_A_STATUS if not sent
     */
    public ControlKey(String name, BoatActionEnum protocolCode) {
        this.name = name;
        this.protocolCode = protocolCode;
    }

    /**
     * Constructor for key state not sent over network
     * @param name name of the key
     */
    public ControlKey(String name){
        this.name = name;
        this.protocolCode = BoatActionEnum.NOT_A_STATUS;
    }

    public BoatActionEnum getProtocolCode() {
        return protocolCode;
    }

    /**
     * To String method
     * @return returns the name of the key
     */
    public String toString(){
        return this.name;
    }

    /**
     * What this key should do when the command is issued for it to do its job.
     */
    public abstract void onAction();

    /**
     * What to do when the key is held
     */
    public abstract void onHold();

    /**
     * What to do when the key is released.
     */
    public abstract void onRelease();
}
