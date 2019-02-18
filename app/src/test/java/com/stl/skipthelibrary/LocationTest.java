package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {
    private Location location;
    private double testLat;
    private double testLon;

    @Before
    public void setup(){
        testLat = 53.525151;
        testLon = -113.527350;
        location = new Location(testLat, testLon);
    }

    @Test
    public void setLongitude() {
        assertEquals(testLat, location.getLatitude(), 0);
        assertEquals(testLon, location.getLongitude(), 0);
    }
}