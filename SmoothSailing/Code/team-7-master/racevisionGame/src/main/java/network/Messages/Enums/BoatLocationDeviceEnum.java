package network.Messages.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Various device sources for a BoatLocation message.
 */
public enum BoatLocationDeviceEnum {


    NOT_A_DEVICE(-1),


    Unknown(0),

    /**
     * A yacht particpating in the race.
     */
    RacingYacht(1),

    CommitteeBoat(2),

    /**
     * A marker boat.
     */
    Mark(3),

    Pin(4),

    ChaseBoat(5),

    MedicalBoat(6),

    MarshallBoat(7),

    UmpireBoat(8),

    UmpireSoftwareApplication(9),

    PrincipalRaceOfficerApplication(10),

    WeatherStation(11),

    Helicopter(12),

    DataProcessingApplication(13);


    /**
     * Value of the enum.
     */
    private byte value;

    /**
     * Creates a BoatLocationDeviceEnum from a given primitive integer value, cast to a byte.
     * @param value Integer, which is cast to byte, to construct from.
     */
    private BoatLocationDeviceEnum(int value) {
        this.value = (byte) value;
    }

    /**
     * Returns the primitive value of the enum.
     * @return Primitive value of the enum.
     */
    public byte getValue() {
        return value;
    }


    /**
     * Stores a mapping between Byte values and BoatLocationDeviceEnum values.
     */
    private static final Map<Byte, BoatLocationDeviceEnum> byteToDeviceMap = new HashMap<>();


    /*
      Static initialization block. Initializes the byteToDeviceMap.
     */
    static {
        for (BoatLocationDeviceEnum type : BoatLocationDeviceEnum.values()) {
            BoatLocationDeviceEnum.byteToDeviceMap.put(type.value, type);
        }
    }


    /**
     * Returns the enumeration value which corresponds to a given byte value.
     * @param deviceValue Byte value to convert to a BoatLocationDeviceEnum value.
     * @return The BoatLocationDeviceEnum value which corresponds to the given byte value.
     */
    public static BoatLocationDeviceEnum fromByte(byte deviceValue) {
        //Gets the corresponding BoatLocationDeviceEnum from the map.
        BoatLocationDeviceEnum type = BoatLocationDeviceEnum.byteToDeviceMap.get(deviceValue);

        if (type == null) {
            //If the byte value wasn't found, return the NOT_A_DEVICE BoatLocationDeviceEnum.
            return BoatLocationDeviceEnum.NOT_A_DEVICE;
        } else {
            //Otherwise, return the BoatLocationDeviceEnum.
            return type;
        }

    }

}
