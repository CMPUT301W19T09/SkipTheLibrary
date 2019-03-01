package com.stl.skipthelibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationTest {
    private Notification notification;

    @Before
    public void setup(){
        notification = new Notification(NotificationType.REQUEST_ACCEPTED, "Test Message");
    }

    @Test
    public void testConstructor() {
        assertEquals(NotificationType.REQUEST_ACCEPTED, notification.getNotificationType());
        assertEquals("Test Message", notification.getMessage());
    }

    @Test
    public void testSetNotificationType() {
        notification.setNotificationType(NotificationType.NEW_REQUEST);
        assertEquals(NotificationType.NEW_REQUEST, notification.getNotificationType());
        notification.setNotificationType(NotificationType.REQUEST_DENIED);
        assertEquals(NotificationType.REQUEST_DENIED, notification.getNotificationType());
    }

    @Test
    public void testSetMessage() {
        notification.setMessage("Message to test");
        assertEquals("Message to test", notification.getMessage());
    }

    @Test
    public void testEquals() {
        Notification secondNotification = new Notification(NotificationType.REQUEST_ACCEPTED, "Test Message");
        assertTrue(notification.equals(secondNotification));
        secondNotification.setNotificationType(NotificationType.REQUEST_DENIED);
        assertFalse(notification.equals(secondNotification));
        secondNotification.setNotificationType(NotificationType.REQUEST_ACCEPTED);
        assertTrue(notification.equals(secondNotification));
        secondNotification.setMessage("Different message");
        assertFalse(notification.equals(secondNotification));
    }
}