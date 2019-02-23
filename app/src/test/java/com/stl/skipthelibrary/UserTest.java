package com.stl.skipthelibrary;

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
    public void testSetOwnerUserIdentity(){
        UserIdentity ownerUserIdentity = new UserIdentity(UserMode.OWNER);
        user.setOwnerUserIdentity(ownerUserIdentity);
        assertEquals(ownerUserIdentity, user.getOwnerUserIdentity());
    }

    @Test
    public void testSetBorrowerUserIdentity(){
        UserIdentity borrowerUserIdentity = new UserIdentity(UserMode.BORROWER);
        user.setBorrowerUserIdentity(borrowerUserIdentity);
        assertEquals(borrowerUserIdentity, user.getBorrowerUserIdentity());
    }

    @Test
    public void testSetNotifications(){
        ArrayList<Notification> notifications = new ArrayList<>();
        Notification notification = new Notification(NotificationType.NEW_REQUEST, "test message");
        notifications.add(notification);
        user.setNotifications(notifications);
        assertEquals(notifications, user.getNotifications());
    }

}