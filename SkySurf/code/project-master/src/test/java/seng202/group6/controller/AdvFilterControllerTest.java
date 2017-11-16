package seng202.group6.controller;

import org.junit.Test;
import seng202.group6.controller.AdvFilterController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by afj19 on 26/08/16.
 */
public class AdvFilterControllerTest {

    @Test
    public void parseRangeDoubleTest() {
        Double[] expectedRange = new Double[2];
        Double[] range;

        expectedRange[0] = 0.00;
        expectedRange[1] = 0.00;
        range = AdvFilterController.parseRangeDouble("0.00, 0.00");
        assertEquals("", expectedRange[0], range[0], 1e-6);
        assertEquals("", expectedRange[1], range[1], 1e-6);

        expectedRange[0] = null;
        expectedRange[1] = 0.00;
        range = AdvFilterController.parseRangeDouble(", 0.00");
        assertNull("", range[0]);
        assertEquals("", expectedRange[1], range[1], 1e-6);

        expectedRange[0] = 0.00;
        expectedRange[1] = null;
        range = AdvFilterController.parseRangeDouble("0.00,");
        assertEquals("", expectedRange[0], range[0], 1e-6);
        assertNull("", range[1]);

        expectedRange[0] = null;
        expectedRange[1] = null;
        range = AdvFilterController.parseRangeDouble(",");
        assertNull("", range[0]);
        assertNull("", range[1]);
    }

    @Test
    public void parseRangeIntegerTest() {
        Integer[] expectedRange = new Integer[2];
        Integer[] range;

        expectedRange[0] = 0;
        expectedRange[1] = 0;
        range = AdvFilterController.parseRangeInteger("0, 0");
        assertEquals("", expectedRange[0], range[0]);
        assertEquals("", expectedRange[1], range[1]);

        expectedRange[0] = null;
        expectedRange[1] = 0;
        range = AdvFilterController.parseRangeInteger(", 0");
        assertNull("", range[0]);
        assertEquals("", expectedRange[1], range[1]);

        expectedRange[0] = 0;
        expectedRange[1] = null;
        range = AdvFilterController.parseRangeInteger("0,");
        assertEquals("", expectedRange[0], range[0]);
        assertNull("", range[1]);

        expectedRange[0] = null;
        expectedRange[1] = null;
        range = AdvFilterController.parseRangeInteger(",");
        assertNull("", range[0]);
        assertNull("", range[1]);
    }
}
