package network.Messages.Enums;

/**
 * Yacht event codes
 */
public enum YachtEventEnum {
    NOT_AN_EVENT(-1),
    COLLISION(1);

    private byte value;

    YachtEventEnum(int value) { this.value = (byte)value; }

    public byte getValue() {
        return value;
    }

    public static YachtEventEnum fromByte(byte value) {
        switch(value) {
            case 1: return COLLISION;
            default: return NOT_AN_EVENT;
        }
    }
}
