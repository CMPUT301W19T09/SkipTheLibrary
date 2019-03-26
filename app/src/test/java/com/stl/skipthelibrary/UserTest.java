package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Entities.ContactInfo;
import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.User;
import com.stl.skipthelibrary.Entities.ViewableImage;
import com.stl.skipthelibrary.Enums.NotificationType;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @Before
    public void setUp(){
        user = new User();
    }

    @Test
    public void testSetName(){
        user.setName("TestFirstName TestLastName");
        assertEquals("TestFirstName TestLastName", user.getName());
    }

    @Test
    public void testSetUserName(){
        user.setUserName("TestUserName");
        assertEquals("TestUserName", user.getUserName());
    }

    @Test
    public void testSetUserID(){
        user.setUserID("TestUserID");
        assertEquals("TestUserID", user.getUserID());
    }

    @Test
    public void testSetImage(){

        ViewableImage testImage = new ViewableImage();
        user.setImage(testImage);
        assertEquals(testImage, user.getImage());
    }

    @Test
    public void testSetContactInfo(){
        ContactInfo contactInfo = new ContactInfo();
        user.setContactInfo(contactInfo);
        assertEquals(contactInfo, user.getContactInfo());
    }

    @Test
    public void testSetOwnerRating(){
        Rating ownerRating = new Rating();
        ownerRating.addRating(2.3);
        user.setOwnerRating(ownerRating);
        assertEquals(ownerRating, user.getOwnerRating());
    }

    @Test
    public void testSetBorrowerRating(){
        Rating borrowerRating = new Rating();
        borrowerRating.addRating(2.3);
        user.setBorrowerRating(borrowerRating);
        assertEquals(borrowerRating, user.getBorrowerRating());
    }

}