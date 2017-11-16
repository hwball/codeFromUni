package shared.enums;

/**
 * Enum for the types of rounding that can be done
 */
public enum RoundingType {
    /**
     * This is means it must be rounded port side
     */
    Port,

    /**
     * This is means it must be rounded starboard side
     */
    Starboard,

    /**
     * The boat within the compound mark with the SeqID
     * of 1 should be rounded to starboard and the boat
     * within the compound mark with the SeqID of 2 should
     * be rounded to port.
     */
    SP,

    /**
     * The boat within the compound mark with the SeqID
     * of 1 should be rounded to port and the boat
     * within the compound mark with the SeqID of 2 should
     * be rounded to starboard.
     *
     * opposite of SP
     */
    PS;

    public static RoundingType getValueOf(String value) {
        switch (value) {
            case "Port":
                return RoundingType.Port;
            case "Starboard":
                return RoundingType.Starboard;
            case "SP":
                return RoundingType.Port;
            case "PS":
                return RoundingType.Starboard;
            default:
                return null;
        }
    }

    public static String getStringOf(RoundingType value) {
        switch (value) {
            case Port:
                return "Port";
            case Starboard:
                return "Starboard";
            case SP:
                return "SP";
            case PS:
                return "PS";
            default:
                return null;
        }
    }
}
