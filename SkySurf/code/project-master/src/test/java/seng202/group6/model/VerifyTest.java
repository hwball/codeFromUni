package seng202.group6.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seng202.group6.utils.Verify.*;

public class VerifyTest {
    @Test
    public void validateNameTest() throws Exception {
        assertTrue(validateName("Name 'dfkjh \\dsfhoehoho\\"));
        assertTrue(validateName(""));
    }

    @Test
    public void validateAirportIATATest() throws Exception {
        assertTrue(validateAirportIATA("ABC"));
        assertTrue(validateAirportIATA(""));
        assertTrue(validateAirportIATA("123"));

        assertFalse(validateAirportIATA("ABCD"));
        assertFalse(validateAirportIATA("A.B"));

    }

    @Test
    public void validateAirportICAOTest() throws Exception {
        assertTrue(validateAirportICAO("ICAO"));
        assertTrue(validateAirportICAO(""));
        assertTrue(validateAirportICAO("1234"));

        assertFalse(validateAirportICAO("12"));
        assertFalse(validateAirportICAO("AB#C"));
        assertFalse(validateAirportICAO("123ABD"));
    }

    @Test
    public void validateLatitudeTest() throws Exception {
        assertTrue(validateLatitude("1"));
        assertTrue(validateLatitude("90.0"));
        assertTrue(validateLatitude("-1"));
        assertTrue(validateLatitude("-90.0"));

        assertFalse(validateLatitude(""));
        assertFalse(validateLatitude("-100"));
        assertFalse(validateLatitude("1.035W"));
        assertFalse(validateLatitude("1.0.0"));
    }

    @Test
    public void validateLongitudeTest() throws Exception {
        assertTrue(validateLongitude("1"));
        assertTrue(validateLongitude("180.0"));
        assertTrue(validateLongitude("-1"));
        assertTrue(validateLongitude("-180.0"));

        assertFalse(validateLongitude(""));
        assertFalse(validateLongitude("-181"));
        assertFalse(validateLongitude("1.035W"));
        assertFalse(validateLongitude("1.0.0"));
    }

    @Test
    public void validateLatitude1Test() throws Exception {
        assertTrue(validateLatitude(1));
        assertTrue(validateLatitude(-1));
        assertTrue(validateLatitude(0.0));
        assertTrue(validateLatitude(90.0));
        assertTrue(validateLatitude(-90.0));

        assertFalse(validateLatitude(90.001));
        assertFalse(validateLatitude(-90.001));
    }

    @Test
    public void validateLongitude1Test() throws Exception {
        assertTrue(validateLongitude(1));
        assertTrue(validateLongitude(-1));
        assertTrue(validateLongitude(0.0));
        assertTrue(validateLongitude(180.0));
        assertTrue(validateLongitude(-180.0));

        assertFalse(validateLongitude(180.001));
        assertFalse(validateLongitude(-180.001));
    }

    @Test
    public void validateIntegerTest() throws Exception {
        assertTrue(validateInteger("1"));
        assertTrue(validateInteger("999"));

        assertFalse(validateInteger("0xff"));
        assertFalse(validateInteger("1.0"));
        assertFalse(validateInteger("a"));
        assertFalse(validateInteger(""));
    }

    @Test
    public void validateDecimalTest() throws Exception {
        assertTrue(validateDecimal("1"));
        assertTrue(validateDecimal("999.00"));

        assertFalse(validateDecimal("0xff"));
        assertFalse(validateDecimal("1*0"));
        assertFalse(validateDecimal("a"));
        assertFalse(validateDecimal(""));
    }

    @Test
    public void validateDaylightSavingTimeTest() throws Exception {
        assertTrue(validateDaylightSavingTime("U"));
        assertTrue(validateDaylightSavingTime("Z"));
        assertTrue(validateDaylightSavingTime("S"));

        assertFalse(validateDaylightSavingTime("Q"));
        assertFalse(validateDaylightSavingTime("ZZ"));
        assertFalse(validateDaylightSavingTime(""));
    }

    @Test
    public void validateTimezoneDBTest() throws Exception {
        assertTrue(validateTimezoneDB("America/Bahia_Banderas"));
        assertTrue(validateTimezoneDB("America/Indiana/Indianapolis"));
        assertTrue(validateTimezoneDB("Locat///-__dkfjhkdfu"));

        assertFalse(validateTimezoneDB("America\\Bahia_Banderas"));

    }

    @Test
    public void validateFlightPointTypeTest() throws Exception {
        assertTrue(validateFlightPointType("VOR"));
        assertTrue(validateFlightPointType("APT"));
        assertTrue(validateFlightPointType("LATLON"));

        assertFalse(validateFlightPointType("+VOR"));
        assertFalse(validateFlightPointType("ART"));
        assertFalse(validateFlightPointType("nope"));
    }

    @Test
    public void validateAirlineIATATest() throws Exception {
        assertTrue(validateAirlineIATA("AB"));
        assertTrue(validateAirlineIATA(""));
        assertTrue(validateAirlineIATA("12"));

        assertFalse(validateAirlineIATA("ABCD"));
        assertFalse(validateAirlineIATA("A."));
    }

    @Test
    public void validateAirlineICAOTest() throws Exception {
        assertTrue(validateAirlineICAO("ICA"));
        assertTrue(validateAirlineICAO(""));
        assertTrue(validateAirlineICAO("123"));

        assertFalse(validateAirlineICAO("12"));
        assertFalse(validateAirlineICAO("A#C"));
        assertFalse(validateAirlineICAO("123ABD"));
    }

    @Test
    public void validateYesNoTest() throws Exception {
        assertTrue(validateYesNo("Y"));
        assertTrue(validateYesNo("y"));
        assertTrue(validateYesNo("N"));
        assertTrue(validateYesNo("n"));
        assertTrue(validateYesNo(""));

        assertFalse(validateYesNo("t"));
        assertFalse(validateYesNo("F"));
        assertFalse(validateYesNo("-"));
    }

    @Test
    public void validateRouteEquipmentListTest() throws Exception {
        assertTrue(validateRouteEquipmentList("ABC DEF ghi"));
        assertTrue(validateRouteEquipmentList("ABC 123 GHI"));
        assertTrue(validateRouteEquipmentList("ABC D2F GHI"));
        assertTrue(validateRouteEquipmentList(""));
        assertTrue(validateRouteEquipmentList("ABC"));

        assertFalse(validateRouteEquipmentList("... ... ..."));
        assertFalse(validateRouteEquipmentList("abc d"));
        assertFalse(validateRouteEquipmentList("abc defg"));
    }

    @Test
    public void validateTimezoneTest() throws Exception {
        assertTrue(validateTimezone("14"));
        assertTrue(validateTimezone("10"));
        assertTrue(validateTimezone("-2"));
        assertTrue(validateTimezone("03"));
        assertTrue(validateTimezone("13.25"));
        assertFalse(validateTimezone("Abel is 400mmr"));


    }
}