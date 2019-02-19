package com.stl.skipthelibrary;

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
    }

    @Test(expected = RatingOutOfBoundsException.class)
    public void testRatingLowerBoundException(){
        rating.addRating(rating.getMinRating() - 1);
    }

    @Test(expected = RatingOutOfBoundsException.class)
    public void testRatingHigherBoundException(){
        rating.addRating(rating.getMaxRating() + 1);
    }
}