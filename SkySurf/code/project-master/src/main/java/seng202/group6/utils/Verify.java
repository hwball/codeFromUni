package seng202.group6.utils;

/**
 * Contains methods used to check that text inputs match the required formats for model class
 * properties.
 */
public class Verify {

    /**
     * Checks that the given name matches the requirements for names.
     * <p>Used to check airline name, alias, call sign and country;
     * airport name, city and country; and flight point id.<p>Currently always returns true.
     * Included for completeness and for maintainability against the possibility of constraints
     * being placed on names.
     *
     * @param string the name to check
     * @return true
     */
    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    public static boolean validateName(String string) {
        return true;
    }


    /**
     * Checks that the given string is either empty or a valid airport IATA code. International Air
     * Transport Association codes for airports must be three alphanumeric characters long.
     *
     * @param string the string to check
     * @return true if the string is valid or empty else false
     */
    public static boolean validateAirportIATA(String string) {
        boolean valid = string.length() == 3 || string.isEmpty();
        return valid && validateIATAICAO(string);
    }


    /**
     * Checks that the given string is either empty or a valid airport ICAO code. International
     * Civil Aviation Authority codes must be four alphanumeric characters long.
     *
     * @param string the string to check
     * @return true if the string is valid or empty else false
     */
    public static boolean validateAirportICAO(String string) {
        boolean valid = string.length() == 4 || string.isEmpty();
        return valid && validateIATAICAO(string);
    }


    /**
     * Checks that the given string is a valid representation of a latitude. That is, it represents
     * an decimal between positive and negative 90.
     *
     * @param string the string to check
     * @return true if the string represents a latitude, else false
     */
    public static boolean validateLatitude(String string) {
        if (string.length() > 0) {
            try {
                return validateLatitude(Double.parseDouble(string));
            } catch (NumberFormatException e) {
                //
            }
        }
        return false;
    }


    /**
     * Checks that the given string is a valid representation of a longitude. That is, it represents
     * an decimal between positive and negative 180.
     *
     * @param string the string to check
     * @return true if the string represents a longitude, else false
     */
    public static boolean validateLongitude(String string) {
        if (string.length() > 0) {
            try {
                return validateLongitude(Double.parseDouble(string));
            } catch (NumberFormatException e) {
                //
            }
        }
        return false;
    }


    /**
     * Checks that the given double is a valid latitude, that is, it is between positive and
     * negative 90.
     *
     * @param latitude the double to check
     * @return true if the double is a valid latitude, else false
     */
    public static boolean validateLatitude(double latitude) {
        return -90.0 <= latitude && latitude <= 90.0;
    }


    /**
     * Checks that the given double is a valid longitude, that is, it is between positive and
     * negative 180.
     *
     * @param longitude the double to check
     * @return true if the double is a valid longitude, else false
     */
    public static boolean validateLongitude(double longitude) {
        return -180.0 <= longitude && longitude <= 180.0;
    }


    /**
     * Checks that the given string can be parsed as an integer.
     *
     * @param string the string to check
     * @return true if the string can be parsed as an integer, else false
     * @see java.lang.Integer#parseInt(String)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean validateInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Checks that the given string can be parsed as a double.
     *
     * @param string the string to check
     * @return true if the string can be parsed, else false
     * @see java.lang.Double#parseDouble(String)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean validateDecimal(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Checks that the string represents a daylight savings time indicator. That is, that the string
     * is one of {@code "E"} (Europe), {@code "A"} (US/Canada), {@code "S"} (South America), {@code
     * "O"} (Australia), {@code "Z"} (New Zealand), {@code "N"} (None) or {@code "U"} (Unknown)
     *
     * @param string the string to check
     * @return true if the string is a valid daylight savings time flag, else false
     */
    public static boolean validateDaylightSavingTime(String string) {
        return string.length() == 1 && (string.equals("E") || string.equals("A") ||
                string.equals("S") || string.equals("O") || string.equals("Z") ||
                string.equals("N") || string.equals("U"));
    }


