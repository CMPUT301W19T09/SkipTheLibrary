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
}