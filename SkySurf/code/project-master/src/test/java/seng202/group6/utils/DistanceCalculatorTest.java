package seng202.group6.utils;

import org.junit.Test;

import seng202.group6.utils.DistanceCalculator;

import static org.junit.Assert.assertEquals;

public class DistanceCalculatorTest {
    @Test
    public void calculateAirpDistTest(){
        double zeroTest = DistanceCalculator.calculateAirpDist(0, 0, 0 , 0, 0, 0);
        assertEquals(0, zeroTest, 0.001);
        double firstTest = DistanceCalculator.calculateAirpDist(100, 50, 50, 50, 100, 30);
        assertEquals(firstTest, 5220401, 1);
        double secondTest = DistanceCalculator.calculateAirpDist(147.22005, -9.443383, 146, 146.726242, -6.569828, 239.0);
        assertEquals(secondTest, 324117, 1);
    }
}
