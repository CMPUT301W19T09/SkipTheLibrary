package com.stl.skipthelibrary;

public class Notification {
    private NotificationType notificationType;
    private String message;

    public Notification(NotificationType notificationType, String message) {
        this.notificationType = notificationType;
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getMessage() {
        return message;
    }
}
