package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.State;
import com.stl.skipthelibrary.Entities.ViewableImage;
import com.stl.skipthelibrary.Enums.BookStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

public class BookTest {
    private Book book;

    @Before
    public void setUp(){
        book = new Book("test ISBN", new BookDescription("test title", "test synopsis", "test author"),
                "testUserName", new RequestHandler(new State()), null, null);
    }

    @Test
    public void testConstructor(){
         assertEquals("test title", book.getDescription().getTitle());
         assertEquals("testUserName", book.getOwnerUserName());
         assertTrue(book.getImages().isEmpty());
    }

    @Test
    public void testSetUUID() {
        String uuid = UUID.randomUUID().toString();
        book.setUuid(uuid);
        assertEquals(uuid, book.getUuid());
    }

    @Test
    public void testSetRequests() {
        State state = new State();
        RequestHandler requests = new RequestHandler(state);
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
                "test author");
        book.setDescription(description);
        assertEquals(description, book.getDescription());
    }

    @Test
    public void testSetOwnerUserName() {
        String name = "test user name";
        book.setOwnerUserName(name);
        assertEquals(name, book.getOwnerUserName());
    }

    @Test
    public void testSetISBN() {
        String isbn = "test ISBN";
        book.setISBN(isbn);
        assertEquals(isbn, book.getISBN());
        book.setISBN(null);
        assertEquals(null, book.getISBN());
    }

    @Test
    public void testUserIsInterested() {
        book.getRequests().addRequestor("test user");
        assertFalse(book.userIsInterested("test name"));
        assertFalse(book.userIsInterested("test user 2"));
        assertTrue(book.userIsInterested("test user"));
        book.getRequests().addRequestor("test user 2");
        assertTrue(book.userIsInterested("test user 2"));
        book.setRequests(null);
        assertFalse(book.userIsInterested("test user"));
    }

    @Test
    public void testEquals() {
        Book secondBook = new Book("test ISBN", new BookDescription("test title", "test synopsis", "test author"),
                "testUserName", new RequestHandler(new State()), null, null);
        secondBook.setUuid(book.getUuid());
        assertTrue(book.equals(secondBook));
        secondBook.setOwnerUserName("test name");
        assertFalse(book.equals(secondBook));
        secondBook = book;
        assertTrue(book.equals(secondBook));
    }
}