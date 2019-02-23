package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Luke Slevinsky on 2019-02-18.
 */
public class StateTest {
    private State state;

    @Before
    public void setUp() {
        state = new State();
    }

    @Test
    public void testSetBookStatus() {
        state.setBookStatus(BookStatus.BORROWED);
        assertEquals(BookStatus.BORROWED, state.getBookStatus());
    }

    @Test
    public void testSetLocation() {
        Location location = new Location(53.525151, -113.527350);
        state.setLocation(location);
        assertEquals(location, state.getLocation());
    }

    @Test
    public void testSetHandOffState() {
        state.setHandoffState(HandoffState.OWNER_LENT);
        assertEquals(HandoffState.OWNER_LENT, state.getHandoffState());
    }

}