    /**
     * Checks that the string represents a valid timezone database, in olson format. <p>Checks that
     * the string contains only alphanumeric characters, forward slash, hyphen and underscores.
     *
     * @param string the string to check
     * @return true if the string is valid, else false
     * @see <a href="https://en.wikipedia.org/wiki/Tz_database#Names_of_time_zones"
     * target="_blank">Wikipedia: tz database</a>
     */
    public static boolean validateTimezoneDB(String string) {
        for (char c : string.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || c == '/' || c == '_' || c == '-')) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks that the given string is a valid flight point type. That is, it is one of
     * <p>{@code "UKN"} - Unknown
     * <p>{@code "APT"} - Airport
     * <p>{@code "NDB"} - Non-directional beacon (NDB)
     * <p>{@code "VOR"} - VHF omni-directional range (VOR)
     * <p>{@code "FIX"} - Navigational fix
     * <p>{@code "DME"} - Distance measuring equipment
     * <p>{@code "LATLON"} - Latitude/Longitude point
     *
     * @param string the string to check
     * @return true if the string is valid, else false
     * @see <a href="https://flightplandatabase.com/dev/api#route" target="_blank">Flight Plan
     * Database: API specs</a>
     */
    public static boolean validateFlightPointType(String string) {
        return string.equals("UKN") || string.equals("APT") || string.equals("NDB") ||
                string.equals("VOR") || string.equals("FIX") || string.equals("DME") ||
                string.equals("LATLON");
    }


    /**
     * Checks that the given string is either empty or a valid airline IATA code. International Air
     * Transport Association codes for airlines must be two alphanumeric characters long.
     *
     * @param string the string to check
     * @return true if the string is valid or empty else false
     */
    public static boolean validateAirlineIATA(String string) {
        boolean valid = string.length() == 2 || string.isEmpty();
        return valid && validateIATAICAO(string);
    }


    /**
     * Checks that the given string is either empty or a valid airline ICAO code. International
     * Civil Aviation Organisation codes for airlines must be three alphanumeric characters long.
     *
     * @param string the string to check
     * @return true if the string is valid or empty else false
     */
    public static boolean validateAirlineICAO(String string) {
        boolean valid = string.length() == 3 || string.isEmpty();
        return valid && validateIATAICAO(string);
    }


    /**
     * Checks that the given string represents yes or no. Yes is represented by {@code "Y"}, and no
     * by {@code "N"} or an empty string.
     *
     * @param string the string to check
     * @return true if the string is valid or empty else false
     */
    public static boolean validateYesNo(String string) {
        return string.isEmpty() || string.equalsIgnoreCase("Y") || string.equalsIgnoreCase("N");
    }


    /**
     * Checks that the given string is a sequence of space separated equipment codes or is empty.
     *
     * @param string the string to check
     * @return true if the string is valid else false
     */
    public static boolean validateRouteEquipmentList(String string) {
        if (string.isEmpty()) {
            return true;
        }
        for (String sub : string.split(" +")) {
            if (!validateRouteEquipment(sub)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks that the given string is a valid timezone.
     *
     * @param string the string to check
     * @return true if the string is valid, else false
     */
    public static boolean validateTimezone(String string) {
        if (validateDecimal(string)) {
            if ((Double.parseDouble(string) <= 14) && (Double.parseDouble(string) >= -14)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks that the given string is a valid three character alpha-numeric equipment code.
     *
     * @param string the string to check
     * @return true if the string is valid, else false
     */
    private static boolean validateRouteEquipment(String string) {
        if (string.length() == 3) {
            for (char c : string.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * Checks that the given string is suitable for use a an IATA or ICAO code, that is, it is
     * alphanumeric.
     *
     * @param string the string to check
     * @return true if the string is valid, else false
     */
    private static boolean validateIATAICAO(String string) {
        for (char c : string.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
}