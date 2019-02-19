package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookDescriptionTest {
    private BookDescription bookDescription;

    @Before
    public void setup(){
        bookDescription = new BookDescription("test title", "test synopsis", "test author", null);
    }

    @Test
    public void testConstructor() {
        assertEquals("test title", bookDescription.getTitle());
        assertEquals("test synopsis", bookDescription.getSynopsis());
        assertEquals("test author", bookDescription.getAuthor());
        assertEquals(null, bookDescription.getRating());
    }
}