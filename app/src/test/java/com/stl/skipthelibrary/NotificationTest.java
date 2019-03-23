package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Enums.NotificationType;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class NotificationTest {
    private Notification notification;
    private String uuid;
    @Before
    public void setup(){
        uuid = UUID.randomUUID().toString();
        notification = new Notification(NotificationType.REQUEST_ACCEPTED, "Test UserName",
                uuid,"Test BookName");
    }

    @Test
    public void testConstructor() {
        assertEquals(NotificationType.REQUEST_ACCEPTED, notification.getNotificationType());
        assertEquals("Test UserName", notification.getUserName());
        assertFalse(notification.getBookID().isEmpty());
        assertEquals("Test BookName", notification.getBookName());
    }

    @Test
    public void testSetNotificationType() {
        notification.setNotificationType(NotificationType.NEW_REQUEST);
        assertEquals(NotificationType.NEW_REQUEST, notification.getNotificationType());
        notification.setNotificationType(NotificationType.REQUEST_DENIED);
        assertEquals(NotificationType.REQUEST_DENIED, notification.getNotificationType());
    }

    @Test
    public void testSetUserName() {
        notification.setUserName("New UserName");
        assertEquals("New UserName", notification.getUserName());
    }

    @Test
    public void testSetBookID() {
        String newID = UUID.randomUUID().toString();
        notification.setBookID(newID);
        assertEquals(newID, notification.getBookID());
    }

    @Test
    public void testSetBookName() {
        notification.setBookName("New BookName");
        assertEquals("New BookName", notification.getBookName());
    }

    @Test
    public void testEquals() {
        Notification secondNotification = new Notification(NotificationType.REQUEST_ACCEPTED,
                "Test UserName", uuid,"Test BookName");
        assertEquals(notification,secondNotification);
        secondNotification.setNotificationType(NotificationType.REQUEST_DENIED);
        assertNotEquals(notification, secondNotification);
        secondNotification.setNotificationType(NotificationType.REQUEST_ACCEPTED);
        assertEquals(notification,secondNotification);
        secondNotification.setBookName("Different name");
        assertNotEquals(notification, secondNotification);
    }
}