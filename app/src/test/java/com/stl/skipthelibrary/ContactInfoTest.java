package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactInfoTest {
    private ContactInfo contactInfo;

    @Before
    public void setUp() {
        contactInfo = new ContactInfo("test@test.com", "aaabbbcccc", null);
    }

    @Test
    public void testConstructor() {
        assertEquals("test@test.com", contactInfo.getEmail());
        assertEquals("aaabbbcccc", contactInfo.getPhoneNumber());
    }
}