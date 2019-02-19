package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @Before
    public void setUp(){
        user = new User("test name", "test username", null, null, null, null, null, null);
    }

    @Test
    public void testConstructor(){
        assertEquals("test name", user.getName());
    }
}