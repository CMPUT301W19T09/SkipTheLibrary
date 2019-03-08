package com.stl.skipthelibrary;

import android.content.Context;
import android.os.Bundle;

import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Entities.ContactInfo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactInfoTest {
    private ContactInfo contactInfo;
    private String phone;
    private String email;

    @Before
    public void setUp() {
        phone = "1-780-611-8111";
        email = "test@test.com";
        contactInfo = new ContactInfo(email, phone, null);
    }

    @Test
    public void testConstructor() {
        assertEquals(email, contactInfo.getEmail());
        assertEquals(phone, contactInfo.getPhoneNumber());
    }

    @Test
    public void testSetEmail() {
        String email_two = "test@email.com";
        assertEquals(email, contactInfo.getEmail());
        contactInfo.setEmail(email_two);
        assertEquals(email_two, contactInfo.getEmail());
    }

    @Test
    public void testSetContext() {
        Context context = new LoginActivity() {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }
        };
        assertEquals(null, contactInfo.getContext());
        contactInfo.setContext(context);
        assertEquals(context, contactInfo.getContext());
    }

    @Test
    public void testSetPhoneNumber() {
        String phone_two = "1-111-111-1111";
        assertEquals(phone, contactInfo.getPhoneNumber());
        contactInfo.setPhoneNumber(phone_two);
        assertEquals(phone_two, contactInfo.getPhoneNumber());
    }

    @Test
    public void testEquals() {
        ContactInfo secondContact = new ContactInfo(email, phone, null);
        assertTrue(contactInfo.equals(secondContact));
        secondContact.setEmail("testtitle@email.com");
        assertFalse(contactInfo.equals(secondContact));
        secondContact = contactInfo;
        assertTrue(contactInfo.equals(secondContact));
        secondContact = null;
        assertFalse(contactInfo.equals(secondContact));
    }
}