package com.stl.skipthelibrary;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

public class BookTest {
    private Book book;

    @Before
    public void setUp(){
        book = new Book(new BookDescription("test title", "test synopsis", "test author", null),
                "testUserName", new RequestHandler(new State()), null, null);
    }

    @Test
    public void testConstructor(){
         assertEquals("test title", book.getDescription().getTitle());
         assertEquals("testUserName", book.getOwnerUserName());
         assertEquals(null, book.getImages());
    }

    @Test
    public void testSetUUID() {
        String uuid = UUID.randomUUID().toString();
        book.setUuid(uuid);
        assertEquals(uuid, book.getUuid());
    }

    @Test
    public void testSetRequests() {
        RequestHandler requests = new RequestHandler(new State());
        book.setRequests(requests);
        assertEquals(requests, book.getRequests());
    }

    @Test
    public void testSetRating() {
        Rating rating = new Rating();
        book.setRating(rating);
        assertEquals(rating, book.getRating());
    }

    @Test
    public void testSetImages() {
        ArrayList<ViewableImage> images = new ArrayList<ViewableImage>();
        book.setImages(images);
        assertEquals(images, book.getImages());
    }

    @Test
    public void testSetDescription() {
        BookDescription description = new BookDescription("test title", "test synopsis",
                "test author", new Rating());
        book.setDescription(description);
        assertEquals(description, book.getDescription());
    }

    @Test
    public void testSetOwnerUserName() {
        String name = "test user name";
        book.setOwnerUserName(name);
        assertEquals(name, book.getOwnerUserName());
    }
}