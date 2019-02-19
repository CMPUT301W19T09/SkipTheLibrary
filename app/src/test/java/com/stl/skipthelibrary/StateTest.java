package com.stl.skipthelibrary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Luke Slevinsky on 2019-02-18.
 */
public class StateTest {
    private State state;
    private Location location;

    @Before
    public void setUp() throws Exception {
        location= new Location(53.631611,	-113.323975);
        state = new State(BookStatus.ACCEPTED,location,HandoffState.READY_FOR_PICKUP);
    }

    @Test
    public void testConstructor() {
        assertEquals(BookStatus.ACCEPTED, state.getBookStatus());
        assertEquals(location,state.getLocation());
        assertEquals(HandoffState.READY_FOR_PICKUP, state.getHandoffState());
    }
}