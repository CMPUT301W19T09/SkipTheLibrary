package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Exceptions.RatingOutOfBoundsException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RatingTest {

    private Rating rating;

    @Before
    public void setup(){
        rating = new Rating();
    }

    @Test
    public void testAddRating(){
        assertEquals(0, rating.getAverageRating(), 0);
        rating.addRating(4.0);
        assertEquals(4.0, rating.getAverageRating(), 0);
        rating.addRating(2.0);
        assertEquals(3.0, rating.getAverageRating(), 0);
        rating.addRating(4.5);
        assertEquals(3.5, rating.getAverageRating(), 0);

        assertEquals(3, rating.getCount());
    }

    @Test(expected = RatingOutOfBoundsException.class)
    public void testRatingLowerBoundException(){
        rating.addRating(rating.getMinRating() - 1);
    }

    @Test(expected = RatingOutOfBoundsException.class)
    public void testRatingHigherBoundException(){
        rating.addRating(rating.getMaxRating() + 1);
    }

    @Test
    public void testSetAverageRating() {
        double num = 2.5;
        rating.setAverageRating(num);
        assertEquals(num, rating.getAverageRating(), 0);
    }

    @Test
    public void testSetCount() {
        int count = 6;
        rating.setCount(count);
        assertEquals(count, rating.getCount());
    }

    @Test
    public void testSetMaxRating() {
        int maxRate = 25;
        rating.setMaxRating(maxRate);
        assertEquals(maxRate, rating.getMaxRating());
    }

    @Test
    public void testSetMinRating() {
        int minRate = -5;
        rating.setMinRating(minRate);
        assertEquals(minRate, rating.getMinRating());
    }

    @Test
    public void testToString() {
        double averageRating = 2.3;
        rating.setAverageRating(averageRating);
        assertEquals("{rating = " + averageRating + ", count " +  rating.getCount() + "}", rating.toString());
    }
}