package seng202.group6.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import seng202.group6.model.Filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests abstract class Filter
 */
public class FilterTest {
    @Test
    public void inRangeIntegerTest() {
        Integer[] range = {5, 9};
        assertTrue(Filter.inRange(range, 7));
        assertTrue(Filter.inRange(range, 5));
        assertTrue(Filter.inRange(range, 9));
        assertFalse(Filter.inRange(range, 1));
        assertFalse(Filter.inRange(range, 10));
        range[0] = null;
        assertTrue(Filter.inRange(range, 1));
    }

    @Test
    public void inRangeDoubleTest() {
        Double[] range = {2.5, 7.5};
        assertTrue(Filter.inRange(range, 5.0));
        assertTrue(Filter.inRange(range, 2.5));
        assertTrue(Filter.inRange(range, 7.5));
        assertFalse(Filter.inRange(range, 2.2));
        assertFalse(Filter.inRange(range, 7.7));
        range[1] = null;
        assertTrue(Filter.inRange(range, 7.7));
    }

    @Test
    public void stringMatchesTest() {
        HashSet<String> filter = new HashSet<>();
        String query = "Test";
        assertTrue(Filter.stringMatches(filter, query));
        filter.add("Fail".toLowerCase());
        assertFalse(Filter.stringMatches(filter, query));
        filter.add(query.toLowerCase());
        assertTrue(Filter.stringMatches(filter, query));
    }

    @Test
    public void rangeIsValidTest() {
        Double[] range1 = {2.5, 7.5};
        Double[] range2 = {null, 7.5};
        Integer[] range3 = {5, 17};
        Integer[] range4 = {999, null};
        assertTrue(Filter.rangeIsValid(range1));
        assertTrue(Filter.rangeIsValid(range2));
        assertTrue(Filter.rangeIsValid(range3));
        assertTrue(Filter.rangeIsValid(range4));

        Double[] range5 = {10.0, 2.0};
        Integer[] range6 = {12, 1};
        assertFalse(Filter.rangeIsValid(range5));
        assertFalse(Filter.rangeIsValid(range6));
    }

    @Test
    public void stringMatchesArrayTest() {
        ArrayList<String> values = new ArrayList<>();
        HashSet<String> filter = new HashSet<>();

        values.add("AAA".toLowerCase());
        values.add("BBB".toLowerCase());
        assertTrue("Empty filter matches everything", Filter.stringMatches(filter,values));


        filter.add("ABC".toLowerCase());
        filter.add("XYZ".toLowerCase());
        assertFalse("Filter values not present in list", Filter.stringMatches(filter, values));

        values.add("ABC".toLowerCase());
        assertTrue("Value 'ABC' present in list", Filter.stringMatches(filter, values));
    }
}
