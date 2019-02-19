package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

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
        Book book = new Book(null, null, null, null, null);
        ownerIdentity.addBook(book);
        assertEquals(1, ownerIdentity.getBookList().size());
    }

    @Test
    public void testRemoveBook(){
        assertEquals(0, ownerIdentity.getBookList().size());
        Book book1 = new Book(null, null, null, null, null);
        ownerIdentity.addBook(book1);

        Book book2 = new Book(null, null, null, null, null);
        assertFalse(ownerIdentity.removeBook(book2));
        assertTrue(ownerIdentity.removeBook(book1));

    }
}