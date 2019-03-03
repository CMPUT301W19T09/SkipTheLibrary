package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserIdentityTest {
    private UserIdentity ownerIdentity;
    private UserIdentity borrowerIdentity;

    @Before
    public void setUp(){
        ownerIdentity = new UserIdentity(UserMode.OWNER);
        borrowerIdentity = new UserIdentity(UserMode.BORROWER);
    }

    @Test
    public void testModes(){
        assertEquals(UserMode.OWNER, ownerIdentity.getUserMode());
        assertEquals(UserMode.BORROWER, borrowerIdentity.getUserMode());
    }

    @Test
    public void testAddBook(){
        assertEquals(0, ownerIdentity.getBookList().size());
        Book book = new Book(null, null, null, null, null, null);
        ownerIdentity.addBook(book);
        assertEquals(1, ownerIdentity.getBookList().size());
    }

    @Test
    public void testRemoveBook(){
        assertEquals(0, ownerIdentity.getBookList().size());
        Book book1 = new Book(null, null, null, null, null, null);
        ownerIdentity.addBook(book1);

        Book book2 = new Book(null, null, null, null, null, null);
        assertFalse(ownerIdentity.removeBook(book2));
        assertTrue(ownerIdentity.removeBook(book1));
    }

    @Test
    public void testSetBookList() {
        ArrayList<Book> books = new ArrayList<Book>();
        Book book = new Book("test ISBN", new BookDescription("test title", "test synopsis", "test author", null),
                "testUserName", new RequestHandler(new State()), null, null);
        books.add(book);
        ownerIdentity.setBookList(books);
        assertEquals(1, ownerIdentity.getBookList().size());
        assertEquals(book, ownerIdentity.getBookList().get(0));
    }

    @Test
    public void testSetRating() {
        Rating rating = new Rating();
        ownerIdentity.setRating(rating);
        assertEquals(rating, ownerIdentity.getRating());
    }

    @Test
    public void testSetUserMode() {
        ownerIdentity.setUserMode(UserMode.BORROWER);
        assertEquals(UserMode.BORROWER, ownerIdentity.getUserMode());
        ownerIdentity.setUserMode(UserMode.OWNER);
        assertEquals(UserMode.OWNER, ownerIdentity.getUserMode());
    }

    @Test
    public void testToString() {
        Rating rating = new Rating();
        UserMode ownerMode = UserMode.OWNER;
        UserMode borrowerMode = UserMode.BORROWER;
        ownerIdentity.setUserMode(ownerMode);
        assertEquals("{UserMode= " + ownerMode.name() + " ,Rating = " + rating.toString() + "}",
                ownerIdentity.toString());
        ownerIdentity.setUserMode(borrowerMode);
        assertEquals("{UserMode= " + borrowerMode.name() + " ,Rating = " + rating.toString() + "}",
                ownerIdentity.toString());
    }

    @Test
    public void testEquals() {
        UserIdentity secondIdentity = new UserIdentity(UserMode.OWNER);
        assertTrue(ownerIdentity.equals(secondIdentity));
        secondIdentity.setUserMode(UserMode.BORROWER);
        assertTrue(borrowerIdentity.equals(secondIdentity));
    }
}