package com.stl.skipthelibrary.Entities;

import com.stl.skipthelibrary.Enums.NotificationType;

import java.util.Objects;

/**
 * This class describes a notification
 */
public class Notification {
    private NotificationType notificationType;
    private String message;

    /**
     * Constructor will all parameters
     * @param notificationType: the type of notification
     * @param message: the notification message
     */
    public Notification(NotificationType notificationType, String message) {
        this.notificationType = notificationType;
        this.message = message;
    }

    /**
     * Set the notification type
     * @param notificationType: the notification type to set
     */
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * Set the notification message
     * @param message: the notification message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the notification type
     * @return the notification type
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Get the notification message
     * @return the notification message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Determines if the notification is identical to another object
     * @param o: An object to compare the notification to
     * @return true if o is identical to the current notification
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return getNotificationType() == that.getNotificationType() &&
                Objects.equals(getMessage(), that.getMessage());
    }

    /**
     * Calculate and return the notification's hashcode
     * @return the notification's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNotificationType(), getMessage());
    }
}
