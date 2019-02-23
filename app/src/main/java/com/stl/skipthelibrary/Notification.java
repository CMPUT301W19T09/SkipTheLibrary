package com.stl.skipthelibrary;

import java.util.Objects;

public class Notification {
    private NotificationType notificationType;
    private String message;

    public Notification(NotificationType notificationType, String message) {
        this.notificationType = notificationType;
        this.message = message;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return getNotificationType() == that.getNotificationType() &&
                Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotificationType(), getMessage());
    }
}
