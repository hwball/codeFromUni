package seng202.group6.model;

import java.util.Collection;
import java.util.Set;

/**
 * Abstract Filter Class for Records.
 */
public abstract class Filter<T extends Record> {

    /**
     * Compares the given Record to the filter's criteria.
     *
     * @param t the record to check
     * @return true if the record matches all defined criteria, else false.
     */
    public abstract boolean matches(T t);


    /**
     * Checks if the passed string matches any in the given set. The strings in the set must all be
     * lower case. If the set of strings is empty accept all strings.
     *
     * @param filter the set of strings to accept
     * @param value  the string being tested
     * @return true if the string matches any in the given set, else false
     */
    public static boolean stringMatches(Set<String> filter, String value) {
        boolean matches = true;
        if (!filter.isEmpty()) {
            matches = filter.contains(value.toLowerCase());
        }
        return matches;
    }


    /**
     * Checks if any of the given strings match any of the given strings. The given strings must all
     * be lower case. If the filter set of strings is empty accept all strings.
     *
     * @param filter the set of strings to accept
     * @param values the strings to test for acceptance
     * @return true if at least one of the given strings matches, else false
     */
    public static boolean stringMatches(Set<String> filter, Collection<String> values) {
        boolean matches = true;
        if (!filter.isEmpty()) {
            matches = false;
            for (String value : values) {
                matches = matches || filter.contains(value.toLowerCase());
            }
        }
        return matches;
    }


    /**
     * Checks the given value is inside the given range. A null value in the range specifies that it
     * is unbounded in that direction.
     *
     * @param range the (inclusive) ranmesge of values to accept
     * @param value the value to check
     * @return true if the value is in the range, else false
     */
    public static boolean inRange(Double[] range, double value) {
        boolean matches = true;
        // If a minimum is specified and the value is below this return false
        if (range[0] != null) {
            if (range[0].compareTo(value) > 0) {
                matches = false;
            }
        }
        // If a maximum is specified and the value is above this return false
        if (range[1] != null) {
            if (range[1].compareTo(value) < 0) {
                matches = false;
            }
        }
        return matches;
    }


    /**
     * Checks the given value is inside the given range. A null value in the range specifies that it
     * is unbounded in that direction.
     *
     * @param range the (inclusive) range of values to accept
     * @param value value to check
     * @return true if the value in the range, else false
     */
    public static boolean inRange(Integer[] range, int value) {
        boolean matches = true;
        // If a minimum is specified and the value is below this return false
        if (range[0] != null) {
            if (value < range[0]) {
                //System.out.println("Below min");
                matches = false;
            }
        }
        // If a maximum is specified and the value is above this return false
        if (range[1] != null) {
            if (range[1] < value) {
                //System.out.println("Above max");
                matches = false;
            }
        }
        return matches;
    }


    /**
     * Check that the two booleans match. If the Boolean to test against is null, accept both true
     * and false.
     *
     * @param filter boolean to test against
     * @param testee boolean being tested
     * @return true if booleans match, else false
     */
    public static boolean booleanMatches(Boolean filter, boolean testee) {
        boolean matches = true;
        if (filter != null) {
            matches = filter == testee;
        }
        return matches;
    }


    /**
     * Verify that the array is a valid range, that is, that it has two elements and if neither are
     * null the first is not greater than the second.
     *
     * @param range the array to check
     * @return true if the array is a valid range, else false
     */
    public static boolean rangeIsValid(Integer[] range) {
        boolean valid = true;
        if (range.length != 2) {
            valid = false;
        }
        if (range[0] != null && range[1] != null) {
            if (range[0] > range[1]) {
                valid = false;
            }
        }
        return valid;
    }


    /**
     * Verify that the array is a valid range, that is, that it has two elements and if neither are
     * null the first is not greater than the second.
     *
     * @param range the array to check
     * @return true if the array is a valid range, else false
     */
    public static boolean rangeIsValid(Double[] range) {
        boolean valid = true;
        if (range.length != 2) {
            valid = false;
        }
        if (range[0] != null && range[1] != null) {
            if (range[0].compareTo(range[1]) > 0) {
                valid = false;
            }
        }
        return valid;
    }
}
