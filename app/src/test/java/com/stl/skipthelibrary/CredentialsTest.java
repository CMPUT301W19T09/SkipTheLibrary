package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CredentialsTest {
    private Credentials credentials;
    private UUID uuid;

    @Before
    public void setUp(){
        uuid = UUID.randomUUID();
        credentials = new Credentials("test userName", uuid, "test hashedPassword");
    }

    @Test
    public void testConstructor() {
        assertEquals("test userName", credentials.getUserName());
        assertEquals(uuid, credentials.getSecretID());
        assertEquals("test hashedPassword", credentials.getHashedPassword());

    }
}