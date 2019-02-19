package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    private Book book;

    @Before
    public void setUp(){
        book = new Book(new BookDescription("test title", "test synopsis", "test author", null), "testUserName", new RequestHandler(new State()), null, null);
    }

    @Test
    public void testConstructor(){
         assertEquals("test title", book.getDescription().getTitle());
         assertEquals("testUserName", book.getOwnerUserName());
         assertEquals(null, book.getImages());
    }

}