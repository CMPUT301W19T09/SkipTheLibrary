package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.Rating;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookDescriptionTest {
    private BookDescription bookDescription;

    @Before
    public void setup(){
        bookDescription = new BookDescription("test title", "test synopsis", "test author");
    }

    @Test
    public void testConstructor() {
        assertEquals("test title", bookDescription.getTitle());
        assertEquals("test synopsis", bookDescription.getSynopsis());
        assertEquals("test author", bookDescription.getAuthor());
    }

    @Test
    public void testSetTitle() {
        bookDescription.setTitle("test set title");
        assertEquals("test set title", bookDescription.getTitle());
    }

    @Test
    public void testSetAuthor(){
        bookDescription.setAuthor("test set author");
        assertEquals("test set author", bookDescription.getAuthor());
    }

    @Test
    public void testSetSynopsis(){
        bookDescription.setSynopsis("test set synopsis");
        assertEquals("test set synopsis", bookDescription.getSynopsis());
    }

    @Test
    public void testEquals() {
        BookDescription secondDescription = new BookDescription("title1", "synopsis1", "author1");
        assertFalse(bookDescription.equals(secondDescription));
        secondDescription.setTitle("test title");
        assertFalse(bookDescription.equals(secondDescription));
        secondDescription.setSynopsis("test synopsis");
        assertFalse(bookDescription.equals(secondDescription));
        secondDescription.setAuthor("test author");
        assertTrue(bookDescription.equals(secondDescription));
        secondDescription = bookDescription;
        assertTrue(bookDescription.equals(secondDescription));
    }
}